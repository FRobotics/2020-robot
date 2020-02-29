package frc.robot.base.input;

import java.util.HashMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Controller {

    private Joystick joystick;
    private HashMap<Integer, Boolean> buttonsPressed;

    public Controller(Joystick joystick) {
        this.joystick = joystick;
        this.buttonsPressed = new HashMap<>();
        for (Button button : Button.values()) {
            int id = button.getId();
            buttonsPressed.put(id, false);
        }
    }

    /**
     * @param button the button you want to specify
     * @return whether the specified button is current being pressed
     */
    public boolean buttonDown(Button button) {
        return joystick.getRawButton(button.getId());
    }

    /**
     * Returns true during the first loop the specified button is pressed
     * 
     * @param button the button you want to specify
     * @return whether the specified button was just pressed
     */
    public boolean buttonPressed(Button button) {
        return (!buttonsPressed.get(button.getId()) && joystick.getRawButton(button.getId()));
    }

    /**
     * Returns the value of an axis on the controller
     * 
     * @param axis the axis you want to measure
     * @return the value of the axis
     */
    public double getAxis(Axis axis) {
        return joystick.getRawAxis(axis.getId());
    }

    /**
     * Returns the value of a pov on the controller
     * 
     * @param axis the pov you want to measure
     * @return the angle of the POV in degrees, or -1 if the POV is not pressed.
     */
    public int getPov(Pov pov) {
        return joystick.getPOV(pov.getId());
    }

    /**
     * A method that should be run after the main periodic code;
     * makes buttonPressed(Button button) work
     */
    public void postPeriodic() {
        for (Button button : Button.values()) {
            int id = button.getId();
            boolean pressed = joystick.getRawButton(id);
            buttonsPressed.put(id, pressed);
            SmartDashboard.putBoolean("vars2/controllers/port_" + joystick.getPort() + "/buttons/" + button, pressed);
        }
    }

}