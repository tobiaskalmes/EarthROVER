import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Sound;

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
        LCD.drawString("Initialising...", 0, 0);
        sensorCollection = SensorCollection.getInstance();
        LCD.drawString("Done!", 0, 1);
        doSleep(1500);
        LCD.clear();
        LCD.drawString("Driving...", 0, 0);
        LCD.drawString("Forward 2s, 50", 0, 1);
        Driver.getInstance().forward(50);
        doSleep(2000);
        LCD.drawString("Right 2s, 100", 0, 2);
        Driver.getInstance().turnRight(100);
        doSleep(2000);
        LCD.drawString("Backward 3s, 30", 0, 3);
        Driver.getInstance().backward(30);
        doSleep(3000);
        LCD.drawString("Left 4s, 100", 0, 4);
        Driver.getInstance().turnLeft(100);
        doSleep(4000);
        LCD.drawString("Forward 4s, 20", 0, 5);
        Driver.getInstance().forward(20);
        doSleep(4000);
        Driver.getInstance().stop();
        LCD.drawString("Arrived!", 0, 6);
        doSleep(5000);
        try {
            Driver.getInstance().driveHomeSimple();
        } catch (InterruptedException e) {
            LCD.clear();
            LCD.drawString("ThreadException", 0, 1);
        }
        LCD.clear();
        LCD.drawString("Back Home!", 0, 0);
        while (true) {
            /*outputSensorValues();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LCD.clear();
                LCD.drawString("ThreadException", 0, 1);
            }*/
        }
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
