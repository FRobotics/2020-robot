package frc.robot.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Variables;
import frc.robot.base.Util;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.Subsystem;

import java.util.HashMap;
import java.util.function.Supplier;

public class Climber extends Subsystem {

    private Controller controller;

    private DoubleSolenoid bottomSolenoid = new DoubleSolenoid(Variables.Climber.BOTTOM_SOLENOID_FORWARD_ID, Variables.Climber.BOTTOM_SOLENOID__REVERSE_ID);
    private DoubleSolenoid leftTopSolenoid = new DoubleSolenoid(Variables.Climber.TOP_LEFT_SOLENOID_FORWARD_ID, Variables.Climber.TOP_LEFT_SOLENOID_REVERSE_ID);
    private DoubleSolenoid rightTopSolenoid = new DoubleSolenoid(Variables.Climber.TOP_RIGHT_SOLENOID_FORWARD_ID, Variables.Climber.TOP_RIGHT_SOLENOID_REVERSE_ID);

    public Climber(
            Controller controller
    ) {
        super("climber");
        this.controller = controller;
    }

    @Override
    public void stop() {
        // :)
    }

    @Override
    public void control() {
        if (leftTopSolenoid.get() == DoubleSolenoid.Value.kReverse) {
            if (controller.buttonPressed(Button.BACK)) {
                bottomSolenoid.set(DoubleSolenoid.Value.kReverse);
            }

            if (controller.buttonPressed(Button.START)) {
                bottomSolenoid.set(DoubleSolenoid.Value.kForward);
            }
        }

        if (bottomSolenoid.get() == DoubleSolenoid.Value.kForward) {
            if (controller.buttonPressed(Button.LEFT_BUMPER)) {
                leftTopSolenoid.set(DoubleSolenoid.Value.kForward);
            }

            if (controller.buttonPressed(Button.RIGHT_BUMPER)) {
                rightTopSolenoid.set(DoubleSolenoid.Value.kForward);
            }
        }

    }

    @Override
    public HashMap<String, Supplier<Object>> createNTMap() {
        return new HashMap<>() {{
            put("topSolenoid", Util.solenoidNTV(leftTopSolenoid));
            put("bottomSolenoid", Util.solenoidNTV(bottomSolenoid));
        }};
    }
}
