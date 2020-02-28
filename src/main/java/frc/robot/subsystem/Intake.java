package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.IDs;
import frc.robot.base.util.Util;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.subsystem.motor.PhoenixMotor;
import frc.robot.base.subsystem.motor.Motor;

import java.util.Map;
import java.util.function.Supplier;

public class Intake extends Subsystem {

    private Controller controller;

    private DoubleSolenoid solenoid = new DoubleSolenoid(IDs.Intake.ARM_FORWARD, IDs.Intake.ARM_REVERSE);
    private Motor spinner = new PhoenixMotor(new VictorSPX(IDs.Intake.MOTOR));

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

        // flip up

        if (controller.buttonPressed(Button.A)) {
            solenoid.set(DoubleSolenoid.Value.kReverse);
        }

        // flip out

        if (controller.buttonPressed(Button.Y)) {
            solenoid.set(DoubleSolenoid.Value.kForward);
        }

        // spin if solenoid is out

        if (controller.buttonDown(Button.B) && solenoid.get() == DoubleSolenoid.Value.kForward) {
            spinner.setPercentOutput(1);
        } else {
            spinner.setPercentOutput(0);
        }
    }

    @Override
    public Map<String, Supplier<Object>> NTSets() {
        return Map.of(
            "solenoid", Util.solenoidNTV(solenoid),
            "motor", spinner::getOutputPercent
        );
    }

}