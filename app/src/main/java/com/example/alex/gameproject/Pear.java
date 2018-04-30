package com.example.alex.gameproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Alex on 21/04/2018.
 */

public class Pear extends Fruit {
    public Pear(Context context, int screenX, int screenY, float scaleFactor) {
        super(context, screenX, screenY, scaleFactor);

        originBitmapNormal = BitmapFactory.decodeResource(context.getResources(), R.drawable.pear);
        bitmapNormal = Bitmap.createScaledBitmap(originBitmapNormal, (int)(originBitmapNormal.getWidth() * scaleFactor),
                (int)(originBitmapNormal.getHeight() * scaleFactor), false);
        originBitmapFrozen = BitmapFactory.decodeResource(context.getResources(), R.drawable.fpear);
        bitmapFrozen = Bitmap.createScaledBitmap(originBitmapFrozen, (int)(originBitmapFrozen.getWidth() * scaleFactor),
                (int)(originBitmapFrozen.getHeight() * scaleFactor), false);
        bitmap = bitmapNormal;

        maxX = screenX - bitmap.getWidth() * 3;
        maxY = screenY - screenY/9;
        minX = 0;
        minY = -bitmap.getHeight() * 2;
        setupPosition();
    }

    @Override
    public void update(float fps){
        float translation = 1 / fps;

        if (y > maxY) {
            setupPosition();
        }
        y += speed * translation;
    }

    @Override
    public void onCollision(GameObject other) {
        if(other instanceof Player)
        {
            ((Player)other).changeHealthAndScore(price);
            GameView.addScore(new Score(x + 5, y , price));
            setupPosition();
        }
        else if (other instanceof Ice && !isFrozen)
        {
            bitmap = bitmapFrozen;
            isFrozen = true;
            price *= -1;
        }
    }

    @Override
    public void setupPosition(){
        bitmap = bitmapNormal;
        price = 10;
        x = GameView.getRandom((int)maxX) + bitmap.getWidth();
        super.setupPosition();
    }
}
