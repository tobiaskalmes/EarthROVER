package de.htw.tojato.robotik.earthrover.driver;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 30.09.13
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public class Driver {
    private static Driver instance;
    private List<IDriverListener> listeners;
    private int runningRequestID;
    private NXTRegulatedMotor motorA;
    private NXTRegulatedMotor motorB;
    private NXTRegulatedMotor motorC;

    private Driver() {
        listeners = new ArrayList<IDriverListener>();
        runningRequestID = 0;
        motorA = new NXTRegulatedMotor(MotorPort.A);
        motorB = new NXTRegulatedMotor(MotorPort.B);
        motorC = new NXTRegulatedMotor(MotorPort.C);
    }

    public static Driver getInstance() {
        if (instance == null) {
            instance = new Driver();
        }
        return instance;
    }

    //AB, -C
    public void drive(int power, int degrees) {
        motorA.setSpeed(power);
        motorB.setSpeed(power);
        motorC.setSpeed(power);
        motorB.resetTachoCount();
        int tachoCount = motorB.getTachoCount();
        System.out.println("Starting motorA...");
        if (degrees >= 0) {
            motorA.forward();
        } else {
            motorA.backward();
        }
        System.out.println("Starting motorB...");
        motorB.forward();
        System.out.println("Starting motorC...");
        motorC.backward();
        while (motorB.getTachoCount() < degrees) {

        }
        motorA.stop();
        System.out.println("done");
    }
}
