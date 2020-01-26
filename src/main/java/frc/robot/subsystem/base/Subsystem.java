package frc.robot.subsystem.base;

import frc.robot.Robot;
import frc.robot.RobotMode;

import java.util.ArrayList;
import java.util.List;

public abstract class Subsystem<S extends Enum<?>> {

    private S state;
    private int stateLength;

    private int queuePos;
    private List<StateInstance<S>> stateQueue;

    private long stateStartTime;

    /**
     * Creates a new subsystem
     *
     * @param initState    the state to start in
     */
    public Subsystem(S initState) {
        this.stateQueue = new ArrayList<>();
        this.state = initState;
        this.stateStartTime = 0;
        this.stateLength = -1;
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
                stateQueue != null && System.currentTimeMillis() - stateStartTime > stateLength
        ) {
            StateInstance<S> instance = stateQueue.get(queuePos++);
            this.setState(instance.state, instance.length);
            if(stateQueue.size() == queuePos) {
                stateQueue = null;
                queuePos = 0;
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
