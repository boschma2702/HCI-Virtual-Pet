package example.com.virtualpet;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by reneb_000 on 3-12-2015.
 */
public final class ResourceManager {

    public static ResourceManager INSTANCE;

    private int screenWidth, screenHeight;
    private DisplayMetrics metrics;

    public ResourceManager(Activity a){
        DisplayMetrics metrics = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.metrics = metrics;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        log();
        INSTANCE = this;
    }

    public float convertDpToPixel(float dp){
        return dp * (metrics.densityDpi / 160f);
    }

    public float convertPixelsToDp(float px){
        return px / (metrics.densityDpi / 160f);
    }

    private void log(){
        String s = "Recoursemanager initialized. \nscreenwidth: "+screenWidth + "\nscreenheight: "+screenHeight;
        Log.d("RecourceManager", s);
    }

    public int getScreenwidht() {
        return screenWidth;
    }
}
