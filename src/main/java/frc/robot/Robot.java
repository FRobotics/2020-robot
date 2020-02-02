package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.input.Controller;
import frc.robot.subsystem.DriveTrain;
import frc.robot.subsystem.base.Subsystem;

import java.util.ArrayList;

public class Robot extends TimedRobot {

  private ArrayList<Subsystem<?>> subsystems = new ArrayList<>();

  private Controller movementController;
  private Controller actionsController;

  public void initSubsystems() {
    movementController = new Controller(new Joystick(0));
    actionsController = new Controller(new Joystick(1));
    register(new DriveTrain());
    // TODO: register these for the real robot
    // register(new Climber());
    // register(new Shooter());
    // register(new Spinner());
  }

  @Override
  public void robotInit() {
    this.initSubsystems();
    NTHandler.init(this.subsystems);
  }

  @Override
  public void robotPeriodic() {
    subsystems.forEach(subsystem -> subsystem.periodic(this));
    movementController.postPeriodic();
    actionsController.postPeriodic();
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

  public Controller getMovementController() {
    return movementController;
  }

  public Controller getActionsController() {
    return actionsController;
  }

  private <S extends Subsystem<?>> S register(S subsystem) {
    this.subsystems.add(subsystem);
    return subsystem;
  }
}
