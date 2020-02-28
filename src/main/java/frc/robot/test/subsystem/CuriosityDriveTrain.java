package frc.robot.test.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.StandardDriveTrain;
import frc.robot.base.subsystem.motor.CANDriveMotorPair;
import frc.robot.base.subsystem.motor.EncoderMotorConfig;

public class CuriosityDriveTrain extends StandardDriveTrain {

    public static final EncoderMotorConfig CONFIG = new EncoderMotorConfig(
            3,
            360 * 4,
            0.92,
            0.8,
            0.0012,
            0.01,
            150
    );

    public CuriosityDriveTrain(Controller controller) {
        super(
                new CANDriveMotorPair(
                        new TalonSRX(14),
                        new TalonSRX(13),
                        CONFIG
                ),
                new CANDriveMotorPair(
                        new TalonSRX(10),
                        new TalonSRX(12),
                        CONFIG
                ).invert(),
                5, 10, controller
        );
    }
}
