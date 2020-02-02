package frc.robot.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public abstract class Subsystem<S extends Enum<?>, R extends Robot4150<R>> {

    public final String name;

    private S state;
    private int stateLength;
    private long stateStartTime;

    private S defaultState;

    private int queuePos;
    private List<StateInstance<S>> stateQueue;

    /**
     * Creates a new subsystem
     *
     * @param initState    the state to start in
     */
    public Subsystem(String name, S initState) {
        this.name = name;
        this.stateQueue = new ArrayList<>();
        this.state = initState;
        this.stateStartTime = 0;
        this.stateLength = -1;
        this.defaultState = initState;
    }

    /**
     * called during the beginning of each mode
     *
     * @param mode the mode the robot is in
     */
    public void onInit(RobotMode mode) {
    }

    /**
     * Use this method to run code during each state;
     * should not be called outside of this (Subsystem) class
     *
     * @param robot the robot
     * @param state the current state
     */
    public abstract void handleState(R robot, S state);

    /**
     * Use this method to specify what values you want to put on the dashboard;
     * The keys are the names of the entries and the suppliers are functions that return the values to set the entries to
     */
    public HashMap<String, Supplier<Object>> createNTMap() {
        return new HashMap<>();
    }

    /**
     * Handles the states and calls handleState for the current one;
     * call this method in the robot's periodic methods
     * @param robot the robot
     */
    public void periodic(R robot) {
        if (
                stateQueue != null && System.currentTimeMillis() - stateStartTime > stateLength
        ) {
            if(stateQueue.size() == queuePos) {
                // if there's no more states in the queue
                stateQueue = null;
                queuePos = 0;
                this.setState(defaultState);
            } else {
                // if there's another state in the queue
                StateInstance<S> instance = stateQueue.get(queuePos++);
                this.setState(instance.state, instance.length);
            }
        }
        this.handleState(robot, state);
    }

    /**
     * Sets the state queue
     *
     * @param stateQueue a list of states to execute from first to last
     */
    public void setStateQueue(List<StateInstance<S>> stateQueue) {
        this.stateQueue = stateQueue;
    }

    public void clearStateQueue() {
        this.stateQueue = null;
    }

    /**
     * Sets the current state
     *
     * @param state the state you want to switch to
     */
    public void setState(S state) {
        this.setState(state, -1);
    }

    public void setDefaultState(S defaultState) {
        this.defaultState = defaultState;
    }

    public void setStateAndDefault(S state) {
        this.setState(state);
        this.setDefaultState(state);
    }

    /**
     * Sets the current state
     *
     * @param state the state you want to switch to
     * @param length how long the state should run
     */
    public void setState(S state, int length) {
        this.state = state;
        this.stateStartTime = System.currentTimeMillis();
        this.stateLength = length;
    }
}
