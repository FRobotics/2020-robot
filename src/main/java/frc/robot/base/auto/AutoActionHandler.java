package frc.robot.base.auto;

import frc.robot.base.action.Action;
import frc.robot.base.action.ActionHandler;

public class AutoActionHandler extends ActionHandler<AutoAction, Action> {

    public AutoActionHandler() {
        this.startActionAndSetDefault(new AutoAction(() -> {}));
    }

    public void periodic() {
        this.getAction().func.run();
        this.updateAction();
    }
}
