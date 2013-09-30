package de.htw.tojato.robotik.earthrover.sensors.extendedsensors;

import de.htw.tojato.robotik.earthrover.logger.LoggerNames;
import de.htw.tojato.robotik.earthrover.logger.RootLogger;

import java.util.Date;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 27.09.13
 * Time: 23:55
 * To change this template use File | Settings | File Templates.
 */
public class KalmanCompassExt {
    private static KalmanCompassExt instance;
    private        RootLogger         logger;
    private        GyroSensorExt      gyroSensor;
    private        CompassHTSensorExt magneticSensor;
    private        float              heading;
    private        float              mounting;
    private        Thread             updateHeading;
    private        boolean            keepUpdating;

    private KalmanCompassExt() {
        heading = -1.0f;
        mounting = 1.0f;
        logger = RootLogger.getInstance(LoggerNames.MAIN_LOGGER);
        keepUpdating = true;
        updateHeading = new Thread() {
            @Override
            public void run() {
                float varCompass = 0.9f;
                float varGyro = 0.119f;
                float varFilterPredicted;
                float varFilterUpdated;
                float kalmanGain;
                float compassMeasured;
                float compassPredicted;
                float compassUpdated;
                float gyroMeasured;
                float timeSpan;
                long timeStart = Long.MAX_VALUE;
                long timeEnd;

                boolean disturbed;

                //Get Gyro offset
                int tempGyroOffset = 0;
                gyroSensor.setOffset(0);
                for (int i = 0; i < 100; i++) {
                    tempGyroOffset += gyroSensor.getAngularVelocity();
                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e) {
                        logger.log("Thread Exception: " + e.getMessage(), Level.SEVERE, e);
                    }
                }
                tempGyroOffset = (tempGyroOffset / 100);
                gyroSensor.setOffset(tempGyroOffset);

                // initialise the filter;
                compassUpdated = magneticSensor.getDegrees();
                varFilterUpdated = 0;
                // Run the filter forever;
                while (true) {
                    // get time span;
                    timeEnd = new Date().getTime();
                    timeSpan = (long) ((timeEnd - timeStart) / 1000.0);
                    if (timeSpan <= 0) {
                        timeSpan = 0.02f; // this is to compensate for wrapping around the nPgmtime variable;
                    }
                    timeStart = new Date().getTime();

                    // get measurements from sensors
                    // (when changing the sample size of the gyro, one must also change the variance)
                    compassMeasured = magneticSensor.getDegrees();
                    gyroMeasured = mounting * (getMeanSensorGyro(5, 4));

                    // predict;
                    compassPredicted = compassUpdated + timeSpan * gyroMeasured;
                    varFilterPredicted = varFilterUpdated + varGyro;

                    // heading must be between 0 and 359
                    if (compassPredicted < 0) {
                        compassPredicted += 360;
                    }
                    if (compassPredicted >= 360) {
                        compassPredicted -= 360;
                    }
                    // Detect _compass disturbance;
                    if (Math.abs(compassPredicted - compassMeasured) > 2 * Math.sqrt(varFilterPredicted)) {
                        disturbed = true;
                    } else {
                        disturbed = false;
                    }

                    // get Kalman gain;
                    if (disturbed) {
                        kalmanGain = 0;
                    } else {
                        kalmanGain = varFilterPredicted / (varFilterPredicted + varCompass);
                    }

                    // update;
                    compassUpdated = compassPredicted + kalmanGain * (compassMeasured - compassPredicted);
                    varFilterUpdated = varFilterPredicted + kalmanGain * (varCompass - varFilterPredicted);

                    // make result available gobally
                    heading = compassUpdated;
                }
            }
        };
        updateHeading.start();
    }

    public static KalmanCompassExt getInstance(GyroSensorExt gyroSensor, CompassHTSensorExt magneticSensor) {
        if (instance == null) {
            instance = new KalmanCompassExt();
        }
        instance.gyroSensor = gyroSensor;
        instance.magneticSensor = magneticSensor;
        if (!instance.updateHeading.isAlive()) {
            instance.updateHeading.start();
        }
        return instance;
    }

    public float getHeading() {
        return heading;
    }

    private float getMeanSensorMagnetic(int interval, int n) {
        float result = 0;
        for (int i = 0; i < n; i++) {
            result += magneticSensor.getDegrees();
            try {
                Thread.sleep(interval);
            }
            catch (InterruptedException e) {
                logger.log("Thread Exception: " + e.getMessage(), Level.SEVERE, e);
            }
        }
        result = result / n;
        return result;
    }

    private float getMeanSensorGyro(int interval, int n) {
        float result = 0;
        for (int i = 0; i < n; i++) {
            result += gyroSensor.getAngularVelocity();
            try {
                Thread.sleep(interval);
            }
            catch (InterruptedException e) {
                logger.log("Thread Exception: " + e.getMessage(), Level.SEVERE, e);
            }
        }
        result = result / n;
        return result;
    }
}
