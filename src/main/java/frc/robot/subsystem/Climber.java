package frc.robot.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Robot2020;
import frc.robot.Util;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;

import java.util.HashMap;
import java.util.function.Supplier;

public class Climber extends Subsystem<Robot2020> {

    private DoubleSolenoid bottomSolenoid = new DoubleSolenoid(0, 1); // TODO: device number
    private DoubleSolenoid leftTopSolenoid = new DoubleSolenoid(2, 3); // TODO: device number
    private DoubleSolenoid rightTopSolenoid = new DoubleSolenoid(6, 8); // TODO: device number

    public Climber() {
        super("climber");
    }

    @Override
    public void stop(Robot2020 robot) {
        // :)
    }

    @Override
    public void control(Robot2020 robot) {
        Controller controller = robot.auxiliaryController;
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
            put("topSolenoid", Util.solenoidValueSupplier(leftTopSolenoid));
            put("bottomSolenoid", Util.solenoidValueSupplier(bottomSolenoid));
        }};
    }
}
