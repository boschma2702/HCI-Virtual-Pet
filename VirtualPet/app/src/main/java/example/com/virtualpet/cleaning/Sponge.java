package example.com.virtualpet.cleaning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import example.com.virtualpet.Util.ResourceManager;

/**
 * Created by reneb_000 on 9-1-2016.
 */
public class Sponge {

    private int x, y, width, height;
    private Bitmap bitmap;

    public Sponge(){
        bitmap = ResourceManager.INSTANCE.cleaning_sponge;
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        setXY(ResourceManager.INSTANCE.getScreenWidth()/2, ResourceManager.INSTANCE.getScreenHeight()/2);
    }

    public void setXY(int x, int y){
        this.x = x - width/2;
        this.y = y - width/2;
    }

    public void onDraw(Canvas c){
        c.drawBitmap(bitmap, x, y, null);
    }
}
