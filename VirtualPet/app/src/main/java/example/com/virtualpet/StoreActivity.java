package example.com.virtualpet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import example.com.virtualpet.Util.ResourceManager;

public class StoreActivity extends Activity  {

    ListView listView ;
    int money;
    ArrayList<StoreItem> all_items = new ArrayList<StoreItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        money = getIntent().getIntExtra("money", 0);

        StoreItemList storeitemlist = new StoreItemList(this);

        all_items = storeitemlist.getAllItems();
        listView = (ListView) findViewById(R.id.listview);

        CustomList adapter = new CustomList(this, 1, all_items, true);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        Bitmap background = ResourceManager.INSTANCE.storeBackground;
        LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayout_id);
        ll.setBackground(new BitmapDrawable(getResources(),background));

    }

    public void buy_selected_items(View v) {
        SparseBooleanArray positions = listView.getCheckedItemPositions();
        int size = positions.size();
        int total_cost = 0;

        ArrayList<StoreItem> try_buy_items = new ArrayList<StoreItem>();
        for(int index = 0; index < size; index++) {
            if (positions.valueAt(index)) {
                StoreItem item = all_items.get(positions.keyAt(index));
                try_buy_items.add(item);
                total_cost += item.getCost();
            }
        }

        //check if enough money is available.
        if (money >= total_cost) {
            Intent returnIntent = new Intent();
            returnIntent.putParcelableArrayListExtra("items", try_buy_items);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Je hebt niet genoeg geld. Wacht tot je zakgeld krijgt.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }



    public void onCancelClicked(View v) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
