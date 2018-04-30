package com.example.alex.gameproject;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Alex on 21/04/2018.
 */

public abstract class Fruit extends GameObject {

    protected boolean isFrozen;
    protected  int price;
    protected Bitmap originBitmapNormal, bitmapNormal, originBitmapFrozen, bitmapFrozen;

    public Fruit(Context context, int screenX, int screenY, float scaleFactor) {
        super(context, screenX, screenY, scaleFactor);
    }


    @Override
    public void setupPosition(){
        speed = GameView.getRandom(200) + 200;
        y = minY - GameView.getRandom(100);
        isFrozen = false;
    }
}
