package frc.robot.subsystem.base

interface EncoderMotor : Motor {
    val speed: Double
    val outputPercent: Double
    val distance: Double
    override fun setInverted(inverted: Boolean): EncoderMotor?
    override fun invert(): EncoderMotor?
}