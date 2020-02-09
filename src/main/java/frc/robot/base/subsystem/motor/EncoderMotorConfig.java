package frc.robot.base.subsystem.motor;

public class EncoderMotorConfig {

    /**
     *
     * @param wheelRadius the radius of the wheels in inches
     * @param f ???
     * @param p ???
     * @param i ???
     * @param d ???
     * @param integralZone ???
     */
    public EncoderMotorConfig(double wheelRadius, double f, double p, double i, double d, int integralZone) {
        WHEEL_RADIUS = wheelRadius;
        PID_LOOP_INDEX = 0;
        TIMEOUT_MS = 30;
        F = f;
        P = p;
        I = i;
        D = d;
        INTEGRAL_ZONE = integralZone;

        WHEEL_CIRCUMFERENCE = (WHEEL_RADIUS / 12) * 2 * Math.PI;
        DISTANCE_MULTIPLIER = WHEEL_CIRCUMFERENCE / COUNTS_PER_REVOLUTION;
        INPUT_MULTIPLIER = 10 * DISTANCE_MULTIPLIER;
        OUTPUT_MULTIPLIER = 1 / INPUT_MULTIPLIER;
    }

    /**
     * in inches
     */
    public final double WHEEL_RADIUS;
    public final double COUNTS_PER_REVOLUTION = 360 * 4;

    /**
     * in feet
     */
    public final double WHEEL_CIRCUMFERENCE;

    public final int PID_LOOP_INDEX;
    public final int TIMEOUT_MS;
    public final double F;
    public final double P;
    public final double I;
    public final double D;
    public final int INTEGRAL_ZONE;

    public final double DISTANCE_MULTIPLIER;
    public final double INPUT_MULTIPLIER;
    public final double OUTPUT_MULTIPLIER;
}
