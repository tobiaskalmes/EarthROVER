import lejos.nxt.comm.RConsole;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 01.10.13
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public class Discovery {
    private static Discovery instance;
    private Driver driver;
    private SensorCollection sensorCollection;

    private Discovery() {
        driver = Driver.getInstance();
        sensorCollection = SensorCollection.getInstance();
    }

    public static Discovery getInstance() {
        return instance == null ? instance = new Discovery() : instance;
    }

    public void searchBall() throws InterruptedException {
        for (int i = 0; i < 3; ++i) {
            searchBallForward();
            searchBallTurnRight();
        }
        driver.stop();
    }

    private void searchBallForward() throws InterruptedException {
        driver.forward(75);
        while (sensorCollection.getDistance() > 35) {
            Thread.sleep(200);
        }
    }

    private void searchBallTurnRight() throws InterruptedException {
        driver.turnRight(50);
        while (sensorCollection.getDistance() < 75) {
            Thread.sleep(200);
        }
        driver.turnRight(50, 40, true);
    }

    public void discoverRoomBorders() throws InterruptedException {
        RoomBorderData roomBorderData = new RoomBorderData();
        //drive along the positive y-axis
        RConsole.println("Heading: " + sensorCollection.getHeading());
        driver.forward(50);
        while (sensorCollection.getDistance() > 30) {
            Thread.sleep(100);
            RConsole.println("Heading: " + sensorCollection.getHeading());
        }
        driver.stop();
        //Sensor distance + Driven distance (360° = 11cm) + distance of sensor to center of robot
        roomBorderData.setyPositiveDistance(sensorCollection.getDistance() + (driver.getDrivenDistance() / 360 * 11) + 20);
        //turn 90° right
        RConsole.println("Turn right");
        RConsole.println("Heading: " + sensorCollection.getHeading());
        driver.turnRight(50, 90);
        Thread.sleep(1000);
        RConsole.println("Heading: " + sensorCollection.getHeading());
        driver.forward(50);
        while (sensorCollection.getDistance() > 30) {
            Thread.sleep(200);
            RConsole.println("Heading: " + sensorCollection.getHeading());
        }
        driver.stop();
        roomBorderData.setyPositiveRightDistance(sensorCollection.getDistance() + (driver.getDrivenDistance() / 360 * 11) + 20);
        //turn 180° left
        RConsole.println("Turn left");
        RConsole.println("Heading: " + sensorCollection.getHeading());
        driver.turnLeft(50, 180);
        Thread.sleep(1000);
        RConsole.println("Heading: " + sensorCollection.getHeading());
        driver.forward(50);
        while (sensorCollection.getDistance() > 30) {
            Thread.sleep(200);
        }
        driver.stop();
        roomBorderData.setyPositiveLeftDistance(sensorCollection.getDistance() + (driver.getDrivenDistance() / 360 * 11) + 20);
    }
}
