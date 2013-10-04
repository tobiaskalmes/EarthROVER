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
    //private EOPD eopd;
    private KalmanCompass kalmanCompass;
    private IRSeeker irSeeker;

    private SensorCollection() {
        init();
    }

    public static SensorCollection getInstance() {
        return instance == null ? (instance = new SensorCollection()) : instance;
    }

    private void init() {
        CompassHTSensor compass = new CompassHTSensor(SensorPort.S1);
        GyroSensor gyro = new GyroSensor(SensorPort.S2);
        ultrasonicSensor = new UltrasonicSensor(SensorPort.S3);
        //eopd = new EOPD(SensorPort.S3, true);
        irSeeker = new IRSeeker(SensorPort.S4);
        kalmanCompass = KalmanCompass.getInstance(gyro, compass);
    }

    public float getHeading() {
        return kalmanCompass.getHeading();
    }

    public int getDistance() {
        return ultrasonicSensor.getDistance();
        //return (int) ((1095.35 / Math.sqrt(eopd.readRawValue())) - 12);
    }

    public IRSeeker getIrSeeker() {
        return irSeeker;
    }
}
