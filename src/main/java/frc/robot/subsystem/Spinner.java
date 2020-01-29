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
        DISABLED, CONTROLLED
    }

    private Motor motor = new CANMotor(new TalonSRX(0)); // TODO: device number

    public Spinner() {
        super(State.DISABLED);
    }

    @Override
    public void onInit(RobotMode mode) {
        switch (mode) {
            case DISABLED:
                this.setStateAndDefault(State.DISABLED);
                break;
            case TELEOP:
                this.setStateAndDefault(State.CONTROLLED);
                break;
        }
    }

    @Override
    public void handleState(Robot robot, State state) {
        switch (state) {
            case DISABLED:
                motor.setPercentOutput(0);
                break;
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
