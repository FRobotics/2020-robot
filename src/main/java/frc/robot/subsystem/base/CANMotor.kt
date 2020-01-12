package frc.robot.subsystem.base

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.BaseMotorController

class CANMotor(private val motor: BaseMotorController) : Motor {
    override fun setSpeed(speed: Double) {
        motor[ControlMode.Velocity] = speed
    }

    override fun setPercentOutput(percent: Double) {
        motor[ControlMode.PercentOutput] = percent
    }

    override fun setInverted(inverted: Boolean): Motor? {
        motor.inverted = inverted
        return this
    }

    override fun invert(): Motor? {
        motor.inverted = !motor.inverted
        return this
    }

    init {
        motor.setNeutralMode(NeutralMode.Brake)
    }
}