package de.htw.tojato.robotik.earthrover;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import de.htw.tojato.robotik.earthrover.logger.LoggerNames;
import de.htw.tojato.robotik.earthrover.logger.RootLogger;
import de.htw.tojato.robotik.earthrover.sensors.SensorCollection;
import de.htw.tojato.robotik.earthrover.sensors.extendedsensors.CompassHTSensorExt;
import de.htw.tojato.robotik.earthrover.sensors.extendedsensors.GyroSensorExt;
import de.htw.tojato.robotik.earthrover.sensors.extendedsensors.IRSeekerExt;
import de.htw.tojato.robotik.earthrover.sensors.extendedsensors.SensorPortExt;
import lejos.nxt.*;
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

    public static void main2(String[] args) throws Exception {
        NXTInfo slaveInfo = new NXTInfo(NXTComm.LCP, "SlaveDriver", "0016530EBFFD");
        NXTComm slaveComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
        slaveComm.open(slaveInfo, NXTComm.LCP);
        NXTCommand slaveCommand = new NXTCommand(slaveComm);
        SensorPortExt.setNXTCommand(slaveCommand, 3);
        IRSeekerExt irSeekerExt = new IRSeekerExt(slaveCommand, SensorPortExt.S4);
        while (true) {
            System.out.println("IR: " + irSeekerExt.getDirection());
            Thread.sleep(1000);

        }
    }

    public static void main(String[] args) throws Exception {
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

        //KalmanCompass kalmanCompass = KalmanCompass.getInstance(gyro, compass);

        SensorCollection sensorCollection = SensorCollection.getInstance(nxt09Command, slaveCommand);

        //GyroSensor gyro = new GyroSensor(SensorPort.S2);
        while (true) {
            System.out.println("Heading: " + sensorCollection.getHeading());
            System.out.println("IRSeeker: " + sensorCollection.getIrSeeker().getDirection());
            Thread.sleep(1000);

        }
    }

    /*private static void i2cSend(NXTCommand nxtCommand) {
        byte[] addr = new byte[1];
        addr[0] = 0x02; // default I2C address

        if (nxtCommand == null)	return;
        try {
            nxtCommand.LSWrite(
                    (byte) sensorList.getSelectedIndex(),
                    appendBytes(addr, fromHex(txData.getText())),
                    ((Number) rxDataLength.getValue()).byteValue());
        } catch (IOException ioe) {
            showMessage("IO Exception sending txData");
        }
    }

    private static void i2cStatus(NXTCommand nxtCommand) {
        if (nxtCommand == null)	return;
        try {
            byte[] reply = nxtCommand.LSGetStatus((byte) sensorList.getSelectedIndex());
            if (reply != null) {
                System.out.println("LSStatus reply length = " + reply.length);
                String hex = toHex(reply);
                rxData.setText(hex);
            } else
                rxData.setText("null");
        } catch (IOException ioe) {
            showMessage("IO Exception getting status");
        }
    }
    private static void i2cReceive(NXTCommand nxtCommand) {
        if (nxtCommand == null)	return;
        try {
            byte[] reply = nxtCommand.LSRead((byte) sensorList.getSelectedIndex());
            if (reply != null) {
                String hex = toHex(reply);
                rxData.setText(hex);
            } else
                rxData.setText("null");
        } catch (IOException ioe) {
            showMessage("IO Exception reading rxData");
        }
    }*/

    /**
     * Convert a byte array to a string of hex characters
     */
    private static String toHex(byte[] b) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            if (i > 0) {
                output.append(' ');
            }
            byte j = b[i];
            output.append(Character.forDigit((j >> 4) & 0xF, 16));
            output.append(Character.forDigit(j & 0xF, 16));
        }
        return output.toString();
    }

    /**
     * Convert a string of hex characters to a byte array
     */
    private static byte[] fromHex(String s) {
        byte[] reply = new byte[s.length() / 2];
        for (int i = 0; i < reply.length; i++) {
            char c1 = s.charAt(i * 2);
            char c2 = s.charAt(i * 2 + 1);
            reply[i] = (byte) (getHexDigit(c1) << 4 | getHexDigit(c2));
        }
        return reply;
    }

    /**
     * Convert a character to a hex digit
     */
    private static int getHexDigit(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        return 0;
    }

    /**
     * Add one byte array to another
     */
    private static byte[] appendBytes(byte[] array1, byte[] array2) {
        byte[] array = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, array, 0, array1.length);
        System.arraycopy(array2, 0, array, array1.length, array2.length);
        return array;
    }

    private static void motor() {
        NXTMotor motor = new NXTMotor(MotorPort.C, BasicMotorPort.PWM_FLOAT);
        motor.setPower(30);
        motor.forward();
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        motor.setPower(0);
        motor.stop();
    }

    private static void distance() {
        UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S3);
        UltrasonicSensor ultrasonicSensor2 = new UltrasonicSensor(SensorPort.S4);
        while (true) {
            System.out.println("Distances: " + ultrasonicSensor.getDistance() + "/" + ultrasonicSensor2.getDistance());
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
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
        }
        catch (IOException e) {
            logger.log("IOException" + e.getMessage(), Level.SEVERE, e);
        }
    }
}
