package frc.robot.subsystem;

import edu.wpi.first.wpilibj.Servo;
import frc.robot.Robot;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.input.Button;

public class ServoTest extends Subsystem<Robot> {

    Servo servo = new Servo(0);

    public ServoTest() {
        super("servoTest");
    }

    @Override
    public void stop(Robot robot) {
        servo.set(0);
    }

    @Override
    public void control(Robot robot) {
        if(robot.auxiliaryController.buttonDown(Button.X)) {
            servo.setPosition(1);
        } else {
            servo.setPosition(0);
        }
    }
}
