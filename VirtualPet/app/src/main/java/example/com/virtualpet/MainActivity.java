package example.com.virtualpet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ResourceManager(this);
    }

    public void mainPlayClicked(View v){
        setContentView(R.layout.game_layout);
    }

    public void mapsClicked(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

}
