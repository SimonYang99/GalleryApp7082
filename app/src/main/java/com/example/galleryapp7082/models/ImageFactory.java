package com.example.galleryapp7082.models;

import android.content.SharedPreferences;

import java.io.File;

public class ImageFactory {
    public ImageInterface createImage(File file, String caption, String date, SharedPreferences.Editor sd){
        return new Image(file, caption, date, sd);
    }
}
