package com.example.matianqiong.mycomponentdemo.entity;

import java.io.Serializable;

/**
 * Created by MaTianQiong on 2016/4/14.
 */
public class ThreadInfo implements Serializable{
    private int id;
    private String url;
    private int start;
    private int end;
    private int finished;

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ThreadInfo(int end, int finished, int id, int start, String url) {
        this.end = end;
        this.finished = finished;
        this.id = id;
        this.start = start;
        this.url = url;
    }

    public ThreadInfo() {
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "end=" + end +
                ", id=" + id +
                ", url='" + url + '\'' +
                ", start=" + start +
                ", finished=" + finished +
                '}';
    }
}
