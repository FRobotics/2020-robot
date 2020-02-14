package frc.robot.base.auto;

import frc.robot.base.action.Action;

public class AutoSetupAction extends AutoAction {
    public AutoSetupAction(Action action) {
        super(action, () -> true);
    }
}
