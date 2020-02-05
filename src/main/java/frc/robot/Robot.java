package frc.robot;

import frc.robot.base.Robot4150;
import frc.robot.base.input.Controller;
import frc.robot.subsystem.DriveTrain;

public class Robot extends Robot4150<Robot> {

  public final Controller driveController = registerController(0);
  public final Controller auxiliaryController = registerController(1);

  public final DriveTrain driveTrain = register(new DriveTrain());
  // TODO: uncomment for real robot
  //public final Climber climber = register(new Climber());
  //public final Shooter shooter = register(new Shooter());
  //public final Spinner spinner = register(new Spinner());
}
