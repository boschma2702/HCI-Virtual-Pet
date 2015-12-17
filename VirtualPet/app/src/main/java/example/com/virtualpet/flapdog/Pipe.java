package example.com.virtualpet.flapdog;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import example.com.virtualpet.ResourceManager;

/**
 * Created by reneb_000 on 16-12-2015.
 */
public class Pipe {

    private int x, speed = 5;
    private Paint paint = new Paint();
    private int widht = 100;
    private int screenHeight;
    private int gapHalfWidth = 500;

    public Pipe(){
        this(ResourceManager.INSTANCE.getScreenWidth());
    }

    public Pipe(int x){
        screenHeight = ResourceManager.INSTANCE.getScreenHeight();
        this.x = x;
        paint.setColor(Color.BLACK);
    }

    /**
     *
     * @return true if x of pipe is bigger then 0 otherwise returns false.
     */
    public boolean update(){
        x -= speed;
        if(x<0){
            return true;
        }else{
            return false;
        }
    }

    public void draw(Canvas c){
        c.drawRect(x-widht/2, 0, x+widht/2, screenHeight/2-gapHalfWidth, paint);
        c.drawRect(x-widht/2, screenHeight/2+gapHalfWidth, x+widht/2, screenHeight, paint);
    }

    public void setX(int x){
        this.x = x;
    }

}
