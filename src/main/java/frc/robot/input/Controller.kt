package frc.robot.input

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import java.util.*

class Controller(private val joystick: Joystick) {
    private val buttonsPressed: HashMap<Int, Boolean> = HashMap()
    /**
     * @param button - the button you want to specify
     * @return whether the specified button is current being pressed
     */
    fun buttonDown(button: Button): Boolean {
        return joystick.getRawButton(button.id)
    }

    /**
     * Returns true as soon as the specified button is pressed and then goes back to
     * false until it's pressed again
     *
     * @param button - the button you want to specify
     * @return whether the specified button was just pressed
     */
    fun buttonPressed(button: Button): Boolean {
        return !buttonsPressed[button.id]!! && joystick.getRawButton(button.id)
    }

    /**
     * Returns the value of an axis on the controller
     *
     * @param axis - the axis you want to measure
     * @return the values of the axis
     */
    fun getAxis(axis: Axis): Double {
        return joystick.getRawAxis(axis.id)
    }

    /**
     * A method that should be run after the main periodic code; makes
     * buttonPressed(Button button) work
     */
    fun postPeriodic() {
        for (button in Button.values()) {
            val id = button.id
            val pressed = joystick.getRawButton(id)
            buttonsPressed[id] = pressed
            SmartDashboard.putBoolean("vars2/controllers/port_" + joystick.port + "/buttons/" + button, pressed)
        }
    }

    init {
        for (button in Button.values()) {
            val id = button.id
            buttonsPressed[id] = false
        }
    }
}