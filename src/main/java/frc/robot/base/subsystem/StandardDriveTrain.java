package frc.robot.base.subsystem;

import frc.robot.base.input.Button;
import frc.robot.base.util.Util;
import frc.robot.base.device.motor.EncoderMotor;
import frc.robot.base.device.motor.EncoderMotorConfig;
import frc.robot.base.input.Axis;
import frc.robot.base.input.Controller;

import java.util.Map;
import java.util.function.Supplier;

/**
 * A drive train with two encoder motors and a rate limiter for each motor that is controlled with a controller
 * It features two driving modes, closed loop and open loop in case something goes wrong
 * NOTE: rate limiters are currently disabled
 */
public class StandardDriveTrain extends Subsystem {

    protected Controller controller;

    private EncoderMotor leftMotor; // 1.565
    private EncoderMotor rightMotor; // 1.565
    //private RateLimiter leftRateLimiter;
    //private RateLimiter rightRateLimiter;

    private double controllerScale;
    private double maxSpeed;
    private double controllerDeadBand = 0.2;
    private int controllerPower = 2;

    private boolean useClosedLoop = true;

    public StandardDriveTrain(
            EncoderMotor leftMotor, EncoderMotor rightMotor,
            double maxAcceleration, double controllerScale, Controller controller) {
        super("driveTrain");
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        //this.rightRateLimiter = new RateLimiter(maxAcceleration / 50);
        //this.leftRateLimiter = new RateLimiter(maxAcceleration / 50);
        this.maxSpeed = controllerScale;
        this.controllerScale = controllerScale;
        this.controller = controller;
    }

    private double leftDemand = 0;
    private double leftOutputRaw = 0;

    private double rightDemand = 0;
    private double rightOutputRaw = 0;

    public void setLeftVelocity(double velocity) {
        velocity = safeVelocity(velocity);
        this.leftDemand = velocity;
        this.leftOutputRaw = this.leftMotor.setVelocity(velocity);
    }

    public void setRightVelocity(double velocity) {
        velocity = safeVelocity(velocity);
        this.rightDemand = velocity;
        this.rightOutputRaw = this.rightMotor.setVelocity(velocity);
    }

    public void setVelocity(double velocity) {
        setLeftVelocity(velocity);
        setRightVelocity(velocity);
    }

    public void setLeftPercentOutput(double percent) {
        percent = safePercent(percent);
        this.leftDemand = percent;
        this.leftOutputRaw = percent;
        this.leftMotor.setPercentOutput(percent);
    }

    public void setRightPercentOutput(double percent) {
        percent = safePercent(percent);
        this.rightDemand = percent;
        this.rightOutputRaw = percent;
        this.rightMotor.setPercentOutput(percent);
    }

    public void setPercentOutput(double percent) {
        this.setLeftPercentOutput(percent);
        this.setRightPercentOutput(percent);
    }

    @Override
    public void control() {
        double fb = -Util.adjustInput(controller.getAxis(Axis.LEFT_Y), controllerDeadBand, controllerPower);
        double lr = Util.adjustInput(controller.getAxis(Axis.RIGHT_X), controllerDeadBand, controllerPower);

        double left = fb - lr;
        double right = fb + lr;

        if (useClosedLoop) {
            setLeftVelocity(left * controllerScale);
            setRightVelocity(right * controllerScale);
        } else {
            setLeftPercentOutput(left);
            setRightPercentOutput(right);
        }

        if (controller.buttonPressed(Button.START)) {
            this.useClosedLoop = true;
        }

        if (controller.buttonPressed(Button.BACK)) {
            this.useClosedLoop = false;
        }

        if(controller.getAxis(Axis.LEFT_TRIGGER) > 0.5) {
            this.resetDistance();
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
        setPercentOutput(0);
        //this.leftMotor.resetDistance();
        //this.rightMotor.resetDistance();
    }

    @Override
    public Map<String, Supplier<Object>> NTSets() {
        // there are more than 10 so you can't use Map.of() :(
        return Map.ofEntries(
                Map.entry("left/velocity", leftMotor::getVelocity),
                Map.entry("left/distance", leftMotor::getDistance),
                Map.entry("left/demand", () -> leftDemand),
                Map.entry("left/demandRaw", () -> leftOutputRaw),
                Map.entry("left/received/velocity", leftMotor::getVelocityRaw),
                Map.entry("left/received/outputPercent", leftMotor::getOutputPercent),

                Map.entry("right/velocity", rightMotor::getVelocity),
                Map.entry("right/distance", rightMotor::getDistance),
                Map.entry("right/demand", () -> rightDemand),
                Map.entry("right/demandRaw", () -> rightOutputRaw),
                Map.entry("right/received/velocity", rightMotor::getVelocityRaw),
                Map.entry("right/received/outputPercent", rightMotor::getOutputPercent),

                Map.entry("closedLoopControl", () -> useClosedLoop)
        );
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setClosedLoop(boolean useClosedLoop) {
        this.useClosedLoop = useClosedLoop;
    }

    public void setControllerDeadBand(double controllerDeadBand) {
        this.controllerDeadBand = controllerDeadBand;
    }

    public void setControllerPower(int controllerPower) {
        this.controllerPower = controllerPower;
    }

    public double getLeftDemand() {
        return this.leftDemand;
    }

    public double getRightDemand() {
        return this.rightDemand;
    }

    public double getAverageDemand() {
        return (this.leftDemand + this.rightDemand) / 2;
    }

    public double getLeftVelocity() {
        return this.leftMotor.getVelocity();
    }

    public double getRightVelocity() {
        return this.leftMotor.getVelocity();
    }

    public double getAverageVelocity() {
        return (this.leftMotor.getVelocity() + this.rightMotor.getVelocity()) / 2;
    }


    public Controller getController() {
        return controller;
    }

    public double getAverageDistance() {
        return (leftMotor.getDistance() + rightMotor.getDistance()) / 2;
    }

    public void setLeftMotorConfig(EncoderMotorConfig config) {
        this.leftMotor.setConfig(config);
    }

    public void setRightMotorConfig(EncoderMotorConfig config) {
        this.rightMotor.setConfig(config);
    }

    public void setMotorConfigs(EncoderMotorConfig config) {
        setLeftMotorConfig(config);
        setRightMotorConfig(config);
    }

    public void resetDistance() {
        this.leftMotor.resetDistance();
        this.rightMotor.resetDistance();
    }
}
