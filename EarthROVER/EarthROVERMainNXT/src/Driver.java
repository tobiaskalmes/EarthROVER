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
        stop(false);
        pathMarkCheck();
        lastUsedType = PathMark.PathMarkType.BACKWARD;
        motorA.setPower(power);
        motorB.setPower(power);
        motorC.setPower(-power);
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
        stop(false);
        resetTachoCounts();
        motorA.setPower(power);
        motorB.setPower(power);
        motorC.setPower(-power);
        motorA.backward();
        motorB.backward();
        motorC.backward();
        while (motorB.getTachoCount() > -degrees) {
            Thread.sleep(100);
        }
        stop(false);
    }

    public void forward(int power) {
        stop(false);
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
        stop(false);
        resetTachoCounts();
        motorA.setPower(power);
        motorB.setPower(power);
        motorC.setPower(-power);
        motorA.forward();
        motorB.forward();
        motorC.forward();
        while (motorB.getTachoCount() < degrees) {
            Thread.sleep(100);
        }
        stop(false);
    }

    public void turnRight(int power) {
        stop(false);
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
        stop(false);
        float startHeading = sensorCollection.getHeading();
        float destinationHeading = startHeading + headingDelta;
        if (destinationHeading > 360.f) {
            destinationHeading -= 360.f;
            //wait to jump past the 359.9 degree mark
            while (sensorCollection.getHeading() >= startHeading) {
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
        stop(false);
        pathMarkCheck();
        lastUsedType = PathMark.PathMarkType.TURN_LEFT;
        motorA.setPower(power);
        motorB.setPower(-power);
        motorC.setPower(power);
        motorA.forward();
        motorB.forward();
        motorC.forward();
    }

    private void turnLeft(int power, float headingDelta) throws InterruptedException {
        stop(false);
        float startHeading = sensorCollection.getHeading();
        float destinationHeading = startHeading + headingDelta;
        if (destinationHeading < 0.f) {
            destinationHeading += 360.f;
            //wait to jump past the 0 degree mark
            while (sensorCollection.getHeading() >= 0) {
                Thread.sleep(100);
            }
            while (sensorCollection.getHeading() > destinationHeading) {
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

    public void stop() {
        stop(true);
    }

    private void stop(boolean doCheck) {
        motorA.setPower(0);
        motorB.setPower(0);
        motorC.setPower(0);
        motorA.stop();
        motorB.stop();
        motorC.stop();
        if (doCheck) {
            pathMarkCheck();
            lastUsedType = PathMark.PathMarkType.NONE;
        }
    }

    private void resetTachoCounts() {
        motorA.resetTachoCount();
        motorB.resetTachoCount();
        motorC.resetTachoCount();
    }

    private void pathMarkCheck() {
        switch (lastUsedType) {
            case FORWARD: {
                pathMarks.add(new PathMark(motorB.getTachoCount(), PathMark.PathMarkType.FORWARD));
                break;
            }
            case BACKWARD: {
                pathMarks.add(new PathMark(-motorB.getTachoCount(), PathMark.PathMarkType.BACKWARD));
                break;
            }
            case TURN_LEFT: {
                float headingDelta;
                float destinationHeading = sensorCollection.getHeading();
                if (destinationHeading > lastHeading) {
                    headingDelta = lastHeading + (359.9f - destinationHeading);
                } else {
                    headingDelta = lastHeading - destinationHeading;
                }
                pathMarks.add(new PathMark(headingDelta, PathMark.PathMarkType.TURN_RIGHT));
                break;
            }
            case TURN_RIGHT: {
                float headingDelta;
                float destinationHeading = sensorCollection.getHeading();
                if (destinationHeading < lastHeading) {
                    headingDelta = destinationHeading + (359.9f - lastHeading);
                } else {
                    headingDelta = destinationHeading - lastHeading;
                }
                pathMarks.add(new PathMark(headingDelta, PathMark.PathMarkType.TURN_LEFT));
                break;
            }
        }
        resetTachoCounts();
    }

    public void driveHomeSimple() throws InterruptedException {
        for (int i = pathMarks.size() - 1; i >= 0; --i) {
            PathMark mark = pathMarks.get(i);
            switch (mark.getType()) {
                case FORWARD: {
                    backward(30, mark.getDegrees());
                    break;
                }
                case BACKWARD: {
                    forward(30, mark.getDegrees());
                    break;
                }
                case TURN_LEFT: {
                    turnRight(50, mark.getHeadingDelta());
                    break;
                }
                case TURN_RIGHT: {
                    turnLeft(50, mark.getHeadingDelta());
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
                LCD.drawString(pathMark.getType().name() + pathMark.getHeadingDelta(), 0, pos);

            }
        }
    }
}
