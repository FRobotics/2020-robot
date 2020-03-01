package frc.robot.hailfire.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.input.Pov;
import frc.robot.base.subsystem.StandardDriveTrain;
import frc.robot.hailfire.IDs;
import frc.robot.base.device.motor.PhoenixMotorPair;
import frc.robot.base.device.DoubleSolenoid4150;
import frc.robot.base.device.motor.EncoderMotorConfig;

public class DriveTrain extends StandardDriveTrain {

    public static final EncoderMotorConfig LOW_CONFIG = new EncoderMotorConfig(
            3f/12f,
            4 * 360,
            2.02895,
            1.76430,
            0.00264,
            0.02205,
            150
    );

    public static final EncoderMotorConfig HIGH_CONFIG = new EncoderMotorConfig(
            3f/12f,
            4 * 360,
            0.5873,
            0.51069,
            0.00076,
            0.00638,
            150
    );

    /*
     * LOW MULT: 2.2053804347826086956521739130435
     * HIGH MULT: 0.63836956521739130434782608695652
     * OLD CONFIG
     * 0.92,
     * 0.8,
     * 0.0012,
     * 0.01,
     * 150
    */

    private DoubleSolenoid4150 leftEvoShifter = new DoubleSolenoid4150(
            IDs.DriveTrain.LEFT_EVO_SHIFTER_FORWARD,
            IDs.DriveTrain.LEFT_EVO_SHIFTER_REVERSE
    );
    private DoubleSolenoid4150 rightEvoShifter = new DoubleSolenoid4150(
            IDs.DriveTrain.RIGHT_EVO_SHIFTER_FORWARD,
            IDs.DriveTrain.RIGHT_EVO_SHIFTER_REVERSE
    );

    private boolean autoShift = false;

    /*
    public List<SubsystemTimedAction> ün_ün_ün = Arrays.asList(
            new SubsystemTimedAction(() -> setVelocity(-3), 250),
            new SubsystemTimedAction(() -> setVelocity(3), 250)
    );
     */

    public DriveTrain(Controller controller) {
        super(
                new PhoenixMotorPair(
                        new TalonSRX(IDs.DriveTrain.LEFT_MOTOR_MASTER),
                        new VictorSPX(IDs.DriveTrain.LEFT_MOTOR_FOLLOWER),
                        LOW_CONFIG
                ),
                new PhoenixMotorPair(
                        new TalonSRX(IDs.DriveTrain.RIGHT_MOTOR_MASTER),
                        new VictorSPX(IDs.DriveTrain.RIGHT_MOTOR_FOLLOWER),
                        LOW_CONFIG
                ).invert(),
                10, 19, controller);
    }

    @Override
    public void control() {
        super.control();

        // shift gears

        if(controller.buttonPressed(Button.LEFT_BUMPER)){
            shiftToLowGear();
            autoShift = false;
        }

        if (controller.buttonPressed(Button.RIGHT_BUMPER)) {
            shiftToHighGear();
            autoShift = false;
        }

        if(controller.getPov(Pov.D_PAD) >= 0) {
            autoShift = true;
        }

        if(autoShift) {
            if (
                Math.abs(getAverageDemand()) > 4.5
                && Math.abs(getAverageVelocity()) > 4.5
            ) {
                shiftToHighGear();
            }

            if (Math.abs(getAverageDemand()) < 4.25) {
                shiftToLowGear();
            }
        }
    }

    public void shiftToHighGear() {
        if(leftEvoShifter.extend() || rightEvoShifter.extend()) {
            setMaxSpeed(19);
            setMotorConfigs(HIGH_CONFIG);
        }
    }

    public void shiftToLowGear() {
        if(leftEvoShifter.retract() || rightEvoShifter.retract()) {
            setMaxSpeed(5.5);
            setMotorConfigs(LOW_CONFIG);
        }
    }
}
