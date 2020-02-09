package frc.robot.base.subsystem;

import frc.robot.Util;
import frc.robot.base.Robot4150;

import java.util.function.Consumer;

public class SSActionInstance<R extends Robot4150<R>> {

    public final Consumer<R> action;
    public final int length;

    public SSActionInstance(Consumer<R> action, int length) {
        this.action = action;
        this.length = length;
    }

    public SSActionInstance(Util.Action action, int length) {
        this.action = (R robot) -> action.run();
        this.length = length;
    }
}
