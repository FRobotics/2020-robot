package frc.robot.subsystem.base;

public interface Motor {
    void setSpeed(double speed);
    void setPercentOutput(double percent);
    Motor setInverted(boolean inverted);
    Motor invert();
}