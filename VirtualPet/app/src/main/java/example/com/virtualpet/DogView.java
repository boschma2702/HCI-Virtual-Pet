package example.com.virtualpet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Arrays;

import example.com.virtualpet.Util.ResourceManager;
import example.com.virtualpet.Util.SpriteSheet;
import example.com.virtualpet.cleaning.CleaningManger;


/**
 * Created by reneb_000 on 3-12-2015.
 */
public class DogView extends SurfaceView implements SurfaceHolder.Callback, Runnable, View.OnTouchListener {

    //private Bitmap test;
    private int x, y;
    private SurfaceHolder holder;
    private Paint paint = new Paint();
    private SpriteSheet currentSheet;

    private Dog dog;

    private MainActivity activity;

    private boolean drawDirty = false;
    private CleaningManger cleaningManger;
    private Bitmap background;

    private int[] black = new int[]{0,0,0};
    private int[] blue = new int[]{122, 222, 240};
    private int[] currentBackgroundColor = new int[]{112,222,240};
    private Time morning = ResourceManager.INSTANCE.morning; // moet weer lichter worden.
    private Time night = ResourceManager.INSTANCE.night; // moet donker zijn
    private Time midDay = ResourceManager.INSTANCE.midDay; // moet velste blauw zijn
    private Time currentTime = new Time();

    private int secondCounter = 0;
    private int second = 30;


    public DogView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.setOnTouchListener(this);
        holder = getHolder();
        holder.addCallback(this);
        //test = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        currentSheet = ResourceManager.INSTANCE.dogHappy;
        background = ResourceManager.INSTANCE.gameBackground;
        x = y = 0;
        activity = (MainActivity) context;
        dog = new Dog(context, this);
        cleaningManger = new CleaningManger(x, y, currentSheet.getBitmap().getWidth(), (int) ResourceManager.INSTANCE.dogHeight);
        //new Thread(this).start();
    }

    public void pause(){
        running = false;
    }

    public void resume(){
        new Thread(this).start();
    }

    private void updateSecond() {
        activity.statisfactionChanged();
    }

    public void onDraw(Canvas c){
        if(c!=null) {
//            c.drawARGB(255, 200, 200, 200);
            c.drawARGB(255, currentBackgroundColor[0], currentBackgroundColor[1], currentBackgroundColor[2]);
            c.drawBitmap(background, 0, 0, null);
            currentSheet.draw(c);
            if (drawDirty) {
                cleaningManger.onDraw(c);
            }
        }
    }

    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
        currentSheet.setXY(x, y);
    }

    public void setSprite(Dog.DogMood mood){
        currentSheet.setSheet(mood);
    }

    public void setBackgroundColor(){
        currentTime.setToNow();
        if(currentTime.after(night)||currentTime.before(morning)){
            //moet zwart zijn;
            currentBackgroundColor = black;
        } else if(currentTime.after(morning)&&currentTime.before(midDay)){
            //moet lichter worden

            int maxDif = midDay.hour - morning.hour;
            int deltaHour = maxDif - (currentTime.hour-morning.hour);
            double percentage = (double)deltaHour/maxDif;
//            currentBackgroundColor[0] = (int) ((black[0]-blue[0])*percentage-black[0]);
            currentBackgroundColor[0] = (int) ((blue[0]-black[0])*percentage);
            currentBackgroundColor[1] = (int) ((blue[1]-black[1])*percentage);
//            currentBackgroundColor[2] = (int) ((blue[2]-black[2])*percentage);
        } else if(currentTime.after(midDay)&&currentTime.before(night)){
            //moet donkerder worden
            int maxDif = night.hour - midDay.hour;
            int deltaHour = maxDif - (currentTime.hour-midDay.hour);
            double percentage = (double)deltaHour/maxDif;
//            currentBackgroundColor[0] = (int) ((black[0]-blue[0])*percentage+blue[0]);
            currentBackgroundColor[0] = (int) (blue[0]-(blue[0]-black[0])*percentage);
            currentBackgroundColor[1] = (int) (blue[1]-(blue[1]-black[1])*percentage);
//            currentBackgroundColor[2] = (int) (blue[2]-(blue[2]-black[2])*percentage);
        }
//        Log.e("color", currentBackgroundColor[0] +" "+currentBackgroundColor[1]+" "+currentBackgroundColor[2]);
    }

    // desired fps
    private final static int MAX_FPS = 30;
    // maximum number of frames to be skipped
    private final static int MAX_FRAME_SKIPS = 5;
    // the frame period
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;
    private boolean running = false;


    @Override
    public void run() {
        Canvas canvas;
        running = true;
        long beginTime;        // the time when the cycle begun
        long timeDiff;        // the time it took for the cycle to execute
        int sleepTime;        // ms to sleep (<0 if we're behind)
        int framesSkipped;    // number of frames being skipped

        sleepTime = 0;

        while (running) {
            canvas = null;
            // try locking the canvas for exclusive pixel editing
            // in the surface
            try {
                canvas = this.holder.lockCanvas();
                synchronized (holder) {
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0;    // resetting the frames skipped
                    // update game state
                    dog.update();
                    secondCounter = (secondCounter+1)%second;
                    if(secondCounter==0){
                        updateSecond();
                    }

                    //draw
                    onDraw(canvas);
                    // calculate how long did the cycle take
                    timeDiff = System.currentTimeMillis() - beginTime;
                    // calculate sleep time
                    sleepTime = (int) (FRAME_PERIOD - timeDiff);

                    if (sleepTime > 0) {
                        // if sleepTime > 0 we're OK
                        try {
                            // send the thread to sleep for a short period
                            // very useful for battery saving
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                    }

                    while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                        // we need to catch up
                        // update without rendering
                        dog.update();
                        // add frame period to check if in next frame
                        sleepTime += FRAME_PERIOD;
                        framesSkipped++;
                    }
                }
            } finally {
                // in case of an exception the surface is not left in
                // an inconsistent state
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }    // end finally
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void setDirty() {
        drawDirty = true;
        cleaningManger.reset();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(drawDirty&&event.getAction()==MotionEvent.ACTION_MOVE){
            if(cleaningManger.onTouch((int)event.getX(), (int)event.getY())){
                drawDirty = false;
            }
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }




}
