package example.com.virtualpet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

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

    SharedPreferences sharedPref;
    private ProgressBar progressBar;


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

        storeitemlist = new StoreItemList(this);
        all_items = storeitemlist.getAllItems();

        sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        money = sharedPref.getInt(getString(R.string.moneyString), 20);

        //if we lost the contents of buyed_items then refill it using shared preferences.
        if (buyed_items.isEmpty()) {
            int buyed_items_size = sharedPref.getInt("buyed_items_size", 0);
            for (int i = 0; i < buyed_items_size; i++) {
                int id = sharedPref.getInt("buyed_items_" + i, 0);

                StoreItem item = storeitemlist.getItemById(id);
                buyed_items.add(item);
                updateItemsShowing(item);
            }
        }


        moneyTV = (TextView) findViewById(R.id.moneyTV);
        updateMoneyTextView();




        // set the drawable as progress drawable


        view = (DogView) findViewById(R.id.surfaceView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.custom_progressbar));
        progressBar.setMax(DogService.MAXSATISFACTION);
        progressBar.setProgress(DogService.INSTANCE.getSatisfaction());
        Log.e("test", String.valueOf(progressBar));
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

        StoreItem bal = storeitemlist.getItemByName(getString(R.string.ball));

        if (ItemisBought(new ArrayList<>(Arrays.asList(bal)))) {
            Intent intent = new Intent(this, FlapDogActivity.class);
            startActivity(intent);
        }
    }

    public void FeedClicked(View v){
        //check for 'hondenvoer'.
        StoreItem hondenvoer = storeitemlist.getItemByName(getString(R.string.food));
        StoreItem voerbak = storeitemlist.getItemByName(getString(R.string.bowl));

        if (ItemisBought(new ArrayList<>(Arrays.asList(hondenvoer, voerbak)))) {
            Intent intent = new Intent(this, FeedActivity.class);
            startActivity(intent);
        }
    }

    public void shopClicked(View v){
        Intent intent = new Intent(this, StoreActivity.class);
        intent.putExtra("money", money);

        startActivityForResult(intent, 1);
    }

    public void showerClicked(View v) {

        StoreItem spons = storeitemlist.getItemByName(getString(R.string.sponge));

        if (ItemisBought(new ArrayList<>(Arrays.asList(spons)))) {
            if(view.isDogDirty()) {
                view.getCleaningManger().activate();
            }else{
                //TODO show notification that dog is not dirty.
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                StoreItem item =data.getParcelableExtra("item");
                buyed_items.add(item);
                money = money - item.getCost();

                //sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.moneyString), money);


                editor.putInt("buyed_items_size", buyed_items.size());

                //store the items in shared preferences
                for(int i = 0; i<buyed_items.size(); i++) {
                    editor.putInt("buyed_items_" + i, buyed_items.get(i).getId());
                }

                editor.commit();

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

    public void statisfactionChanged(){
        if(inGame){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.e("Statisfactionn", "statisfaction is: "+DogService.INSTANCE.getSatisfaction());
                    progressBar.setProgress(DogService.INSTANCE.getSatisfaction()+100);
                }
            });
        }
    }

    public void resetGame(View view) {
        sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPref.edit().clear().commit();
    }

    public void showGoToShopDialog(String message_text, String positive_text, String negative_text, ArrayList<StoreItem> items) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

        builder1.setMessage(message_text);
        builder1.setCancelable(true);
        builder1.setView(R.layout.custom_dialog);

        builder1.setPositiveButton(
                positive_text,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        shopClicked(null);
                    }
                });

        builder1.setNegativeButton(
                negative_text,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        TableLayout table = new TableLayout(this);
        table.setBackgroundColor(Color.WHITE);

        for (StoreItem item : items) {

            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TableRow row = (TableRow) vi.inflate(R.layout.storeitemlistitem, null);

            TextView title_tv = (TextView) row.findViewById(R.id.storeitemli_title);
            title_tv.setText(item.getName());

            TextView cost_tv = (TextView) row.findViewById(R.id.storeitemli_cost);
            cost_tv.setText(Integer.toString(item.getCost()));

            ImageButton imgbtn = (ImageButton) row.findViewById(R.id.storeitemli_img);
            imgbtn.setImageDrawable(item.getDrawable());

            table.addView(row);
        }

        builder1.setView(table);



        AlertDialog alert11 = builder1.create();
        alert11.show();


    }


// check if one or more items are bought.
    // if false, return
    public boolean ItemisBought(ArrayList<StoreItem> items) {
        //check for all passed items if there is a buyed item which equals it
        for(StoreItem item : items) {
            for (StoreItem buyed_item : buyed_items) {
                 if (buyed_item.getId() == item.getId()) {
                     //if we have a item already bought remove it from the arraylist
                     items.remove(item);
                     if (items.isEmpty()) {
                         return true;
                     }
               }
           }
        }
        showGoToShopDialog("Je moet nog het volgende kopen in de winkel:", "nu kopen", "terug", items);
        return false;
    }
}

