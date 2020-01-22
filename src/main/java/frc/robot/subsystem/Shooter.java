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

    private Motor leftMotor = new CANMotor(new TalonSRX(0));
    private Motor rightMotor = new CANMotor(new TalonSRX(0));

    public Shooter() {
        super(new HashMap<>() {{

        }}, State.CONTROLLED, new State[]{});
    }

    @Override
    public void onInit(RobotMode mode) {

    }

    @Override
    public void handleState(Robot robot, State state) {
        switch (state) {
            case CONTROLLED:
                if (robot.getMovementController().buttonDown(Button.X)) {
                    leftMotor.setPercentOutput(1);
                    rightMotor.setPercentOutput(1);
                } else {
                    leftMotor.setPercentOutput(0);
                    rightMotor.setPercentOutput(0);
                }
                break;
        }
    }

}
