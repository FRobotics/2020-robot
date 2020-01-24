package frc.robot.subsystem;

/*
This should have 2 (private) EncoderMotors to set and measure the speed
The constructor should simply initialize them without taking any input to keep the motor ids in this class
The ids will be available later, I'll let whoever's working on this know them, just set them both to 0 for now
Also fyi the motors should both be CANDriveMotorPairs with TalonSRXs
It should also have a controlled state where the controllers are used to control it
For auto and manual usage, it would also be useful to make public methods to set the speed of each motor individually
These methods should be used to set the speed from then on, as rate limiting will eventually be added
Also, they will be useful to easily switch between closed loop and percent output in case something goes wrong
We'll probably get a gyro on the robot later, so that will have to be added eventually (so look at the 2019 code to prepare)
There should be getVelocity methods to measure the velocity which will be sent to the dashboard by other code
check https://github.com/FRobotics/robot-2019 for the drive logic
 */

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Robot;
import frc.robot.RobotMode;
import frc.robot.input.Axis;
import frc.robot.subsystem.base.Subsystem;
import frc.robot.subsystem.base.motor.CANDriveMotorPair;
import frc.robot.subsystem.base.motor.EncoderMotor;

import java.util.HashMap;

public class DriveTrain extends Subsystem<DriveTrain.State> {

    public enum State {
        CONTROLLED
    }

    private EncoderMotor leftMotor = new CANDriveMotorPair(new TalonSRX(14), new TalonSRX(13)).invert();
    private EncoderMotor rightMotor = new CANDriveMotorPair(new TalonSRX(10), new TalonSRX(12));

    public DriveTrain() {
        super(State.CONTROLLED);
    }

    public void setLeftMotorVelocity(double velocity) {
        this.leftMotor.setVelocity(velocity);
    }

    public void setRightMotorVelocity(double velocity) {
        this.rightMotor.setVelocity(velocity);
    }

    @Override
    public void handleState(Robot robot, State state) {
        switch(state) {
            case CONTROLLED:
                double leftY = robot.getMovementController().getAxis(Axis.LEFT_Y);
                double rightY = robot.getMovementController().getAxis(Axis.RIGHT_Y);

                setLeftMotorVelocity(leftY*5);
                setRightMotorVelocity(rightY*5);

                break;
        }
    }

    public double getLeftMotorDistance() {
        return this.leftMotor.getDistance();
    }

    public double getRightMotorDistance() {
        return this.rightMotor.getDistance();
    }
}
