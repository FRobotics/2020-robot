package frc.robot.base.util.action;

public class TimedAction extends Action implements ActionFunc.Timed {

    private long startTime;
    private int length;

    public TimedAction(int length) {
        super(() -> {});
        this.length = length;
    }

    public TimedAction(ActionFunc action, int length) {
        super(action);
        this.length = length;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.finishCondition = () -> (System.currentTimeMillis() - startTime) > length;
    }
}
