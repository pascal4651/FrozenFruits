package com.example.alex.gameproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener{

    private ImageView buttonLevel1, buttonLevel2, buttonLevel3, buttonAbout, buttonHow, buttonExit;
    private static int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLevel1 = findViewById(R.id.imageViewLevel1);
        buttonLevel2 = findViewById(R.id.imageViewLevel2);
        buttonLevel3 = findViewById(R.id.imageViewLevel3);
        buttonAbout = findViewById(R.id.imageViewAbout);
        buttonHow = findViewById(R.id.imageViewHow);
        buttonExit = findViewById(R.id.imageViewExit);
        // Listen for clicks
        buttonLevel1.setOnClickListener(this);
        buttonLevel2.setOnClickListener(this);
        buttonLevel3.setOnClickListener(this);
        buttonHow.setOnClickListener(this);
        buttonAbout.setOnClickListener(this);
        buttonExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.imageViewLevel1:
                level = 1;
                i = new Intent(this, GameActivity.class);
                // Start our GameActivity class via the Intent
                startActivity(i);
                break;
            case R.id.imageViewLevel2:
                level = 2;
                i = new Intent(this, GameActivity.class);
                // Start our GameActivity class via the Intent
                startActivity(i);
                break;
            case R.id.imageViewLevel3:
                level = 3;
                i = new Intent(this, GameActivity.class);
                // Start our GameActivity class via the Intent
                startActivity(i);
                break;
            case R.id.imageViewAbout:
                i = new Intent(this, AboutActivity.class);
                // Start our GameActivity class via the Intent
                startActivity(i);
                break;
            case R.id.imageViewHow:
                i = new Intent(this, HowToPlayActivity.class);
                // Start our GameActivity class via the Intent
                startActivity(i);
                break;
            case R.id.imageViewExit:
                finish();
                System.exit(0);
        }
    }

    public static int getLevel(){
        return  level;
    }
}
