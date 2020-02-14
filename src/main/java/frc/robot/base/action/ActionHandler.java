package frc.robot.base.action;

import java.util.List;

public abstract class ActionHandler <A extends GenericAction<F>, F> {

    private A action;
    private A defaultAction;

    private int queuePos;
    private List<? extends A> actionQueue;

    private boolean finished = true;

    public void startAction(A action) {
        finished = false;
        this.action = action;
        if(this.action instanceof Timed) {
            ((Timed)this.action).start();
        }
    }

    public void startActionAndSetDefault(A action) {
        this.startAction(action);
        this.defaultAction = action;
        finished = true;
    }

    public void updateAction() {
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

    public A getAction() {
        return action;
    }

    public void startActionQueue(List<? extends A> actionQueue) {
        this.queuePos = 1;
        this.startAction(actionQueue.get(0));
        this.actionQueue = actionQueue;
    }

    public boolean isFinished() {
        return finished;
    }
}
