package example.com.virtualpet.Util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;

import example.com.virtualpet.R;

/**
 * Created by reneb_000 on 3-12-2015.
 */
public final class ResourceManager {

    public static ResourceManager INSTANCE;

    private int screenWidth, screenHeight;
    private float dpscreenWidth, dpscreenHeight;
    private DisplayMetrics metrics;

    public Bitmap testSheet;

    public ResourceManager(Activity a){
        DisplayMetrics metrics = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.metrics = metrics;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        dpscreenWidth = screenWidth/metrics.density;
        dpscreenHeight = screenHeight/metrics.density;
        testSheet = BitmapFactory.decodeResource(a.getResources(), R.drawable.dead_normal);
        log();
        INSTANCE = this;
    }

    public float convertDpToPixel(float dp){
        return dp * (metrics.densityDpi / 160f);
    }

    public float convertPixelsToDp(float px){
        return px / (metrics.densityDpi / 160f);
    }

    /**
     * Calculates the percentage distance.
     * @param percentage needed, 100 = 100%.
     * @param height if length of height is needed or false for width of screen
     * @return distance in px
     */
    public double getPercentageLength(double percentage, boolean height){
        if(height){
            return (double)screenHeight*(percentage/100);
        }else{
            return (double)screenWidth*(percentage/100);
        }
    }

    private void log(){
        String s = "Recoursemanager initialized. \nscreenwidth: "+screenWidth + "\nscreenheight: "
                +screenHeight+"\ndpscreenwidth: "+dpscreenWidth+"\ndpscreenheight: "+dpscreenHeight;
        Log.d("RecourceManager", s);
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight(){
        return screenHeight;
    }
}
