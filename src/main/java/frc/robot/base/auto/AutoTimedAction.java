package frc.robot.base.auto;

import frc.robot.base.action.Action;
import frc.robot.base.action.Timed;

public class AutoTimedAction extends AutoAction implements Timed {

    private long startTime;
    private int length;

    public AutoTimedAction(int length) {
        super(() -> {});
        this.length = length;
    }

    public AutoTimedAction(Action action, int length) {
        super(action);
        this.length = length;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.finishCondition = () -> (System.currentTimeMillis() - startTime) > length;
    }
}
