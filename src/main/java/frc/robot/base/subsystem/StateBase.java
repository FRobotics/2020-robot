package frc.robot.base.subsystem;

import frc.robot.base.Robot4150;

public interface StateBase<R extends Robot4150> {
    void run(R robot);
}
