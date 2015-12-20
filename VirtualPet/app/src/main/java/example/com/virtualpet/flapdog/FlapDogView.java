package example.com.virtualpet.flapdog;

import android.content.Context;
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
public class FlapDogView extends SurfaceView implements View.OnTouchListener {

    private SurfaceHolder holder;
    private ArrayList<Pipe> pipes = new ArrayList();
    private FlapDog flapDog = new FlapDog();
    private Paint font;

    private int score = 0;
    private int scorex = (int) ResourceManager.INSTANCE.getPercentageLength(25, true);
    private int scorey = (int) ResourceManager.INSTANCE.getPercentageLength(50, false);

    private FlapDogActivity flapDogActivity;

    public FlapDogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        this.setOnTouchListener(this);
        font = new Paint();
        font.setTextSize(ResourceManager.INSTANCE.convertDpToPixel(100));
    }

    public void start(FlapDogActivity flapDogActivity){
        this.flapDogActivity = flapDogActivity;
        pipes.add(new Pipe());
    }


    public void onDraw(){
        if(holder.getSurface().isValid()){
            Canvas c = holder.lockCanvas();
            if(c!=null) {
                c.drawARGB(255, 255, 255, 255);
                //c.drawBitmap(test, x, y, paint);
                flapDog.draw(c);
                for(Pipe p:pipes){
                    p.draw(c);
                }
                c.drawText(String.valueOf(score), scorex, scorey,font);
                holder.unlockCanvasAndPost(c);
            }
        }

    }

    public void update(){
        flapDog.update();
        updatePipes();
        if(checkCollisions()){
            flapDogActivity.gameOver(score);
        }
    }

    private boolean checkCollisions() {
        Rect flapdogRect = flapDog.getCollisionRect();
        for(Pipe p: pipes){
            if(p.collide(flapdogRect)){
//                Log.e("collision", "Colission");
                return true;
            }
        }
        return false;
    }

    private void updatePipes(){
        int flapx = flapDog.getX();
        for(Pipe p:pipes){
            if(p.update(flapx, this)){
                pipes.remove(p);
                pipes.add(new Pipe());
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
//            Log.e("Flapdog", "clicked");
            flapDog.jump();
        }
        return false;
    }

    public void addScore() {
        score ++;
    }

    public void reset() {
        score = 0;
        pipes.clear();
        flapDog.reset();
        pipes.add(new Pipe());
    }
}
