package frc.robot.base;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.base.input.Controller;

import java.util.ArrayList;

public abstract class Robot4150<This extends Robot4150<This>> extends TimedRobot {

    private ArrayList<Subsystem<?, This>> subsystems = new ArrayList<>();
    private ArrayList<Controller> controllers = new ArrayList<>();

    @Override
    public void robotInit() {
        NTHandler.init(this.subsystems);
    }

    @Override
    public void robotPeriodic() {
        //noinspection unchecked
        subsystems.forEach(subsystem -> subsystem.periodic((This)this));
        controllers.forEach(Controller::postPeriodic);
        NTHandler.update();
    }

    @Override
    public void autonomousInit() {
        subsystems.forEach(subsystem -> subsystem.onInit(RobotMode.AUTONOMOUS));
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        subsystems.forEach(subsystem -> subsystem.onInit(RobotMode.TELEOP));
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        subsystems.forEach(subsystem -> subsystem.onInit(RobotMode.TEST));
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void disabledInit() {
        subsystems.forEach(subsystem -> subsystem.onInit(RobotMode.DISABLED));
    }

    public <S extends Subsystem<?, This>> S register(S subsystem) {
        this.subsystems.add(subsystem);
        return subsystem;
    }

    public Controller registerController(int port) {
        return this.registerController(new Controller(new Joystick(port)));
    }

    public Controller registerController(Controller c) {
        this.controllers.add(c);
        return c;
    }

}
