package frc.robot.subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Robot;
import frc.robot.base.RobotMode;
import frc.robot.base.input.Button;
import frc.robot.base.Subsystem;
import frc.robot.base.input.Controller;

import java.util.HashMap;
import java.util.function.Supplier;

public class Climber extends Subsystem<Climber.State, Robot> {

    public enum State {
        DISABLE, CONTROLLED
    }

    private DoubleSolenoid bottomSolenoid = new DoubleSolenoid(0, 1); // TODO: device number
    private DoubleSolenoid leftTopSolenoid = new DoubleSolenoid(2, 3); // TODO: device number
    private DoubleSolenoid rightTopSolenoid = new DoubleSolenoid(6, 8); // TODO: device number

    public Climber() {
        super("climber", State.DISABLE);
    }

    @Override
    public void onInit(RobotMode mode) {
        this.clearStateQueue();
        switch (mode) {
            default:
            case DISABLED:
                this.setStateAndDefault(State.DISABLE);
                break;
            case TELEOP:
                this.setStateAndDefault(State.CONTROLLED);
                break;
        }
    }

    @Override
    public void handleState(Robot robot, State state) {
        switch (state) {
            case CONTROLLED:
                Controller controller = robot.auxiliaryController;
                if(leftTopSolenoid.get() == DoubleSolenoid.Value.kReverse) {
                    if (controller.buttonPressed(Button.BACK)) {
                        bottomSolenoid.set(DoubleSolenoid.Value.kReverse);
                    }

                    if (controller.buttonPressed(Button.START)) {
                        bottomSolenoid.set(DoubleSolenoid.Value.kForward);
                    }
                }

                if(bottomSolenoid.get() == DoubleSolenoid.Value.kForward) {
                    if (controller.buttonPressed(Button.LEFT_BUMPER)) {
                        leftTopSolenoid.set(DoubleSolenoid.Value.kForward);
                    }

                    if (controller.buttonPressed(Button.RIGHT_BUMPER)) {
                        rightTopSolenoid.set(DoubleSolenoid.Value.kForward);
                    }
                }

                break;
        }
    }

    private Supplier<Object> solenoidValue(DoubleSolenoid solenoid) {
        return () -> solenoid.get() == DoubleSolenoid.Value.kForward ? "forward" : "backward";
    }

    @Override
    public HashMap<String, Supplier<Object>> createNTMap() {
        return new HashMap<>() {{
            put("topSolenoid", solenoidValue(leftTopSolenoid));
            put("bottomSolenoid", solenoidValue(bottomSolenoid));
        }};
    }
}
