package frc.robot

import edu.wpi.first.wpilibj.TimedRobot

class Robot : TimedRobot() {
    override fun robotInit() {
        println("works v6")
    }

    override fun autonomousInit() {}
    override fun autonomousPeriodic() {}
    override fun teleopInit() {}
    override fun teleopPeriodic() {}
    override fun testInit() {}
    override fun testPeriodic() {}
}