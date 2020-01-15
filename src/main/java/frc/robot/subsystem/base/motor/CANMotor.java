package frc.robot.subsystem.base.motor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

public class CANMotor implements Motor {

    private BaseMotorController motor;

    public CANMotor(BaseMotorController motor) {
        this.motor = motor;
        this.motor.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    public void setVelocity(double speed) {
        motor.set(ControlMode.Velocity, speed);
    }

    @Override
    public void setPercentOutput(double percent) {
        motor.set(ControlMode.PercentOutput, percent);
    }

    @Override
    public Motor setInverted(boolean inverted) {
        motor.setInverted(inverted);
        return this;
    }

    @Override
    public Motor invert() {
        motor.setInverted(!motor.getInverted());
        return this;
    }

}