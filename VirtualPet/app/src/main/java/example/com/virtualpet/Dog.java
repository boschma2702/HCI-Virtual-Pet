package example.com.virtualpet;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import example.com.virtualpet.Util.ResourceManager;


/**
 * Created by reneb_000 on 3-12-2015.
 * Last updated by gijsbeernink on 04-Jan-2015.
 */
public class Dog {

    private DogView view;
    private int x, y, screenwidth, speed;

    private int satisfaction;
    private long lastUpdate;
    public static final long THIRTYMINUTES = 1800000; // thirty minutes in milliseconds


    public Dog(Context c, DogView view) {
        this.view = view;
//        x = (int) ResourceManager.INSTANCE.convertPixelsToDp(500);
//        y = (int) ResourceManager.INSTANCE.convertPixelsToDp(2000);
        speed = 5;
        screenwidth = ResourceManager.INSTANCE.getScreenWidth();
        x = screenwidth/2;
        y = (int) (ResourceManager.INSTANCE.getScreenHeight()-ResourceManager.INSTANCE.getPercentageLength(20, true));
        view.setXY(x, y);
    }

    public void update() {
//        if(x>screenwidth){
//            speed = -speed;
//        }else if(x<0){
//            speed = -speed;
//        }
//        x += speed;
        Log.e("Dog", "Time: " + new Date(DogService.INSTANCE.getTime()).toString());
        view.setXY(x, y);

        checkUpdates();
    }

    public void checkUpdates() {
        if (DogService.INSTANCE.getDirty()) {
//            TODO show DIRTY
            DogService.INSTANCE.updateSatisfaction(-5, 40);
        }
        if (DogService.INSTANCE.getWantsToWalk() || DogService.INSTANCE.getWantsToPlay()) {
//            TODO show PLAYFULL
            DogService.INSTANCE.updateSatisfaction(-10);
        }
        if (DogService.INSTANCE.getHungry()) {
//            TODO show HUNGRY
            DogService.INSTANCE.updateSatisfaction(-20);
        }
        if (Math.random() >= 0.7) {
//            TODO show BARKING
        }
    }


    public void playWithDog(boolean gettingDirty) {
        if((getTime() - getTimeLastEaten()) > 2*THIRTYMINUTES) {
            DogService.INSTANCE.updateSatisfaction(15, 80);
        } else {
            DogService.INSTANCE.updateSatisfaction(-5, 60);
        }
        if(Math.random() < 0.75 && gettingDirty) {
            setDirty(true);
        }
    }

    // Getters & Setters
    public long getTimeLastPlayed() {
        return DogService.INSTANCE.getTimeLastPlayed();
    }

    public long getTimeLastEaten() {
        return DogService.INSTANCE.getTimeLastEaten();
    }

    public void setTimeLastEaten(Date date) {
        DogService.INSTANCE.setTimeLastEaten(date);
    }

    public void setTimeLastPlayed(Date date) {
        DogService.INSTANCE.setTimeLastPlayed(date);
    }

    public boolean getDirty() {
        return DogService.INSTANCE.getDirty();
    }

    public void setDirty(boolean dirty) {
        DogService.INSTANCE.setDirty(dirty);
    }

    public long getTime(){
        return DogService.INSTANCE.getTime();
    }

    public DogView getView() {
        return view;
    }

    public enum dogMood {
        BARKING, HAPPY, PLAYFULL, SAD, DEAD
    }

    public void setView(dogMood mood) {

    }

}
