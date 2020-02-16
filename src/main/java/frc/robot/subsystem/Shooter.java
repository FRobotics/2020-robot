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

import java.util.HashMap;
import java.util.function.Supplier;

public class Shooter extends Subsystem {

    Controller mainController;
    Controller carouselController;

    private Motor leftMotor = new CANMotor(new TalonSRX(IDs.Shooter.LEFT_MOTOR)).invert();
    private Motor rightMotor = new CANMotor(new TalonSRX(IDs.Shooter.RIGHT_MOTOR));
    private Motor yawMotor = new CANMotor(new TalonSRX(IDs.Shooter.YAW_MOTOR));
    private EncoderMotor pitchMotor = new CANMotor(new TalonSRX(IDs.Shooter.PITCH_MOTOR));
    private EncoderMotor carousel = new CANMotor(new TalonSRX(IDs.Shooter.CAROUSEL_MOTOR)).invert();

    private Relay spike = new Relay(0);

    public Shooter(Controller mainController, Controller carouselController) {
        super("shooter");
        this.mainController = mainController;
        this.carouselController = carouselController;
    }

    @Override
    public void stop() {
        leftMotor.setPercentOutput(0);
        rightMotor.setPercentOutput(0);
    }

    private long shooterStartTime = 0;

    @Override
    public void control() {

        if (mainController.getAxis(Axis.RIGHT_TRIGGER) > .5) {
            if(System.currentTimeMillis() - shooterStartTime > 500) {
                carousel.setPercentOutput(.5);
            }

            leftMotor.setPercentOutput(.86 * .9);
            rightMotor.setPercentOutput(.76 * .9);
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

        double speed = .25;

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
    public HashMap<String, Supplier<Object>> createNTMap() {
        return new HashMap<>() {{
            put("leftMotorPercent", leftMotor::getOutputPercent);
            put("rightMotorPercent", rightMotor::getOutputPercent);
            put("pitchMotorVelocity", pitchMotor::getVelocity);
            put("pitchMotorDistance", pitchMotor::getDistance);
            put("yawMotorVelocity", yawMotor::getOutputPercent);
            put("carouselVelocity", carousel::getVelocity);
            put("carouselDistance", carousel::getDistance);
        }};
    }
}