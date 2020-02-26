package frc.robot.base.subsystem.motor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

public class CANMotor implements Motor, EncoderMotor {

    private double inputMultiplier;
    private double outputMultiplier;
    private double distanceMultiplier;
    private BaseMotorController motor;

    public CANMotor(BaseMotorController motor) {
        this(motor, null);
    }

    public CANMotor(BaseMotorController motor, EncoderMotorConfig config) {
        this.motor = motor;
        this.motor.setNeutralMode(NeutralMode.Brake);
        this.motor.setSensorPhase(false);
        if(config != null) {
            setConfig(config);
        } else {
            inputMultiplier = 1;
            outputMultiplier = 1;
            distanceMultiplier = 1;
        }
    }

    @Override
    public void setConfig(EncoderMotorConfig config) {
        int slotIdx = config.PID_LOOP_INDEX;
        int timeoutMS = config.TIMEOUT_MS;

        this.motor.config_kF(slotIdx, config.F, timeoutMS);
        this.motor.config_kP(slotIdx, config.P, timeoutMS);
        this.motor.config_kI(slotIdx, config.I, timeoutMS);
        this.motor.config_kD(slotIdx, config.D, timeoutMS);
        this.motor.config_IntegralZone(slotIdx, config.INTEGRAL_ZONE, timeoutMS);

        this.motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, slotIdx, timeoutMS);

        inputMultiplier = config.INPUT_MULTIPLIER;
        outputMultiplier = config.OUTPUT_MULTIPLIER;
        distanceMultiplier = config.DISTANCE_MULTIPLIER;
    }

    @Override
    public void setVelocity(double velocity) {
        motor.set(ControlMode.Velocity, velocity * outputMultiplier);
    }

    @Override
    public void setPercentOutput(double percent) {
        motor.set(ControlMode.PercentOutput, percent);
    }

    @Override
    public double getOutputPercent() {
        return motor.getMotorOutputPercent();
    }

    @Override
    public CANMotor setInverted(boolean inverted) {
        motor.setInverted(inverted);
        return this;
    }

    @Override
    public CANMotor invert() {
        motor.setInverted(!motor.getInverted());
        return this;
    }

    /**
     * NOTE: this only works if it was created using config and has an encoder
     * @return the velocity of the motor in feet per second
     */
    @Override
    public double getVelocity() {
        return motor.getSelectedSensorVelocity() * inputMultiplier;
    }

    /**
     * NOTE: this only works if it was created using config and has an encoder
     * @return distance the motor has spun in feet
     */
    @Override
    public double getDistance() {
        return motor.getSelectedSensorPosition() * distanceMultiplier;
    }

    @Override
    public void resetDistance() {
        motor.setSelectedSensorPosition(0);
    }

    public void follow(CANMotor motor) {
        this.motor.follow(motor.motor);
    }

}