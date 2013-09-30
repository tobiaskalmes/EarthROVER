package de.htw.tojato.robotik.earthrover.communication.nxt;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 29.09.13
 * Time: 12:58
 * To change this template use File | Settings | File Templates.
 */
public class NXTConnectionCollection {
    private static NXTConnectionCollection instance;

    private NXTConnectionCollection() throws NXTCommException {
        NXTInfo nxt09Info = new NXTInfo(NXTComm.LCP, "NXT09", "0016530BEB04");
        NXTComm nxt09Comm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
        nxt09Comm.open(nxt09Info, NXTComm.LCP);

        NXTInfo slaveDriverInfo = new NXTInfo(NXTComm.LCP, "SlaveDriver", "0016530EBFFD");
        NXTComm slaveDriverComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
        slaveDriverComm.open(slaveDriverInfo, NXTComm.LCP);

    }

    public static NXTConnectionCollection getInstance() throws NXTCommException {
        if (instance == null) {
            instance = new NXTConnectionCollection();
        }
        return instance;
    }
}
