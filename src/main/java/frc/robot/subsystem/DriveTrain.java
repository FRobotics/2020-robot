package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.IDs;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.StandardDriveTrain;
import frc.robot.base.subsystem.motor.PhoenixDriveMotorPair;
import frc.robot.base.subsystem.motor.EncoderMotorConfig;

public class DriveTrain extends StandardDriveTrain {

    public static final EncoderMotorConfig CONFIG = new EncoderMotorConfig(
            3f/12f,
            4 * 360,
            0.92,
            0.8,
            0.0012,
            0.01,
            150
    );

    private DoubleSolenoid leftEvoShifter = new DoubleSolenoid(
            IDs.DriveTrain.LEFT_EVO_SHIFTER_FORWARD,
            IDs.DriveTrain.LEFT_EVO_SHIFTER_REVERSE
    );
    private DoubleSolenoid rightEvoShifter = new DoubleSolenoid(
            IDs.DriveTrain.RIGHT_EVO_SHIFTER_FORWARD,
            IDs.DriveTrain.RIGHT_EVO_SHIFTER_REVERSE
    );

    /*
    public List<SubsystemTimedAction> ün_ün_ün = Arrays.asList(
            new SubsystemTimedAction(() -> setVelocity(-3), 250),
            new SubsystemTimedAction(() -> setVelocity(3), 250)
    );
     */

    public DriveTrain(Controller controller) {
        super(
                new PhoenixDriveMotorPair(
                        new TalonSRX(IDs.DriveTrain.LEFT_MOTOR_MASTER),
                        new VictorSPX(IDs.DriveTrain.LEFT_MOTOR_FOLLOWER),
                        CONFIG
                ),
                new PhoenixDriveMotorPair(
                        new TalonSRX(IDs.DriveTrain.RIGHT_MOTOR_MASTER),
                        new VictorSPX(IDs.DriveTrain.RIGHT_MOTOR_FOLLOWER),
                        CONFIG
                ).invert(),
                5, 10, controller);
    }

    @Override
    public void control() {
        super.control();

        if(getController().buttonPressed(Button.LEFT_BUMPER)){
            leftEvoShifter.set(DoubleSolenoid.Value.kReverse);
            rightEvoShifter.set(DoubleSolenoid.Value.kReverse);
        }

        if (getController().buttonPressed(Button.RIGHT_BUMPER)) {
            leftEvoShifter.set(DoubleSolenoid.Value.kForward);
            rightEvoShifter.set(DoubleSolenoid.Value.kForward);
        }
    }
}
