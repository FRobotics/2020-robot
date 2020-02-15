package frc.robot;

import frc.robot.base.subsystem.motor.EncoderMotorConfig;

public class Variables {

    public static class DriveTrain {
        public static final EncoderMotorConfig CONFIG = new EncoderMotorConfig(
                3,
                0.92,
                0.8,
                0.0012,
                0.01,
                150
        );

        public static final int LEFT_MOTOR_MASTER_ID = 10;
        public static final int LEFT_MOTOR_FOLLOWER_ID = 11;
        public static final int RIGHT_MOTOR_MASTER_ID = 16;
        public static final int RIGHT_MOTOR_FOLLOWER_ID = 17;
    }

    public static class Shooter {
        public static final int LEFT_MOTOR_ID = 13;
        public static final int RIGHT_MOTOR_ID = 14;
        public static final int YAW_MOTOR_ID = 18;
        public static final int PITCH_MOTOR_ID = 15;
        public static final int CAROUSEL_MOTOR_ID = 19;

        public static final double LEFT_MOTOR_SPEED = .86;
        public static final double RIGHT_MOTOR_SPEED = .76;

        public static final double CAROUSEL_WHILE_SHOOTING = .5;
        public static final double CAROUSEL_ALONE = .7;
    }
}
