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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by reneb_000 on 30-12-2015.
 */
public class DogService extends Service implements Runnable {

    private int satisfaction;
    private static final int MAXSATISFACTION = 100;
    private static final int MINSATISFACTION = 0;
    public static final long THIRTYMINUTES = 1800000; // thirty minutes in milliseconds

    protected ArrayList<Calendar> eatTimes = new ArrayList<Calendar>();
    protected ArrayList<Calendar> walkTimes = new ArrayList<Calendar>();
    private Calendar now = Calendar.getInstance();
    private long lastPlayed;
    private long lastWalked;
    private long lastEaten;
    private Intent intent;

    NotificationManager mNotificationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        new Thread(this).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void run() {
        intent.addCategory("DogService");
        startService(intent);

        onBind(intent);

        initialize();
        while (true){
            try {
                Thread.sleep(5000);
//                Toast.makeText(this, "Still running", Toast.LENGTH_SHORT).show();
                Log.e("DogService", "Still running!");
                showNotification("DogService", "Still running!");

                update();

            } catch (InterruptedException e) {
                Log.e("DogService", "InterruptedException: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void initialize() {
        lastPlayed = getTime();
        lastWalked = getTime();
        lastEaten = getTime();
        setEatTimes();
        setWalkTimes();
    }

    public void update() {
        checkForEatTime();
        checkForWalkTime();
        checkStatus();
    }

    public void checkStatus() {
        if ((getTime() - getTimeLastPlayed()) > 5*THIRTYMINUTES) {
            showNotification("Bark bark!", "I am bored!");
            updateSatisfaction(-5);
        }
        if ((getTime() - getTimeLastWalked()) > 5*THIRTYMINUTES) {
            showNotification("Bark bark!", "I want to walk!");
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

    public void checkForEatTime() {
        for (int i = 0; i < eatTimes.size(); i++) {
            if (eatTimes.get(i).get(Calendar.HOUR_OF_DAY) == now.get(Calendar.HOUR_OF_DAY)) {
                showNotification("Bark bark!", "I am hungry!");
                updateSatisfaction(-5);
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
            if (walkTimes.get(i).get(Calendar.HOUR_OF_DAY) == now.get(Calendar.HOUR_OF_DAY)) {
                showNotification("Bark bark!", "I want to go outside for a walk!");
            }
        }
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

    // Getters & Setters
    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }

    public long getTimeLastPlayed() {
        return lastPlayed;
    }

    public long getTimeLastWalked() {
        return lastWalked;
    }

    public void setTimeLastEaten(Date date) {
        this.lastEaten = date.getTime();
    }

    public void setTimeLastPlayed(Date date) {
        this.lastPlayed = date.getTime();
    }

    public long getTime(){
        return now.getTimeInMillis();
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
