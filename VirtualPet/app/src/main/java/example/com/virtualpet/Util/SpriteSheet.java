package example.com.virtualpet.Util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import example.com.virtualpet.Dog;

/**
 * Created by reneb_000 on 14-12-2015.
 */
public class SpriteSheet implements Runnable {


    private Bitmap sheet;
    private int frameCount;
    private int counter = 0;
    private boolean horizontal;
    private Activity a;

    private Paint paint = new Paint();

    private int frameHeight, frameWidth, x, y;

    private Rect dst;

    private Dog.DogMood moodToSet;
    private int frameCountToSet;

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

    public void setSheet(Dog.DogMood mood){
        this.moodToSet = mood;
        this.frameCountToSet = mood.getFrames();
        new Thread(this).start();
    }

    public void update(){
        synchronized (this) {
            counter = (counter + 1) % frameCount;
        }
    }

    public int[] getPosition(){
        return new int[]{x, y};
    }

    /**
     * Sets the posistion of the sprite to wich it needs to be drawn to. De origin of the sprite is
     * middle x and y bottom. Needs to be called before the draw function gets calleld.
     * @param x of the position
     * @param y of the position
     */
    public void setXY(int x, int y){
        synchronized (this) {
            this.x = x;
            this.y = y;
        }
        dst = new Rect(this.x - frameWidth / 2, this.y - frameHeight, this.x + frameWidth / 2, this.y);
    }

    public void draw(Canvas c){
        synchronized (this) {
            Rect src;
            if (horizontal) {
                src = new Rect(counter * frameWidth, 0, counter * frameWidth + frameWidth, frameHeight);
            } else {
                src = new Rect(0, counter * frameHeight, frameWidth, counter * frameHeight + frameHeight);
            }
            c.drawBitmap(sheet, src, dst, paint);
            update();
        }
    }

    @Override
    public void run() {
        Bitmap b = BitmapLoader.loadBitmap(moodToSet, frameCountToSet);
        synchronized (this){
            this.sheet = b;
            this.frameCount = frameCountToSet;
            this.counter = 0;
        }
//        b.recycle();
    }
}
