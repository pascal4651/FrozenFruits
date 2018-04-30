package com.example.alex.gameproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Alex on 21/04/2018.
 */

public class Ice extends GameObject {
    private int direction;
    private Bitmap originBitmap;

    public Ice(Context context, int screenX, int screenY, float scaleFactor) {
        super(context, screenX, screenY, scaleFactor);
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ice);
        bitmap = Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false);

        maxX = screenX + bitmap.getWidth()/2;
        maxY = screenY/2;
        minX = -bitmap.getWidth() - bitmap.getWidth()/2;
        minY = 0;
        setupPosition();
    }

    @Override
    public void update(float fps){
        float translation = 1 / fps;

        if (x > maxX || x < minX) {
            setupPosition();
        }
        x += speed * translation * direction;
    }

    @Override
    public void onCollision(GameObject other) {

    }

    @Override
    public void setupPosition() {
        x = GameView.getRandom(2) == 0 ? minX - GameView.getRandom(200) : maxX + GameView.getRandom(200);
        y = GameView.getRandom((int)maxY) + bitmap.getHeight()/2;
        direction = x < 0 ? 1 : -1;
        speed = GameView.getRandom(200) + 200;
    }
}
