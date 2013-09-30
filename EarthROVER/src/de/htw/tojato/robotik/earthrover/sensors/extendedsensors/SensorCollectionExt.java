package de.htw.tojato.robotik.earthrover.sensors.extendedsensors;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.remote.NXTCommand;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 29.09.13
 * Time: 12:20
 * To change this template use File | Settings | File Templates.
 */
public class SensorCollectionExt {
    private static SensorCollectionExt instance;
    private        UltrasonicSensor leftUltrasonic;
    private        UltrasonicSensor rightUltrasonic;
    private KalmanCompassExt kalmanCompass;
    private        IRSeekerExt      irSeeker;
    private        NXTCommand       nxtCommandMain;
    private        NXTCommand       nxtCommandSecond;

    private SensorCollectionExt() {
    }

    private void init() {
        CompassHTSensorExt compass = new CompassHTSensorExt(nxtCommandMain, SensorPortExt.S1);
        GyroSensorExt gyro = new GyroSensorExt(SensorPortExt.S2);
        kalmanCompass = KalmanCompassExt.getInstance(gyro, compass);
        leftUltrasonic = new UltrasonicSensor(SensorPort.S3);
        rightUltrasonic = new UltrasonicSensor(SensorPort.S4);
        irSeeker = new IRSeekerExt(nxtCommandSecond, SensorPortExt.S4);
    }

    public static SensorCollectionExt getInstance(NXTCommand nxtCommandMain, NXTCommand nxtCommandSecond) {
        if (instance == null) {
            instance = new SensorCollectionExt();
        }
        instance.nxtCommandMain = nxtCommandMain;
        instance.nxtCommandSecond = nxtCommandSecond;
        instance.init();
        return instance;
    }

    public float getHeading() {
        return kalmanCompass.getHeading();
    }

    public int getDistanceLeft() {
        return leftUltrasonic.getDistance();
    }

    public int getDistanceRight() {
        return rightUltrasonic.getDistance();
    }

    public IRSeekerExt getIrSeeker() {
        return irSeeker;
    }
}
