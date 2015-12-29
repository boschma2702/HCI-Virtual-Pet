package example.com.virtualpet.flapdog;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

import example.com.virtualpet.Util.ResourceManager;

/**
 * Created by reneb_000 on 16-12-2015.
 */
public class Pipe {

    private int x, speed = (int) ResourceManager.INSTANCE.getPercentageLength(0.6, false);
    private Paint paint = new Paint();
    private final static int widht = (int) ResourceManager.INSTANCE.convertDpToPixel(60);
    private int screenHeight, screenWidth;
    private int gapHalfWidth = (int) ResourceManager.INSTANCE.getPercentageLength(15, true);
    private int type;

    private boolean added = false;

    private FlapDogView controller;

    private Rect bottom, top;

    public Pipe(FlapDogView controller){
        this(ResourceManager.INSTANCE.getScreenWidth()+widht/2, controller);
    }

    public Pipe(int x, FlapDogView controller){
        screenHeight = ResourceManager.INSTANCE.getScreenHeight();
        screenWidth = ResourceManager.INSTANCE.getScreenWidth();
        this.x = x;
        paint.setColor(Color.GREEN);
        top = new Rect(this.x-widht/2, 0, this.x+widht/2, screenHeight/2-gapHalfWidth);
        bottom = new Rect(this.x-widht/2, screenHeight/2+gapHalfWidth, this.x+widht/2, screenHeight);
        this.controller = controller;
        Random random = new Random();
        type = random.nextInt(3);
    }

    /**
     *
     * @return true if x of pipe is bigger then 0 otherwise returns false.
     */
    public boolean update(int flapx){
        this.x -= speed;
        top.set(x - widht / 2, 0, x + widht / 2, screenHeight / 2 - gapHalfWidth*type);
        bottom.set(x - widht / 2, screenHeight / 2 + gapHalfWidth*(2-type), x + widht / 2, screenHeight);

        if(!added&&this.x<flapx){
            controller.addScore();
            added = true;
            controller.addPipe();
        }
        if(x<-widht){
            return true;
        }
        return false;


//        if(x<=screenWidth/2){
//            return true;
//        }else{
//            return false;
//        }
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
