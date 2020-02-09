package frc.robot.base;

import frc.robot.Util;

public class ActionInstance {

    public final Util.Action action;
    public final int length;

    public ActionInstance(Util.Action action, int length) {
        this.action = action;
        this.length = length;
    }

}
