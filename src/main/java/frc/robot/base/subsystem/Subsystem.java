package frc.robot.base.subsystem;

import frc.robot.base.RobotMode;
import frc.robot.base.util.action.Action;
import frc.robot.base.util.action.ActionHandler;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract class Subsystem extends ActionHandler {

    private final Action STOP = new Action(this::stop);
    private final Action CONTROL = new Action(this::control);

    public final String name;

    /**
     * Creates a new subsystem
     * @param name the name of the subsystem
     */
    public Subsystem(String name) {
        this.name = name;
        this.startActionAndSetDefault(STOP);
    }

    public void stop() {}
    public void control() {}

    /**
     * Use this method to specify what values you want to put on the dashboard;
     * The keys are the names of the entries and the suppliers are functions that return the values to set the entries to
     */
    public HashMap<String, Supplier<Object>> createNTMap() {
        return new HashMap<>();
    }

    /**
     * called during the beginning of each mode
     *
     * @param mode the mode the robot is in
     */
    public void onInit(RobotMode mode) {
        this.clearActionQueue();
        switch (mode) {
            case AUTONOMOUS:
            case DISABLED:
                startActionAndSetDefault(STOP);
                break;
            case TEST:
            case TELEOP:
                startActionAndSetDefault(CONTROL);
                break;
        }
    }
}
