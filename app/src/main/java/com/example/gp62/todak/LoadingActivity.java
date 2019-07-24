package com.example.gp62.todak;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LoadingActivity extends AppCompatActivity {
    // 앱을 시작할 때 보이는 로딩화면

    Handler handler;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        thread = new Thread(runnable);
        handler= new Handler();
        thread.start();
        Log.e("스레드 시작 후, 스레드 상태",""+thread.isAlive());
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
                    Log.e("finish 후, 스레드 상태",""+thread.isAlive());
                }
            });
        }
    };
}
