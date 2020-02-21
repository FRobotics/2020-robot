package frc.robot.base.subsystem;

import frc.robot.base.util.RateLimiter;
import frc.robot.base.util.Util;
import frc.robot.base.input.Axis;
import frc.robot.base.input.Controller;
import frc.robot.base.subsystem.motor.EncoderMotor;

import java.util.HashMap;
import java.util.function.Supplier;

public class StandardDriveTrain extends Subsystem {

    private Controller controller;

    private EncoderMotor leftMotor;
    private EncoderMotor rightMotor;
    private RateLimiter leftRateLimiter;
    private RateLimiter rightRateLimiter;

    private double maxSpeed;
    private double controllerDeadBand = 0.2;
    private int controllerPower = 2;

    public StandardDriveTrain(
            EncoderMotor leftMotor, EncoderMotor rightMotor,
            double maxAcceleration, double maxSpeed, Controller controller) {
        super("driveTrain");
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.rightRateLimiter = new RateLimiter(maxAcceleration);
        this.leftRateLimiter = new RateLimiter(maxAcceleration);
        this.maxSpeed = maxSpeed;
        this.controller = controller;
    }

    private double leftTargetVel = 0;
    public void setLeftVelocity(double velocity) {
        velocity = leftRateLimiter.get(safeVelocity(velocity));
        this.leftTargetVel = velocity;
        this.leftMotor.setVelocity(velocity);
    }

    private double rightTargetVel = 0;
    public void setRightVelocity(double velocity) {
        velocity = rightRateLimiter.get(safeVelocity(velocity));
        this.rightTargetVel = velocity;
        this.rightMotor.setVelocity(velocity);
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

        setLeftVelocity(left * maxSpeed);
        setRightVelocity(right * maxSpeed);
    }

    public double safeVelocity(double velocity) {
        return Math.min(Math.max(velocity, maxSpeed), -maxSpeed);
    }

    @Override
    public void stop() {
        setVelocity(0);
    }

    @Override
    public HashMap<String, Supplier<Object>> createNTMap() {
        return new HashMap<>(){{
            put("leftVelocity", leftMotor::getVelocity);
            put("rightVelocity", rightMotor::getVelocity);
            put("leftDistance", leftMotor::getDistance);
            put("rightDistance", rightMotor::getDistance);
            put("leftTarget", () -> leftTargetVel);
            put("rightTarget", () -> rightTargetVel);
        }};
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
}
