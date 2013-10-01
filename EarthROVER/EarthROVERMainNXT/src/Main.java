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
        LCD.drawString("Sensors initialised...", 0, 1);

        LCD.drawString("Forward...", 0, 2);
        Driver.getInstance().forward(50);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LCD.clear();
            LCD.drawString("ThreadException", 0, 1);
        }
        Driver.getInstance().stop();
        LCD.drawString("Done", 0, 3);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LCD.clear();
            LCD.drawString("ThreadException", 0, 1);
        }
        Driver.getInstance().displayPathData();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LCD.clear();
            LCD.drawString("ThreadException", 0, 1);
        }
        /*LCD.clear();
        LCD.drawString("Driving Home", 0, 0);
        try {
            Driver.getInstance().driveHomeSimple();
        } catch (InterruptedException e) {
            LCD.clear();
            LCD.drawString("ThreadException", 0, 1);
        }
        LCD.drawString("arrived", 0, 1);*/
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

    private static void outputSensorValues() {
        LCD.clear();
        LCD.drawString("Sensor values:", 0, 0);
        LCD.drawString("Heading: " + sensorCollection.getHeading(), 0, 1);
        LCD.drawString("Distance: " + sensorCollection.getDistance(), 0, 2);
        LCD.drawString("Direction: " + sensorCollection.getIrSeeker().getDirection(), 0, 3);
    }
}
