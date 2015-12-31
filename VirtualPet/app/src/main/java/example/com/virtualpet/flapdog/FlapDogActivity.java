package example.com.virtualpet.flapdog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
public class FlapDogActivity extends Activity {

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
        new Thread(view).start();
    }


    public void gameOver(final int score) {
        running = false;
        final SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        final int prevHighscore = sp.getInt("flapdog_highscore", 0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreField.setText(String.valueOf(score));
                if(prevHighscore<score){
                    bestField.setText(String.valueOf(score));
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("flapdog_highscore", score);
                    editor.apply();
                }else{
                    bestField.setText(String.valueOf(prevHighscore));
                }
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
                new Thread(view).start();
            }
        });
    }
}
