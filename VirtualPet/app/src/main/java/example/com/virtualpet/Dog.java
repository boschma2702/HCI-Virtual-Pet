package example.com.virtualpet;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


/**
 * Created by reneb_000 on 3-12-2015.
 * Last updated by gijsbeernink on 14-12-2015.
 */
public class Dog {

    private DogView view;
    private int x, y, screenwidth, speed;

    private int satisfaction = 0;
    private static final int MAXSATISFACTION = 100;
    private static final int MINSATISFACTION = 0;

    protected ArrayList<Calendar> eatTimes = new ArrayList<Calendar>();
    protected ArrayList<Calendar> walkTimes = new ArrayList<Calendar>();
    private Calendar now = Calendar.getInstance();
    private boolean eaten;
    private boolean walked;


    public Dog(Context c, DogView view){
        this.view = view;
        x = (int) ResourceManager.INSTANCE.convertPixelsToDp(1);
        y = (int) ResourceManager.INSTANCE.convertPixelsToDp(2000);
        speed = 5;
        screenwidth = ResourceManager.INSTANCE.getScreenwidht();
        initialize();
    }

    public void update(){
        if(x>screenwidth){
            speed = -speed;
        }else if(x<0){
            speed = -speed;
        }
        x += speed;

        checkUpdates();
    }

    public void checkUpdates() {
        checkForEatTime();
        checkForWalkTime();
    }

    public void initialize() {
        Log.e("dog", "Initializing...");
        updateSatisfaction(50);
        now.setTimeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
        setEatTimes();
        setWalkTimes();
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
        for (int i = 0; i < eatTimes.size(); i++) {
            if (eatTimes.get(i).get(Calendar.HOUR_OF_DAY) == getHourOfDay()) {
                eat = true;
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
        for (int i = 0; i < walkTimes.size(); i++) {
            if (walkTimes.get(i).get(Calendar.HOUR_OF_DAY) == getHourOfDay()) {
                walk = true;
            }
        }
        return walk;
    }

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

    public int getHourOfDay(){
        return now.get(Calendar.HOUR_OF_DAY);
    }

    public DogView getView() {
        return view;
    }
}
