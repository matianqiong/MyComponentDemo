package com.example.matianqiong.mycomponentdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.matianqiong.mycomponentdemo.entity.FileInfo;
import com.example.matianqiong.mycomponentdemo.service.DownLoadService;

public class MainActivity extends AppCompatActivity {

    private TextView tvFileName;
    private ProgressBar pbProgress;
    private Button btnStop;
    private Button btnLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        final FileInfo fileInfo=new FileInfo("cloudmusicsetup_2_0_2[128316].exe",0,0,"http://s1.music.126.net/download/pc/cloudmusicsetup_2_0_2[128316].exe",0);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, DownLoadService.class);
                intent.setAction("Start_Action");
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, DownLoadService.class);
                intent.setAction("Stop_Action");
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
            }
        });
        IntentFilter filter=new IntentFilter();
        filter.addAction(DownLoadService.Action_Update);
        registerReceiver(receiver,filter);
    }
    private void initView(){
        tvFileName= (TextView) findViewById(R.id.tv_fileName);
        pbProgress= (ProgressBar) findViewById(R.id.pb_progress);
        pbProgress.setMax(100);
        btnLoad= (Button) findViewById(R.id.btn_load);
        btnStop= (Button) findViewById(R.id.btn_stop);
    }
    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownLoadService.Action_Update.equals(intent.getAction())){

                int finished=intent.getIntExtra("mFinished", 0);
                Log.i("finished==",finished+"");
                pbProgress.setProgress(finished);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
