package frc.robot.base.util.action;

import java.util.List;

public abstract class ActionHandler {

    private Action action;
    private Action defaultAction;

    private int queuePos;
    private List<? extends Action> actionQueue;

    private boolean finished = true;

    public void startAction(Action action) {
        finished = false;
        this.action = action;
        if(this.action instanceof ActionFunc.Timed) {
            ((ActionFunc.Timed)this.action).start();
        }
    }

    public void startActionAndSetDefault(Action action) {
        this.startAction(action);
        this.defaultAction = action;
        finished = true;
    }

    public void periodic() {
        this.action.func.run();
        if (
                action.isFinished()
        ) {
            if(actionQueue != null) {
                if (queuePos == actionQueue.size()) {
                    // if there's no more states in the queue
                    actionQueue = null;
                    queuePos = 0;
                    this.startAction(defaultAction);
                    finished = true;
                } else {
                    // if there's another state in the queue
                    this.startAction(actionQueue.get(queuePos++));
                }
            } else {
                this.startAction(defaultAction);
                this.finished = true;
            }
        }
    }

    public void clearActionQueue() {
        this.queuePos = 0;
        this.actionQueue = null;
    }

    public Action getAction() {
        return action;
    }

    public void startActionQueue(List<? extends Action> actionQueue) {
        this.queuePos = 1;
        this.startAction(actionQueue.get(0));
        this.actionQueue = actionQueue;
    }

    public boolean isFinished() {
        return finished;
    }
}
