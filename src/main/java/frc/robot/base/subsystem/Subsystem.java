package frc.robot.base.subsystem;

import frc.robot.base.RobotMode;
import frc.robot.base.action.Action;
import frc.robot.base.action.ActionHandler;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract class Subsystem extends ActionHandler<SubsystemAction, Action> {

    private final SubsystemAction STOP = new SubsystemAction(this::stop);
    private final SubsystemAction CONTROL = new SubsystemAction(this::control);

    public final String name;

    /**
     * Creates a new subsystem
     * @param name the name of the subsystem
     */
    public Subsystem(String name) {
        this.name = name;
        this.startActionAndSetDefault(STOP);
    }

    public abstract void stop();
    public abstract void control();

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

    /**
     * Handles the states and calls handleState for the current one;
     * call this method in the robot's periodic methods
     */
    public void periodic() {
        getAction().func.run();
        this.updateAction();
    }
}
