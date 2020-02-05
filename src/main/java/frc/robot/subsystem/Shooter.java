package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Robot;
import frc.robot.base.RobotMode;
import frc.robot.base.input.Axis;
import frc.robot.base.Subsystem;
import frc.robot.base.motor.CANMotor;
import frc.robot.base.motor.Motor;
import frc.robot.base.input.Controller;

public class Shooter extends Subsystem<Shooter.State, Robot> {
    // 2 motors that spin the wheels to shoot the ball out
    public enum State {
        DISABLED, CONTROLLED
    }

    private Motor leftMotor = new CANMotor(new TalonSRX(0)); // TODO: device number
    private Motor rightMotor = new CANMotor(new TalonSRX(0)); // TODO: device number

    public Shooter() {
        super("shooter", State.CONTROLLED);
    }

    @Override
    public void onInit(RobotMode mode) {
        this.clearStateQueue();
        switch (mode) {
            case DISABLED:
                this.setStateAndDefault(State.DISABLED);
                break;
            case TELEOP:
                this.setStateAndDefault(State.CONTROLLED);
                break;
        }
    }

    @Override
    public void handleState(Robot robot, State state) {
        switch (state) {
            case DISABLED:
                leftMotor.setPercentOutput(0);
                rightMotor.setPercentOutput(0);
                break;
            case CONTROLLED:
                Controller controller = robot.auxiliaryController;
                if (controller.getAxis(Axis.RIGHT_TRIGGER) > .5) {
                    leftMotor.setPercentOutput(.5);
                    rightMotor.setPercentOutput(.5);
                } else {
                    leftMotor.setPercentOutput(0);
                    rightMotor.setPercentOutput(0);
                }
                break;
        }
    }
}
