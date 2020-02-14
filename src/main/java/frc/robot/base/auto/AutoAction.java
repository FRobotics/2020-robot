package frc.robot.base.auto;

import frc.robot.base.action.Action;
import frc.robot.base.action.GenericAction;

import java.util.function.Supplier;

public class AutoAction extends GenericAction<Action> {
    public AutoAction(Action action, Supplier<Boolean> finishCondition) {
        super(action, finishCondition);
    }
    public AutoAction(Action action) {
        this(action, () -> false);
    }
    public AutoAction(Supplier<Boolean> finishCondition) {
        this(() -> {}, finishCondition);
    }
}
