import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.comm.RConsole;

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
        lastHeading = sensorCollection.getHeading();
        motorA.setPower(power);
        motorB.setPower(-power);
        motorC.setPower(power);
        motorA.forward();
        motorB.forward();
        motorC.forward();
    }

    public void turnRight(int power, float headingDelta) throws InterruptedException {
        stop(false);
        float startHeading = sensorCollection.getHeading();
        float destinationHeading = startHeading + headingDelta;
        motorA.setPower(power);
        motorB.setPower(-power);
        motorC.setPower(power);
        motorA.forward();
        motorB.forward();
        motorC.forward();
        if (destinationHeading > 360.f) {
            destinationHeading -= 360.f;
            //wait to jump past the 359.9 degree mark
            while (sensorCollection.getHeading() >= startHeading - 2) {
                Thread.sleep(100);
            }
        }
        while (sensorCollection.getHeading() < destinationHeading) {
            Thread.sleep(100);
        }
    }

    public void turnLeft(int power) {
        stop(false);
        pathMarkCheck();
        lastUsedType = PathMark.PathMarkType.TURN_LEFT;
        lastHeading = sensorCollection.getHeading();
        motorA.setPower(-power);
        motorB.setPower(power);
        motorC.setPower(-power);
        motorA.forward();
        motorB.forward();
        motorC.forward();
    }

    public void turnLeft(int power, float headingDelta) throws InterruptedException {
        stop(false);
        float startHeading = sensorCollection.getHeading();
        float destinationHeading = startHeading - headingDelta;
        motorA.setPower(-power);
        motorB.setPower(power);
        motorC.setPower(-power);
        motorA.forward();
        motorB.forward();
        motorC.forward();
        if (destinationHeading < 0.f) {
            destinationHeading += 360.f;
            //wait to jump past the 0 degree mark
            while (sensorCollection.getHeading() <= startHeading + 2) {
                Thread.sleep(100);
            }
        }

        while (sensorCollection.getHeading() > destinationHeading) {
            Thread.sleep(100);
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
                RConsole.println("FORWARD");
                pathMarks.add(new PathMark(motorB.getTachoCount(), PathMark.PathMarkType.FORWARD));
                break;
            }
            case BACKWARD: {
                RConsole.println("BACKWARD");
                pathMarks.add(new PathMark(-motorB.getTachoCount(), PathMark.PathMarkType.BACKWARD));
                break;
            }
            case TURN_LEFT: {
                RConsole.println("TURN_LEFT");
                float headingDelta;
                float destinationHeading = sensorCollection.getHeading();
                RConsole.println("LastHeading: " + Float.toString(lastHeading));
                RConsole.println("DestinationHeading: " + Float.toString(destinationHeading));
                if (destinationHeading > lastHeading) {
                    headingDelta = lastHeading + (359.9f - destinationHeading);
                } else {
                    headingDelta = lastHeading - destinationHeading;
                }
                RConsole.println("headingDelta: " + Float.toString(headingDelta));
                pathMarks.add(new PathMark(headingDelta, PathMark.PathMarkType.TURN_LEFT));
                break;
            }
            case TURN_RIGHT: {
                RConsole.println("TURN_RIGHT");
                float headingDelta;
                float destinationHeading = sensorCollection.getHeading();
                RConsole.println("LastHeading: " + Float.toString(lastHeading));
                RConsole.println("DestinationHeading: " + Float.toString(destinationHeading));
                if (destinationHeading < lastHeading) {
                    headingDelta = destinationHeading + (359.9f - lastHeading);
                } else {
                    headingDelta = destinationHeading - lastHeading;
                }
                RConsole.println("headingDelta: " + Float.toString(headingDelta));
                pathMarks.add(new PathMark(headingDelta, PathMark.PathMarkType.TURN_RIGHT));
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
        RConsole.println("PathMarker:");
        int pos = 1;
        for (PathMark pathMark : pathMarks) {
            if (pathMark.getType() == PathMark.PathMarkType.BACKWARD || pathMark.getType() == PathMark.PathMarkType.FORWARD) {
                RConsole.println("PathMarker " + Integer.toString(pos));
                RConsole.println("\tType: " + pathMark.getType().name());
                RConsole.println("\tDegrees Driven: " + Integer.toString(pathMark.getDegrees()));
            } else {
                RConsole.println("PathMarker " + Integer.toString(pos));
                RConsole.println("\tType: " + pathMark.getType().name());
                RConsole.println("\tHeadingDelte: " + Float.toString(pathMark.getHeadingDelta()));

            }
        }
    }

    public int getDrivenDistance(){
        return motorB.getTachoCount();
    }
}
