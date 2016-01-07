package example.com.virtualpet.Util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import example.com.virtualpet.R;

/**
 * Created by reneb_000 on 7-1-2016.
 */
public class BitmapLoader {

    public static Bitmap loadBitmap(String name, int frames){
        double dogHeight = ResourceManager.INSTANCE.dogHeight;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        //Bitmap b = BitmapFactory.decodeResource(ResourceManager.a.getResources(), R.drawable.+"name", options);
//        return ResourceManager.getResizedBitmap(b, (int)(b.getWidth()*(dogHeight*frames)/b.getHeight()), (int) (dogHeight*frames));
        return null;
    }
}
