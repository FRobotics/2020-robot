package frc.robot.subsystem.base;

import frc.robot.Robot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class Subsystem {

    private List<Integer> stateQueue;
    private int state;
    private long stateStartTime;
    private HashMap<Integer, Long> stateTimeMap;

    /**
     * Creates a new subsystem
     * @param stateTimeMap - a map of states to the time in milliseconds each of them should run for
     * @param initState - the state to start in
     */
    public Subsystem(HashMap<Integer, Long> stateTimeMap, int initState) {
        this(stateTimeMap, initState, new Integer[]{});
    }

    /**
     * Creates a new subsystem
     * @param stateTimeMap - a map of states to the time in milliseconds each of them should run for
     * @param initState - the state to start in
     * @param stateQueue - a list of states to execute after the init state
     */
    public Subsystem(HashMap<Integer, Long> stateTimeMap, int initState, Integer[] stateQueue) {
        this.stateQueue = Arrays.asList(stateQueue);
        this.state = initState;
        this.stateStartTime = 0;
        this.stateTimeMap = stateTimeMap;
    }

    /**
     * Use this method to run code during each state;
     * should not be called outside of this (Subsystem) class
     * @param state - the current state
     */
    public abstract void handleState(Robot robot, int state);

    /**
     * Handles the states and calls handleState for the current one;
     * call this method in the robot's periodic methods
p     */
    public void periodic(Robot robot) {
        if(
                !stateQueue.isEmpty() && stateTimeMap.get(state) != null
                && System.currentTimeMillis() - stateStartTime > stateTimeMap.get(state)
        ) {
            this.state = stateQueue.remove(0);
            this.stateStartTime = System.currentTimeMillis();
        }
        this.handleState(robot, state);
    }

    /**
     * Sets the state queue
     * @param stateQueue - a list of states to execute from first to last
     */
    public void setStateQueue(Integer[] stateQueue) {
        this.stateQueue = Arrays.asList(stateQueue);
    }
}
