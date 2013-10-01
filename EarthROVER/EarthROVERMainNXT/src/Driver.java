import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 30.09.13
 * Time: 22:51
 * To change this template use File | Settings | File Templates.
 */
public class Driver {
    private static Driver instance;
    private NXTMotor motorA;
    private NXTMotor motorB;
    private NXTMotor motorC;
    private List<PathMark> pathMarks;
    private PathMark.PathMarkType lastUsedType;
    private float lastHeading;
    private SensorCollection sensorCollection;

    private Driver() {
        //Init Motors
        motorA = new NXTMotor(MotorPort.A);
        motorB = new NXTMotor(MotorPort.B);
        motorC = new NXTMotor(MotorPort.C);
        pathMarks = new ArrayList<PathMark>();
        lastUsedType = PathMark.PathMarkType.NONE;
        sensorCollection = SensorCollection.getInstance();
    }

    public static Driver getInstance() {
        return instance == null ? (instance = new Driver()) : instance;
    }

    public void backward(int power) {
        pathMarkCheck();
        lastUsedType = PathMark.PathMarkType.BACKWARD;
        motorA.setPower(-power);
        motorB.setPower(-power);
        motorC.setPower(power);
        motorA.backward();
        motorB.backward();
        motorC.backward();
    }

    /**
     * Only used to drive home
     *
     * @param power
     * @param degrees
     * @throws InterruptedException
     */
    private void backward(int power, int degrees) throws InterruptedException {
        resetTachoCounts();
        motorA.setPower(-power);
        motorB.setPower(-power);
        motorC.setPower(power);
        motorA.backward();
        motorB.backward();
        motorC.backward();
        while (motorC.getTachoCount() > -degrees) {
            Thread.sleep(100);
        }
    }

    public void forward(int power) {
        pathMarkCheck();
        lastUsedType = PathMark.PathMarkType.FORWARD;
        motorA.setPower(power);
        motorB.setPower(power);
        motorC.setPower(-power);
        motorA.forward();
        motorB.forward();
        motorC.forward();
    }

    /**
     * Only used to drive home
     *
     * @param power
     * @param degrees
     */
    private void forward(int power, int degrees) throws InterruptedException {
        resetTachoCounts();
        motorA.setPower(power);
        motorB.setPower(power);
        motorC.setPower(-power);
        motorA.forward();
        motorB.forward();
        motorC.forward();
        while (motorC.getTachoCount() < degrees) {
            Thread.sleep(100);
        }
    }

    public void turnRight(int power) {
        pathMarkCheck();
        lastUsedType = PathMark.PathMarkType.TURN_RIGHT;
        motorA.setPower(power);
        motorB.setPower(-power);
        motorC.setPower(power);
        motorA.backward();
        motorB.backward();
        motorC.backward();
    }

    private void turnRight(int power, float headingDelta) throws InterruptedException {
        float startHeading = sensorCollection.getHeading();
        float destinationHeading = startHeading + headingDelta;
        if (destinationHeading > 360.f) {
            destinationHeading -= 360.f;
            //wait to jump past the 359.9 degree mark
            while (sensorCollection.getHeading() <= 2 && sensorCollection.getHeading() >= 0) {
                Thread.sleep(100);
            }
            while (sensorCollection.getHeading() < destinationHeading) {
                Thread.sleep(100);
            }
        } else {
            motorA.setPower(power);
            motorB.setPower(-power);
            motorC.setPower(power);
            motorA.backward();
            motorB.backward();
            motorC.backward();
            while (sensorCollection.getHeading() < destinationHeading) {
                Thread.sleep(100);
            }
        }
    }

    public void turnLeft(int power) {
        pathMarkCheck();
        lastUsedType = PathMark.PathMarkType.TURN_LEFT;
        motorA.setPower(power);
        motorB.setPower(-power);
        motorC.setPower(power);
        motorA.forward();
        motorB.forward();
        motorC.forward();
    }

    public void stop() {
        motorA.setPower(0);
        motorB.setPower(0);
        motorC.setPower(0);
        motorA.stop();
        motorB.stop();
        motorC.stop();
        pathMarkCheck();
        lastUsedType = PathMark.PathMarkType.NONE;
    }

    private void resetTachoCounts() {
        motorA.resetTachoCount();
        motorB.resetTachoCount();
        motorC.resetTachoCount();
    }

    private void pathMarkCheck() {
        switch (lastUsedType) {
            case FORWARD: {
                pathMarks.add(new PathMark(-motorC.getTachoCount(), PathMark.PathMarkType.FORWARD));
                break;
            }
            case BACKWARD: {
                pathMarks.add(new PathMark(motorC.getTachoCount(), PathMark.PathMarkType.BACKWARD));
                break;
            }
            case TURN_LEFT: {
                pathMarks.add(new PathMark(sensorCollection.getHeading() - lastHeading, PathMark.PathMarkType.TURN_LEFT));
                break;
            }
            case TURN_RIGHT: {
                pathMarks.add(new PathMark(-(sensorCollection.getHeading() - lastHeading), PathMark.PathMarkType.TURN_RIGHT));
                break;
            }
        }
        resetTachoCounts();
    }

    public void driveHomeSimple() {
        for (int i = pathMarks.size() - 1; i >= 0; ++i) {
            PathMark mark = pathMarks.get(i);
            switch (mark.getType()) {
                case FORWARD: {

                    break;
                }
                case BACKWARD: {

                    break;
                }
                case TURN_LEFT: {

                    break;
                }
                case TURN_RIGHT: {

                    break;
                }
            }
        }
    }

    public void displayPathData() {
        LCD.clear();
        LCD.drawString("PathData:", 0, 0);
        int pos = 1;
        for (PathMark pathMark : pathMarks) {
            if (pathMark.getType() == PathMark.PathMarkType.BACKWARD || pathMark.getType() == PathMark.PathMarkType.FORWARD) {
                LCD.drawString(pathMark.getType().name() + pathMark.getDegrees(), 0, pos);
            } else {
                LCD.drawString(pathMark.getType().name() + pathMark.getHeading(), 0, pos);

            }
        }
    }
}
