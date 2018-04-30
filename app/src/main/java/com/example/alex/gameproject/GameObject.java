package com.example.alex.gameproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alex on 20/04/2018.
 */

public abstract class GameObject {
    protected Context context;
    protected Bitmap bitmap;
    protected float x, y, maxX, maxY, minX, minY;
    protected float speed;

    protected float screenX, screenY;
    protected float scaleFactor;

    public GameObject(Context context, int screenX, int screenY, float scaleFactor){
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;
        this.scaleFactor = scaleFactor;
    }

    public void draw(Canvas canvas, Paint paint){

        // Draw Hit box
        /*
        if(BuildConfig.DEBUG) {
            paint.setColor(Color.argb(100, 255, 255, 255));
            canvas.drawRect(getCollisionBox().left, getCollisionBox().top, getCollisionBox().right, getCollisionBox().bottom, paint);
        }*/
        paint.setColor(Color.argb(255, 255, 255, 255));
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    public void update(float fps){

    }

    public void checkCollision( ArrayList<GameObject> objects) {
        for (GameObject go : objects) {
        if (go != this) {
            if (this.isCollidingWith(go)) {
                onCollision(go);
            }
        }
    }
    }

    public Rect getCollisionBox(){
        return new Rect((int)x, (int)y, (int)(x + bitmap.getWidth()), (int)(y + bitmap.getHeight()));
    }

    public boolean isCollidingWith(GameObject other)
    {
        // Uses intersects with to determine if a collision is taking place
        return getCollisionBox().intersect(other.getCollisionBox());
    }

    public abstract void onCollision(GameObject other);

    public abstract void setupPosition();
}
