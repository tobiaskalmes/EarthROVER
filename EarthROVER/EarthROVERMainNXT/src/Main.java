import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 30.09.13
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private static SensorCollection sensorCollection;

    public static void main(String[] args) {
        Button.ESCAPE.addButtonListener(new ButtonListener() {
            @Override
            public void buttonPressed(Button b) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void buttonReleased(Button b) {
                Sound.beep();
                System.exit(0);
            }
        });
        sensorCollection = SensorCollection.getInstance();
        RConsole.open();
        while (true) {
            RConsole.println("Distance: " + Integer.toString(sensorCollection.getDistance()));
            doSleep(250);
        }
       /* try {
            Discovery.getInstance().discoverRoomBorders();
        } catch (InterruptedException e) {
            LCD.clear();
            LCD.drawString("ThreadException", 0, 1);
        }*/

        /*RConsole.println("Test");
        RConsole.println("Initialising...");
        RConsole.println("Done!");
        doSleep(1500);
        RConsole.println("Driving...");
        RConsole.println("Forward 2s, 50");
        Driver.getInstance().forward(50);
        doSleep(2000);
        RConsole.println("Right 2s, 100");
        Driver.getInstance().turnRight(100);
        doSleep(2000);
        RConsole.println("Backward 3s, 30");
        Driver.getInstance().backward(30);
        doSleep(3000);
        RConsole.println("Left 4s, 100");
        Driver.getInstance().turnLeft(100);
        doSleep(4000);
        RConsole.println("Forward 4s, 20");
        Driver.getInstance().forward(20);
        doSleep(4000);
        Driver.getInstance().stop();
        RConsole.println("Arrived!");
        Driver.getInstance().displayPathData();
        doSleep(1000);
        try {
            Driver.getInstance().driveHomeSimple();
        } catch (InterruptedException e) {
            LCD.clear();
            LCD.drawString("ThreadException", 0, 1);
        }
        RConsole.println("Back Home!");*/
        //RConsole.close();
        //System.exit(0);
    }

    private static void doSleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            LCD.clear();
            LCD.drawString("ThreadException", 0, 1);
        }
    }

    private static void outputSensorValues() {
        LCD.clear();
        LCD.drawString("Sensor values:", 0, 0);
        LCD.drawString("Heading: " + sensorCollection.getHeading(), 0, 1);
        LCD.drawString("Distance: " + sensorCollection.getDistance(), 0, 2);
        LCD.drawString("Direction: " + sensorCollection.getIrSeeker().getDirection(), 0, 3);
    }
}
