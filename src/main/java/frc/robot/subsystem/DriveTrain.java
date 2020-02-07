package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Robot;
import frc.robot.base.RobotMode;
import frc.robot.base.input.Axis;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.StateInstance;
import frc.robot.base.Subsystem;
import frc.robot.base.motor.CANDriveMotorPair;
import frc.robot.base.motor.EncoderMotor;
import frc.robot.base.motor.EncoderMotorConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class DriveTrain extends Subsystem<DriveTrain.State, Robot> {

    // TODO: update for 2020

    public static final EncoderMotorConfig driveConfig = new EncoderMotorConfig(
            3,
            0.92,
            0.8,
            0.0012,
            0.01,
            150
    );

    public enum State {
        DISABLED,
        CONTROLLED,
        TEST1,
        TEST2
    }

    public List<StateInstance<State>> TEST = Arrays.asList(
            new StateInstance<>(State.TEST1, 250),
            new StateInstance<>(State.TEST2, 250)
    );

    // TODO: real motor ids
    private EncoderMotor leftMotor = new CANDriveMotorPair(new TalonSRX(14), new TalonSRX(13), driveConfig);
    private EncoderMotor rightMotor = new CANDriveMotorPair(new TalonSRX(10), new TalonSRX(12), driveConfig).invert();
    private DoubleSolenoid leftEvoShifter = new DoubleSolenoid(6,1);
    private DoubleSolenoid rightEvoShifter = new DoubleSolenoid(6,1);

    public DriveTrain() {
        super("driveTrain", State.DISABLED);
    }

    private double leftTargetVel = 0;
    public void setLeftVelocity(double velocity) {
        this.leftTargetVel = velocity;
        this.leftMotor.setVelocity(velocity);
    }

    private double rightTargetVel = 0;
    public void setRightVelocity(double velocity) {
        this.rightTargetVel = velocity;
        this.rightMotor.setVelocity(velocity);
    }

    public void setVelocity(double velocity) {
        setLeftVelocity(velocity);
        setRightVelocity(velocity);
    }

    @Override
    public void onInit(RobotMode mode) {
        this.clearStateQueue();
        switch(mode) {
            default:
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
        switch(state) {
            case DISABLED:
                setVelocity(0);
                break;
            case CONTROLLED:
                Controller controller = robot.driveController;

                double MAX_SPEED = 5;

                double fb = -ununun(controller.getAxis(Axis.LEFT_Y));
                double lr = ununun(controller.getAxis(Axis.RIGHT_X));

                double left = fb + (1 - Math.abs(fb)) * lr;
                double right = fb + (1 - Math.abs(fb)) * -lr;

                setLeftVelocity(left * MAX_SPEED);
                setRightVelocity(right * MAX_SPEED);

                if(controller.buttonPressed(Button.A)) {
                    setStateQueue(TEST);
                }

                if(controller.buttonPressed(Button.LEFT_BUMPER)){
                    leftEvoShifter.set(DoubleSolenoid.Value.kReverse);
                    rightEvoShifter.set(DoubleSolenoid.Value.kReverse);
                }

                if(controller.buttonPressed(Button.RIGHT_BUMPER)){
                    leftEvoShifter.set(DoubleSolenoid.Value.kForward);
                    rightEvoShifter.set(DoubleSolenoid.Value.kForward);
                }

                break;
            case TEST1:
                setVelocity(-3);
                break;
            case TEST2:
                setVelocity(3);
                break;
        }
    }

    private double ununun(double input) {
        double absInput = Math.abs(input);
        double DEADBAND = 0.2;
        double deadbanded = absInput < DEADBAND ? 0 : (absInput - DEADBAND) * (1 / (1 - DEADBAND));
        double smoothed = Math.pow(deadbanded, 2);
        return input > 0 ? smoothed : -smoothed;
    }

    @Override
    public HashMap<String, Supplier<Object>> createNTMap() {
        return new HashMap<>(){{
            put("leftVelocity", leftMotor::getVelocity);
            put("rightVelocity", rightMotor::getVelocity);
            put("leftDistance", leftMotor::getDistance);
            put("rightDistance", rightMotor::getDistance);
            put("leftTarget", () -> leftTargetVel);
            put("rightTarget", () -> rightTargetVel);
        }};
    }
}
