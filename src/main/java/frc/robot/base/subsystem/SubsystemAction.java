package frc.robot.base.subsystem;

import frc.robot.base.Robot;
import frc.robot.base.action.Action;
import frc.robot.base.action.GenericAction;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SubsystemAction<R extends Robot<R>> extends GenericAction<Consumer<R>> {
    public SubsystemAction(Consumer<R> action, Supplier<Boolean> finishCondition) {
        super(action, finishCondition);
    }

    public SubsystemAction(Action action, Supplier<Boolean> finishCondition) {
        this((R robot) -> action.run(), finishCondition);
    }

    public SubsystemAction(Consumer<R> action) {
        this(action, () -> false);
    }

    public SubsystemAction(Action action) {
        this(action, () -> false);
    }
}
