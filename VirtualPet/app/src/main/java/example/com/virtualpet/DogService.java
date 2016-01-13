package example.com.virtualpet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by reneb_000 on 30-12-2015.
 * Last updated by Gijs on 07-Jan-2016.
 */
public class DogService extends Service implements Runnable {

    public static DogService INSTANCE;

    private int satisfaction;
    public static final int MAXSATISFACTION = 100;
    public static final int MINSATISFACTION = 0;
    public static final long THIRTYMINUTES = 1800000; // thirty minutes in milliseconds
    public static final long QUICKTIME = 1/8; // decrease to make time go faster
    public static final long GetTimeFASTER = 60; // increase to make getTime(), which is used in DogService.java & Dog.java, return higher values.

    protected ArrayList<Calendar> eatTimes = new ArrayList<Calendar>();
    protected ArrayList<Calendar> walkTimes = new ArrayList<Calendar>();
    private Calendar now = Calendar.getInstance();
    private long lastPlayed;
    private long lastWalked;
    private long lastEaten;
    private Intent intent;
    private boolean dirty;
    private boolean hungry;
    private boolean wantsToWalk;
    private boolean wantsToPlay;

    NotificationManager mNotificationManager;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        INSTANCE = this;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        new Thread(this).start();
        initialize();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        //intent.addCategory("DogService");
        //startService(intent);

        //onBind(intent);

        while (true){
            try {
                Thread.sleep(5000);
                //showNotification("DogService", "Still running!");

                update();

            } catch (InterruptedException e) {
                Log.e("DogService", "InterruptedException: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void initialize() {
        setTimeLastEaten(new Date(getTime()));
        setTimeLastPlayed(new Date(getTime()));
        setTimeLastWalked(new Date(getTime()));
        setDirty(false);
        setHungry(false);
        setWantsToPlay(true);
        setWantsToWalk(false);
        setEatTimes();
        setWalkTimes();
    }

    public void update() {
        checkForEatTime();
        checkForWalkTime();
        checkStatus();
    }

    public void checkStatus() {
        if ((getTime() - getTimeLastPlayed()) > (5*THIRTYMINUTES * QUICKTIME)) {
            showNotification("Bark bark!", "I am bored!");
            updateSatisfaction(50);
            setWantsToPlay(true);
            setWantsToWalk(false);
        }
        if ((getTime() - getTimeLastWalked()) > (5*THIRTYMINUTES * QUICKTIME)) {
            showNotification("Bark bark!", "I want to walk!");
            updateSatisfaction(40);
            setWantsToWalk(true);
            setWantsToPlay(true);
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

    public void checkForEatTime() {
        for (int i = 0; i < eatTimes.size(); i++) {
            if (eatTimes.get(i).get(Calendar.HOUR_OF_DAY) == now.get(Calendar.HOUR_OF_DAY)) {
                showNotification("Bark bark!", "I am hungry!");
                updateSatisfaction(-5);
                setHungry(true);
            }
        }
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

    public void checkForWalkTime() {
        for (int i = 0; i < walkTimes.size(); i++) {
            if (walkTimes.get(i).get(Calendar.HOUR_OF_DAY) == now.get(Calendar.HOUR_OF_DAY) && getTime() - getTimeLastWalked() > THIRTYMINUTES * QUICKTIME) {
                showNotification("Bark bark!", "I want to go outside for a walk!");
                setWantsToWalk(true);
            }
        }
    }

    // Satisfaction
    public void updateSatisfaction(int satisfaction) {
        synchronized (this) {

            /* if (this.satisfaction + satisfaction > MAXSATISFACTION ) {
                this.satisfaction = MAXSATISFACTION;
            } else if (this.satisfaction + satisfaction < MINSATISFACTION) {
                this.satisfaction = MINSATISFACTION;
            } else {
                this.satisfaction += satisfaction;
            } /*

            this.satisfaction = satisfaction;

            statisfactionChanged();
        }
    }

    public void updateSatisfaction(int satisfaction, long chance) {
        if (Math.random() * 100 <= chance) {
            synchronized (this) {

               /* if (this.satisfaction + satisfaction > MAXSATISFACTION) {
                    this.satisfaction = MAXSATISFACTION;
                } else if (this.satisfaction + satisfaction < MINSATISFACTION) {
                    this.satisfaction = MINSATISFACTION;
                } else {
                    this.satisfaction += satisfaction;
                }
                */
                this.satisfaction = satisfaction;
                statisfactionChanged();
          //  }
        }
    }

    private void statisfactionChanged(){
        //TODO observer pattern would be nice here, but hard to implement. Workaround is added
    }

//    Getters

    public boolean getHungry() {
        synchronized (this) {
            return this.hungry;
        }
    }

    public long getTimeLastWalked() {
        synchronized (this) {
            return lastWalked;
        }
    }

    public boolean getDirty() {
        synchronized (this) {
            return this.dirty;
        }
    }

    public int getSatisfaction() {
        synchronized (this) {
            return satisfaction;
        }
    }

    public long getTimeLastPlayed() {
        synchronized(this) {
            return lastPlayed;
        }
    }

    public long getTimeLastEaten() {
        synchronized(this) {
            return lastEaten;
        }
    }

    public long getTime(){
        synchronized (this) {
            long time = System.currentTimeMillis() * GetTimeFASTER;
            //Log.e("Application Time:", new Date(time).toString());
            return time;
        }
    }

    public boolean getWantsToWalk() {
        synchronized (this) {
            return wantsToWalk;
        }
    }

    public boolean getWantsToPlay() {
        synchronized (this) {
            return wantsToPlay;
        }
    }

    //    Setters

    public void setHungry(boolean hungry) {
        synchronized (this) {
            this.hungry = hungry;
        }
    }

    public void setDirty(boolean dirty) {
        synchronized (this) {
          this.dirty = dirty;
        }
}

    public void setSatisfaction(int satisfaction) {
        synchronized (this) {
            this.satisfaction = satisfaction;
        }
    }

    public void setTimeLastEaten(Date date) {
        synchronized (this) {
            this.lastEaten = date.getTime();
        }
    }

    public void setTimeLastPlayed(Date date) {
        synchronized (this) {
            this.lastPlayed = date.getTime();
        }
    }

    public void setTimeLastWalked(Date date) {
        synchronized (this) {
            this.lastWalked = date.getTime();
        }
    }

    public void setWantsToWalk(boolean wantsToWalk) {
        synchronized (this) {
            this.wantsToWalk = wantsToWalk;
        }
    }

    public void setWantsToPlay(boolean wantsToPlay) {
        synchronized (this) {
            this.wantsToPlay = wantsToPlay;
        }
    }

    private void showNotification(String title, String content){
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Notification noti = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(resultPendingIntent)
                .build();
        mNotificationManager.notify(0, noti);
    }
}
