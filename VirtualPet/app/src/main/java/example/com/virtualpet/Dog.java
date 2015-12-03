package example.com.virtualpet;

import android.content.Context;

/**
 * Created by reneb_000 on 3-12-2015.
 */
public class Dog {

    private DogView view;

    public Dog(Context c){
        view = new DogView(c, null);
    }

    public DogView getView() {
        return view;
    }
}
