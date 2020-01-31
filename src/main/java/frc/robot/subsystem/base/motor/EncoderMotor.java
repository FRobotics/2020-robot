package frc.robot.subsystem.base.motor;

public interface EncoderMotor extends Motor {
    public double getVelocity();
    public double getOutputPercent();
    public double getDistance();
    @Override
    EncoderMotor setInverted(boolean inverted);
    @Override
    EncoderMotor invert();
}