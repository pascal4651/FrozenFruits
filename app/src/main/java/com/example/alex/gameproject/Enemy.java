package com.example.alex.gameproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

/**
 * Created by Alex on 23/04/2018.
 */

public class Enemy extends GameObject {
    private int direction;
    private Bitmap originBitmap;
    private float currentIndex;
    private int animationSpeed;
    private ArrayList<Bitmap> animationFrames;

    public Enemy(Context context, int screenX, int screenY, float scaleFactor) {
        super(context, screenX, screenY, scaleFactor);
        animationFrames = new ArrayList<>();
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.snowball);
        animationFrames.add(Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false));
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.snowballtwo);
        animationFrames.add(Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false));
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.snowballthree);
        animationFrames.add(Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false));
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.snowballfour);
        animationFrames.add(Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false));
        bitmap = animationFrames.get(0);

        maxX = screenX + bitmap.getWidth()/2;
        maxY = screenY/2;
        minX = -bitmap.getWidth() - bitmap.getWidth()/2;
        minY = 0;
        animationSpeed = 10;
        setupPosition();
    }

    @Override
    public void update(float fps){
        float translation = 1 / fps;

        if (x > maxX || x < minX) {
            setupPosition();
        }
        x += speed * translation * direction;
        updateAnimation(translation);
    }

    public void updateAnimation(float translation)
    {
        // Claculates the current index
        currentIndex += translation * animationSpeed;
        // Checks if we need to reset the animation
        if (currentIndex >= animationFrames.size())
        {
            currentIndex = 0;
        }
        // Changes the sprite
        bitmap = animationFrames.get((int)currentIndex);
    }

    @Override
    public void onCollision(GameObject other) {

    }

    @Override
    public void setupPosition() {
        x = GameView.getRandom(2) == 0 ? minX - GameView.getRandom(200): maxX + GameView.getRandom(200);
        y = screenY - screenY/6;
        direction = x < 0 ? 1 : -1;
        speed = GameView.getRandom(100) + 100;
    }
}
