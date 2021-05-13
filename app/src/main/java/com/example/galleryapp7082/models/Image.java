package com.example.galleryapp7082.models;

import android.content.SharedPreferences;

import java.io.File;

public class Image implements ImageInterface {
    private SharedPreferences.Editor editor;
    private File file;
    private String caption;
    private String date;

    public Image(File file, String caption, String date, SharedPreferences.Editor sd){
        this.editor = sd;
        this.file = file;
        this.caption = caption;
        this.date = date;
    }

    public File getFile(){
        return this.file;
    }

    public void setCaption(String newCaption) {
        this.caption = newCaption;
        editor.putString(this.file.getName(), newCaption);
        editor.apply();
    }

    public String getCaption(){
        return this.caption;
    }

    public String getDate(){
        return this.date;
    }

}
