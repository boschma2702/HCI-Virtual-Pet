package example.com.virtualpet;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by mrlukasbos on 09/01/16.
 */
public class StoreItemList {

    ArrayList<StoreItem> all_items = new ArrayList<StoreItem>();

    Context c;

    StoreItemList(Context c) {
        // add all the items
        //storeitem(name, id, cost, imagepath)
        all_items.add(new StoreItem("Bal", 0, 2, c.getResources().getDrawable(R.drawable.item_ball)));
        all_items.add(new StoreItem("Voerbak", 1, 6, c.getResources().getDrawable(R.drawable.item_bowl)));
        all_items.add(new StoreItem("Hondenvoer", 2, 4, c.getResources().getDrawable(R.drawable.item_food)));
        all_items.add(new StoreItem("Bot", 3, 3, c.getResources().getDrawable(R.drawable.item_bone)));
    }

    ArrayList<StoreItem> getAllItems() {
        return all_items;
    }
}
