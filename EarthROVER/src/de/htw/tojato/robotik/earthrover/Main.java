package de.htw.tojato.robotik.earthrover;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import de.htw.tojato.robotik.earthrover.logger.LoggerNames;
import de.htw.tojato.robotik.earthrover.logger.RootLogger;
import de.htw.tojato.robotik.earthrover.sensors.KalmanCompass;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.addon.GyroSensor;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 27.09.13
 * Time: 18:02
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private static RootLogger logger = RootLogger.getInstance(LoggerNames.MAIN_LOGGER);

    public static void main(String[] args) {
        CompassHTSensor compass = new CompassHTSensor(SensorPort.S1);
        GyroSensor gyro = new GyroSensor(SensorPort.S2);
        KalmanCompass kalmanCompass = KalmanCompass.getInstance(gyro, compass);
        while (true) {
            logger.log("KalmanValue: " + kalmanCompass.getHeading(), Level.INFO);
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                logger.log("Thread Exception: " + e.getMessage(), Level.SEVERE, e);
            }
        }
//        startServer();
    }

    private static void startServer() {
        try {
            HttpServer server = HttpServerFactory.create("http://localhost:9998/");
            server.start();
            logger.log("Server running", Level.INFO);
            logger.log("Hit return to stop...", Level.INFO);

            System.in.read();
            logger.log("Stopping server", Level.INFO);
            server.stop(0);
            logger.log("Server stopped", Level.INFO);
        }
        catch (IOException e) {
            logger.log("IOException" + e.getMessage(), Level.SEVERE, e);
        }
    }
}
