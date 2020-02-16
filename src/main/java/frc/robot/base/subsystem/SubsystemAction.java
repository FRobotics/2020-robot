package frc.robot.base.subsystem;

import frc.robot.base.action.Action;
import frc.robot.base.action.GenericAction;

import java.util.function.Supplier;

public class SubsystemAction extends GenericAction<Action> {
    public SubsystemAction(Action action, Supplier<Boolean> finishCondition) {
        super(action, finishCondition);
    }

    public SubsystemAction(Action action) {
        this(action, () -> false);
    }
}
