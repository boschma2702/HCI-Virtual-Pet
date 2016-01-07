package example.com.virtualpet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import example.com.virtualpet.Util.ResourceManager;
import example.com.virtualpet.flapdog.FlapDogActivity;
import example.com.virtualpet.maps.MapsActivity;


public class MainActivity extends Activity {

    private DogView view;

    private boolean inGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, DogService.class));
        setContentView(R.layout.activity_main);
        new ResourceManager(this);
    }



    public void mainPlayClicked(View v){
        inGame = true;
        setContentView(R.layout.game_layout);

        // Get the Drawable custom_progressbar
        ProgressBar progressBar= (ProgressBar) findViewById(R.id.progressBar);

        // set the drawable as progress drawable
        progressBar.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.custom_progressbar));

        view = (DogView) findViewById(R.id.surfaceView);
        new Thread(view).start();
    }

    public void mapsClicked(View v){

        // if gps is disabled
        if (!((LocationManager) this.getSystemService(Context.LOCATION_SERVICE))
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Context context = getApplicationContext();
            CharSequence text = "Je moet locatie op 'zeer nauwkeurig' instellen.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        } else {
            Intent maps = new Intent(this, MapsActivity.class);
            startActivity(maps);
        }

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
            view.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(inGame) {
            view.pause();
        }
    }

    public static void setFullScreen(Activity a){
        a.requestWindowFeature(Window.FEATURE_NO_TITLE);
        a.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        a.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        a.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}
