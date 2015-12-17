package example.com.virtualpet.flapdog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import example.com.virtualpet.MainActivity;
import example.com.virtualpet.R;

/**
 * Created by reneb_000 on 16-12-2015.
 */
public class FlapDogActivity extends Activity implements Runnable {

    private boolean running;
    private FlapDogView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flapdog);
        view = (FlapDogView) findViewById(R.id.flapdogView);
        view.start();
        running = true;
        new Thread(this).start();
    }




    @Override
    public void run() {
        long ticksPS = (long)1000 / MainActivity.FPS;
        Log.e("Main", "ticks ps: " + ticksPS);
        long startTime;
        long sleepTime;
        while (running) {
            startTime = System.nanoTime() / 1000000;
            //update
            view.update();

            //draw
            view.onDraw();
            try {
                sleepTime = ticksPS - (System.nanoTime() / 1000000-startTime);
                if(sleepTime>0){
                    Thread.sleep(sleepTime);
                }else{
                    Thread.sleep(10);
//                    Log.e("GameLoop", "Couldn't work through loop in less then 1/30 of a second");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
