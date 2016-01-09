package example.com.virtualpet;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mrlukasbos on 09/01/16.
 */
public class StoreItem implements Parcelable {

    private int cost = 0;
    private String name, drawablePath;
    private int id;

    // some strange functions needed for the parcelable. Parcelable is needed to pass an arraylist to another activity.
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(cost);
    }

    public static final Parcelable.Creator<StoreItem> CREATOR
            = new Parcelable.Creator<StoreItem>() {
        public StoreItem createFromParcel(Parcel in) {
            return new StoreItem(in);
        }

        public StoreItem[] newArray(int size) {
            return new StoreItem[size];
        }
    };

    private StoreItem(Parcel in) {
        cost = in.readInt();
    }
    //end of strange functions


    public StoreItem(String name, int id, int cost, String drawable_path) {
        this.name = name;
        this.id = id;
        this.cost = cost;
        this.drawablePath = drawable_path;
    }

    //standard getters and setters
    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDrawablePath(String path) {
        this.drawablePath = path;
    }

    public String getDrawablePath() {
        return drawablePath;
    }

}
