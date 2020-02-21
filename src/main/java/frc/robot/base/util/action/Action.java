package frc.robot.base.util.action;

import java.util.function.Supplier;

public class Action {
    public final ActionFunc func;
    public Supplier<Boolean> finishCondition;

    public Action(ActionFunc func, Supplier<Boolean> finishCondition) {
        this.func = func;
        this.finishCondition = finishCondition;
    }

    public Action(ActionFunc func) {
        this(func, () -> false);
    }

    public Action(Supplier<Boolean> finishCondition) {
        this(() -> {}, finishCondition);
    }

    public boolean isFinished() {
        return finishCondition.get();
    }
}
