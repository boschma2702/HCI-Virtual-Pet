package example.com.virtualpet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
    private long lastRefreshed;


    public Dog(Context c, DogView view) {
        this.view = view;
//        x = (int) ResourceManager.INSTANCE.convertPixelsToDp(500);
//        y = (int) ResourceManager.INSTANCE.convertPixelsToDp(2000);
        speed = 5;
        screenwidth = ResourceManager.INSTANCE.getScreenWidth();
        x = screenwidth/2;
        y = (int) (ResourceManager.INSTANCE.getScreenHeight()-ResourceManager.INSTANCE.getPercentageLength(20, true));
        view.setXY(x, y);
        setLastRefreshed(getTime());
    }

    public void update() {
//        if(x>screenwidth){
//            speed = -speed;
//        }else if(x<0){
//            speed = -speed;
//        }
//        x += speed;
        view.setXY(x, y);
        view.setBackgroundColor();

        checkUpdates();
        randomBark();


    }



    public void checkUpdates() {
        if (DogService.INSTANCE.getDirty()) {
            setView(DogMood.DIRTY);
            DogService.INSTANCE.updateSatisfaction(-5, 40);
        }
        if (DogService.INSTANCE.getWantsToWalk() || DogService.INSTANCE.getWantsToPlay()) {
            setView(DogMood.PLAYFULL);
            DogService.INSTANCE.updateSatisfaction(-10);
        }
        if (DogService.INSTANCE.getHungry()) {
            setView(DogMood.HUNGRY);
            DogService.INSTANCE.updateSatisfaction(-20);
        }
    }

    public void randomBark() {
        if (Math.random() >= 0.7) {
            setView(DogMood.BARKING);
        }
    }

//    Start activity:

    




//      Actions from other activities:

    public void playedWithDog(boolean gettingDirty) {
        if((getTime() - getTimeLastEaten()) > (THIRTYMINUTES * DogService.QUICKTIME)) {
            DogService.INSTANCE.updateSatisfaction(15, 80);
            setView(DogMood.HAPPY);
        } else {
            DogService.INSTANCE.updateSatisfaction(-5, 60);
            setView(DogMood.SAD);
        }
        if(Math.random() < 0.75 && gettingDirty) {
            setDirty(true);
            setView(DogMood.DIRTY);
        }
    }

    public void cleanedDog() {
        DogService.INSTANCE.updateSatisfaction(10);
        DogService.INSTANCE.setDirty(false);
        setView(DogMood.HAPPY);
    }

    public void walkedWithDog(boolean gettingDirty) {
        if((getTime() - DogService.INSTANCE.getTimeLastWalked()) > (THIRTYMINUTES * DogService.QUICKTIME)) {
            DogService.INSTANCE.updateSatisfaction(10, 75);
            setView(DogMood.HAPPY);
        } else {
            DogService.INSTANCE.updateSatisfaction(-5, 60);
            setView(DogMood.SAD);
        }
        if(Math.random() < 0.75 && gettingDirty) {
            setDirty(true);
            setView(DogMood.DIRTY);
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

    public void setLastRefreshed(long time) {
        lastRefreshed = time;
    }

    public enum DogMood {
        BARKING, HAPPY, PLAYFULL, SAD, DEAD, DIRTY, HUNGRY;

        public int getRes(){
            switch (this){
                case BARKING:
                    return R.drawable.dog_barking_f30;
                case HAPPY:
                    return R.drawable.dog_happy_f30;
                case PLAYFULL:
                    return R.drawable.dog_playfull_f30;
                case SAD:
                    return R.drawable.dog_sad_f30;
                case DEAD:
                    return -1;
                case HUNGRY:
                    return -1; // TODO, remove "return -1"
//                  TODO return R.drawable.dog.
                case DIRTY:
                    return -1; // TODO, remove "return -1"
//                  TODO return R.drawable.dog.
                default:
                    return -1;
            }
        }

        public int getFrames(){
            switch (this){
                case BARKING:
                    return 30;
                case HAPPY:
                    return 30;
                case PLAYFULL:
                    return 30;
                case SAD:
                    return 30;
                case DIRTY:
                    return 30;
                case HUNGRY:
                    return 30;
                case DEAD:
                    return -1;
                default:
                    return -1;
            }
        }
    }

    public void setView(DogMood mood) {
        if (getTime() - getTimeLastRefreshed() > (THIRTYMINUTES/30)) {
            view.setSprite(mood);
            setLastRefreshed(getTime());
        }
    }

    public long getTimeLastRefreshed() {
        return lastRefreshed;
    }


    public void setDirty(){
        view.setDirty();
    }
}
