package frc.robot.subsystem.base;

public class StateInstance<S extends Enum<?>> {

    public final S state;
    public final int length;

    public StateInstance(S state, int length) {
        this.state = state;
        this.length = length;
    }
}
