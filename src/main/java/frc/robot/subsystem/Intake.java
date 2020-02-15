package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.Robot2020;
import frc.robot.Util;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.subsystem.motor.CANMotor;
import frc.robot.base.subsystem.motor.Motor;

import java.util.HashMap;
import java.util.function.Supplier;

public class Intake extends Subsystem<Robot2020> {

    private DoubleSolenoid solenoid = new DoubleSolenoid(0, 1);
    private Motor spinner = new CANMotor(new VictorSPX(12));

    public Intake() {
        super("intake");
    }

    @Override
    public void stop(Robot2020 robot) {
        spinner.setPercentOutput(0);
    }

    @Override
    public void control(Robot2020 robot) {
        Controller controller = robot.auxiliaryController;

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
            put("solenoid", Util.solenoidValueSupplier(solenoid));
            put("motor", spinner::getOutputPercent);
        }};
    }

}