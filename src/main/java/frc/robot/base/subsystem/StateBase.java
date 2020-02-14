package frc.robot.base.subsystem;

import frc.robot.base.Robot;

public interface StateBase<R extends Robot> {
    void run(R robot);
}
