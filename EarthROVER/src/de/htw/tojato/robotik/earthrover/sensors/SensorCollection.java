package de.htw.tojato.robotik.earthrover.sensors;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.addon.IRSeeker;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 30.09.13
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public class SensorCollection {
    private static SensorCollection instance;
    private UltrasonicSensor ultrasonicSensor;
    private KalmanCompass kalmanCompass;
    private IRSeeker irSeeker;

    private SensorCollection() {
        init();
    }

    public static SensorCollection getInstance() {
        if (instance == null) {
            instance = new SensorCollection();
        }
        return instance;
    }

    private void init() {
        CompassHTSensor compass = new CompassHTSensor(SensorPort.S1);
        GyroSensor gyro = new GyroSensor(SensorPort.S2);
        ultrasonicSensor = new UltrasonicSensor(SensorPort.S3);
        irSeeker = new IRSeeker(SensorPort.S4);
        kalmanCompass = KalmanCompass.getInstance(gyro, compass);
    }

    public float getHeading() {
        return kalmanCompass.getHeading();
    }

    public int getDistanceLeft() {
        return ultrasonicSensor.getDistance();
    }

    public IRSeeker getIrSeeker() {
        return irSeeker;
    }
}
