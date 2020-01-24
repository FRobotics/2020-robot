package frc.robot.subsystem.base;

import frc.robot.Robot;
import frc.robot.RobotMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class Subsystem<S extends Enum<?>> {

    private ArrayList<S> stateQueue;
    private S state;
    private long stateStartTime;
    private HashMap<S, Long> stateTimeMap;

    /**
     * Creates a new subsystem
     *
     * @param initState the state to start in
     */
    public Subsystem(S initState) {
        this(initState, new HashMap<>() {{
        }});
    }

    /**
     * Creates a new subsystem
     *
     * @param stateTimeMap a map of states to the time in milliseconds each of them should run for
     * @param initState    the state to start in
     */
    public Subsystem(S initState, HashMap<S, Long> stateTimeMap) {
        this.stateQueue = new ArrayList<S>();
        this.state = initState;
        this.stateStartTime = 0;
        this.stateTimeMap = stateTimeMap;
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
    public abstract void handleState(Robot robot, S state);

    /**
     * Handles the states and calls handleState for the current one;
     * call this method in the robot's periodic methods
     * @param robot the robot
     */
    public void periodic(Robot robot) {
        if (
                !stateQueue.isEmpty() && stateTimeMap.get(state) != null
                        && System.currentTimeMillis() - stateStartTime > stateTimeMap.get(state)
        ) {
            this.setState(stateQueue.remove(0));
        }
        this.handleState(robot, state);
    }

    /**
     * Sets the state queue
     *
     * @param stateQueue a list of states to execute from first to last
     */
    public void setStateQueue(S[] stateQueue) {
        this.stateQueue = new ArrayList<>(Arrays.asList(stateQueue));
    }

    /**
     * Sets the current state
     *
     * @param state the state you want to switch to
     */
    public void setState(S state) {
        this.state = state;
        this.stateStartTime = System.currentTimeMillis();
    }
}
