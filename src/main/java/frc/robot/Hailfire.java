package frc.robot;

import frc.robot.base.Robot;
import frc.robot.base.util.action.Action;
import frc.robot.base.util.action.SetupAction;
import frc.robot.base.util.action.TimedAction;
import frc.robot.base.input.Controller;
import frc.robot.subsystem.DriveTrain;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Hailfire extends Robot {

    private final Controller driveController = registerController(0);
    private final Controller auxiliaryController = registerController(1);

    private final DriveTrain driveTrain = register(new DriveTrain(driveController));
    private final Shooter shooter = register(new Shooter(auxiliaryController, driveController));
    private final Intake intake = register(new Intake(auxiliaryController));
    // private final Climber climber = register(new Climber(auxiliaryController));
    // private final Spinner spinner = register(new Spinner(auxiliaryController));

    @Override
    public List<? extends Action> getAutoActions() {
        //noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList(
                new SetupAction(() -> driveTrain.startAction(
                        new TimedAction(() -> driveTrain.setVelocity(-40), 2000)
                ))
        );
    }
}
