package example.com.virtualpet;

import android.content.Context;

/**
 * Created by reneb_000 on 3-12-2015.
 */
public class Dog {

    private DogView view;
    private int x, y, screenwidth, speed;

    public Dog(Context c, DogView view){
        this.view = view;
        x = (int) ResourceManager.INSTANCE.convertPixelsToDp(1);
        y = (int) ResourceManager.INSTANCE.convertPixelsToDp(2000);
        speed = 5;
        screenwidth = ResourceManager.INSTANCE.getScreenwidht();
    }

    public void update(){
        if(x>screenwidth){
            speed = -speed;
        }else if(x<0){
            speed = -speed;
        }
        x += speed;
        view.setXY(x, y);
    }


    public DogView getView() {
        return view;
    }
}
