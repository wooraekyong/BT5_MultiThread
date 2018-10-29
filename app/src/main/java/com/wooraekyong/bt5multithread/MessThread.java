package com.wooraekyong.bt5multithread;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

import static java.lang.Thread.*;

public class MessThread extends AppCompatActivity {
    ProgressBar pb_status;
    TextView tv_status,tv_display;
    Button btn_new;

    boolean isRunning = false;

    int MAX_sec=60;//(giây) chu kỳ cho background thread
    String strTest="Global value seen by all threads";
    int intTest=0;

    Handler myhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            String returnedvalue = (String)msg.obj;
            //sau khi nhận được giá trị từ background thread ta sẽ hiển thị giá trị ra textview display
            tv_display.setText("Returned by background thread: \n\n"+returnedvalue);
            //tăng giá trị progressbar lên 2 đơn vị
            pb_status.incrementProgressBy(2);
            //kt xem đến điểm kết thúc luồng chưa?
            if (pb_status.getProgress()==MAX_sec){
                tv_display.setText("Done \nback thread has been stopped");
                isRunning=false;
            }
            if (pb_status.getProgress()==pb_status.getMax()){
                tv_status.setText("Done");
                pb_status.setVisibility(View.INVISIBLE);
                btn_new.setVisibility(View.VISIBLE);
            }
            else {
                tv_status.setText("Working..."+pb_status.getProgress());
            }//kết thúc handler



        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_thread);

        pb_status=(ProgressBar)findViewById(R.id.pb_status);
        pb_status.setMax(MAX_sec);
        pb_status.setVisibility(View.INVISIBLE);

        tv_status=(TextView)findViewById(R.id.tv_status);
        tv_display=(TextView)findViewById(R.id.tv_display);

        btn_new=(Button)findViewById(R.id.btn_thread);
        btn_new.setVisibility(View.VISIBLE);

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_status.setVisibility(View.VISIBLE);
                pb_status.setProgress(0);
                strTest +="-01";
                intTest = 1;

                final Thread backgroundThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i=0; i<MAX_sec && isRunning;i++){
                                //ở đây ta ko thể dùng phương thức TOast để thay đổi UI được

                                Thread.sleep(1000);
                                Random randomValue=new Random();
                                String data="Thread Value: "+(int) randomValue.nextInt(101);
                                data +="\n" + strTest + " " + intTest;
                                intTest++;

                                Message msg=myhandler.obtainMessage(1,(String)data);

                                if (isRunning){
                                    myhandler.sendMessage(msg);
                                }


                            }
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }); //background

                isRunning=true;
                backgroundThread.start();
                pb_status.setVisibility(View.VISIBLE);
                btn_new.setVisibility(View.INVISIBLE);

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning=false;
    }
}
