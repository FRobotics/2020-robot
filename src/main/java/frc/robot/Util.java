package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

import java.util.function.Supplier;

public class Util {

    @FunctionalInterface
    public interface Action {
        void run();
    }

    public static Supplier<Object> solenoidValueSupplier(DoubleSolenoid solenoid) {
        return () -> solenoid.get() == DoubleSolenoid.Value.kForward ? "forward" : "backward";
    }

}
