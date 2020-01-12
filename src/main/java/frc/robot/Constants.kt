package frc.robot

class Constants {
    object Drive {
        /**
         * in inches
         */
        const val WHEEL_RADIUS = 3.0
        const val COUNTS_PER_REVOLUTION = 360 * 4.toDouble()
        /**
         * in feet
         */
        const val WHEEL_CIRCUMFERENCE = WHEEL_RADIUS / 12 * 2 * Math.PI
        const val PID_LOOP_INDEX = 0
        const val TIMEOUT_MS = 30
        const val F = 0.92
        const val P = 0.8
        const val I = 0.0012
        const val D = 0.01
        const val INTEGRAL_ZONE = 150
        const val DISTANCE_MULTIPLIER = WHEEL_CIRCUMFERENCE / COUNTS_PER_REVOLUTION
        const val INPUT_MULTIPLIER = 10 * DISTANCE_MULTIPLIER
        const val OUTPUT_MULTIPLIER = 1 / INPUT_MULTIPLIER
    }
}