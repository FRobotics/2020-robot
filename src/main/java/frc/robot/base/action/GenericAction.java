package frc.robot.base.action;

import java.util.function.Supplier;

public class GenericAction<F> {
    public final F func;
    public Supplier<Boolean> finishCondition;
    public GenericAction(F func, Supplier<Boolean> finishCondition) {
        this.func = func;
        this.finishCondition = finishCondition;
    }

    public boolean isFinished() {
        return finishCondition.get();
    }
}
