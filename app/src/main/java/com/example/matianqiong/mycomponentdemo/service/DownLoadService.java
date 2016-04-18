package com.example.matianqiong.mycomponentdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.matianqiong.mycomponentdemo.entity.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MaTianQiong on 2016/4/15.
 */
public class DownLoadService extends Service {
    private final String Action_Start="Start_Action";
    private final  String Action_End="End_Action";
    public static final String Action_Update="Update_Action";
    private final  int MSG_WATE=1;
    public final static  String DOWNLOAD_PATH=Environment.getExternalStorageDirectory().
            getAbsolutePath()+"/downloads/";
    private DownLoadTask mTask=null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Action_Start.equals(intent.getAction())){
           FileInfo fileInfo= (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.i("test","start:"+fileInfo.toString());
            new InitThread(fileInfo).start();
        }else if (Action_End.equals(intent.getAction())){
            FileInfo fileInfo= (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.i("test","end:"+fileInfo.toString());
            if (mTask!=null){
                mTask.isPause=true;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
    class InitThread extends Thread{
        private FileInfo mfileInfo;
        public InitThread(FileInfo fileInfo){
            this.mfileInfo=fileInfo;
        }
        public void run(){
            HttpURLConnection connection=null;
            RandomAccessFile rad=null;
            try {
                //连接网络文件
                URL mUrl=new URL(mfileInfo.getUrl());
                connection= (HttpURLConnection) mUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
                int length=-1;
                if (connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    //获取文件长度
                    length=connection.getContentLength();
                }
                if (length<=0){
                    return;
                }
                File dir=new File(DOWNLOAD_PATH);
                if (!dir.exists()){
                    dir.mkdir();
                }
                File file=new File(dir,mfileInfo.getFileName());
                 rad=new RandomAccessFile(file,"rwd");
                rad.setLength(length);
                mfileInfo.setLength(length);
                mHandle.obtainMessage(MSG_WATE,mfileInfo).sendToTarget();

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                connection.disconnect();
                try {
                    if (rad!=null){
                        rad.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    Handler mHandle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_WATE:
                    FileInfo fileInfo=(FileInfo)msg.obj;
                    Log.i("fileinfo",fileInfo.toString());
                    mTask=new DownLoadTask(DownLoadService.this,fileInfo);
                    mTask.downLoad();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
