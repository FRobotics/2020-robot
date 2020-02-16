package frc.robot.subsystem;

import edu.wpi.first.wpilibj.Servo;
import frc.robot.base.input.Button;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.Subsystem;

public class ServoTest extends Subsystem {

    private Controller controller;

    Servo servo = new Servo(0);

    public ServoTest(Controller controller) {
        super("servoTest");
        this.controller = controller;
    }

    @Override
    public void stop() {
        servo.set(0);
    }

    @Override
    public void control() {
        if(controller.buttonDown(Button.X)) {
            servo.setPosition(1);
        } else {
            servo.setPosition(0);
        }
    }
}
