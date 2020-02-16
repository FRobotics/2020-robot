package frc.robot;

import frc.robot.base.Robot;
import frc.robot.base.auto.AutoAction;
import frc.robot.base.auto.AutoSetupAction;
import frc.robot.base.auto.AutoTimedAction;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.SubsystemTimedAction;
import frc.robot.subsystem.DriveTrain;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.ServoTest;
import frc.robot.subsystem.Shooter;

import java.util.Arrays;
import java.util.List;

public class Robot2020 extends Robot<Robot2020> {

    public final Controller driveController = registerController(0);
    public final Controller auxiliaryController = registerController(1);

    public final DriveTrain driveTrain = register(new DriveTrain());
    public final Shooter shooter = register(new Shooter());
    public final Intake intake = register(new Intake());
    // public final Climber climber = register(new Climber());
    // public final Spinner spinner = register(new Spinner());

    //ServoTest test = register(new ServoTest());

    @Override
    public List<? extends AutoAction> getAutoActions() {
        return Arrays.asList(
                new AutoSetupAction(() -> {
                    driveTrain.startAction(new SubsystemTimedAction<>(() -> driveTrain.setVelocity(-40), 2000));
                })
                /*new AutoAction(driveTrain::isFinished),
                new AutoTimedAction(2000),
                new AutoSetupAction(() -> driveTrain.startActionQueue(driveTrain.TEST)),
                new AutoAction(driveTrain::isFinished),
                new AutoSetupAction(()-> driveTrain.startActionQueue(driveTrain.TEST))*/
        );
    }
}
