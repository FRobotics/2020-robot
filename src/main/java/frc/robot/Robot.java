package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.input.Controller;
import frc.robot.subsystem.DriveTrain;

public class Robot extends TimedRobot {

  private Controller movementController;
  private DriveTrain driveTrain;

  @Override
  public void robotInit() {
    movementController = new Controller(new Joystick(0));
    driveTrain = new DriveTrain();
  }

  @Override
  public void robotPeriodic() {
    driveTrain.periodic(this);
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  public Controller getMovementController() {
    return movementController;
  }
}
