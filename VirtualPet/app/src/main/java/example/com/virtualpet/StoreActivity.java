package example.com.virtualpet;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

public class StoreActivity extends Activity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ArrayList<StoreItem> buyed_items = getIntent().getParcelableArrayListExtra("buyed_items");
        ArrayList<StoreItem> all_items = new ArrayList<StoreItem>();


        all_items.add(new StoreItem("Bal", 3, 0, ""));
        all_items.add(new StoreItem("Voerbak", 6, 0, ""));
        all_items.add(new StoreItem("Hondenvoer", 2, 0, ""));
        all_items.add(new StoreItem("Bot", 3, 0, ""));



        for (StoreItem item : all_items) {
            // if we already bought the item it won't be available in the store
            // else it will be.

            for (StoreItem buyed_item) {
                if(item.getId() != buyed_item.getId()) {
                    // TODO put it in the store
                }
            }
        }
    }
}
