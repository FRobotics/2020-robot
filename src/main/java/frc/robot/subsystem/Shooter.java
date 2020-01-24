package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Robot;
import frc.robot.RobotMode;
import frc.robot.input.Button;
import frc.robot.subsystem.base.Subsystem;
import frc.robot.subsystem.base.motor.CANMotor;
import frc.robot.subsystem.base.motor.Motor;

import java.util.HashMap;

public class Shooter extends Subsystem<Shooter.State> {
    // 2 motors that spin the wheels to shoot the ball out
    public enum State {
        CONTROLLED
    }

    private Motor leftMotor = new CANMotor(new TalonSRX(0)); // TODO: device number
    private Motor rightMotor = new CANMotor(new TalonSRX(0)); // TODO: device number

    public Shooter() {
        super(State.CONTROLLED);
    }

    @Override
    public void onInit(RobotMode mode) {

    }

    @Override
    public void handleState(Robot robot, State state) {
        switch (state) {
            case CONTROLLED:
                if (robot.getActionsController().buttonDown(Button.X)) {
                    leftMotor.setPercentOutput(.5);
                    rightMotor.setPercentOutput(.5);
                } else {
                    leftMotor.setPercentOutput(0);
                    rightMotor.setPercentOutput(0);
                }
                break;
        }
    }
}
