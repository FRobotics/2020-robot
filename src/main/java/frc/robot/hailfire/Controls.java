package frc.robot.hailfire;

import frc.robot.base.input.Axis;
import frc.robot.base.input.Button;
import frc.robot.base.input.Pov;

public class Controls {
    public static class Climber {
        // TODO: actually name these
        public static Button RETRACT = Button.START;
        public static Button EXTEND = Button.BACK;
        public static Button SPIN_WINCH = Button.RIGHT_BUMPER;
    }

    public static class DriveTrain {
        public static Button TURN_RIGHT = Button.B;
        public static Button TURN_LEFT = Button.X;
        public static Button LOW_GEAR = Button.LEFT_BUMPER;
        public static Button HIGH_GEAR = Button.RIGHT_BUMPER;
        public static Pov AUTO_SHIFT = Pov.D_PAD;
    }

    public static class Intake {
        public static Button ARM_UP = Button.Y;
        public static Button ARM_DOWN = Button.A;
        public static Button SPIN_FORWARD = Button.B;
        public static Button SPIN_BACKWARD = Button.X;
    }

    public static class Shooter {
        public static Axis SHOOT = Axis.RIGHT_TRIGGER;
        public static Axis MANUAL_CAROUSEL = Axis.LEFT_X;
        public static Axis AUTO_CAROUSEL_LEFT = Axis.LEFT_TRIGGER;
        public static Axis AUTO_CAROUSEL_RIGHT = Axis.RIGHT_TRIGGER;
        public static Button CAROUSEL_UP = Button.A;
        public static Button CAROUSEL_DOWN = Button.Y;
        public static Button TURN_ON_LIGHTS = Button.LEFT_BUMPER;
    }
}
