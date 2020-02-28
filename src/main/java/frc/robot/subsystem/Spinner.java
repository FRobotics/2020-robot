package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.IDs;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.subsystem.motor.PhoenixMotor;
import frc.robot.base.subsystem.motor.Motor;

import java.util.Map;
import java.util.function.Supplier;

public class Spinner extends Subsystem {

    private Controller controller;

    private Motor motor = new PhoenixMotor(new TalonSRX(IDs.Spinner.MOTOR));

    public Spinner(Controller controller) {
        super("spinner");
        this.controller = controller;
    }

    @Override
    public void stop() {
        motor.setPercentOutput(0);
    }

    @Override
    public void control() {

        // spin

        if (controller.buttonDown(Button.Y)) {
            motor.setPercentOutput(.5);
        } else {
            motor.setPercentOutput(0);
        }
    }

    @Override
    public Map<String, Supplier<Object>> NTSets() {
        return Map.of(
            "motorPercent", motor::getOutputPercent
        );
    }
}