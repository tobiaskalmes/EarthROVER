import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Sound;

/**
 * Created with IntelliJ IDEA.
 * User: golo
 * Date: 05.10.13
 * Time: 17:37
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
            BTSend.getInstance().BTSend();
        }
    });
        while (true){}
}
}
