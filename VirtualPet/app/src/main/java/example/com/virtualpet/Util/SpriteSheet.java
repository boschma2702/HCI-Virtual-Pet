package example.com.virtualpet.Util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by reneb_000 on 14-12-2015.
 */
public class SpriteSheet {


    private Bitmap sheet;
    private int frameCount;
    private int counter = 0;
    private boolean horizontal;
    private Activity a;

    private Paint paint = new Paint();

    private int frameHeight, frameWidth;

    private Rect dst;

    public SpriteSheet(Bitmap spritesheet, int frameCount, boolean horizontal){
        if(horizontal){
            frameHeight = spritesheet.getHeight();
            frameWidth = spritesheet.getWidth()/frameCount;
        }else{
            frameHeight = spritesheet.getHeight()/frameCount;
            frameWidth = spritesheet.getWidth();
        }
        this.sheet = spritesheet;
        this.frameCount = frameCount;
        this.horizontal = horizontal;
    }

    public void setSheet(String name, int frameCount){

    }

    public void update(){
        counter = (counter+1)%frameCount;
    }

    /**
     * Sets the posistion of the sprite to wich it needs to be drawn to. De origin of the sprite is
     * middle x and y bottom. Needs to be called before the draw function gets calleld.
     * @param x of the position
     * @param y of the position
     */
    public void setXY(int x, int y){
        dst = new Rect(x-frameWidth/2, y-frameHeight, x+frameWidth/2, y);
    }

    public void draw(Canvas c){
        Rect src;
        if(horizontal) {
            src = new Rect(counter * frameWidth, 0, counter * frameWidth + frameWidth, frameHeight);
        }else{
            src = new Rect(0, counter * frameHeight, frameWidth, counter * frameHeight + frameHeight);
        }
        c.drawBitmap(sheet, src, dst,paint);
        update();
    }
}
