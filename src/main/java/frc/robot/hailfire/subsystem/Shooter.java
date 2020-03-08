package frc.robot.hailfire.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import frc.robot.base.input.Axis;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.util.PosControl;
import frc.robot.hailfire.IDs;
import frc.robot.base.device.motor.PhoenixMotor;
import frc.robot.base.device.motor.EncoderMotor;
import frc.robot.base.device.motor.Motor;
import frc.robot.hailfire.MotorConfig;
import frc.robot.hailfire.Vision;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Shooter extends Subsystem {

    private Controller auxController;
    private Controller driveController;

    private EncoderMotor leftMotor = new PhoenixMotor(new TalonSRX(IDs.Shooter.LEFT_MOTOR), MotorConfig.Shooter.CONFIG);
    private EncoderMotor rightMotor = new PhoenixMotor(new TalonSRX(IDs.Shooter.RIGHT_MOTOR), MotorConfig.Shooter.CONFIG).invert();

    private Motor pitchMotor = new PhoenixMotor(new TalonSRX(IDs.Shooter.PITCH_MOTOR));
    private Motor carousel = new PhoenixMotor(new TalonSRX(IDs.Shooter.CAROUSEL_MOTOR)).invert();
    private DigitalInput carouselSwitch = new DigitalInput(1);
    private boolean carouselHit = false;

    private Relay spike = new Relay(0);

    // configurable variables for tuning from dash

    public double leftK;
    public double leftP;
    public double leftI;
    public double leftD;

    public double rightK;
    public double rightP;
    public double rightI;
    public double rightD;

    public double leftSpeedDemand;
    public double rightSpeedDemand;

    private double carouselOutput = 0;
    private boolean autoCarousel = false;
    private boolean spinForShooter = true;

    public Shooter(Controller mainController, Controller carouselController) {
        super("shooter");
        this.auxController = mainController;
        this.driveController = carouselController;
    }

    @Override
    public void stop() {
        leftMotor.setPercentOutput(0);
        rightMotor.setPercentOutput(0);
        carousel.setPercentOutput(0);
        shooterStartTime = System.currentTimeMillis();
        carouselOutput = 0;
        autoCarousel = false;
    }

    @Override
    public void control() {

        if (driveController.axisDown(Axis.RIGHT_TRIGGER)) {
            shoot(true);
        } else {
            spinForShooter = false;

            shooterStartTime = System.currentTimeMillis();

            leftMotor.setPercentOutput(0);
            rightMotor.setPercentOutput(0);
        }

        // I am fully aware that carousel control is a boolean mess
        // I should probably implement a simpler priority system
        // Either way, it works :)

        // manual control
        if (auxController.getAxis(Axis.LEFT_X) > 0.5) {
            carouselOutput = 0.7;
            autoCarousel = false;
        } else if (auxController.getAxis(Axis.LEFT_X) < -0.5) {
            carouselOutput = -0.7;
            autoCarousel = false;
        } else if (auxController.getAxis(Axis.LEFT_TRIGGER) > 0.5) { // semi manual (go to limit switch)
            carouselOutput = -0.7;
            autoCarousel = true;
        } else if (auxController.getAxis(Axis.RIGHT_TRIGGER) > 0.5) {
            carouselOutput = 0.7;
            autoCarousel = true;
        } else if (autoCarousel) {
            // edge on detection
            if (!carouselSwitch.get()) {
                if (!carouselHit) {
                    carouselHit = true;
                    carouselOutput = 0;
                    autoCarousel = false;
                }
            } else {
                carouselHit = false;
            }
        } else { // if nothing else
            if (!spinForShooter) {
                carouselOutput = 0;
            }
        }

        carousel.setPercentOutput(carouselOutput);

        /*if (driveController.axisDown(Axis.LEFT_TRIGGER)) {
            EncoderMotorConfig leftConfig = new EncoderMotorConfig(2048 * 4, 0.01611, leftP, leftI, leftD, 800);
            leftMotor.setConfig(leftConfig);
            EncoderMotorConfig rightConfig = new EncoderMotorConfig(2048 * 4, 0.01611, rightP, rightI, rightD, 800);
            rightMotor.setConfig(rightConfig);
        }*/

        // move carousel up/down

        if (driveController.buttonDown(Button.A)) {
            pitchMotor.setPercentOutput(.125);
        } else if (driveController.buttonDown(Button.Y)) {
            pitchMotor.setPercentOutput(-0.5);
        } else {
            pitchMotor.setPercentOutput(0);
        }

        // turn on lights

        if (auxController.buttonPressed(Button.LEFT_BUMPER)) {
            spike.set(spike.get() == Relay.Value.kForward ? Relay.Value.kOff : Relay.Value.kForward);
        }
    }

    @Override
    public Map<String, Supplier<Object>> NTSets() {
        return Map.of(
                "leftPercent", leftMotor::getOutputPercent,
                "leftVelocity", leftMotor::getVelocity,
                "rightPercent", rightMotor::getOutputPercent,
                "rightVelocity", rightMotor::getVelocity,

                "pitchOutput", pitchMotor::getOutputPercent,
                "carouselOutput", carousel::getOutputPercent,
                "carouselSwitch", carouselSwitch::get,
                "lights", () -> spike.get() == Relay.Value.kForward
        );
    }

    @Override
    public Map<String, Consumer<Object>> NTGets() {
        return Map.of(
                "leftK", doubleSetter(d -> leftK = d),
                "leftP", doubleSetter(d -> leftP = d),
                "leftI", doubleSetter(d -> leftI = d),
                "leftD", doubleSetter(d -> leftD = d),

                "rightK", doubleSetter(d -> rightK = d),
                "rightP", doubleSetter(d -> rightP = d),
                "rightI", doubleSetter(d -> rightI = d),
                "rightD", doubleSetter(d -> rightD = d),

                "leftSpeedDemand", doubleSetter(d -> leftSpeedDemand = d),
                "rightSpeedDemand", doubleSetter(d -> rightSpeedDemand = d)
        );
    }

    public Consumer<Object> doubleSetter(Consumer<Double> setter) {
        return (Object obj) -> {
            if (obj instanceof Double) {
                setter.accept((double) obj);
            }
        };
    }

    private long shooterStartTime = 0;

    public void shoot(boolean controlled) {
        // spin up motors and then carousel to shoot
        double carouselSpeed = .25;
        if (System.currentTimeMillis() - shooterStartTime > 2000 && !autoCarousel) {
            if (controlled) {
                carouselOutput = carouselSpeed;
                spinForShooter = true;
            } else {
                carousel.setPercentOutput(carouselSpeed);
            }
        }

        leftMotor.setVelocity(leftSpeedDemand); // 2600
        rightMotor.setVelocity(rightSpeedDemand); // 3000
    }

    PosControl aimPosControl = new PosControl(0, 1, 0.5, 0.2, 0.5);

    public void autoAim() {
        if(Vision.isStale()) {
            double calculatedSpeed = aimPosControl.getSpeed(Vision.getPitchOffset());

        }
    }
}