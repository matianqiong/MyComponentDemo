package com.example.matianqiong.mycomponentdemo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.matianqiong.mycomponentdemo.entity.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MaTianQiong on 2016/4/15.
 */
public class ThreadInfoImpl implements DownLoadDAO {
    private DBHelper mDBHelper;
    public ThreadInfoImpl(Context context){
        mDBHelper=new DBHelper(context);
    }
    @Override
    public void insertThreadInfo(ThreadInfo threadInfo) {
        SQLiteDatabase db=mDBHelper.getWritableDatabase();
        db.execSQL("insert into  thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)",
        new Object[]{threadInfo.getId(),threadInfo.getUrl(),threadInfo.getStart(),
                        threadInfo.getEnd(),threadInfo.getFinished()});
        db.close();
    }

    @Override
    public void deleteThreadInfo(String url, int threadId) {
        SQLiteDatabase db=mDBHelper.getWritableDatabase();
        db.execSQL("delete from thread_info where url=?and thread_id=?",
                new Object[]{url,threadId});
        db.close();

    }

    @Override
    public void updateThreadInfo(String url, int threadId, int finished) {
        SQLiteDatabase db=mDBHelper.getWritableDatabase();
        db.execSQL("update thread_info set finished=? where url=? and thread_id=?",
                new Object[]{finished,url,threadId});
        db.close();

    }

    @Override
    public List<ThreadInfo> queryThreadInfo(String url) {
        SQLiteDatabase db=mDBHelper.getWritableDatabase();
        List<ThreadInfo> list=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from thread_info where url=?",new String[]{url});
       while (cursor.moveToNext()){
           ThreadInfo threadInfo=new ThreadInfo();
           threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
           threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
           threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
           threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
           list.add(threadInfo);
       }
        db.close();
        cursor.close();
        return list;
    }

    @Override
    public boolean isExists(String url, int threadId) {
        SQLiteDatabase db=mDBHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from thread_info where url=? and thread_id=?",
                new String[]{url, threadId + ""});
        boolean exists=cursor.moveToNext();
        cursor.close();
        return exists;
    }
}
