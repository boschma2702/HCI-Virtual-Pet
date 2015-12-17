package example.com.virtualpet.flapdog;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by reneb_000 on 16-12-2015.
 */
public class FlapDogView extends SurfaceView implements View.OnTouchListener {

    private SurfaceHolder holder;
    private ArrayList<Pipe> pipes = new ArrayList();
    private FlapDog flapDog = new FlapDog();

    public FlapDogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        this.setOnTouchListener(this);
    }

    public void start(){
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
                holder.unlockCanvasAndPost(c);
            }
        }

    }

    public void update(){
        flapDog.update();
        updatePipes();
    }

    private void updatePipes(){
        for(Pipe p:pipes){
            if(p.update()){
                pipes.remove(p);
                pipes.add(new Pipe());
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            Log.e("Flapdog", "clicked");
            flapDog.jump();
        }
        return false;
    }
}
