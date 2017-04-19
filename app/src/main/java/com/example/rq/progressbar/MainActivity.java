package com.example.rq.progressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private JiKeProgressBar mJiKeProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mJiKeProgressBar = (JiKeProgressBar)findViewById(R.id.pb);
        mJiKeProgressBar.setClickable(true);
        mJiKeProgressBar.setJump(true);
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0 ; i < 100 ; i++) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mJiKeProgressBar.setRatio(finalI /100.0f);
                        }
                    });
                }
            }
        }).start();
    }
}
