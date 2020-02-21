package frc.robot.base.util.action;

@FunctionalInterface
public interface ActionFunc {
    void run();

    interface Timed {
        void start();
    }
}
