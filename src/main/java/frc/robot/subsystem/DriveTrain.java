package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.IDs;
import frc.robot.base.Util;
import frc.robot.base.input.Axis;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.subsystem.SubsystemTimedAction;
import frc.robot.base.subsystem.motor.CANDriveMotorPair;
import frc.robot.base.subsystem.motor.EncoderMotor;
import frc.robot.base.subsystem.motor.EncoderMotorConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class DriveTrain extends Subsystem {

    public static final EncoderMotorConfig CONFIG = new EncoderMotorConfig(
            3,
            0.92,
            0.8,
            0.0012,
            0.01,
            150
    );

    private Controller controller;

    private EncoderMotor leftMotor = new CANDriveMotorPair(
            new TalonSRX(IDs.DriveTrain.LEFT_MOTOR_MASTER),
            new VictorSPX(IDs.DriveTrain.LEFT_MOTOR_FOLLOWER),
            CONFIG
    );
    private EncoderMotor rightMotor = new CANDriveMotorPair(
            new TalonSRX(IDs.DriveTrain.RIGHT_MOTOR_MASTER),
            new VictorSPX(IDs.DriveTrain.RIGHT_MOTOR_FOLLOWER),
            CONFIG
    ).invert();
    private DoubleSolenoid leftEvoShifter = new DoubleSolenoid(
            IDs.DriveTrain.LEFT_EVO_SHIFTER_FORWARD,
            IDs.DriveTrain.LEFT_EVO_SHIFTER_REVERSE
    );
    private DoubleSolenoid rightEvoShifter = new DoubleSolenoid(
            IDs.DriveTrain.RIGHT_EVO_SHIFTER_FORWARD,
            IDs.DriveTrain.RIGHT_EVO_SHIFTER_REVERSE
    );

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public List<SubsystemTimedAction> ununun = Arrays.asList(
            new SubsystemTimedAction(() -> setVelocity(-3), 250),
            new SubsystemTimedAction(() -> setVelocity(3), 250)
    );

    public DriveTrain(Controller controller) {
        super("driveTrain");
        this.controller = controller;
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
    public void control() {
        double MAX_SPEED = 1;

        double fb = -Util.adjustInput(controller.getAxis(Axis.LEFT_Y), 0.2, 2);
        double lr = Util.adjustInput(controller.getAxis(Axis.RIGHT_X), 0.2, 2);

        double left = fb - lr;
        double right = fb + lr;

        leftMotor.setPercentOutput(left * MAX_SPEED);
        rightMotor.setPercentOutput(right * MAX_SPEED);

        /*if(controller.buttonPressed(Button.LEFT_BUMPER)){
            leftEvoShifter.set(DoubleSolenoid.Value.kReverse);
            rightEvoShifter.set(DoubleSolenoid.Value.kReverse);
        }*/

        if(controller.buttonPressed(Button.RIGHT_BUMPER)){
            leftEvoShifter.set(DoubleSolenoid.Value.kForward);
            rightEvoShifter.set(DoubleSolenoid.Value.kForward);
        }
    }

    @Override
    public void stop() {
        setVelocity(0);
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
