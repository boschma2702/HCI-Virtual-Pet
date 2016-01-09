package example.com.virtualpet.cleaning;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import example.com.virtualpet.Util.ResourceManager;

/**
 * Created by reneb_000 on 9-1-2016.
 */
public class Stain {

    private Paint paint = new Paint();
    private int x, y;
    private Rect hitBox;
    private Bitmap bitmap;
    private Paint debug = new Paint();

    public Stain(int x, int y){
        bitmap = ResourceManager.INSTANCE.cleaning_stain;
        this.x = x - bitmap.getWidth()/2;
        this.y = y - bitmap.getHeight()/2;
        Log.e("stainpos", "x: " + this.x + "y: "+this.y);
        hitBox = new Rect(x-bitmap.getWidth()/2, y-bitmap.getHeight()/2, x+bitmap.getWidth()/2, y+bitmap.getHeight()/2);
        debug.setColor(Color.RED);
    }

    public void onDraw(Canvas c){
        c.drawBitmap(bitmap, x, y, paint);
        //c.drawRect(hitBox, debug);
    }

    public boolean clean(int x, int y){
        if(hitBox.contains(x, y)){
            if(paint.getAlpha()-5>0){
                paint.setAlpha(paint.getAlpha()-5);
            }else{
                Log.e("stain", "clean");
                return true;
            }
        }
        return false;
    }

    public void reset() {
        paint.setAlpha(255);
    }
}
