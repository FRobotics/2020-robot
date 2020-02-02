package frc.robot.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Robot;
import frc.robot.base.RobotMode;
import frc.robot.base.input.Button;
import frc.robot.base.StateInstance;
import frc.robot.base.Subsystem;
import frc.robot.base.input.Controller;

import java.util.Arrays;
import java.util.List;

public class Climber extends Subsystem<Climber.State, Robot> {

    public enum State {
        DISABLE, CONTROLLED, RAISE_BOTTOM, RAISE_TOP, LOWER_TOP, LOWER_BOTTOM
    }

    public List<StateInstance<State>> UNFOLD = Arrays.asList(
            new StateInstance<>(State.RAISE_BOTTOM, 500),
            new StateInstance<>(State.RAISE_TOP, 500)
    );

    public List<StateInstance<State>> FOLD = Arrays.asList(
            new StateInstance<>(State.LOWER_TOP, 500),
            new StateInstance<>(State.LOWER_BOTTOM, 500)
    );

    private DoubleSolenoid bottomSolenoid = new DoubleSolenoid(0, 0); // TODO: device number
    private DoubleSolenoid topSolenoid = new DoubleSolenoid(0, 0); // TODO: device number

    public Climber() {
        super("climber", State.DISABLE);
    }

    @Override
    public void onInit(RobotMode mode) {
        this.clearStateQueue();
        switch (mode) {
            default:
            case DISABLED:
                this.setStateAndDefault(State.DISABLE);
                break;
            case TELEOP:
                this.setStateAndDefault(State.CONTROLLED);
                break;
        }
    }

    @Override
    public void handleState(Robot robot, State state) {
        switch (state) {
            case CONTROLLED:
                Controller controller = robot.actionsController;
                if (controller.buttonPressed(Button.A)) {
                    if (bottomSolenoid.get() == DoubleSolenoid.Value.kForward) {
                        // switches whether it's up or down
                        topSolenoid.set(
                                topSolenoid.get() == DoubleSolenoid.Value.kForward
                                        ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward
                        );
                    } else {
                        setStateQueue(UNFOLD);
                    }
                }
                if (controller.buttonPressed(Button.B)) {
                    setStateQueue(FOLD);
                }
                break;
            case RAISE_BOTTOM:
                bottomSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case RAISE_TOP:
                topSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case LOWER_BOTTOM:
                bottomSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
            case LOWER_TOP:
                topSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
        }
    }
}
