package frc.robot.test.subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.base.subsystem.TestSubsystem;

import java.util.HashMap;
import java.util.function.Supplier;

public class LimitSwitchTest extends TestSubsystem {

    private DigitalInput limitSwitch = new DigitalInput(0);

    @Override
    public HashMap<String, Supplier<Object>> createNTMap() {
        return new HashMap<>() {{
            put("limitSwitch", limitSwitch::get);
        }};
    }
}
