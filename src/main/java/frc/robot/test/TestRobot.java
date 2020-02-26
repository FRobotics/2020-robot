package frc.robot.test;

import frc.robot.base.Robot;
import frc.robot.base.util.PosControl;
import frc.robot.base.util.action.Action;
import frc.robot.base.input.Controller;
import frc.robot.base.util.action.SetupAction;
import frc.robot.test.subsystem.CuriosityDriveTrain;
import frc.robot.test.subsystem.LimitSwitchTest;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class TestRobot extends Robot {

    Controller driveController = registerController(0);

    private final CuriosityDriveTrain driveTrain = register(new CuriosityDriveTrain(driveController));
    private final LimitSwitchTest switchTest = register(new LimitSwitchTest());
    // private final ServoTest test = register(new ServoTest());

    private PosControl posControl = new PosControl(10, 2, 0.2, .5, 5);

    @Override
    public List<? extends Action> getAutoActions() {
        return Arrays.asList(
                new SetupAction(
                        () -> {
                            driveTrain.startAction(
                                    new Action(
                                            () -> {
                                                driveTrain.setVelocity(posControl.getSpeed(driveTrain.getAverageDistance()));
                                            }, posControl::isFinished
                                    )
                            );
                        }
                ),
                new Action(driveTrain::isFinished)
        );
    }
}
