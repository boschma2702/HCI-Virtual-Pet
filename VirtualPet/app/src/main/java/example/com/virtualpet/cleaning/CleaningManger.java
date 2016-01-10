package example.com.virtualpet.cleaning;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reneb_000 on 9-1-2016.
 */
public class CleaningManger {

    private Sponge sponge;
    private List<Stain> toDrawList = new ArrayList<>();
    private List<Stain> stains = new ArrayList<>();
    private int dogx, dogy, dogWidth, dogHeight;

    private boolean drawSponge = false;

    public CleaningManger(int dogx, int dogy, int dogWidth, int dogHeight){
        sponge = new Sponge();
        int h = (int) (dogHeight*0.1);
        toDrawList.add(new Stain(dogx-dogWidth/3, dogy-(dogHeight/3)*2+h));
        toDrawList.add(new Stain(dogx, dogy-dogHeight/2+h));
        toDrawList.add(new Stain(dogx+dogWidth/3, dogy-dogHeight/3+h));
        stains.addAll(toDrawList);
    }

    public void activate(){
        drawSponge = false;
    }

    public void reset(){
        for(Stain s:stains){
            s.reset();
        }
        toDrawList.addAll(stains);
    }

    public void onDraw(Canvas c){
        for(Stain s:toDrawList){
            s.onDraw(c);
        }
        if(drawSponge) {
            sponge.onDraw(c);
        }
    }

    public boolean onTouch(int x, int y){
        List<Stain> toDelete = new ArrayList<>();
        sponge.setXY(x, y);
        for(Stain s:toDrawList){
            if(s.clean(x, y)){
                toDelete.add(s);
            }
        }
        toDrawList.removeAll(toDelete);
        if(toDrawList.size()==0){
            drawSponge = false;
            return true;
        }
        return false;
    }
}
