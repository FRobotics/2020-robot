package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Robot2020;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.input.Axis;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.motor.CANMotor;
import frc.robot.base.subsystem.motor.EncoderMotor;
import frc.robot.base.subsystem.motor.Motor;

import java.util.HashMap;
import java.util.function.Supplier;

public class Shooter extends Subsystem<Robot2020> {

    private Motor leftMotor = new CANMotor(new TalonSRX(0)); // TODO: device number u
    private Motor rightMotor = new CANMotor(new TalonSRX(0)); // TODO: device number n
    private Motor yawMotor = new CANMotor(new TalonSRX(0)); // TODO: device number u
    private EncoderMotor pitchMotor = new CANMotor(new TalonSRX(0)); // TODO: device number n
    private EncoderMotor carousel = new CANMotor(new TalonSRX(0)); // TODO: device number u

    public Shooter() {
        super("shooter");
    }

    @Override
    public void stop(Robot2020 robot) {
        leftMotor.setPercentOutput(0);
        rightMotor.setPercentOutput(0);
    }

    @Override
    public void control(Robot2020 robot) {
        Controller controller = robot.driveController;
        if (controller.getAxis(Axis.RIGHT_TRIGGER) > .5) {
            leftMotor.setPercentOutput(.5);
            rightMotor.setPercentOutput(.5);
            carousel.setPercentOutput(.5);
        } else {
            leftMotor.setPercentOutput(0);
            rightMotor.setPercentOutput(0);
            if (controller.buttonPressed(Button.X)) {
                carousel.setPercentOutput(0.25); // TODO: pos control -> spin 1/5
            } else {
                carousel.setPercentOutput(0);
            }
        }

        double speed = .25;

        if (controller.buttonDown(Button.A)) {
            pitchMotor.setPercentOutput(speed);
        } else if (controller.buttonDown(Button.Y)) {
            pitchMotor.setPercentOutput(-speed);
        } else {
            pitchMotor.setPercentOutput(0);
        }

        if (controller.buttonDown(Button.B)) {
            yawMotor.setPercentOutput(speed);
        } else if (controller.buttonDown(Button.X)) {
            yawMotor.setPercentOutput(-speed);
        } else {
            yawMotor.setPercentOutput(0);
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