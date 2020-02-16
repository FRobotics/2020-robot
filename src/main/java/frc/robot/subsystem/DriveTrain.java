package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Variables;
import frc.robot.base.input.Axis;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.subsystem.SubsystemTimedAction;
import frc.robot.base.subsystem.motor.CANDriveMotorPair;
import frc.robot.base.subsystem.motor.EncoderMotor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class DriveTrain extends Subsystem {

    private Controller controller;

    private EncoderMotor leftMotor = new CANDriveMotorPair(new TalonSRX(Variables.DriveTrain.LEFT_MOTOR_MASTER_ID), new VictorSPX(Variables.DriveTrain.LEFT_MOTOR_FOLLOWER_ID), Variables.DriveTrain.CONFIG);
    private EncoderMotor rightMotor = new CANDriveMotorPair(new TalonSRX(Variables.DriveTrain.RIGHT_MOTOR_MASTER_ID), new VictorSPX(Variables.DriveTrain.RIGHT_MOTOR_FOLLOWER_ID), Variables.DriveTrain.CONFIG).invert();
    private DoubleSolenoid leftEvoShifter = new DoubleSolenoid(Variables.DriveTrain.LEFT_EVO_SHIFTER_FORWARD_ID,Variables.DriveTrain.LEFT_EVO_SHIFTER_REVERSE_ID);
    private DoubleSolenoid rightEvoShifter = new DoubleSolenoid(Variables.DriveTrain.RIGHT_EVO_SHIFTER_FORWARD_ID,Variables.DriveTrain.RIGHT_EVO_SHIFTER_REVERSE_ID);

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

        double fb = -adjustInput(controller.getAxis(Axis.LEFT_Y));
        double lr = adjustInput(controller.getAxis(Axis.RIGHT_X));

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

    private double adjustInput(double input) {
        double absInput = Math.abs(input);
        double DEAD_BAND = Variables.DriveTrain.JOYSTICK_DEAD_BAND;
        double deadBanded = absInput < DEAD_BAND ? 0 : (absInput - DEAD_BAND) * (1 / (1 - DEAD_BAND));
        double smoothed = Math.pow(deadBanded, 2);
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
