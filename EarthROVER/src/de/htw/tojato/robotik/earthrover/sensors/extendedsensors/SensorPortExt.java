package de.htw.tojato.robotik.earthrover.sensors.extendedsensors;

import lejos.nxt.I2CPort;
import lejos.nxt.LegacySensorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.remote.InputValues;
import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.NXTProtocol;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 29.09.13
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */
public class SensorPortExt implements NXTProtocol, LegacySensorPort, I2CPort {
    private NXTCommand nxtCommand;

    private int id;

    public static SensorPortExt S1 = new SensorPortExt(0);
    public static SensorPortExt S2 = new SensorPortExt(1);
    public static SensorPortExt S3 = new SensorPortExt(2);
    public static SensorPortExt S4 = new SensorPortExt(3);

    private static SensorPortExt[] ports = {S1, S2, S3, S4};


    private SensorPortExt(int port) {
        id = port;
    }

    public int getId() {
        return id;
    }

    public static void setNXTCommand(NXTCommand nxtCommand, int port) {
        ports[port].nxtCommand = nxtCommand;
    }

    public static SensorPortExt getInstance(int port) {
        return ports[port];
    }

    public void setTypeAndMode(int type, int mode) {
        try {
            nxtCommand.setInputMode(id, type, mode);
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public void setType(int type) {
        int mode = getMode();
        try {
            nxtCommand.setInputMode(id, type, mode);
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public void setMode(int mode) {
        int type = getType();
        try {
            nxtCommand.setInputMode(id, type, mode);
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public int getType() {
        InputValues vals;
        try {
            vals = nxtCommand.getInputValues(id);
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            return 0;
        }
        return vals.sensorType;
    }

    public int getMode() {
        InputValues vals;
        try {
            vals = nxtCommand.getInputValues(id);
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            return 0;
        }
        return vals.sensorMode;
    }

    /**
     * Reads the boolean value of the sensor.
     *
     * @return Boolean value of sensor.
     */
    public boolean readBooleanValue() {
        InputValues vals;
        try {
            vals = nxtCommand.getInputValues(id);
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            return false;
        }
        return (vals.rawADValue < 600);
    }

    /**
     * Reads the raw value of the sensor.
     *
     * @return Raw sensor value. Range is device dependent.
     */
    public int readRawValue() {
        InputValues vals;
        try {
            vals = nxtCommand.getInputValues(id);
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            return 1023;
        }
        return vals.rawADValue;
    }

    /**
     * Reads the normalized value of the sensor.
     *
     * @return Normalized value. 0 to 1023
     */
    public int readNormalizedValue() {
        InputValues vals;
        try {
            vals = nxtCommand.getInputValues(id);
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            return 0;
        }
        return vals.normalizedADValue;
    }

    /**
     * Returns scaled value, depending on mode of sensor.
     * e.g. BOOLEANMODE returns 0 or 1.
     * e.g. PCTFULLSCALE returns 0 to 100.
     *
     * @return the value
     * @see SensorPort#setTypeAndMode(int, int)
     */
    public int readValue() {
        InputValues vals;
        try {
            vals = nxtCommand.getInputValues(id);
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            return 0;
        }
        return vals.scaledValue;
    }

    /**
     * Activate an RCX Light Sensor
     */
    public void activate() {
        setType(REFLECTION);
    }

    /**
     * Passivate an RCX Light Sensor
     */
    public void passivate() {
        setType(NO_SENSOR);
    }

    /**
     * Return a variable number of sensor values.
     * NOTE: Currently there is no way to return multiple results from a
     * remote sensor, so we return an error.
     *
     * @param values An array in which to return the sensor values.
     * @return The number of values returned.
     */
    public int readValues(int[] values) {
        return -1;
    }

    /**
     * Return a variable number of raw sensor values
     * NOTE: Currently there is no way to return multiple results from a
     * remote sensor, so we return an error.
     *
     * @param values An array in which to return the sensor values.
     * @return The number of values returned.
     */
    public int readRawValues(int[] values) {
        return -1;
    }

    public void enableColorSensor() {

    }
}
