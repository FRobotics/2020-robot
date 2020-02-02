package frc.robot;

import frc.robot.base.Robot4150;
import frc.robot.base.input.Controller;
import frc.robot.base.motor.EncoderMotorConfig;
import frc.robot.subsystem.DriveTrain;

public class Robot extends Robot4150<Robot> {

  public final Controller movementController = registerController(0);
  public final Controller actionsController = registerController(1);

  public final DriveTrain driveTrain = register(new DriveTrain());
  // TODO: uncomment for real robot
  //public final Climber climber = register(new Climber());
  //public final Shooter shooter = register(new Shooter());
  //public final Spinner spinner = register(new Spinner());

  // TODO: update for 2020

  public static final EncoderMotorConfig driveConfig = new EncoderMotorConfig(
          3,
          0.92,
          0.8,
          0.0012,
          0.01,
          150
  );

}
