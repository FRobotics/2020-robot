package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Robot;
import frc.robot.base.RobotMode;
import frc.robot.base.Subsystem;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.motor.CANMotor;
import frc.robot.base.motor.Motor;

public class Intake extends Subsystem<Intake.State, Robot> {

    private DoubleSolenoid solenoid = new DoubleSolenoid(0, 0);
    private Motor spinner = new CANMotor(new TalonSRX(0));

    public Intake() {
        super("intake", State.DISABLED);
    }

    public enum State {
        DISABLED,
        CONTROLLED
    }

    @Override
    public void onInit(RobotMode mode) {
        this.clearStateQueue();
        switch (mode) {
            default:
            case DISABLED:
                setStateAndDefault(State.DISABLED);
                break;
            case TELEOP:
                setStateAndDefault(State.CONTROLLED);
                break;
        }
    }

    @Override
    public void handleState(Robot robot, State state) {
        switch (state) {
            case DISABLED:
                spinner.setPercentOutput(0);
                break;
            case CONTROLLED:
                Controller controller = robot.auxiliaryController;

                if (controller.buttonPressed(Button.A)) {
                    solenoid.set(DoubleSolenoid.Value.kReverse);
                }

                if (controller.buttonPressed(Button.Y)) {
                    solenoid.set(DoubleSolenoid.Value.kForward);
                }

                if (controller.buttonPressed(Button.B) && solenoid.get() == DoubleSolenoid.Value.kForward) {
                    spinner.setPercentOutput(0.4);
                } else {
                    spinner.setPercentOutput(0);
                }

                break;
        }
    }
}
