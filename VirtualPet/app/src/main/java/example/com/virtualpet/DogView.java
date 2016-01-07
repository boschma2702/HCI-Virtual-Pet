package example.com.virtualpet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import example.com.virtualpet.Util.ResourceManager;
import example.com.virtualpet.Util.SpriteSheet;


/**
 * Created by reneb_000 on 3-12-2015.
 */
public class DogView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    //private Bitmap test;
    private int x, y;
    private SurfaceHolder holder;
    private Paint paint = new Paint();
    private SpriteSheet currentSheet;

    private Dog dog;


    public DogView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        holder = getHolder();
        holder.addCallback(this);
        //test = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Log.e("Sheet", "value of sheet: "+ResourceManager.INSTANCE.getBitmapFromMemCache("dogHappy"));
        currentSheet = new SpriteSheet(ResourceManager.INSTANCE.getBitmapFromMemCache("dogHappy"), 30, false);
        x = y = 0;
        dog = new Dog(context, this);
        //new Thread(this).start();
    }

    public void pause(){
        running = false;
    }

    public void onDraw() {
        if (holder.getSurface().isValid()) {
            Canvas c = holder.lockCanvas();
            holder.unlockCanvasAndPost(c);
        }
    }
    public void resume(){
        new Thread(this).start();
    }

    public void onDraw(Canvas c){
        if(c!=null) {
            c.drawARGB(255, 200, 200, 200);
            currentSheet.draw(c);
        }
    }

    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
        currentSheet.setXY(x, y);
    }

    public void setAnimation(SpriteSheet sheet){
        currentSheet = sheet;
        currentSheet.setXY(x, y);
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
}
