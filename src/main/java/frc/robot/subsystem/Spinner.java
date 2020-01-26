package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Robot;
import frc.robot.RobotMode;
import frc.robot.input.Button;
import frc.robot.subsystem.base.Subsystem;
import frc.robot.subsystem.base.motor.CANMotor;
import frc.robot.subsystem.base.motor.Motor;

public class Spinner extends Subsystem<Spinner.State> {

    public enum State {
        CONTROLLED
    }

    private Motor motor = new CANMotor(new TalonSRX(0)); // TODO: device number

    public Spinner() {
        super(State.CONTROLLED);
    }

    // TODO: disabled + default states

    @Override
    public void handleState(Robot robot, State state) {
        switch (state) {
            case CONTROLLED:
                if (robot.getActionsController().buttonDown(Button.Y)) {
                    motor.setPercentOutput(.5);
                } else {
                    motor.setPercentOutput(0);
                }
                break;
        }
    }
}
