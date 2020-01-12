package frc.robot.subsystem.base

interface Motor {
    fun setSpeed(speed: Double)
    fun setPercentOutput(percent: Double)
    fun setInverted(inverted: Boolean): Motor?
    fun invert(): Motor?
}