package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Robot2020;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.motor.CANMotor;
import frc.robot.base.subsystem.motor.Motor;

import java.util.HashMap;
import java.util.function.Supplier;

public class Spinner extends Subsystem<Robot2020> {

    private Motor motor = new CANMotor(new TalonSRX(0)); // TODO: device number

    public Spinner() {
        super("spinner");
    }

    @Override
    public void stop(Robot2020 robot) {
        motor.setPercentOutput(0);
    }

    @Override
    public void control(Robot2020 robot) {
        Controller controller = robot.auxiliaryController;
        if (controller.buttonDown(Button.Y)) {
            motor.setPercentOutput(.5);
        } else {
            motor.setPercentOutput(0);
        }
    }

    @Override
    public HashMap<String, Supplier<Object>> createNTMap() {
        return new HashMap<>(){{
            put("motorPercent", motor::getOutputPercent);
        }};
    }
}