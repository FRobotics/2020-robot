package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Robot2020;
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

public class DriveTrain extends Subsystem<Robot2020> {

    // TODO: real motor ids
    private EncoderMotor leftMotor = new CANDriveMotorPair(new TalonSRX(Variables.DriveTrain.LEFT_MOTOR_MASTER_ID), new VictorSPX(Variables.DriveTrain.LEFT_MOTOR_FOLLOWER_ID), Variables.DriveTrain.CONFIG);
    private EncoderMotor rightMotor = new CANDriveMotorPair(new TalonSRX(Variables.DriveTrain.RIGHT_MOTOR_MASTER_ID), new VictorSPX(Variables.DriveTrain.RIGHT_MOTOR_FOLLOWER_ID), Variables.DriveTrain.CONFIG).invert();
    private DoubleSolenoid leftEvoShifter = new DoubleSolenoid(2,3);
    private DoubleSolenoid rightEvoShifter = new DoubleSolenoid(4,5);

    public List<SubsystemTimedAction<Robot2020>> TEST = Arrays.asList(
            new SubsystemTimedAction<>(() -> setVelocity(-3), 250),
            new SubsystemTimedAction<>(() -> setVelocity(3), 250)
    );

    public DriveTrain() {
        super("driveTrain");
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
    public void control(Robot2020 robot) {
        Controller controller = robot.driveController;

        double MAX_SPEED = 20;

        double fb = -adjustInput(controller.getAxis(Axis.LEFT_Y));
        double lr = adjustInput(controller.getAxis(Axis.RIGHT_X));

        double left = fb + (1 - Math.abs(fb)) * -lr;
        double right = fb + (1 - Math.abs(fb)) * lr;

        setLeftVelocity(left * MAX_SPEED);
        setRightVelocity(right * MAX_SPEED);

        /*if(controller.buttonPressed(Button.A)) {
            startActionQueue(TEST);
        }*/

        if(controller.buttonPressed(Button.LEFT_BUMPER)){
            leftEvoShifter.set(DoubleSolenoid.Value.kReverse);
            rightEvoShifter.set(DoubleSolenoid.Value.kReverse);
        }

        if(controller.buttonPressed(Button.RIGHT_BUMPER)){
            leftEvoShifter.set(DoubleSolenoid.Value.kForward);
            rightEvoShifter.set(DoubleSolenoid.Value.kForward);
        }
    }

    @Override
    public void stop(Robot2020 robot) {
        setVelocity(0);
    }

    private double adjustInput(double input) {
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
