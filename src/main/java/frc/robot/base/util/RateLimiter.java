package frc.robot.base.util;

/**
 * prevents a value from changing too much
 */
public class RateLimiter {

    private double maxChange;
    private double lastVal;

    public RateLimiter(double maxChange, double startVal) {
        this.maxChange = maxChange;
        this.lastVal = startVal;
    }

    public RateLimiter(double maxChange) {
        this(maxChange, 0);
    }

    public double get(double val) {
        double absVal = Math.abs(val);
        double output;
        if (absVal - lastVal > maxChange) {
            output = lastVal + maxChange;
        } else {
            output = val;
        }
        lastVal = absVal;
        return val > 0 ? output : -output;
    }

    public void setMaxChange(double maxChange) {
        this.maxChange = maxChange;
    }
}
