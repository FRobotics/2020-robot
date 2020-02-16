package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Variables;
import frc.robot.base.Util;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.subsystem.motor.CANMotor;
import frc.robot.base.subsystem.motor.Motor;

import java.util.HashMap;
import java.util.function.Supplier;

public class Intake extends Subsystem {

    private Controller controller;

    private DoubleSolenoid solenoid = new DoubleSolenoid(Variables.Intake.ARM_FORWARD_ID, Variables.Intake.ARM_REVERSE_ID);
    private Motor spinner = new CANMotor(new VictorSPX(Variables.Intake.MOTOR_ID));

    public Intake(Controller controller) {
        super("intake");
        this.controller = controller;
    }

    @Override
    public void stop() {
        spinner.setPercentOutput(0);
    }

    @Override
    public void control() {
        if (controller.buttonPressed(Button.A)) {
            solenoid.set(DoubleSolenoid.Value.kReverse);
        }

        if (controller.buttonPressed(Button.Y)) {
            solenoid.set(DoubleSolenoid.Value.kForward);
        }

        if (controller.buttonDown(Button.B) /*&& solenoid.get() == DoubleSolenoid.Value.kForward*/) {
            spinner.setPercentOutput(1);
        } else {
            spinner.setPercentOutput(0);
        }
    }

    @Override
    public HashMap<String, Supplier<Object>> createNTMap() {
        return new HashMap<>() {{
            put("solenoid", Util.solenoidNTV(solenoid));
            put("motor", spinner::getOutputPercent);
        }};
    }

}