package example.com.virtualpet.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import example.com.virtualpet.Dog;

/**
 * Created by reneb_000 on 7-1-2016.
 */
public class BitmapLoader {

    public static Bitmap loadBitmap(Dog.DogMood mood, int frames){
//        double dogHeight = ResourceManager.INSTANCE.dogHeight;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
//        options.inDither = true;
//        Bitmap b = BitmapFactory.decodeResource(ResourceManager.a.getResources(), mood.getRes(), options);
        Log.e("Bitmapload", "loading img: " + mood.toString());
        Bitmap b = BitmapFactory.decodeResource(ResourceManager.a.getResources(), mood.getRes(), ResourceManager.INSTANCE.options);
//        return ResourceManager.getResizedBitmap(b, (int)(b.getWidth()*(dogHeight*frames)/b.getHeight()), (int) (dogHeight*frames));
        return b;
    }
}
