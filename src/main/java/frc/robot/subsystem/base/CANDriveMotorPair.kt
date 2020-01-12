package frc.robot.subsystem.base

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.BaseMotorController
import frc.robot.Constants

class CANDriveMotorPair(private val trevor: BaseMotorController, private val ahmad: BaseMotorController) : EncoderMotor {
    override fun setSpeed(speed: Double) {
        trevor[ControlMode.Velocity] = speed * Constants.Drive.OUTPUT_MULTIPLIER
    }

    override fun setPercentOutput(percent: Double) {
        trevor[ControlMode.PercentOutput] = percent
    }

    override val speed: Double
        get() = trevor.selectedSensorVelocity * Constants.Drive.INPUT_MULTIPLIER

    override val outputPercent: Double
        get() = trevor.motorOutputPercent

    override fun setInverted(inverted: Boolean): EncoderMotor? {
        trevor.inverted = inverted
        ahmad.inverted = inverted
        return this
    }

    override fun invert(): EncoderMotor? {
        setInverted(!trevor.inverted)
        return this
    }

    override val distance: Double
        get() = trevor.selectedSensorPosition * Constants.Drive.DISTANCE_MULTIPLIER

    init {
        trevor.setNeutralMode(NeutralMode.Brake)
        ahmad.setNeutralMode(NeutralMode.Brake)
        trevor.setSensorPhase(false)
        ahmad.setSensorPhase(false)
        val slotIdx = Constants.Drive.PID_LOOP_INDEX
        val timeoutMS = Constants.Drive.TIMEOUT_MS
        trevor.config_kF(slotIdx, Constants.Drive.F, timeoutMS)
        trevor.config_kP(slotIdx, Constants.Drive.P, timeoutMS)
        trevor.config_kI(slotIdx, Constants.Drive.I, timeoutMS)
        trevor.config_kD(slotIdx, Constants.Drive.D, timeoutMS)
        trevor.config_IntegralZone(slotIdx, Constants.Drive.INTEGRAL_ZONE, timeoutMS)
        trevor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, slotIdx, timeoutMS)
        ahmad.follow(trevor)
    }
}