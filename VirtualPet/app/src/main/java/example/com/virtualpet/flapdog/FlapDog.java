package example.com.virtualpet.flapdog;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import example.com.virtualpet.Util.ResourceManager;

/**
 * Created by reneb_000 on 16-12-2015.
 */
public class FlapDog {

    //TODO values screen independed
    private int x, y, yspeed, screenHeight;
    private final int width = (int) ResourceManager.INSTANCE.getPercentageLength(10, true);
    private final int jumpSpeed = (int)ResourceManager.INSTANCE.convertDpToPixel(10);
    private final double gravity = ResourceManager.INSTANCE.convertDpToPixel(1);
    private Paint paint = new Paint();
    private Rect rect;
    private Paint debug = new Paint();


    public FlapDog(){
        screenHeight = ResourceManager.INSTANCE.getScreenHeight();
        this.x = ResourceManager.INSTANCE.getScreenWidth()/2;
        this.y = screenHeight/2;
        this.yspeed = 0;
        paint.setColor(Color.BLACK);
        debug.setColor(Color.RED);
        rect = new Rect(x-width/2, y-width/2, x+width/2, y+width/2);
    }

    public void draw(Canvas c){
//        c.drawRect(rect, debug);
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
        rect.set(x-width/2, y-width/2, x+width/2, y+width/2);
        if(y<screenHeight){
            return true;
        }else{
            return false;
        }
    }

    public void reset(){
        this.y = screenHeight/2;
        this.yspeed = 0;
    }

    public Rect getCollisionRect(){
        return rect;
    }


    public int getX() {
        return x;
    }
}
