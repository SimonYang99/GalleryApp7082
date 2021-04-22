package com.example.galleryapp7082;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView photoImageView;
    String currentPhotoPath = null;

    private static final int MY_READ_PERMISSION_CODE = 101;
    private final ArrayList<File> files = new ArrayList<>();
    private View mLayout;
    private ImageView imageView;
    private int currentImageIndex;
    private TextView textView;
    private TextView timestamp;
    private EditText captionEditText;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private LocalDate time;
    String currTime = null ;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int NEW_TIME_ACTIVITY_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photoImageView = findViewById(R.id.imageView);
        mLayout = findViewById(R.id.main_layout);
        captionEditText = findViewById(R.id.editCaption);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        timestamp = findViewById(R.id.timeTextView);
        currentImageIndex = 0;
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

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
//                Log.d("yo", "currentImage: "+ currentImageIndex);
                currentImageIndex += next;
                setCaptionView();
                Bitmap myBitmap = BitmapFactory.decodeFile(files.get(currentImageIndex).getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
//                Log.d("yo", "currentImage: "+ currentImageIndex);
            } else{
                imageView.setImageBitmap(null);
            }

        }
    }
    public ArrayList<File> getFiles(){
        File[] filesArray;
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
        files.clear();

//        Log.d("yo", path);
        File imgFile = new File(path);
        filesArray = imgFile.listFiles();

        if(imgFile.exists()){
            for (int i = filesArray.length-1; i >= 0; i--){
                if(filesArray[i].getName().endsWith(".png") || filesArray[i].getName().endsWith(".jpg")){
                    files.add(filesArray[i]);
                    Log.d("yo", "FileName:" + filesArray[i].getName());
                }
            }
            if(filesArray.length > 0 && currTime != null) {
                editor.putString(files.get(currentImageIndex).getName() + 1, currTime);
                editor.apply();
            }
//            Log.d("yo", "Size: "+ files.size());
        }
        currentImageIndex = 0;
        setImageView(0);
        return files;
    }
    public void setCaptionView(){
//        Log.d("yo", "ran");
        textView.setText(sharedPref.getString(files.get(currentImageIndex).getName(), null));
        timestamp.setText(sharedPref.getString(files.get(currentImageIndex).getName() + 1, null));

    }
    public ArrayList<File> getFiles(String filter){
        File[] filesArray;
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();

        files.clear();

//        Log.d("yo", "STRING FILTER");
        File imgFile = new File(path);
        filesArray = imgFile.listFiles();


        if(imgFile.exists()){
            for (int i = filesArray.length-1; i >= 0; i--){
                if(filesArray[i].getName().endsWith(".png") || filesArray[i].getName().endsWith(".jpg")){
                    if(filesArray[i].getName().contains(filter)){
                        files.add(filesArray[i]);
                        Log.d("yo", "FileName:" + filesArray[i].getName());
                    }
                }
            }
            if(filesArray.length > 0 && currTime != null) {
                editor.putString(files.get(currentImageIndex).getName() + 1, currTime);
                editor.apply();
            }
//            Log.d("yo", "Size: "+ files.size());
        }
        currentImageIndex = 0;
        setImageView(0);
        return files;
    }

    public ArrayList<File> getFiles(String beforeTime, String afterTime){
        File[] filesArray;
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();

        files.clear();

//        Log.d("yo", "STRING FILTER");
        File imgFile = new File(path);
        filesArray = imgFile.listFiles();


        if(imgFile.exists()){
            for (int i = filesArray.length-1; i >= 0; i--){
                if(filesArray[i].getName().endsWith(".png") || filesArray[i].getName().endsWith(".jpg")){
                    if(filesArray[i].getName().contains(beforeTime)){
                        files.add(filesArray[i]);
                        Log.d("yo", "FileName:" + filesArray[i].getName());
                    }
                }
            }
            if(filesArray.length > 0 && currTime != null) {
                editor.putString(files.get(currentImageIndex).getName() + 1, currTime);
                editor.apply();
            }
//            Log.d("yo", "Size: "+ files.size());
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

    private File createImageFile() throws IOException {
        // Create an image file name
        currTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Log.d("josh", currTime);

        String imageFileName = currTime + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
//                editor.putString(files.get(currentImageIndex).getName() + 1, currTime);
//                editor.apply();
                Log.d("JOSH", currTime);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error with something", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            String word = (data.getStringExtra(SearchGalleryActivity.EXTRA_REPLY));
            getFiles(word);
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);

            getFiles();

        }


//            BitmapDrawable drawable = (BitmapDrawable) photoImageView.getDrawable();
//            Bitmap bitmap = drawable.getBitmap();
//
//            File sdCardDirectory = Environment.getExternalStorageDirectory();
//            File image = new File(sdCardDirectory, extras.get("data").toString());
//
//            boolean success = false;
//
//            // Encode the file as a PNG image.
//            FileOutputStream outStream;
//            try {
//
//                outStream = new FileOutputStream(image);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
//                /* 100 to keep full quality of the image */
//
//                outStream.flush();
//                outStream.close();
//                success = true;
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            if (success) {
//                Toast.makeText(getApplicationContext(), "Image saved with success",
//                        Toast.LENGTH_LONG).show();
//                photoImageView.setImageBitmap(imageBitmap);
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Error during image saving", Toast.LENGTH_LONG).show();
//            }
    }

    public void saveCaption(View view) {
//        Log.d("yo", String.valueOf(captionEditText.getText()));
        editor.putString(files.get(currentImageIndex).getName(), captionEditText.getText().toString());
        editor.apply();

        captionEditText.setText("");
        setCaptionView();
    }
}