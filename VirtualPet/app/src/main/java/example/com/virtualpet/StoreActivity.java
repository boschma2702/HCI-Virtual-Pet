package example.com.virtualpet;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class StoreActivity extends Activity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ArrayList<StoreItem> buyed_items = getIntent().getParcelableArrayListExtra("buyed_items");
        ArrayList<StoreItem> all_items = new ArrayList<StoreItem>();

        // add all the items
        //storeitem(name, id, cost, imagepath)
        all_items.add(new StoreItem("Bal", 0, 2, getResources().getDrawable(R.drawable.item_ball)));
        all_items.add(new StoreItem("Voerbak", 1, 6,getResources().getDrawable(R.drawable.item_bowl)));
        all_items.add(new StoreItem("Hondenvoer", 2, 4, getResources().getDrawable(R.drawable.item_food)));
        all_items.add(new StoreItem("Bot", 3, 3, getResources().getDrawable(R.drawable.item_bone)));

        //the layout on which you are working
        TableLayout layout = (TableLayout) findViewById(R.id.storelayout);

        //put the items in the layout!
        for (StoreItem item : all_items) {
            //set the properties for button

            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.VERTICAL);

            ImageButton imgbtn = new ImageButton(this);
            imgbtn.setImageDrawable(item.getDrawable());
            imgbtn.setId(item.getId());
            TableRow.LayoutParams params = new TableRow.LayoutParams(400, 400);
            params.gravity=Gravity.CENTER_HORIZONTAL;
            imgbtn.setLayoutParams(params);
            imgbtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imgbtn.setBackground(null);

            TextView name_tv = new TextView(this);
            name_tv.setText(item.getName());
      //      name_tv.setGravity(Gravity.CENTER);
            name_tv.setTextSize(20);

            TextView cost_tv = new TextView(this);
            cost_tv.setText("€ " + item.getCost());
        //    cost_tv.setGravity(Gravity.CENTER);
            cost_tv.setTextSize(20);


            //add elements to the layout
            tr.addView(imgbtn);
            ll.addView(name_tv);
            ll.addView(cost_tv);
            tr.addView(ll);

            layout.addView(tr);
        }
    }
}
