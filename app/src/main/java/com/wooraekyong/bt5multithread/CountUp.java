package com.wooraekyong.bt5multithread;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CountUp extends AppCompatActivity {

    private Handler mHandler;
    private static final int MSG_UPDATE_NUMBER = 100;
    private static final int MSG_UPDATE_NUMBER_DONE = 101;

    private TextView mTextNumber;
    private Button button_count;
    private boolean mIsCouting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_up);
        initViews();

        button_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countNumbers();
                listenerHandler();
            }
        });


    }

    private void initViews(){
        mTextNumber = findViewById(R.id.text_number);
        button_count = (Button) findViewById(R.id.button_count);

    }

    private void countNumbers(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i <= 10;i++){
                    Message message = new Message();
                    message.what = MSG_UPDATE_NUMBER;
                    message.arg1 = i;
                    mHandler.sendMessage(message);
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

                mHandler.sendEmptyMessage(MSG_UPDATE_NUMBER_DONE);


            }
        }).start();
    }


    private void listenerHandler(){
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_UPDATE_NUMBER:
                        mIsCouting = true;
                        mTextNumber.setText(String.valueOf(msg.arg1));
                        break;
                    case MSG_UPDATE_NUMBER_DONE:
                        mTextNumber.setText("Count Done!");
                        mIsCouting = false;
                        Intent intent_mess_thread = new Intent(CountUp.this, MessThread.class);
                        startActivity(intent_mess_thread);
                        break;
                    default:
                        break;
                }
            }
        };
    }



}
