package com.example.matianqiong.mycomponentdemo.service;

import android.content.Context;
import android.content.Intent;

import com.example.matianqiong.mycomponentdemo.db.DownLoadDAO;
import com.example.matianqiong.mycomponentdemo.db.ThreadInfoImpl;
import com.example.matianqiong.mycomponentdemo.entity.FileInfo;
import com.example.matianqiong.mycomponentdemo.entity.ThreadInfo;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by MaTianQiong on 2016/4/17.
 */
public class DownLoadTask {
    private Context context;
    private FileInfo fileInfo;
    private int mFinished;
    public boolean isPause=false;
    private DownLoadDAO downLoadDAO=null;
    public DownLoadTask(Context context,FileInfo fileInfo){
        this.context=context;
        this.fileInfo=fileInfo;
        downLoadDAO=new ThreadInfoImpl(context);
    }
    public void downLoad(){
        //读取数据库的线程信息
        List<ThreadInfo> threadInfos=downLoadDAO.queryThreadInfo(fileInfo.getUrl());
        ThreadInfo threadInfo=null;
        if (threadInfos.size()==0){
            threadInfo=new ThreadInfo(0,0,0,0,fileInfo.getUrl());
            downLoadDAO.insertThreadInfo(threadInfo);
        }else {
            threadInfo=threadInfos.get(0);
        }
        DownLoadThread downLoadThread=new DownLoadThread(threadInfo);
        downLoadThread.start();
    }
    /**
     * 下载线程
     */
    class DownLoadThread extends Thread{
        private ThreadInfo threadInfo;
        public DownLoadThread(ThreadInfo threadInfo){
            this.threadInfo=threadInfo;
        }
        public void run(){
            //向数据库插入线程信息

            if (!downLoadDAO.isExists(threadInfo.getUrl(),threadInfo.getId())){
                downLoadDAO.insertThreadInfo(threadInfo);
            }
            HttpURLConnection connection=null;
            RandomAccessFile randomAccessFile=null;
            InputStream inputStream=null;
            try {
                URL url=new URL(threadInfo.getUrl());
                connection= (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                //设置下载位置
                int start=threadInfo.getStart()+threadInfo.getFinished();
                connection.setRequestProperty("Range","bytes="+start+"-"+threadInfo.getEnd());
                //设置文件写入位置
                File file =new File(DownLoadService.DOWNLOAD_PATH,fileInfo.getFileName());
                 randomAccessFile=new RandomAccessFile(file,"rdw");
                randomAccessFile.seek(start);
                Intent intent=new Intent("Update_Action");
                mFinished+=threadInfo.getFinished();
                //开始下载文件
                if (connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    inputStream=connection.getInputStream();
                    byte[] bytes=new byte[1024*1024];
                    int len=-1;
                    long time=System.currentTimeMillis();
                    while((len=inputStream.read(bytes))!=-1){
                        randomAccessFile.write(bytes,0,len);
                    }
                    //把下载进度发送给广播
                    mFinished+=len;
                    if (System.currentTimeMillis()-time>=500){
                        time=System.currentTimeMillis();
                        intent.putExtra("mFinished",mFinished*100/fileInfo.getLength());
                        context.sendBroadcast(intent);
                    }
                    //在下载暂停时，保存下载进度
                    if (isPause){
                        downLoadDAO.updateThreadInfo(threadInfo.getUrl(),threadInfo.getId(),mFinished);
                        return;
                    }

                }
                //删除线程信息
                downLoadDAO.deleteThreadInfo(threadInfo.getUrl(),threadInfo.getId());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                connection.disconnect();
                try {
                    if (randomAccessFile!=null){
                        randomAccessFile.close();
                    }
                   if (inputStream!=null){
                       inputStream.close();
                   }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
