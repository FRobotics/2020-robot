package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.input.Controller;
import frc.robot.subsystem.Climber;
import frc.robot.subsystem.DriveTrain;
import frc.robot.subsystem.base.Subsystem;

import java.util.ArrayList;

public class Robot extends TimedRobot {

  private ArrayList<Subsystem<?>> subsystems = new ArrayList<>();

  private Controller movementController;
  private DriveTrain driveTrain;
  private Climber climber;

  @Override
  public void robotInit() {
    movementController = new Controller(new Joystick(0));
    driveTrain = register(new DriveTrain());
    climber = register(new Climber());
  }

  @Override
  public void robotPeriodic() {
    subsystems.forEach(subsystem -> subsystem.periodic(this));
    movementController.postPeriodic();
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

  public Controller getMovementController() {
    return movementController;
  }

  private <S extends Subsystem> S register(S subsystem) {
    this.subsystems.add(subsystem);
    return subsystem;
  }
}
