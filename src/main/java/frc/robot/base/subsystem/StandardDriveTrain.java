package frc.robot.base.subsystem;

import frc.robot.base.input.Button;
import frc.robot.base.util.RateLimiter;
import frc.robot.base.util.Util;
import frc.robot.base.input.Axis;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.motor.EncoderMotor;

import java.util.Map;
import java.util.function.Supplier;

/**
 * A drive train with two encoder motors and a rate limiter for each motor that is controlled with a controller
 * It features two driving modes, closed loop and open loop in case something goes wrong
 */
public class StandardDriveTrain extends Subsystem {

    private Controller controller;

    private EncoderMotor leftMotor; // 1.565
    private EncoderMotor rightMotor; // 1.565
    private RateLimiter leftRateLimiter;
    private RateLimiter rightRateLimiter;

    private double maxSpeed;
    private double controllerDeadBand = 0.2;
    private int controllerPower = 2;

    private boolean useClosedLoop = true;

    public StandardDriveTrain(
            EncoderMotor leftMotor, EncoderMotor rightMotor,
            double maxAcceleration, double maxSpeed, Controller controller) {
        super("driveTrain");
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.rightRateLimiter = new RateLimiter(maxAcceleration / 50);
        this.leftRateLimiter = new RateLimiter(maxAcceleration / 50);
        this.maxSpeed = maxSpeed;
        this.controller = controller;
    }

    private double leftTargetOutput = 0;
    public void setLeftVelocity(double velocity) {
        velocity = leftRateLimiter.get(safeVelocity(velocity));
        this.leftTargetOutput = velocity;
        this.leftMotor.setVelocity(velocity);
    }
    public void setLeftPercentOutput(double percent) {
        percent = safePercent(percent);
        this.leftTargetOutput = percent;
        this.leftMotor.setPercentOutput(percent);
    }

    private double rightTargetOutput = 0;
    public void setRightVelocity(double velocity) {
        velocity = rightRateLimiter.get(safeVelocity(velocity));
        this.rightTargetOutput = velocity;
        this.rightMotor.setVelocity(velocity);
    }
    public void setRightPercentOutput(double percent) {
        percent = safePercent(percent);
        this.rightTargetOutput = percent;
        this.rightMotor.setPercentOutput(percent);
    }

    public void setVelocity(double velocity) {
        setLeftVelocity(velocity);
        setRightVelocity(velocity);
    }

    @Override
    public void control() {
        double fb = -Util.adjustInput(controller.getAxis(Axis.LEFT_Y), controllerDeadBand, controllerPower);
        double lr = Util.adjustInput(controller.getAxis(Axis.RIGHT_X), controllerDeadBand, controllerPower);

        double left = fb - lr;
        double right = fb + lr;

        if(useClosedLoop) {
            setLeftVelocity(left * maxSpeed);
            setRightVelocity(right * maxSpeed);
        } else {
            setLeftPercentOutput(left);
            setRightPercentOutput(right);
        }

        if(controller.buttonPressed(Button.BACK)) {
            this.leftMotor.resetDistance();
            this.rightMotor.resetDistance();
        }

        if (controller.buttonPressed(Button.START)) {
            this.useClosedLoop = !useClosedLoop;
        }
    }

    public double safeVelocity(double velocity) {
        return Math.max(Math.min(velocity, maxSpeed), -maxSpeed);
    }

    public double safePercent(double percent) {
        return Math.max(Math.min(percent, 1), -1);
    }

    @Override
    public void stop() {
        setVelocity(0);
        //this.leftMotor.resetDistance();
        //this.rightMotor.resetDistance();
    }

    @Override
    public Map<String, Supplier<Object>> NTSets() {
        return Map.of(
            "leftVelocity", leftMotor::getVelocity,
            "rightVelocity", rightMotor::getVelocity,
            "leftDistance", leftMotor::getDistance,
            "rightDistance", rightMotor::getDistance,
            "leftTargetOutput", () -> leftTargetOutput,
            "rightTargetOutput", () -> rightTargetOutput
        );
    }

    public void setControllerDeadBand(double controllerDeadBand) {
        this.controllerDeadBand = controllerDeadBand;
    }

    public void setControllerPower(int controllerPower) {
        this.controllerPower = controllerPower;
    }

    public Controller getController() {
        return controller;
    }

    public double getAverageDistance() {
        return (leftMotor.getDistance() + rightMotor.getDistance()) / 2;
    }
}
