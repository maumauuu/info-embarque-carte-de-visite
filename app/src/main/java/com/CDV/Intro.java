package com.CDV;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Intro extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Intro.this, newContact.class));
                finish();
            }
        };

        Timer time =new Timer();
        time.schedule(task,5000);


    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }
}
