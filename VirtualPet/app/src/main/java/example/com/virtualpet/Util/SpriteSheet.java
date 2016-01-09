package example.com.virtualpet.Util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private Paint debug = new Paint();

    private int x, y, desFrameHeight, desFrameWidth;
    private double frameHeight, frameWidth;

    private Rect dst;

    private Dog.DogMood moodToSet;
    private int frameCountToSet;

    public SpriteSheet(Bitmap spritesheet, int frameCount, boolean horizontal){
        if(horizontal){
            frameHeight = spritesheet.getHeight();
            frameWidth = (double)spritesheet.getWidth()/frameCount;
        }else{
            frameHeight = (double)spritesheet.getHeight()/frameCount;
            frameWidth = spritesheet.getWidth();
        }
        desFrameHeight = (int) (frameHeight*ResourceManager.SCALE);
        desFrameWidth = (int) (frameWidth*ResourceManager.SCALE);
        this.sheet = spritesheet;
        this.frameCount = frameCount;
        this.horizontal = horizontal;
        debug.setColor(Color.RED);
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
        dst = new Rect(this.x - desFrameWidth/2, this.y - desFrameHeight, this.x + desFrameWidth/2, this.y);
    }

    public void draw(Canvas c){
        synchronized (this) {
            Rect src;
            if (horizontal) {
                src = new Rect((int)(counter * frameWidth), 0, (int)(counter * frameWidth + frameWidth), (int)frameHeight);
            } else {
                src = new Rect(0, (int)(counter * frameHeight), (int)frameWidth, (int)(counter * frameHeight + frameHeight));
            }
            c.drawBitmap(sheet, src, dst, paint);
            //c.drawRect(dst, debug);
            update();
        }
    }

    public Bitmap getBitmap(){
        return sheet;
    }

    @Override
    public void run() {
        Bitmap b = BitmapLoader.loadBitmap(moodToSet, frameCountToSet);
        synchronized (this){
            this.sheet = b;
            this.frameCount = frameCountToSet;
            this.counter = 0;
            if(horizontal){
                frameHeight = b.getHeight();
                frameWidth = (double)b.getWidth()/frameCount;
            }else{
                frameHeight = (double)b.getHeight()/frameCount;
                frameWidth = b.getWidth();
            }
            desFrameHeight = (int) (frameHeight*ResourceManager.SCALE);
            desFrameWidth = (int) (frameWidth*ResourceManager.SCALE);
        }
//        b.recycle();
    }

    public int getFramecount() {
        return frameCount;
    }
}
