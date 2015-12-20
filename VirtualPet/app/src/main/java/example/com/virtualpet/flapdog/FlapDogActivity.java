package example.com.virtualpet.flapdog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import example.com.virtualpet.MainActivity;
import example.com.virtualpet.R;

/**
 * Created by reneb_000 on 16-12-2015.
 */
public class FlapDogActivity extends Activity implements Runnable {

    private boolean running;
    private FlapDogView view;
    private View gameoverScreen;
    private TextView scoreField;
    private TextView bestField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flapdog);
        view = (FlapDogView) findViewById(R.id.flapdogView);

        scoreField = (TextView) findViewById(R.id.flapdogScoreField);
        bestField = (TextView) findViewById(R.id.flapdogbestscoreField);
        gameoverScreen = findViewById(R.id.flapdogGameoverScreen);
        gameoverScreen.setVisibility(View.INVISIBLE);
        view.start(this);
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


    public void gameOver(final int score) {
        running = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreField.setText(String.valueOf(score));
                bestField.setText("TODO");
                gameoverScreen.setVisibility(View.VISIBLE);
            }
        });

    }

    public void exitButtonClicked(View v){
        finish();
    }

    public void replayButtonClicked(View v){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameoverScreen.setVisibility(View.INVISIBLE);
                view.reset();
                running = true;
                new Thread(FlapDogActivity.this).start();
            }
        });
    }
}
