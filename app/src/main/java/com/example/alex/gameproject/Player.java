package com.example.alex.gameproject;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alex on 20/04/2018.
 */

public class Player extends GameObject {
    private int health;
    private int score;
    private float xAccel;
    private boolean isDead;
    private int lifesAmount;
    private boolean isOnjump;
    private Bitmap originBitmap, bitmapLabel;
    //For the FX
    private SoundPool soundPool;
    int win = -1;
    int lose = -1;
    int die = -1;
    private float currentIndex;
    private int animationSpeed;
    private ArrayList<Bitmap> animationFrames;

    public Player(Context context, int screenX, int screenY, float scaleFactor){
        super(context, screenX, screenY, scaleFactor);
        animationFrames = new ArrayList<>();
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.yetiidle);
        animationFrames.add(Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false));
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fyeti);
        animationFrames.add(Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false));
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.yetione);
        animationFrames.add(Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false));
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.yetitwo);
        animationFrames.add(Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false));
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.yetithree);
        animationFrames.add(Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false));
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.yetifour);
        animationFrames.add(Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false));
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.yetifive);
        animationFrames.add(Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false));
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.yetisix);
        animationFrames.add(Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false));
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.yetilabel);
        bitmapLabel = Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * scaleFactor),
                (int)(originBitmap.getHeight() * scaleFactor), false);
        bitmap = animationFrames.get(0);

        speed = 400;
        animationSpeed = 10;
        xAccel = 0;

        maxX = screenX - bitmap.getWidth();
        maxY = screenY - screenY/5.2f;
        minX = 0;
        minY = screenY/8;

        fullSetupPosition();

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        try{
            //Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            //create our three fx in memory ready for use
            descriptor = assetManager.openFd("pindrop.wav");
            win = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("hiccup.mp3");
            lose = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("grunt.mp3");
            die = soundPool.load(descriptor, 0);
        }catch(IOException e){
            //Print an error message to the console
            Log.e("error", "failed to load sound files");
        }
    }

    @Override
    public void update(float fps) {

        float translation = 1 / fps;

        if (health <= 0 && !isDead) {
            isDead = true;
            bitmap = animationFrames.get(1);
            if(GameView.getPlaySound()){
                soundPool.play(die, 1, 1, 0, 0, 1);
            }
        }
        if (isDead) {
            if (y > screenY + 20) {
                if (lifesAmount > 0) {
                    lifesAmount--;
                    GameView.setLightReset(true);
                }
                else {
                    GameView.setPlayGame(false);
                }
            } else{
                y += speed * translation;
            }
        }
        else {
            xAccel = GameView.getxAccel();
            x += xAccel * speed * translation;
            //y += yAccel * speed * translation;
            if (x > maxX) {
                x = maxX;
            } else if (x < minX) {
                x = minX;
            }
            if (isOnjump) {
                if(GameView.getIsBoosting()){
                    isOnjump = false;
                }
                else if (y > minY) {
                    y -= speed * translation * 2;
                }
                else {
                    isOnjump = false;
                }
            }
            else if (y < maxY) {
                y += speed * translation * 2;
                if(y > maxY){
                    y = maxY;
                    currentIndex = 2;
                }
            }
            if (GameView.getIsBoosting() && !isOnjump && y >= maxY) {
                isOnjump = true;
            }
            if(xAccel > 0.3){
                if(y >= maxY){
                    updateAnimation(translation);
                }
                else{
                    bitmap = animationFrames.get(2);
                }            }
            else if(xAccel < -0.3){
                if(y >= maxY){
                    updateAnimation(translation);
                    flipBitmap();
                }
                else{
                    bitmap = animationFrames.get(2);
                    flipBitmap();
                }
            }
            else{
                bitmap = animationFrames.get(0);
            }
        }
    }

    public void flipBitmap(){
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void updateAnimation(float translation)
    {
        // Claculates the current index
        currentIndex += translation * (animationSpeed + xAccel * 2) ;
        // Checks if we need to reset the animation
        if (currentIndex >= animationFrames.size())
        {
            currentIndex = 2;
        }
        // Changes the sprite
        bitmap = animationFrames.get((int)currentIndex);
    }

    @Override
    public void onCollision(GameObject other) {
        if (!isDead) {
            if (other instanceof Ice || other instanceof Enemy)
            {
                isDead = true;
                bitmap = animationFrames.get(1);
                if(GameView.getPlaySound()){
                    soundPool.play(die, 1, 1, 0, 0, 1);
                }
            }
        }
    }

    @Override
    public void setupPosition(){
        bitmap = animationFrames.get(0);
        x = screenX/2 - bitmap.getWidth()/2;
        y = maxY;
        health = 100;
        isOnjump = false;
        isDead = false;
        currentIndex = 2;
    }

    public void fullSetupPosition(){
        lifesAmount = 5;
        score = 0;
        setupPosition();
    }

    public void changeHealthAndScore(int value)
    {
        if (!isDead) {
            if (value > 0) {
                score += value;
                if(GameView.getPlaySound()){
                    soundPool.play(win, 1, 1, 0, 0, 1);
                }
            }
            else {
                health += value;
                if (health < 0) {
                    health = 0;
                }
                if(GameView.getPlaySound()){
                    soundPool.play(lose, 1, 1, 0, 0, 1);
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint){
        super.draw(canvas, paint);

        paint.setColor(Color.argb(100, 0, 0, 100));
        canvas.drawRect(5, 5, screenX/6, screenY/5, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(screenY/27);
        paint.setColor(Color.argb(255, 245, 255, 170));
        canvas.drawText("SCORE:   " + score, screenX/197.4f, screenY/10.8f, paint);
        canvas.drawText("LIFES:", screenX/197.4f, screenY/7.2f, paint);
        canvas.drawText("HEALTH: " + health, screenX/197.4f, screenY/5.4f, paint);
        float xl = screenX/11;
        for (int i = 0; i < lifesAmount; i++)
        {
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawBitmap(bitmapLabel, xl, screenY/9, paint);
            xl += screenX/95;
        }
    }
}
