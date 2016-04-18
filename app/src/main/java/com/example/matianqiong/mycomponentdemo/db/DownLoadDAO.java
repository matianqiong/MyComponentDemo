package com.example.matianqiong.mycomponentdemo.db;

import com.example.matianqiong.mycomponentdemo.entity.ThreadInfo;

import java.util.List;

/**
 * Created by MaTianQiong on 2016/4/15.
 */
public interface DownLoadDAO {
    /**
     * 添加一个ThreadInfo
     * @param threadInfo
     */
    public void insertThreadInfo(ThreadInfo threadInfo);

    /**
     * 删除一个ThreadInfo
     * @param url
     * @param threadId
     */
    public void  deleteThreadInfo(String url,int threadId);

    /**
     * 更新线程的下载进度
     * @param url
     * @param threadId
     * @param finished
     */
    public void updateThreadInfo(String url,int threadId,int finished);

    /**
     * 查询url中的ThreadInfo
     * @param url
     * @return
     */
    public List<ThreadInfo> queryThreadInfo(String url);

    /**
     * 判断ThreadInfo是否存在
     * @param url
     * @param threadId
     * @return
     */
    public boolean isExists(String url,int threadId);
}
