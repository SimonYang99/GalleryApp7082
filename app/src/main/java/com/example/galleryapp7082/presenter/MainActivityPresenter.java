package com.example.galleryapp7082.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.galleryapp7082.models.ImageInterface;

import java.util.ArrayList;

public class MainActivityPresenter {
    public ArrayList<ImageInterface> imageList = new ArrayList<>();
    private int currentImageIndex = 0;
    private TextView textView;
    private TextView timeView;
    private ImageView imageView;

    public MainActivityPresenter(TextView textView, TextView timeView, ImageView imageView){
        this.textView = textView;
        this.timeView = timeView;
        this.imageView = imageView;
    }
    public void clearImages() {
        imageList = new ArrayList<>();
        this.imageView.setImageBitmap(null);
        this.textView.setText(null);
        this.timeView.setText(null);

    }

    public void nextImage(int next) {
        if(this.currentImageIndex + next < imageList.size() && this.currentImageIndex + next >= 0){
            if(imageList.size() > 0){
                this.currentImageIndex += next;
                ImageInterface currentImage = this.imageList.get(this.currentImageIndex);
                // update info here
                updateViewInfo();
                Bitmap myBitMap = BitmapFactory.decodeFile(currentImage.getFile().getAbsolutePath());
                this.imageView.setImageBitmap((myBitMap));
            }
        }
    }

    public void printImageList(){
        for(int i = 0; i < imageList.size(); i++){
            Log.d("ImageList", "" + imageList.get(i).getCaption());
        }
    }

    public void updateViewInfo(){
        ImageInterface pic = getCurrentImage();
        this.textView.setText(pic.getCaption());
        this.timeView.setText(pic.getDate());
    }

    public ImageInterface getCurrentImage(){
        return this.imageList.get(currentImageIndex);
    }
}
