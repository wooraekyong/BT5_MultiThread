package com.wooraekyong.bt5multithread;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ProgressBar pbCounter;
    private TextView tvMessage;
    private Button btnCounter;
    private static final int MAX = 100000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pbCounter = (ProgressBar) findViewById(R.id.pb_Counter);
        pbCounter.setMax(MAX);

        tvMessage = (TextView) findViewById(R.id.tv_message);
        btnCounter = (Button) findViewById(R.id.btn_counter);
        btnCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateProgresBarTask().execute(MAX);
                btnCounter.setEnabled(false);
            }
        });


    }

    class UpdateProgresBarTask extends AsyncTask <Integer,Integer,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvMessage.setText("Update");
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int n=params[0];
            for (int i = 0; i<n;i++){
                publishProgress(i);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pbCounter.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tvMessage.setText("End");
            btnCounter.setEnabled(true);

            Intent intent = new Intent(MainActivity.this, CountUp.class);
            startActivity(intent);
        }
    }

}
