package example.com.virtualpet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class StoreActivity extends Activity  {

    int money;
    ArrayList<StoreItem> all_items = new ArrayList<StoreItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        money = getIntent().getIntExtra("money", 0);

        StoreItemList storeitemlist = new StoreItemList(this);

        all_items = storeitemlist.getAllItems();

        //the layout on which you are working
        TableLayout layout = (TableLayout) findViewById(R.id.storelayout);
        layout.setBackground(getResources().getDrawable(R.drawable.store_background));

        //put the items in the layout!
        for (StoreItem item : all_items) {
            //set the properties for button

            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setGravity(Gravity.CENTER_VERTICAL);

            ImageButton imgbtn = new ImageButton(this);
            imgbtn.setImageDrawable(item.getDrawable());
            imgbtn.setId(item.getId());
            TableRow.LayoutParams params = new TableRow.LayoutParams(400, 400);
            params.gravity=Gravity.CENTER_HORIZONTAL;
            imgbtn.setLayoutParams(params);
            imgbtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imgbtn.setBackground(null);
            imgbtn.setOnClickListener(itemClicked);

            TextView name_tv = new TextView(this);
            name_tv.setText(item.getName());
      //      name_tv.setGravity(Gravity.CENTER);
            name_tv.setTextColor(Color.BLACK);
            name_tv.setTextSize(20);

            TextView cost_tv = new TextView(this);
            cost_tv.setText("€ " + item.getCost());
            cost_tv.setTextColor(Color.BLACK);
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

    View.OnClickListener itemClicked = new View.OnClickListener() {
        public void onClick(View v) {
            for (StoreItem item : all_items) {
                //get the item which was clicked
                if (item.getId() == v.getId()) {

                    //check if enough money is available.
                    if (money > item.getCost()) {

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("item", item);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    } else {
                        //TODO show error that you don't have enough money
                    }
                }
            }

        }
    };

    void onCancelClicked(View v) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }


}
