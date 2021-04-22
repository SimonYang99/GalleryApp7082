package com.example.galleryapp7082;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_READ_PERMISSION_CODE = 101;
    private final ArrayList<File> files = new ArrayList<>();
    private View mLayout;
    private ImageView imageView;
    private int currentImageIndex;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.main_layout);
        imageView = findViewById(R.id.imageView);
        currentImageIndex = 0;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
        }else{
            getFiles();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == MY_READ_PERMISSION_CODE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "granted",
                        Snackbar.LENGTH_SHORT)
                        .show();
                getFiles();
                setImageView(0);
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "denied",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }
    private void setImageView(int next){
        if(currentImageIndex + next < files.size() && currentImageIndex + next >= 0){
            if(files.size() > 0){
                Log.d("yo", "currentImage: "+ currentImageIndex);
                currentImageIndex += next;
                Bitmap myBitmap = BitmapFactory.decodeFile(files.get(currentImageIndex).getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
                Log.d("yo", "currentImage: "+ currentImageIndex);
            } else{
                imageView.setImageBitmap(null);
            }

        }
    }
    public ArrayList<File> getFiles(){
        File[] filesArray;
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        files.clear();

        Log.d("yo", path);
        File imgFile = new File(path);
        filesArray = imgFile.listFiles();

        if(imgFile.exists()){
            for (int i = 0; i < filesArray.length; i++){
                if(filesArray[i].getName().endsWith(".png") || filesArray[i].getName().endsWith(".jpg")){
                    files.add(filesArray[i]);
                    Log.d("yo", "FileName:" + filesArray[i].getName());
                }
            }
            Log.d("yo", "Size: "+ files.size());
        }
        currentImageIndex = 0;
        setImageView(0);
        return files;
    }
    public ArrayList<File> getFiles(String filter){
        File[] filesArray;
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();

        files.clear();

        Log.d("yo", "STRING FILTER");
        File imgFile = new File(path);
        filesArray = imgFile.listFiles();


        if(imgFile.exists()){
            for (int i = 0; i < filesArray.length; i++){
                if(filesArray[i].getName().endsWith(".png") || filesArray[i].getName().endsWith(".jpg")){
                    if(filesArray[i].getName().contains(filter)){
                        files.add(filesArray[i]);
                        Log.d("yo", "FileName:" + filesArray[i].getName());
                    }
                }
            }
            Log.d("yo", "Size: "+ files.size());
        }
        currentImageIndex = 0;
        setImageView(0);
        return files;
    }
    public void leftClicked(View view) {
        setImageView(-1);
    }

    public void rightClicked(View view) {
        setImageView(+1);
    }

    public void searchButton(View view) {
        Intent intent = new Intent(this, SearchGalleryActivity.class );
        startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String word = (data.getStringExtra(SearchGalleryActivity.EXTRA_REPLY));
            getFiles(word);
        }
    }
}