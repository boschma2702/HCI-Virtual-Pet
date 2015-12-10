package example.com.virtualpet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by reneb_000 on 3-12-2015.
 */
public class DogView extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap test;
    private int x, y;


    public DogView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        getHolder().addCallback(this);
        test = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        x = y = 0;
    }

    public void onDraw(){
        Canvas c = getHolder().lockCanvas();
        if(c!=null) {
            c.drawARGB(255, 0,0,0);
            c.drawBitmap(test, x, y, null);
            getHolder().unlockCanvasAndPost(c);
        }
    }

    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
