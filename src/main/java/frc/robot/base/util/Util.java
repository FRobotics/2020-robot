package frc.robot.base.util;

import edu.wpi.first.wpilibj.DoubleSolenoid;

import java.util.function.Supplier;

public class Util {

    /**
     * @param solenoid the solenoid
     * @return a supplier of the solenoid state as a string for NTVs
     */
    public static Supplier<Object> solenoidNTV(DoubleSolenoid solenoid) {
        return () -> {
            switch (solenoid.get()) {
                case kForward:
                    return "forward";
                case kReverse:
                    return "reverse";
                case kOff:
                    return "off";
                default:
                    return "???";
            }
        };
    }

    public static double adjustInput(double input, double deadBand, int power) {
        double absInput = Math.abs(input);
        double deadBanded = absInput < deadBand ? 0 : (absInput - deadBand) * (1 / (1 - deadBand));
        double smoothed = Math.pow(deadBanded, power);
        return input > 0 ? smoothed : -smoothed;
    }

}
