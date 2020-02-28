package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Relay;
import frc.robot.IDs;
import frc.robot.base.input.Axis;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.subsystem.motor.PhoenixMotor;
import frc.robot.base.subsystem.motor.EncoderMotor;
import frc.robot.base.subsystem.motor.EncoderMotorConfig;
import frc.robot.base.subsystem.motor.Motor;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Shooter extends Subsystem {

    private Controller auxController;
    private Controller driveController;

    private EncoderMotorConfig config = new EncoderMotorConfig(2048 * 4, 0.92, 0.8, 0.0012, .01, 150);

    private EncoderMotor leftMotor = new PhoenixMotor(new TalonSRX(IDs.Shooter.LEFT_MOTOR), config).invert();
    private EncoderMotor rightMotor = new PhoenixMotor(new TalonSRX(IDs.Shooter.RIGHT_MOTOR), config);

    private Motor yawMotor = new PhoenixMotor(new TalonSRX(IDs.Shooter.YAW_MOTOR));
    private Motor pitchMotor = new PhoenixMotor(new TalonSRX(IDs.Shooter.PITCH_MOTOR));
    private Motor carousel = new PhoenixMotor(new TalonSRX(IDs.Shooter.CAROUSEL_MOTOR)).invert();
    //private DigitalInput carouselSwitch = new DigitalInput(0);

    private Relay spike = new Relay(0);

    public Shooter(Controller mainController, Controller carouselController) {
        super("shooter");
        this.auxController = mainController;
        this.driveController = carouselController;
    }

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

    @Override
    public void stop() {
        leftMotor.setPercentOutput(0);
        rightMotor.setPercentOutput(0);
    }

    private long shooterStartTime = 0;

    @Override
    public void control() {
        if (driveController.getAxis(Axis.RIGHT_TRIGGER) > .5) {

            // spin up motors and then carousel to shoot

            if(System.currentTimeMillis() - shooterStartTime > 1000) {
                carousel.setPercentOutput(.5);
            }

            leftMotor.setPercentOutput(.86);
            rightMotor.setPercentOutput(.76);
            //leftMotor.setVelocity(leftSpeedDemand);
            //rightMotor.setVelocity(rightSpeedDemand);
        } else {
            shooterStartTime = System.currentTimeMillis();

            // (if not shooting) control the carousel

            leftMotor.setPercentOutput(0);
            rightMotor.setPercentOutput(0);

            if (auxController.getAxis(Axis.LEFT_X) > 0.5) {
                carousel.setPercentOutput(.7);
            } else if(auxController.getAxis(Axis.LEFT_X) < -0.5) {
                carousel.setPercentOutput(-.7);
            } else {
                carousel.setPercentOutput(0);
            }
        }

        // move carousel up/down

        if (driveController.buttonDown(Button.A)) {
            pitchMotor.setPercentOutput(.125);
        } else if (driveController.buttonDown(Button.Y)) {
            pitchMotor.setPercentOutput(-0.5);
        } else {
            pitchMotor.setPercentOutput(0);
        }

        // move carousel left/right

        if (driveController.buttonDown(Button.B)) {
            yawMotor.setPercentOutput(0.5);
        } else if (driveController.buttonDown(Button.X)) {
            yawMotor.setPercentOutput(-0.5);
        } else {
            yawMotor.setPercentOutput(0);
        }

        // turn on lights

        if(auxController.buttonPressed(Button.LEFT_BUMPER)) {
            spike.set(spike.get() == Relay.Value.kForward ? Relay.Value.kReverse : Relay.Value.kForward);
        }
    }

    @Override
    public Map<String, Supplier<Object>> NTSets() {
        return Map.of(
            "leftPercent", leftMotor::getOutputPercent,
            "rightPercent", rightMotor::getOutputPercent,
            "leftVelocity", leftMotor::getVelocity,
            "rightVelocity", rightMotor::getVelocity,
            
            "pitchOutput", pitchMotor::getOutputPercent,
            "yawOutput", yawMotor::getOutputPercent,
            "carouselOutput", carousel::getOutputPercent
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
            if(obj instanceof Double) {
                setter.accept((double)obj);
            }
        };
    }
}