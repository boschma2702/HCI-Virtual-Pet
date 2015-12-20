package example.com.virtualpet.flapdog;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import example.com.virtualpet.Util.ResourceManager;

/**
 * Created by reneb_000 on 16-12-2015.
 */
public class Pipe {

    private int x, speed = 5;
    private Paint paint = new Paint();
    private final static int widht = (int) ResourceManager.INSTANCE.convertDpToPixel(60);;
    private int screenHeight;
    private int gapHalfWidth = 500;

    private boolean added = false;

    private Rect bottom, top;

    public Pipe(){
        this(ResourceManager.INSTANCE.getScreenWidth()+widht/2);
    }

    public Pipe(int x){
        screenHeight = ResourceManager.INSTANCE.getScreenHeight();
        this.x = x;
        paint.setColor(Color.GREEN);
        top = new Rect(this.x-widht/2, 0, this.x+widht/2, screenHeight/2-gapHalfWidth);
        bottom = new Rect(this.x-widht/2, screenHeight/2+gapHalfWidth, this.x+widht/2, screenHeight);
    }

    /**
     *
     * @return true if x of pipe is bigger then 0 otherwise returns false.
     */
    public boolean update(int flapx, FlapDogView v){
        this.x -= speed;
        top.set(x - widht / 2, 0, x + widht / 2, screenHeight / 2 - gapHalfWidth);
        bottom.set(x - widht / 2, screenHeight / 2 + gapHalfWidth, x + widht / 2, screenHeight);

        if(!added&&this.x<flapx){
            v.addScore();
            added = true;
        }

        if(x<-widht/2){
            return true;
        }else{
            return false;
        }
    }

    //TODO not really good collision checking.
    public boolean collide(Rect rect){
        return rect.bottom<=0 || rect.top>= screenHeight||Rect.intersects(top, rect) || Rect.intersects(bottom, rect);
    }

    public void draw(Canvas c){
        c.drawRect(top, paint);
        c.drawRect(bottom, paint);
    }

    public void setX(int x){
        this.x = x;
    }

}
