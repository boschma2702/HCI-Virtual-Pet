package example.com.virtualpet.flapdog;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import example.com.virtualpet.ResourceManager;

/**
 * Created by reneb_000 on 16-12-2015.
 */
public class FlapDog {

    //TODO values screen independed
    private int x, y, yspeed, screenHeight;
    private final int width = 100;
    private final int jumpSpeed = 30;
    private final int gravity = 2;
    private Paint paint = new Paint();


    public FlapDog(){
        screenHeight = ResourceManager.INSTANCE.getScreenHeight();
        this.y = screenHeight/2;
        this.yspeed = -5;
        paint.setColor(Color.BLACK);
    }

    public void draw(Canvas c){
        c.drawCircle(x, y, width/2, paint);
    }

    public void jump(){
        yspeed = jumpSpeed;
    }

    /**
     *
     * @return true if y is bigger then screenHeight (the dog should die), otherwise returns false.
     */
    public boolean update(){
        y -= yspeed;
        yspeed -= gravity;
        if(y<screenHeight){
            return true;
        }else{
            return false;
        }
    }



}
