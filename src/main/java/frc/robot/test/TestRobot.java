package frc.robot.test;

import frc.robot.base.Robot;
import frc.robot.base.util.action.Action;
import frc.robot.base.input.Controller;
import frc.robot.test.subsystem.CuriosityDriveTrain;
import frc.robot.test.subsystem.LimitSwitchTest;

import java.util.Collections;
import java.util.List;

public class TestRobot extends Robot {

    Controller driveController = registerController(0);

    private final CuriosityDriveTrain driveTrain = register(new CuriosityDriveTrain(driveController));
    private final LimitSwitchTest switchTest = register(new LimitSwitchTest());
    // private final ServoTest test = register(new ServoTest());

    @Override
    public List<? extends Action> getAutoActions() {
        return Collections.emptyList();
    }
}
