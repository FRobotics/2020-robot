package frc.robot.base.subsystem.motor;

public interface EncoderMotor extends Motor {
    double getVelocity();
    double getDistance();
    void resetDistance();
    @Override
    EncoderMotor setInverted(boolean inverted);
    @Override
    EncoderMotor invert();

    void setConfig(EncoderMotorConfig config);
}