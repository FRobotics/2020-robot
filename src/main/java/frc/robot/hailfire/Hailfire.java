package frc.robot.hailfire;

import frc.robot.base.NTHandler;
import frc.robot.base.Robot;
import frc.robot.base.action.Action;
import frc.robot.base.action.SetupAction;
import frc.robot.base.action.TimedAction;
import frc.robot.base.input.Pov;
import frc.robot.base.util.PosControl;
import frc.robot.hailfire.subsystem.Climber;
import frc.robot.hailfire.subsystem.DriveTrain;
import frc.robot.hailfire.subsystem.Intake;
import frc.robot.hailfire.subsystem.Shooter;
import frc.robot.base.input.Controller;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Hailfire extends Robot {

    private final Controller driveController = registerController(0);
    private final Controller auxiliaryController = registerController(1);

    private final DriveTrain driveTrain = register(new DriveTrain(driveController));
    private final Shooter shooter = register(new Shooter(auxiliaryController, driveController));
    private final Intake intake = register(new Intake(auxiliaryController));
    private final Climber climber = register(new Climber(auxiliaryController));

    private PosControl drivePosControl = new PosControl(10, 2, 0.1, 0.5, 5);

    @Override
    public void robotPeriodic() {
        super.robotPeriodic();

        if(auxiliaryController.getPov(Pov.D_PAD) >= 0) {
            var cameraNum = NTHandler.getVisionEntry("cameraNumber");
            cameraNum.setValue(cameraNum.getDouble(-1) + 1);
        }

        Vision.update();
    }

    @Override
    public List<? extends Action> getAutoActions() {
        // uncomment if only one thing lol // noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList(
                new SetupAction(() -> driveTrain.startAction(
                        new Action(
                                () -> driveTrain.setVelocity(drivePosControl.getSpeed(-driveTrain.getAverageDistance())),
                                drivePosControl::isFinished
                        )
                ), driveTrain::isFinished),
                new TimedAction(2000),
                new SetupAction(() -> shooter.startAction(
                        new TimedAction(
                                () -> shooter.shoot(false),
                                7000
                        )
                ), shooter::isFinished)
        );
    }
}
