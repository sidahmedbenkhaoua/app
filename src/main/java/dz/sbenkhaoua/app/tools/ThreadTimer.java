package dz.sbenkhaoua.app.tools;

/**
 * Created by sbenkhaoua on 30/04/15.
 */
import java.util.Timer;
import java.util.TimerTask;

public class ThreadTimer extends TimerTask{
    static int counter = 0;

    public static void main(String [] args) {
        Timer timer = new Timer("MyTimer");
        timer.scheduleAtFixedRate(new ThreadTimer(), 30, 3000);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("TimerTask executing counter is: " + counter);
        counter++;
    }

}