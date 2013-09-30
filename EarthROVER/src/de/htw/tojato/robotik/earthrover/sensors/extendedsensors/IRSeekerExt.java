package de.htw.tojato.robotik.earthrover.sensors.extendedsensors;

import lejos.nxt.I2CPort;
import lejos.nxt.remote.NXTCommand;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 29.09.13
 * Time: 20:57
 * To change this template use File | Settings | File Templates.
 */
public class IRSeekerExt extends I2CSensorExt {
    byte[] buf = new byte[1];

    public IRSeekerExt(NXTCommand nxtCommand, I2CPort port) {
        super(nxtCommand, port);
    }

    /**
     * Returns the direction of the target (1-9)
     * or zero if no target.
     *
     * @return direction
     */
    public int getDirection() {
        int ret = getData(0x42, buf, 1);
        if (ret != 0) {
            return -1;
        }
        return (0xFF & buf[0]);
    }

    /**
     * Returns value of sensor 1 - 5.
     *
     * @return sensor value (0 to 255).
     */
    public int getSensorValue(int id) {
        if (id <= 0 || id > 5) {
            return -1;
        }
        int ret = getData(0x42 + id, buf, 1);
        if (ret != 0) {
            return -1;
        }
        return (0xFF & buf[0]);
    }
}
