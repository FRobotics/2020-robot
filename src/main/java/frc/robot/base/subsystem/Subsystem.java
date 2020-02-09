package frc.robot.base.subsystem;

import frc.robot.base.Robot4150;
import frc.robot.base.RobotMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Subsystem<R extends Robot4150<R>> {

    public final String name;

    private Consumer<R> currentAction;
    private int actionLength;
    private long actionStartTime;

    private Consumer<R> defaultAction;

    private int queuePos;
    private List<SSActionInstance<R>> actionQueue;

    /**
     * Creates a new subsystem
     *
     */
    public Subsystem(String name) {
        this.name = name;
        this.actionQueue = new ArrayList<>();
        this.currentAction = this::stop;
        this.actionStartTime = 0;
        this.actionLength = -1;
        this.defaultAction = this::stop;
    }

    public abstract void stop(R robot);
    public abstract void control(R robot);

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
        actionQueue = null;
        switch (mode) {
            case AUTONOMOUS:
            case DISABLED:
                setActionAndDefault(this::stop);
                break;
            case TEST:
            case TELEOP:
                setActionAndDefault(this::control);
                break;
        }
    }

    /**
     * Handles the states and calls handleState for the current one;
     * call this method in the robot's periodic methods
     * @param robot the robot
     */
    public void periodic(R robot) {
        if (
                actionQueue != null && System.currentTimeMillis() - actionStartTime > actionLength
        ) {
            if(actionQueue.size() == queuePos) {
                // if there's no more states in the queue
                actionQueue = null;
                queuePos = 0;
                this.setCurrentAction(defaultAction);
            } else {
                // if there's another state in the queue
                SSActionInstance<R> instance = actionQueue.get(queuePos++);
                this.setAction(instance.action, instance.length);
            }
        }
        this.currentAction.accept(robot);
    }

    /**
     * Sets the state queue
     *
     * @param actionQueue a list of states to execute from first to last
     */
    public void setActionQueue(List<SSActionInstance<R>> actionQueue) {
        this.actionQueue = actionQueue;
    }

    /**
     * Sets the current state
     *
     * @param currentAction the state you want to switch to
     */
    public void setCurrentAction(Consumer<R> currentAction) {
        this.setAction(currentAction, -1);
    }

    public void setDefaultAction(Consumer<R> defaultAction) {
        this.defaultAction = defaultAction;
    }

    public void setActionAndDefault(Consumer<R> state) {
        this.setCurrentAction(state);
        this.setDefaultAction(state);
    }

    /**
     * Sets the current state
     *
     * @param state the state you want to switch to
     * @param length how long the state should run
     */
    public void setAction(Consumer<R> state, int length) {
        this.currentAction = state;
        this.actionStartTime = System.currentTimeMillis();
        this.actionLength = length;
    }
}
