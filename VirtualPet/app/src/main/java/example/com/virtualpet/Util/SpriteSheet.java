package example.com.virtualpet.Util;

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

    private Paint paint = new Paint();

    private int frameHeight, frameWidth;

    private Rect dst;

    public SpriteSheet(Bitmap spritesheet, int frameCount){
        frameHeight = spritesheet.getHeight();
        frameWidth = spritesheet.getWidth()/frameCount;
        this.sheet = spritesheet;
        this.frameCount = frameCount;
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
        Rect src = new Rect(frameCount*frameWidth, 0, frameCount*frameWidth+frameWidth, frameHeight);

        c.drawBitmap(sheet, src, dst,paint);
    }
}
