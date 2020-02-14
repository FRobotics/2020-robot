package frc.robot.subsystem;

import edu.wpi.first.wpilibj.Servo;
import frc.robot.Robot2020;
import frc.robot.base.subsystem.Subsystem;
import frc.robot.base.input.Button;

public class ServoTest extends Subsystem<Robot2020> {

    Servo servo = new Servo(0);

    public ServoTest() {
        super("servoTest");
    }

    @Override
    public void stop(Robot2020 robot) {
        servo.set(0);
    }

    @Override
    public void control(Robot2020 robot) {
        if(robot.auxiliaryController.buttonDown(Button.X)) {
            servo.setPosition(1);
        } else {
            servo.setPosition(0);
        }
    }
}
