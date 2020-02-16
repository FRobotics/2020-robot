package frc.robot.base.subsystem;

import frc.robot.base.Robot;
import frc.robot.base.action.Action;
import frc.robot.base.action.Timed;

import java.util.function.Consumer;

public class SubsystemTimedAction extends SubsystemAction implements Timed {

    private long startTime;
    private int length;

    public SubsystemTimedAction(Action action, int length) {
        super(action);
        this.length = length;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.finishCondition = () -> (System.currentTimeMillis() - startTime) > length;
    }
}
