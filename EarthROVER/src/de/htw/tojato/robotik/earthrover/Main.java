package de.htw.tojato.robotik.earthrover;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import de.htw.tojato.robotik.earthrover.driver.Driver;
import de.htw.tojato.robotik.earthrover.logger.LoggerNames;
import de.htw.tojato.robotik.earthrover.logger.RootLogger;
import de.htw.tojato.robotik.earthrover.sensors.extendedsensors.CompassHTSensorExt;
import de.htw.tojato.robotik.earthrover.sensors.extendedsensors.GyroSensorExt;
import de.htw.tojato.robotik.earthrover.sensors.extendedsensors.SensorCollectionExt;
import de.htw.tojato.robotik.earthrover.sensors.extendedsensors.SensorPortExt;
import lejos.nxt.remote.NXTCommand;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

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
        /*SensorCollection sensorCollection = SensorCollection.getInstance();
        while (true) {
            logger.log("KalmanCompass - Heading: " + sensorCollection.getHeading(), Level.INFO);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }*/
        Driver.getInstance().drive(30, 360);
    }

    public static void main2(String[] args) throws Exception {
        //create NXTCommand for first NXT
        NXTInfo nxt09Info = new NXTInfo(NXTComm.LCP, "NXT09", "0016530BEB04");
        NXTComm nxt09Comm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
        nxt09Comm.open(nxt09Info, NXTComm.LCP);
        NXTCommand nxt09Command = new NXTCommand(nxt09Comm);
        //set NXTCommand for sensors on first NXT
        SensorPortExt.setNXTCommand(nxt09Command, 0);
        SensorPortExt.setNXTCommand(nxt09Command, 1);

        //create NXTCommand for second NXT
        NXTInfo slaveInfo = new NXTInfo(NXTComm.LCP, "SlaveDriver", "0016530EBFFD");
        NXTComm slaveComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
        slaveComm.open(slaveInfo, NXTComm.LCP);
        NXTCommand slaveCommand = new NXTCommand(slaveComm);

        //set NXTCommand for sensors on second NXT
        SensorPortExt.setNXTCommand(slaveCommand, 3);

        CompassHTSensorExt compass = new CompassHTSensorExt(nxt09Command, SensorPortExt.S1);
        GyroSensorExt gyro = new GyroSensorExt(SensorPortExt.S2);

        //KalmanCompassExt kalmanCompass = KalmanCompassExt.getInstance(gyro, compass);

        SensorCollectionExt sensorCollection = SensorCollectionExt.getInstance(nxt09Command, slaveCommand);

        //GyroSensor gyro = new GyroSensor(SensorPort.S2);
        while (true) {
            System.out.println("Heading: " + sensorCollection.getHeading());
            System.out.println("IRSeeker: " + sensorCollection.getIrSeeker().getDirection());
            Thread.sleep(1000);

        }
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
        } catch (IOException e) {
            logger.log("IOException" + e.getMessage(), Level.SEVERE, e);
        }
    }
}
