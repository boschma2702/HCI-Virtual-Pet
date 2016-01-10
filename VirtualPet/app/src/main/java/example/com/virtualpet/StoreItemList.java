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
        all_items.add(new StoreItem(c.getString(R.string.ball), 0, 2, c.getResources().getDrawable(R.drawable.item_ball)));
        all_items.add(new StoreItem(c.getString(R.string.bowl), 1, 6, c.getResources().getDrawable(R.drawable.item_bowl)));
        all_items.add(new StoreItem(c.getString(R.string.food), 2, 4, c.getResources().getDrawable(R.drawable.item_food)));
        all_items.add(new StoreItem(c.getString(R.string.bone), 3, 3, c.getResources().getDrawable(R.drawable.item_bone)));
        all_items.add(new StoreItem(c.getString(R.string.sponge), 4, 3, c.getResources().getDrawable(R.drawable.cleaning_sponge)));

    }

    ArrayList<StoreItem> getAllItems() {
        return all_items;
    }

    StoreItem getItemByName(String name) {
        for (StoreItem item : all_items) {
            if (name.equals(item.getName())) {
                return item;
            }
        }
        //return null if we did not found anything
        return null;
    }

    StoreItem getItemById(int id) {
        for (StoreItem item : all_items) {
            if (id == item.getId()) {
                return item;
            }
        }
        //return null if we did not found anything
        return null;
    }
}
