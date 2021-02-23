package frc.robot.hailfire.subsystem;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.StandardDriveTrain;
import frc.robot.base.util.PosControl;
import frc.robot.hailfire.Controls;
import frc.robot.hailfire.IDs;
import frc.robot.base.device.motor.PhoenixMotorPair;
import frc.robot.base.device.DoubleSolenoid4150;
import frc.robot.hailfire.MotorConfig;
import frc.robot.hailfire.Vision;

public class DriveTrain extends StandardDriveTrain {

    public final ADIS16448_IMU gyro = new ADIS16448_IMU();

    private static final double LOW_MAX_SPEED = 5.5;

    private DoubleSolenoid4150 evoShifter = new DoubleSolenoid4150(
            IDs.DriveTrain.LEFT_EVO_SHIFTER_FORWARD,
            IDs.DriveTrain.LEFT_EVO_SHIFTER_REVERSE
    );

    private boolean autoShift = false;

    public DriveTrain(Controller controller) {
        super(
                new PhoenixMotorPair(
                        new TalonSRX(IDs.DriveTrain.LEFT_MOTOR_MASTER),
                        new VictorSPX(IDs.DriveTrain.LEFT_MOTOR_FOLLOWER),
                        MotorConfig.DriveTrain.LOW_CONFIG
                ),
                new PhoenixMotorPair(
                        new TalonSRX(IDs.DriveTrain.RIGHT_MOTOR_MASTER),
                        new VictorSPX(IDs.DriveTrain.RIGHT_MOTOR_FOLLOWER),
                        MotorConfig.DriveTrain.LOW_CONFIG
                ).invert(),
                10, 19, LOW_MAX_SPEED, controller);
        setMaxScaleShift(-1.35); // this makes setVelOrPercent scale better for velocity control
    }

    @Override
    public void control() {

        double turnSpeed = 0.2;

        if(controller.buttonDown(Controls.DriveTrain.TURN_RIGHT)){
            setLeftVelOrPercent(-turnSpeed);
            setRightVelOrPercent(turnSpeed);
        } else if(controller.buttonDown(Controls.DriveTrain.TURN_LEFT)){
            setLeftVelOrPercent(turnSpeed);
            setRightVelOrPercent(-turnSpeed);
        } else {
            super.control();
        }

        // shift gears

        if(controller.buttonPressed(Controls.DriveTrain.LOW_GEAR)){
            shiftToLowGear();
            autoShift = false;
        }

        if (controller.buttonPressed(Controls.DriveTrain.HIGH_GEAR)) {
            shiftToHighGear();
            autoShift = false;
        }

        if(controller.getPov(Controls.DriveTrain.AUTO_SHIFT) >= 0) {
            autoShift = true;
        }

        if(autoShift) {
            if (
                Math.abs(getAverageDemand()) > 5
                && Math.abs(getAverageVelocity()) > 5
            ) {
                shiftToHighGear();
            }

            if (
                Math.abs(getAverageDemand()) < 4.5
                && Math.abs(getAverageVelocity()) < 4.5
            ) {
                shiftToLowGear();
            }
        }
    }

    public void shiftToHighGear() {
        if(evoShifter.extend()) {
            setMotorConfigs(MotorConfig.DriveTrain.HIGH_CONFIG);
            setCurrentMaxSpeed(getAbsoluteMaxSpeed());
        }
    }

    public void shiftToLowGear() {
        if(evoShifter.retract()) {
            setMotorConfigs(MotorConfig.DriveTrain.LOW_CONFIG);
            setCurrentMaxSpeed(LOW_MAX_SPEED);
        }
    }

    PosControl aimPosControl = new PosControl(0, 1, 0.5, 0.2, 0.5);;

    public void autoAim() {
        if(!Vision.isStale()) {
            double calculatedSpeed = aimPosControl.getSpeed(Vision.getYawOffset());
            this.setLeftVelOrPercent(-calculatedSpeed);
            this.setRightVelOrPercent(calculatedSpeed);
        }
    }
}
