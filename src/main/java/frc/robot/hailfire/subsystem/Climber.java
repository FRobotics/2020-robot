package frc.robot.hailfire.subsystem;

import frc.robot.base.util.Util;
import frc.robot.hailfire.IDs;
import frc.robot.base.device.DoubleSolenoid4150;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.Subsystem;

import java.util.Map;
import java.util.function.Supplier;

public class Climber extends Subsystem {

    private Controller controller;

    private DoubleSolenoid4150 bottomSolenoid = new DoubleSolenoid4150(IDs.Climber.BOTTOM_SOLENOID_FORWARD, IDs.Climber.BOTTOM_SOLENOID_REVERSE);
    private DoubleSolenoid4150 leftTopSolenoid = new DoubleSolenoid4150(IDs.Climber.TOP_LEFT_SOLENOID_FORWARD, IDs.Climber.TOP_LEFT_SOLENOID_REVERSE);
    private DoubleSolenoid4150 rightTopSolenoid = new DoubleSolenoid4150(IDs.Climber.TOP_RIGHT_SOLENOID_FORWARD, IDs.Climber.TOP_RIGHT_SOLENOID_REVERSE);

    public Climber(
            Controller controller
    ) {
        super("climber");
        this.controller = controller;
    }

    @Override
    public void control() {

        // raise and lower bottom solenoid if tops are down
        if (!leftTopSolenoid.isExtended() && !rightTopSolenoid.isExtended()) {
            if (controller.buttonPressed(Button.BACK)) {
                bottomSolenoid.retract();
            }

            if (controller.buttonPressed(Button.START)) {
                bottomSolenoid.extend();
            }
        }

        // raise and lower top solenoids if bottoms are out
        if (bottomSolenoid.isExtended()) {
            if (controller.buttonPressed(Button.LEFT_BUMPER)) {
                leftTopSolenoid.flip();
            }

            if (controller.buttonPressed(Button.RIGHT_BUMPER)) {
                rightTopSolenoid.flip();
            }
        }

    }

    @Override
    public Map<String, Supplier<Object>> NTSets() {
        return Map.of(
            "topSolenoid", Util.solenoidNTV(leftTopSolenoid),
            "bottomSolenoid", Util.solenoidNTV(bottomSolenoid)
        );
    }
}
