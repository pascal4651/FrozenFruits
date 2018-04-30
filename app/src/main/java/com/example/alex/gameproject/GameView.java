package com.example.alex.gameproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alex on 20/04/2018.
 */

public class GameView extends SurfaceView implements Runnable, SensorEventListener {

    private boolean firstStart;
    private static boolean lightReset; // used to reset game objects if player lost life
    private static boolean fullReset; // used to reset the game
    private static boolean isBoosting;
    private Context context;

    private int screenX;
    private int screenY;

    volatile boolean running;
    private static boolean playGame, playSound;

    private Thread gameThread = null;
    private ArrayList<GameObject> objects;
    private static ArrayList<Score> scores;
    private ArrayList<Score> scoresToemove;
    private Bitmap originBitmap, backgroundBitmap;
    private Bitmap soundButton, soundOn, soundOff;
    // Game objects
    private GameObject player;

    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    long endTime;
    private float fps;
    private SensorManager sensorManager;
    private static float xAccel, yAccel;
    private static Random random;
    private int currentLevel;

    public GameView(Context context, int x, int y) {
        super(context);
        this.context = context;

        screenX = x;
        screenY = y;
        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        backgroundBitmap = Bitmap.createScaledBitmap(originBitmap, screenX, screenY, false);
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sound);
        soundButton = soundOn = Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * 0.3),
                (int)(originBitmap.getHeight() * 0.3), false);
        originBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.soundoff);
        soundOff = Bitmap.createScaledBitmap(originBitmap, (int)(originBitmap.getWidth() * 0.3),
                (int)(originBitmap.getHeight() * 0.3), false);
        random = new Random();
        objects = new ArrayList<>();
        scores = new ArrayList<>();
        scoresToemove = new ArrayList<>();
        currentLevel = MainActivity.getLevel();
        player = new Player(context, screenX, screenY, 0.3f);
        objects.add(new Ice(context, screenX, screenY, 0.5f));
        objects.add(new Ice(context, screenX, screenY, 0.5f));
        objects.add(new Ice(context, screenX, screenY, 0.5f));
        if(currentLevel == 2){
            objects.add(new Ice(context, screenX, screenY, 0.5f));
        }
        else if(currentLevel == 3){
            objects.add(new Ice(context, screenX, screenY, 0.5f));
            objects.add(new Ice(context, screenX, screenY, 0.5f));
        }
        objects.add(new Apple(context, screenX, screenY, 0.4f));
        objects.add(new Apple(context, screenX, screenY, 0.4f));
        objects.add(new Apple(context, screenX, screenY, 0.4f));
        objects.add(new Pear(context, screenX, screenY, 0.4f));
        objects.add(new Pear(context, screenX, screenY, 0.4f));
        objects.add(new Pear(context, screenX, screenY, 0.4f));
        objects.add(new Enemy(context, screenX, screenY, 0.3f));
        objects.add(player);
        isBoosting = lightReset = fullReset = playGame = false;
        firstStart = playSound = true;
    }

    @Override
    public void run() {
        while (running) {
            long startTime = System.currentTimeMillis();
            // Calculate the fps this frame
            long deltaTime = startTime - endTime > 1 ? startTime - endTime : 1;
            fps = 1000 / deltaTime;
            endTime = System.currentTimeMillis();
            update();
            draw();
        }
    }

    public void update(){
        if(lightReset){
            for (GameObject go : objects) {
                go.setupPosition();
            }
            scores.clear();
            scoresToemove.clear();
            lightReset = false;
        }
        else if(fullReset){
            for (GameObject go : objects) {
                if(go instanceof Player){
                    ((Player) go).fullSetupPosition();
                } else{
                    go.setupPosition();
                }
            }
            scores.clear();
            scoresToemove.clear();
            playGame = true;
            fullReset = false;
        }
        if(playGame){
            for (GameObject go : objects) {
                go.update(fps);
                go.checkCollision(objects);
            }
        }
        if(scoresToemove.size() > 0){
            for(Score s : scoresToemove){
                scores.remove(s);
            }
            scoresToemove.clear();
        }
        if(scores.size() > 0){
            for(Score s : scores){
                if(s.getIsInGame()){
                    s.update(fps);
                }
                else{
                    scoresToemove.add(s);
                }
            }
        }
    }

    public void draw() {

        if (ourHolder.getSurface().isValid()) {
            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();
            // Rub out the last frame
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawBitmap(backgroundBitmap, 0, 0, paint);

            for (GameObject go : objects) {
                go.draw(canvas, paint);
            }
            if(scores.size() > 0){
                for(Score s : scores){
                    s.draw(paint, canvas, screenY);
                }
            }
            canvas.drawBitmap(soundButton, screenX - soundButton.getWidth(), 0, paint);
            printData();
            if(!playGame){
                // Show pause screen
                paint.setColor(Color.argb(255, 235, 235, 255));
                paint.setTextAlign(Paint.Align.CENTER);
                if(firstStart){
                    paint.setTextSize(screenY/9);
                    canvas.drawText("FROZEN FRUITS", screenX/2, screenY/2, paint);
                }
                else{
                    paint.setTextSize(screenY/9);
                    canvas.drawText("GAME OVER", screenX/2, screenY/2, paint);
                }
                paint.setTextSize(screenY/22);
                canvas.drawText("Tap to start game!", screenX/2, screenY/2 + 100, paint);
            }

            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void printData(){
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(screenY/27);
        paint.setColor(Color.argb(255, 110, 255, 50));
        canvas.drawText("LEVEL:    " + currentLevel, screenX/197.4f, screenY/21.6f, paint);
        /*
        if(BuildConfig.DEBUG) {
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawText("FPS:" + fps, screenX/4, 50, paint);
        }*/
    }

    // Clean up our thread if the game is interrupted or the player quits
    public void pause() {
        running = false;
        sensorManager.unregisterListener(this);
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    // Make a new thread and start it
    // Execution moves to our R
    public void resume() {
        running = true;
        xAccel = yAccel = 0;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

        gameThread = new Thread(this);
        gameThread.start();
    }

    // SurfaceView allows us to handle the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        // There are many different events in MotionEvent
        // We care about just 2 - for now.
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Has the player lifted there finger up?
            case MotionEvent.ACTION_UP:
                //player.stopBoosting();
                isBoosting = false;
                break;

            // Has the player touched the screen?
            case MotionEvent.ACTION_DOWN:
                if(motionEvent.getX() > screenX - soundButton.getWidth() && motionEvent.getY() < soundButton.getHeight()){
                    playSound = !playSound;
                    soundButton = playSound ? soundOn : soundOff;
                }
                // If we are currently on the pause screen, start a new game
                else if(!playGame){
                    fullReset = true;
                    firstStart = false;
                }
                else {
                    isBoosting = true;
                }
                break;
        }
        return true;
    }

    @Override
    protected void onAttachedToWindow (){
        super.onAttachedToWindow();
        this.setKeepScreenOn(true);
    }

    @Override
    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        this.setKeepScreenOn(false);
    }

    public static boolean getIsBoosting(){
        return  isBoosting;
    }

    public static void setPlayGame(boolean value){
        playGame = value;
    }

    public static void setLightReset(boolean value){
        lightReset = value;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            xAccel = sensorEvent.values[1];
            yAccel = -sensorEvent.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    public static float getxAccel(){
        return xAccel;
    }

    public static int getRandom(int maxValue){
        return random.nextInt(maxValue);
    }

    public static void addScore(Score score){
        scores.add(score);
    }

    public static boolean getPlaySound(){
        return playSound;
    }

}
