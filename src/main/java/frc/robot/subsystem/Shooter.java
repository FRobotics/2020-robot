package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Relay;
import frc.robot.IDs;
import frc.robot.base.input.Axis;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.subsystem.motor.CANMotor;
import frc.robot.base.subsystem.motor.EncoderMotor;
import frc.robot.base.subsystem.motor.Motor;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Shooter extends Subsystem {

    Controller mainController;
    Controller carouselController;

    private EncoderMotor leftMotor = new CANMotor(new TalonSRX(IDs.Shooter.LEFT_MOTOR)).invert();
    private EncoderMotor rightMotor = new CANMotor(new TalonSRX(IDs.Shooter.RIGHT_MOTOR));
    private Motor yawMotor = new CANMotor(new TalonSRX(IDs.Shooter.YAW_MOTOR));
    private EncoderMotor pitchMotor = new CANMotor(new TalonSRX(IDs.Shooter.PITCH_MOTOR));
    private EncoderMotor carousel = new CANMotor(new TalonSRX(IDs.Shooter.CAROUSEL_MOTOR)).invert();

    private Relay spike = new Relay(0);

    public Shooter(Controller mainController, Controller carouselController) {
        super("shooter");
        this.mainController = mainController;
        this.carouselController = carouselController;
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
        if (mainController.getAxis(Axis.RIGHT_TRIGGER) > .5) {
            /*if(System.currentTimeMillis() - shooterStartTime > 1000) {
                carousel.setPercentOutput(.5);
            }

            leftMotor.setPercentOutput(.86 * .9);
            rightMotor.setPercentOutput(.76 * .9);*/
            leftMotor.setVelocity(leftSpeedDemand);
            rightMotor.setVelocity(rightSpeedDemand);
        } else {
            shooterStartTime = System.currentTimeMillis();

            leftMotor.setPercentOutput(0);
            rightMotor.setPercentOutput(0);

            if (carouselController.getAxis(Axis.LEFT_X) > 0.5) {
                carousel.setPercentOutput(.7); // TODO: pos control -> spin 1/5
            } else if(carouselController.getAxis(Axis.LEFT_X) < -0.5) {
                carousel.setPercentOutput(.7);
            } else {
                carousel.setPercentOutput(0);
            }
        }

        double speed = .125;

        if (mainController.buttonDown(Button.A)) {
            pitchMotor.setPercentOutput(speed);
        } else if (mainController.buttonDown(Button.Y)) {
            pitchMotor.setPercentOutput(-speed);
        } else {
            pitchMotor.setPercentOutput(0);
        }

        if (mainController.buttonDown(Button.B)) {
            yawMotor.setPercentOutput(speed);
        } else if (mainController.buttonDown(Button.X)) {
            yawMotor.setPercentOutput(-speed);
        } else {
            yawMotor.setPercentOutput(0);
        }

        // TODO: move this wherever
        if(mainController.buttonPressed(Button.LEFT_BUMPER)) {
            spike.set(spike.get() == Relay.Value.kForward ? Relay.Value.kReverse : Relay.Value.kForward);
        }
    }

    @Override
    public Map<String, Supplier<Object>> NTSets() {
        return Map.of(
            "leftPercent", leftMotor::getOutputPercent,
            "rightPercent", rightMotor::getOutputPercent,
            "leftVelocity", leftMotor::getVelocity,
            "pitchVelocity", pitchMotor::getVelocity,
            "pitchDistance", pitchMotor::getDistance,
            "yawVelocity", yawMotor::getOutputPercent,
            "carouselVelocity", carousel::getVelocity,
            "carouselDistance", carousel::getDistance
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