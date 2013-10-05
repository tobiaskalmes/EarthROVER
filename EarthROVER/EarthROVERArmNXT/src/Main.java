import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Sound;

/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 04.10.13
 * Time: 13:16
 * To change this template use File | Settings | File Templates.
 */
public class Main {

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

       Button.LEFT.addButtonListener(new ButtonListener() {
           @Override
           public void buttonPressed(Button b) {
               //To change body of implemented methods use File | Settings | File Templates.
                          }

           @Override
           public void buttonReleased(Button b) {
               //To change body of implemented methods use File | Settings | File Templates.
               Arm.getInstance().ArmDownAndBack();
           }
       });

       Button.RIGHT.addButtonListener(new ButtonListener() {
           int ButtonCount;
           @Override
           public void buttonPressed(Button b) {
               //To change body of implemented methods use File | Settings | File Templates.
           }

           @Override
           public void buttonReleased(Button b) {

               //To change body of implemented methods use File | Settings | File Templates.
               if(ButtonCount == 0)
               {
               Arm.getInstance().ArmDownWithBall();
               ButtonCount++;
               }
               else
               {
               Arm.getInstance().StartPosition();
               ButtonCount = 0;
               }
           }
       });


       while (true){}
    }
}
