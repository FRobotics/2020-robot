package frc.robot.base.util;

import frc.robot.base.device.DoubleSolenoid4150;

import java.util.function.Supplier;

public class Util {

    /**
     * @param solenoid the solenoid
     * @return a supplier of the solenoid state as a string for NTVs
     */
    public static Supplier<Object> solenoidNTV(DoubleSolenoid4150 solenoid) {
        return () -> {
            switch (solenoid.getRaw()) {
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
