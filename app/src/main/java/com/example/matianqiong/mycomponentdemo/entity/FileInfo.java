package com.example.matianqiong.mycomponentdemo.entity;

import java.io.Serializable;

/**
 * Created by MaTianQiong on 2016/4/14.
 */
public class FileInfo  implements Serializable{
    private int id;
    private String url;
    private String fileName;
    private  int finished;
    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FileInfo(String fileName, int finished, int id, String url,int length) {
        this.fileName = fileName;
        this.finished = finished;
        this.id = id;
        this.url = url;
        this.length=length;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileName='" + fileName + '\'' +
                ", id=" + id +
                ", url='" + url + '\'' +
                ", finished=" + finished +
                ", length=" + length +
                '}';
    }
}
