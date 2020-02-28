package frc.robot.base.subsystem.motor;

/**
 * A generic motor that has an encoder
 */
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