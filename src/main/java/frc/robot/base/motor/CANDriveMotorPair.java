package frc.robot.base.motor;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

public class CANDriveMotorPair implements EncoderMotor {

    private CANMotor parent;
    private CANMotor child;

    public CANDriveMotorPair(BaseMotorController parent, BaseMotorController child, EncoderMotorConfig config) {
        this.parent = new CANMotor(parent, config);
        this.child = new CANMotor(child);
        this.child.follow(parent);
    }

    @Override
    public void setVelocity(double velocity) {
        parent.setVelocity(velocity);
        child.setVelocity(velocity);
    }

    @Override
    public void setPercentOutput(double percent) {
        parent.setPercentOutput(percent);
        child.setPercentOutput(percent);
    }

    @Override
    public double getVelocity() {
        return parent.getVelocity();
    }

    @Override
    public double getOutputPercent() {
        return parent.getOutputPercent();
    }

    @Override
    public EncoderMotor setInverted(boolean inverted) {
        parent.setInverted(inverted);
        child.setInverted(inverted);
        return this;
    }

    @Override
    public EncoderMotor invert() {
        parent.invert();
        child.invert();
        return this;
    }

    @Override
    public double getDistance() {
        return parent.getDistance();
    }

}