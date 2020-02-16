package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Relay;
import frc.robot.Robot2020;
import frc.robot.Variables;
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

    private Motor leftMotor = new CANMotor(new TalonSRX(Variables.Shooter.LEFT_MOTOR_ID)).invert();
    private Motor rightMotor = new CANMotor(new TalonSRX(Variables.Shooter.RIGHT_MOTOR_ID));
    private Motor yawMotor = new CANMotor(new TalonSRX(Variables.Shooter.YAW_MOTOR_ID));
    private EncoderMotor pitchMotor = new CANMotor(new TalonSRX(Variables.Shooter.PITCH_MOTOR_ID));
    private EncoderMotor carousel = new CANMotor(new TalonSRX(Variables.Shooter.CAROUSEL_MOTOR_ID)).invert();

    private Relay spike = new Relay(0);

    public Shooter() {
        super("shooter");
    }

    @Override
    public void stop(Robot2020 robot) {
        leftMotor.setPercentOutput(0);
        rightMotor.setPercentOutput(0);
    }

    private long shooterStartTime = 0;

    @Override
    public void control(Robot2020 robot) {
        Controller controller = robot.driveController;
        Controller carouselController = robot.auxiliaryController;

        if (controller.getAxis(Axis.RIGHT_TRIGGER) > .5) {
            if(System.currentTimeMillis() - shooterStartTime > 500) {
                carousel.setPercentOutput(Variables.Shooter.CAROUSEL_WHILE_SHOOTING);
            }

            leftMotor.setPercentOutput(Variables.Shooter.LEFT_MOTOR_SPEED);
            rightMotor.setPercentOutput(Variables.Shooter.RIGHT_MOTOR_SPEED);
        } else {
            shooterStartTime = System.currentTimeMillis();

            leftMotor.setPercentOutput(0);
            rightMotor.setPercentOutput(0);

            if (carouselController.getAxis(Axis.LEFT_X) > 0.5) {
                carousel.setPercentOutput(Variables.Shooter.CAROUSEL_ALONE); // TODO: pos control -> spin 1/5
            } else if(carouselController.getAxis(Axis.LEFT_X) < -0.5) {
                carousel.setPercentOutput(-Variables.Shooter.CAROUSEL_ALONE);
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

        // TODO: move this wherever
        if(controller.buttonPressed(Button.LEFT_BUMPER)) {
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