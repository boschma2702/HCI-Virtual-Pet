package example.com.virtualpet.flapdog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

import example.com.virtualpet.Util.ResourceManager;

/**
 * Created by reneb_000 on 16-12-2015.
 */
public class FlapDogView extends SurfaceView implements View.OnTouchListener, Runnable {

    private SurfaceHolder holder;
    private ArrayList<Pipe> pipes = new ArrayList();
    private FlapDog flapDog = new FlapDog();
    private Paint font;

    private int score = 0;
    private int scorex = (int) ResourceManager.INSTANCE.getPercentageLength(25, true);
    private int scorey = (int) ResourceManager.INSTANCE.getPercentageLength(50, false);

    private Bitmap background = ResourceManager.INSTANCE.flapdogBackground;

    private FlapDogActivity flapDogActivity;

    public FlapDogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        this.setOnTouchListener(this);
        font = new Paint();
        font.setTextSize(ResourceManager.INSTANCE.convertDpToPixel(100));
    }

    public void start(FlapDogActivity flapDogActivity) {
        this.flapDogActivity = flapDogActivity;
        pipes.add(new Pipe(this));
    }


    public void onDraw(Canvas c) {
//        if(holder.getSurface().isValid()){
//            Canvas c = holder.lockCanvas();
//            if(c!=null) {
//                //c.drawARGB(255, 255, 255, 255);
//                c.drawBitmap(background, 0, 0, null);
//                //c.drawBitmap(test, x, y, paint);
//                flapDog.draw(c);
//                for(Pipe p:pipes){
//                    p.draw(c);
//                }
//                c.drawText(String.valueOf(score), scorex, scorey,font);
//                holder.unlockCanvasAndPost(c);
//            }
//        }
        if(c!=null) {
            c.drawBitmap(background, 0, 0, null);
            flapDog.draw(c);
            for (Pipe p : pipes) {
                p.draw(c);
            }
            c.drawText(String.valueOf(score), scorex, scorey, font);
        }
    }

    public void update() {
        flapDog.update();
        updatePipes();
        if (checkCollisions()) {
            flapDogActivity.gameOver(score);
            this.running = false;
        }
    }

    private boolean checkCollisions() {
        Rect flapdogRect = flapDog.getCollisionRect();
        for (Pipe p : pipes) {
            if (p.collide(flapdogRect)) {
//                Log.e("collision", "Colission");
                return true;
            }
        }
        return false;
    }

    private void updatePipes() {
        int flapx = flapDog.getX();
        ArrayList<Pipe> toRemove = new ArrayList<>();
        for (Pipe p : pipes) {
            if (p.update(flapx)) {
                toRemove.add(p);
            }
        }
        pipes.removeAll(toRemove);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            Log.e("Flapdog", "clicked");
            flapDog.jump();
        }
        return false;
    }

    public void addScore() {
        score++;
    }

    public void reset() {
        score = 0;
        pipes.clear();
        flapDog.reset();
        pipes.add(new Pipe(this));
    }


    public void addPipe() {
        pipes.add(new Pipe(this));
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
                    update();

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
                        update();
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
}
