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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import example.com.virtualpet.Util.ResourceManager;
import example.com.virtualpet.flapdog.FlapDogActivity;
import example.com.virtualpet.maps.MapsActivity;


public class MainActivity extends Activity {

    private DogView view;
    private boolean inGame = false;

    StoreItemList storeitemlist;
    ArrayList<StoreItem> all_items;

    //init a list of items we already have
    private ArrayList<StoreItem> buyed_items = new ArrayList<StoreItem>();
    private int money = 20;
    TextView moneyTV;

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

        moneyTV = (TextView) findViewById(R.id.moneyTV);
        updateMoneyTextView();

        storeitemlist = new StoreItemList(this);
        all_items = storeitemlist.getAllItems();

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

    public void shopClicked(View v){
        //view.setSprite(Dog.DogMood.SAD);
        //now lets do something useful with this button!

        Intent intent = new Intent(this, StoreActivity.class);
        intent.putExtra("money", money);

        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                StoreItem item =data.getParcelableExtra("item");
                buyed_items.add(item);
                money = money - item.getCost();
                updateMoneyTextView();
                updateItemsShowing(item);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                // we are very sad to hear you did not buy anything :(
            }
        }
    }//onActivityResult

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

    private void updateMoneyTextView() {
        moneyTV.setText("€ " + money);
    }

    private void updateItemsShowing(StoreItem item) {

        LinearLayout ll = (LinearLayout) findViewById(R.id.money_items_layout);

        //the problem is that we can't parse a drawable, so i check for the object that has it with comparing id's. a bit dirty actually.
            for(StoreItem full_item : all_items) {
                if (item.getId() == full_item.getId()) {
                    ImageView iv = new ImageView(this);
                    iv.setImageDrawable(full_item.getDrawable());
                    iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    iv.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
                    ll.addView(iv);
                }
            }

    }

}

