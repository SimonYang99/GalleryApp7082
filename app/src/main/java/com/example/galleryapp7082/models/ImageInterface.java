package com.example.galleryapp7082.models;

import java.io.File;

public interface ImageInterface {
    public File getFile();
    public void setCaption(String newCaption);
    public String getCaption();
    public String getDate();
}
