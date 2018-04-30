package com.example.alex.gameproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Alex on 22/04/2018.
 */

public class Score {

    private int value;
    private float speed;
    private float x, y, endY;
    private boolean isInGame;

    public Score(float x, float y, int value){
        this.value = value;
        this.x = x;
        this.y = y;
        endY = y - 100;
        speed = 50;
        isInGame = true;
    }

    public void update(float fps){
        float translation = 1 / fps;

        if (isInGame) {
            if (y > endY) {
                y -= speed * translation;
            }
            else {
                isInGame = false;
            }
        }
    }

    public void draw(Paint paint, Canvas canvas, float screenY){
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(screenY/27);
        if(value > 0){
            paint.setColor(Color.argb(255, 100, 255, 0));
            canvas.drawText("+" + Integer.toString(value), x, y, paint);
        }
        else{
            paint.setColor(Color.argb(255, 255, 200, 0));
            canvas.drawText(Integer.toString(value), x, y, paint);
        }
    }

    public boolean getIsInGame(){
        return isInGame;
    }
}
