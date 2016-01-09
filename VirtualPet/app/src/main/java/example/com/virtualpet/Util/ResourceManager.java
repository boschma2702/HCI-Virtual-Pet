package example.com.virtualpet.Util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;

import example.com.virtualpet.R;
import example.com.virtualpet.flapdog.FlapDog;

/**
 * Created by reneb_000 on 3-12-2015.
 */
public final class ResourceManager {

    public static ResourceManager INSTANCE;
    public static Activity a;

    private int screenWidth, screenHeight;
    private float dpscreenWidth, dpscreenHeight;
    private DisplayMetrics metrics;

    public BitmapFactory.Options options = new BitmapFactory.Options();

    public Bitmap flapdogBackground;
    public Bitmap flapdog_head;

    public SpriteSheet dogHappy;
    public double dogHeight;


    public ResourceManager(Activity a){
        DisplayMetrics metrics = new DisplayMetrics();
        a.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.metrics = metrics;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        dpscreenWidth = screenWidth/metrics.density;
        dpscreenHeight = screenHeight/metrics.density;
        this.a = a;
//        testSheet = BitmapFactory.decodeResource(a.getResources(), R.drawable.dead_normal);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;



        initBitmaps(a.getResources());

        flapdogBackground = getResizedBitmap(BitmapFactory.decodeResource(a.getResources(), R.drawable.flapdog_bg, options), screenWidth, screenHeight);
        int flapdog_width = (int)getPercentageLength(10, true);
        flapdog_head = getResizedBitmap(BitmapFactory.decodeResource(a.getResources(), R.drawable.flapdog_head, options), flapdog_width, flapdog_width);
        log();
        INSTANCE = this;
    }

    private void initBitmaps(Resources r) {
        Bitmap b;
        dogHeight = getPercentageLength(50, true);
        Log.e("heihgt", "Height is: "+dogHeight);

        b = BitmapFactory.decodeResource(r, R.drawable.dog_happy_f30, options);
//        addBitmapToMemoryCache("dogHappy", getResizedBitmap(b, (int) (b.getWidth() * (height * 30) / b.getHeight()), (int) (height * 30)));
        dogHappy = new SpriteSheet(getResizedBitmap(b, (int)(b.getWidth()*(dogHeight*30)/b.getHeight()), (int) (dogHeight*30)), 30, false);
        b.recycle();
//
//        b = BitmapFactory.decodeResource(r, R.drawable.dog_barking_f30, options);
//        addBitmapToMemoryCache("dogBarking", getResizedBitmap(b, (int) (b.getWidth() * (height * 30) / b.getHeight()), (int) (height * 30)));
//        //dogBarking = new SpriteSheet(getResizedBitmap(b, (int)(b.getWidth()*(height*30)/b.getHeight()), (int) (height*30)), 30, false);
//        b.recycle();
//
//        b = BitmapFactory.decodeResource(r, R.drawable.dog_playfull_f30, options);
//        addBitmapToMemoryCache("dogPlayfull", getResizedBitmap(b, (int) (b.getWidth() * (height * 30) / b.getHeight()), (int) (height * 30)));
//        //dogPlayfull = new SpriteSheet(getResizedBitmap(b, (int)(b.getWidth()*(height*30)/b.getHeight()), (int) (height*30)), 30, false);
//        b.recycle();
//
//        b = BitmapFactory.decodeResource(r, R.drawable.dog_sad_f30, options);
//        //dogSad = new SpriteSheet(getResizedBitmap(b, (int)(b.getWidth()*(height*30)/b.getHeight()), (int) (height*30)), 30, false);
//        addBitmapToMemoryCache("dogSad", getResizedBitmap(b, (int)(b.getWidth()*(height*30)/b.getHeight()), (int) (height*30)));
//        b.recycle();
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

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight(){
        return screenHeight;
    }
}
