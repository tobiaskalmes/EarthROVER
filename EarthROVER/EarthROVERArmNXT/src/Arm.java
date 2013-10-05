import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;

/**
 * Created with IntelliJ IDEA.
 * User: golo
 * Date: 04.10.13
 * Time: 13:29
 * To change this template use File | Settings | File Templates.
 */
public class Arm {

    private static Arm instance;
    private NXTRegulatedMotor motorA;
    private NXTRegulatedMotor motorB;

   private Arm(){
       //Init Motors
       motorA = new NXTRegulatedMotor(MotorPort.A);
       motorB = new NXTRegulatedMotor(MotorPort.B);
   }

    public static Arm getInstance() {
        return instance == null ? (instance = new Arm()) : instance;
    }

    public void  OpenArm()
    {
        motorA.setSpeed(100);
        motorA.rotate(120);
    }
    public void  CloseArm()
    {
        motorA.setSpeed(100);
        motorA.rotate(-120);
    }

    public void ArmDownAndBack()
    {
       motorB.setSpeed(80);
       motorB.rotate(200);

      CloseArm();

        motorB.setSpeed(150);
        motorB.rotate(-200);

        OpenArm();
    }

    public void ArmDownWithBall()
    {
        motorB.setSpeed(80);
        motorB.rotate(200);

        CloseArm();
    }

    public void StartPosition()
    {
        OpenArm();

        motorB.setSpeed(150);
        motorB.rotate(-200);
    }
}
