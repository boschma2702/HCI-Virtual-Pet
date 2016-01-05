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
 * Last updated by gijsbeernink on 14-12-2015.
 */
public class Dog {

    private DogView view;
    private int x, y, screenwidth, speed;

    private int satisfaction;
    private static final int MAXSATISFACTION = 100;
    private static final int MINSATISFACTION = 0;
    public static final long UPDATEINTERVAL = 60000; // check for updates every minute
    public static final long THIRTYMINUTES = 1800000; // thirty minutes in milliseconds
    private long lastUpdate;

    protected ArrayList<Calendar> eatTimes = new ArrayList<Calendar>();
    protected ArrayList<Calendar> walkTimes = new ArrayList<Calendar>();
    private Calendar now = Calendar.getInstance();
    private long lastEaten;
    private long lastPlayed;
    private boolean dirty;


    public Dog(Context c, DogView view) {
        this.view = view;
//        x = (int) ResourceManager.INSTANCE.convertPixelsToDp(500);
//        y = (int) ResourceManager.INSTANCE.convertPixelsToDp(2000);
        speed = 5;
        screenwidth = ResourceManager.INSTANCE.getScreenWidth();
        x = screenwidth/2;
        y = (int) (ResourceManager.INSTANCE.getScreenHeight()-ResourceManager.INSTANCE.getPercentageLength(20, true));
        view.setXY(x, y);
        initialize();
    }

    public void update(){
//        if(x>screenwidth){
//            speed = -speed;
//        }else if(x<0){
//            speed = -speed;
//        }
//        x += speed;
        view.setXY(x, y);

//      Update every given interval
        if (getLastUpdate() - getTime() > UPDATEINTERVAL) {
            checkUpdates();
            setLastUpdate(getTime());
        }
    }

    public void checkUpdates() {
        checkForEatTime();
        checkForWalkTime();
        checkActivities();
    }

    public void initialize() {
        Log.e("Dog", "Initializing...");
        updateSatisfaction(60);
        now.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
        setEatTimes();
        setWalkTimes();
    }

    public void checkActivities() {
        if ((getTime() - getTimeLastPlayed()) > 6*THIRTYMINUTES) {
//            TODO: Show animation bored
        }
        if (getDirty()) {
//            TODO: Show animation dirty
        }
        if ((getTime() - getTimeLastEaten()) > 6*THIRTYMINUTES) {
//            TODO: Show animation hungry
        }
    }

    public void playWithDog(boolean gettingDirty) {
        if((getTime() - getTimeLastEaten()) > 2*THIRTYMINUTES) {
            updateSatisfaction(15);
            if(Math.random() < 0.75 && gettingDirty) {
                setDirty(true);
            }
        } else {
            // If an hour has not yet passed
            updateSatisfaction(-10);
        }
    }

    // Eating
    public void setEatTimes() {
        Calendar eatMorning = Calendar.getInstance();
        Calendar eatAfternoon = Calendar.getInstance();
        Calendar eatEvening = Calendar.getInstance();
        eatMorning.set(Calendar.HOUR_OF_DAY, 8);
        eatAfternoon.set(Calendar.HOUR_OF_DAY, 13);
        eatEvening.set(Calendar.HOUR_OF_DAY, 19);
        eatTimes.add(eatMorning);
        eatTimes.add(eatAfternoon);
        eatTimes.add(eatEvening);
    }

    public boolean checkForEatTime() {
        boolean eat = false;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTime());
        for (int i = 0; i < eatTimes.size(); i++) {
            if (eatTimes.get(i).get(Calendar.HOUR_OF_DAY) == cal.get(Calendar.HOUR_OF_DAY)) {
                eat = true;
                // TODO: Show animation
            }
        }
        return eat;
    }

    // Walking
    public void setWalkTimes() {
        Calendar walkMorning = Calendar.getInstance();
        Calendar walkNoon = Calendar.getInstance();
        Calendar walkAfternoon = Calendar.getInstance();
        Calendar walkEvening = Calendar.getInstance();
        walkMorning.set(Calendar.HOUR_OF_DAY, 7);
        walkNoon.set(Calendar.HOUR_OF_DAY, 12);
        walkAfternoon.set(Calendar.HOUR_OF_DAY, 17);
        walkEvening.set(Calendar.HOUR_OF_DAY, 21);
        walkTimes.add(walkMorning);
        walkTimes.add(walkNoon);
        walkTimes.add(walkAfternoon);
        walkTimes.add(walkEvening);
    }

    public boolean checkForWalkTime() {
        boolean walk = false;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getTime());
        for (int i = 0; i < walkTimes.size(); i++) {
            if (walkTimes.get(i).get(Calendar.HOUR_OF_DAY) == cal.get(Calendar.HOUR_OF_DAY)) {
                walk = true;
                // TODO: Show animation
            }
        }

        return walk;
    }

    // Satisfaction
    public void updateSatisfaction(int satisfaction) {
        if (this.satisfaction + satisfaction > MAXSATISFACTION) {
            this.satisfaction = MAXSATISFACTION;
        } else if (this.satisfaction - satisfaction < MINSATISFACTION){
            this.satisfaction = MINSATISFACTION;
        } else {
            this.satisfaction += satisfaction;
        }

    }

    public int getSatisfaction() {
        return satisfaction;
    }

    // Getters & Setters
    public long getTimeLastPlayed() {
        return lastPlayed;
    }

    public long getTimeLastEaten() {
        return lastEaten;
    }

    public void setTimeLastEaten(Date date) {
        this.lastEaten = date.getTime();
    }

    public void setTimeLastPlayed(Date date) {
        this.lastPlayed = date.getTime();
    }

    public boolean getDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public long getTime(){
        return now.getTimeInMillis();
    }

    public void setLastUpdate(long time) {
        lastUpdate = time;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public DogView getView() {
        return view;
    }
}
