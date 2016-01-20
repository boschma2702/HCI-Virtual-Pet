package example.com.virtualpet;

//source: http://www.learn2crack.com/2013/10/android-custom-listview-images-text-example.html

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<StoreItem>{

    private final Activity context;
    private final ArrayList<StoreItem> items;
    private boolean showCheckBox;

    public CustomList(Activity context,  int id, ArrayList<StoreItem> items, boolean showCheckBox) {
        super(context, id, items);
        this.context = context;
        this.items = items;
        this.showCheckBox = showCheckBox;
        Log.e("lukas", "init customlist");

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Log.e("lukas", "getview");



        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.storeitemlistitem, null, true);

        ImageView checkbox = (ImageView) rowView.findViewById(R.id.item_select_checkbox);

        if (showCheckBox) {
            checkbox.setVisibility(View.VISIBLE);
        } else {
            checkbox.setVisibility(View.GONE);
        }

        TextView txtTitle = (TextView) rowView.findViewById(R.id.storeitemli_title);
        txtTitle.setText(items.get(position).getName());

        ImageView imageView = (ImageView) rowView.findViewById(R.id.storeitemli_img);
        imageView.setImageDrawable(items.get(position).getDrawable());

        TextView txtCost = (TextView) rowView.findViewById(R.id.storeitemli_cost);
        txtCost.setText("€ " + Integer.toString(items.get(position).getCost()));

        return rowView;
    }

    public StoreItem getItem(int position) {
        return items.get(position);
    }
}