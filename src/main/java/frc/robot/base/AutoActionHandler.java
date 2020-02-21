package frc.robot.base;

import frc.robot.base.util.action.Action;
import frc.robot.base.util.action.ActionHandler;

public class AutoActionHandler extends ActionHandler {
    public AutoActionHandler() {
        this.startActionAndSetDefault(new Action(() -> {}));
    }
}
