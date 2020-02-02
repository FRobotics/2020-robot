package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Robot;
import frc.robot.base.RobotMode;
import frc.robot.base.input.Button;
import frc.robot.base.Subsystem;
import frc.robot.base.motor.CANMotor;
import frc.robot.base.motor.Motor;
import frc.robot.base.input.Controller;

public class Spinner extends Subsystem<Spinner.State, Robot> {

    public enum State {
        DISABLED, CONTROLLED
    }

    private Motor motor = new CANMotor(new TalonSRX(0)); // TODO: device number

    public Spinner() {
        super("spinner", State.DISABLED);
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
                Controller controller = robot.actionsController;
                if (controller.buttonDown(Button.Y)) {
                    motor.setPercentOutput(.5);
                } else {
                    motor.setPercentOutput(0);
                }
                break;
        }
    }
}
