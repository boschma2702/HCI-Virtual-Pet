package example.com.virtualpet.flapdog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    private FlapDogView view;
    private View gameoverScreen;
    private View mainMenuScreen;
    private View explainScreen;
    private TextView scoreField;
    private TextView bestField;
    private int currentScore = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setFullScreen(this);
        setContentView(R.layout.flapdog);
        view = (FlapDogView) findViewById(R.id.flapdogView);

        scoreField = (TextView) findViewById(R.id.flapdogScoreField);
        bestField = (TextView) findViewById(R.id.flapdogbestscoreField);
        gameoverScreen = findViewById(R.id.flapdogGameoverScreen);
        gameoverScreen.setVisibility(View.INVISIBLE);
        mainMenuScreen = findViewById(R.id.flapdogMenuScreen);
        explainScreen = findViewById(R.id.flapdogExplainScreen);
        explainScreen.setVisibility(View.INVISIBLE);

        view.start(this);
        view.setVisibility(View.INVISIBLE);
//        new Thread(view).start();
    }


    public void gameOver(final int score) {
        currentScore = score;
        final SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        final int prevHighscore = sp.getInt("flapdog_highscore", 0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreField.setText(String.valueOf(score));
                if (prevHighscore < score) {
                    bestField.setText(String.valueOf(score));
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("flapdog_highscore", score);
                    editor.apply();
                } else {
                    bestField.setText(String.valueOf(prevHighscore));
                }
                gameoverScreen.setVisibility(View.VISIBLE);
            }
        });

    }


    public void exitButtonClicked(View v){
        close();
    }

    @Override
    protected void onStop() {
        close();
        super.onStop();
    }

    private void close(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("played",true);
        returnIntent.putExtra("score",currentScore);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void replayButtonClicked(View v){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameoverScreen.setVisibility(View.INVISIBLE);
                view.reset();
                new Thread(view).start();
            }
        });
    }

    public void flapdogStart(View v){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainMenuScreen.setVisibility(View.INVISIBLE);
                explainScreen.setVisibility(View.INVISIBLE);
                view.setVisibility(View.VISIBLE);
                new Thread(view).start();
            }
        });
    }

    public void flapdogUitleg(View v){
        mainMenuScreen.setVisibility(View.INVISIBLE);
        explainScreen.setVisibility(View.VISIBLE);
    }
}
