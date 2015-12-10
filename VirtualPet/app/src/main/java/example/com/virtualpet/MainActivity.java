package example.com.virtualpet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainActivity extends Activity implements Runnable {

    private boolean running = false;
    private static final int FPS = 30;
    private DogView view;
    private Dog dog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ResourceManager(this);
    }

    public void mainPlayClicked(View v){
        setContentView(R.layout.game_layout);
        view = (DogView) findViewById(R.id.surfaceView);
        dog = new Dog(this, view);
        view = dog.getView();
        running = true;
        new Thread(this).start();
    }

    public void mapsClicked(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        //TODO fix infinite framerate to capped at 30
        while (running) {
            dog.update();
            if(view !=null) {
                view.onDraw();
            }
            /*
            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.onDraw(c);
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    Thread.sleep(sleepTime);
                else
                    Thread.sleep(10);
            } catch (Exception e) {}*/
        }
    }
}
