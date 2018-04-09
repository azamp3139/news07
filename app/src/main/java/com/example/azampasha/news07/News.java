package com.example.azampasha.news07;

/**
 * Created by AZAM PASHA on 26-03-2018.
 */






/**
 * Created by AZAM PASHA on 25-03-2018.
 */

public class News {
    private String mtitle;
    private String mauthor;
    private String mtime;
    private String murl;
    private String mimg;
    public News(String title,String author,String time,String url,String img){
        mtitle=title;
        mauthor=author;
        mtime=time;
        murl=url;
        mimg=img;


    }
    public String gettitle() {
        return mtitle;

    }
    public String getauthor() {
        return mauthor;

    }
    public String gettime() {
        String formattedDate=mtime.substring(0,10);
        String formatTime=mtime.substring(11,19);
        return formattedDate+"|"+formatTime;

    }
    public String geturl() {
        return murl;

    }
    public String getimg() {
        return mimg;

    }



}
