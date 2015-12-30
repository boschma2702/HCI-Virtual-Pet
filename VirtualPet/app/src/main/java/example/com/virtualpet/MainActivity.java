package example.com.virtualpet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import example.com.virtualpet.Util.ResourceManager;
import example.com.virtualpet.flapdog.FlapDogActivity;
import example.com.virtualpet.maps.MapsActivity;


public class MainActivity extends Activity implements Runnable {

    private boolean running = false;
    public static final int FPS = 30;
    private DogView view;
    private Dog dog;

    private boolean inGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ResourceManager(this);
        Intent intent = new Intent(this, DogService.class);
        startService(intent);
    }

    public void mainPlayClicked(View v){
        inGame = true;
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

    public void playClicked(View v){
        Intent intent = new Intent(this, FlapDogActivity.class);
        startActivity(intent);
    }

    public void FeedClicked(View v){
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(inGame) {
            running = true;
            new Thread(this).start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(inGame) {
            running = false;
        }
    }

    @Override
    public void run() {
        long ticksPS = (long)1000 / FPS;
        Log.e("Main", "ticks ps: " + ticksPS);
        long startTime;
        long sleepTime;
        while (running) {
            startTime = System.nanoTime() / 1000000;
            dog.update();
            if(view !=null) {
                view.onDraw();
            }
            try {
                sleepTime = ticksPS - (System.nanoTime() / 1000000-startTime);
                if(sleepTime>0){
                    Thread.sleep(sleepTime);
                }else{
                    Thread.sleep(10);
                    Log.e("GameLoop", "Couldn't work through loop in less then 1/30 of a second");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
