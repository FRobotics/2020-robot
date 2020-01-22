package frc.robot.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Robot;
import frc.robot.RobotMode;
import frc.robot.input.Button;
import frc.robot.subsystem.base.Subsystem;

import java.util.HashMap;

public class Climber extends Subsystem<Climber.State> {

    public enum State {
        CONTROLLED, RAISE_BOTTOM, RAISE_TOP, LOWER_TOP, LOWER_BOTTOM
    }

    private DoubleSolenoid bottomSolenoid = new DoubleSolenoid(0, 0);
    private DoubleSolenoid topSolenoid = new DoubleSolenoid(0, 0);

    public Climber() {
        super(new HashMap<>() {{
            put(State.RAISE_BOTTOM, 500L);
            put(State.RAISE_TOP, 500L);
            put(State.LOWER_BOTTOM, 500L);
            put(State.LOWER_TOP, 500L);
        }}, State.CONTROLLED, new State[]{});
    }

    @Override
    public void onInit(RobotMode mode) {

    }

    @Override
    public void handleState(Robot robot, State state) {
        switch (state) {
            case CONTROLLED:
                if (robot.getMovementController().buttonPressed(Button.A)){
                    if (bottomSolenoid.get() == DoubleSolenoid.Value.kForward) {
                        topSolenoid.set(
                                topSolenoid.get() == DoubleSolenoid.Value.kForward
                                        ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward
                        );
                    } else {
                        setState(State.RAISE_BOTTOM);
                        setStateQueue(new State[]{State.RAISE_TOP, State.CONTROLLED});
                    }
                }
                if (robot.getMovementController().buttonPressed(Button.B)){
                    setState(State.LOWER_TOP);
                    setStateQueue(new State[]{State.LOWER_BOTTOM, State.CONTROLLED});
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
