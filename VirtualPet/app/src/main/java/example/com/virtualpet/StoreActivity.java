package example.com.virtualpet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
        TableLayout table = (TableLayout) findViewById(R.id.storelayout);

        //put the items in the layout!
        for (StoreItem item : all_items) {

            //get the custom layout from xml
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TableRow row = (TableRow) vi.inflate(R.layout.storeitemlistitem, null);

            TextView title_tv = (TextView) row.findViewById(R.id.storeitemli_title);
            title_tv.setText(item.getName());

            TextView cost_tv = (TextView) row.findViewById(R.id.storeitemli_cost);
            cost_tv.setText(Integer.toString(item.getCost()));

            ImageButton imgbtn = (ImageButton) row.findViewById(R.id.storeitemli_img);
            imgbtn.setImageDrawable(item.getDrawable());
            imgbtn.setId(item.getId());
            imgbtn.setOnClickListener(itemClicked);

            table.addView(row);

        }
        table.requestLayout();
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

                        Context context = getApplicationContext();
                        CharSequence text = "Je hebt niet genoeg geld. Wacht tot je zakgeld krijgt, of verdien geld door met de hond te spelen.";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            }

        }
    };

    public void onCancelClicked(View v) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
