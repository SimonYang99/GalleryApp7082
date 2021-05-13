package com.example.galleryapp7082;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private static final int MY_WRITE_PERMISSION_CODE = 102;
    private static final int MY_LOCATION_CODE = 103;
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
    String currTime = null;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int LOCATION_CODE = 3;
    private FusedLocationProviderClient fusedLocationClient;

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_LOCATION_CODE);
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
//        }
        else {
            getFiles();
        }
    }

    public void onShare(View view) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(this,
                "com.example.android.fileprovider", files.get(currentImageIndex));
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share image using"));
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

    private void setImageView(int next) {
        if (currentImageIndex + next < files.size() && currentImageIndex + next >= 0) {
            if (files.size() > 0) {

                currentImageIndex += next;
                setCaptionView();
                Bitmap myBitmap = BitmapFactory.decodeFile(files.get(currentImageIndex).getAbsolutePath());
                imageView.setImageBitmap(myBitmap);

            } else {
                imageView.setImageBitmap(null);
            }

        }
    }

    public ArrayList<File> getFiles() {
        File[] filesArray;
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
        files.clear();


        File imgFile = new File(path);
        filesArray = imgFile.listFiles();

        if (imgFile.exists()) {
            for (int i = filesArray.length - 1; i >= 0; i--) {
                if (filesArray[i].getName().endsWith(".png") || filesArray[i].getName().endsWith(".jpg")) {
                    files.add(filesArray[i]);
//                    Log.d("yo", "FileName:" + filesArray[i].getName());
                }
            }
            if (filesArray.length > 0 && currTime != null) {
                editor.putString(files.get(currentImageIndex).getName() + "_timeStamp", currTime);
                editor.apply();
            }

        }
        currentImageIndex = 0;
        setImageView(0);
        return files;
    }
    public ArrayList<File> getFiles(String filter) {
        File[] filesArray;
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();

        files.clear();

        File imgFile = new File(path);
        filesArray = imgFile.listFiles();


        if (imgFile.exists()) {
            for (int i = filesArray.length - 1; i >= 0; i--) {
                if (filesArray[i].getName().endsWith(".png") || filesArray[i].getName().endsWith(".jpg")) {
                    if (filesArray[i].getName().contains(filter)) {
                        files.add(filesArray[i]);
//                        Log.d("yo", "FileName:" + filesArray[i].getName());
                    }
                }
            }

        }
        currentImageIndex = 0;
        setImageView(0);
        return files;
    }
    public ArrayList<File> getFiles(String beforeTime, String afterTime) {
        File[] filesArray;
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();

        files.clear();

        File imgFile = new File(path);
        filesArray = imgFile.listFiles();


        if (imgFile.exists()) {
            for (int i = filesArray.length - 1; i >= 0; i--) {
                if (filesArray[i].getName().endsWith(".png") || filesArray[i].getName().endsWith(".jpg")) {
                    if (filesArray[i].getName().contains(beforeTime)) {
                        files.add(filesArray[i]);
                        Log.d("yo", "FileName:" + filesArray[i].getName());
                    }
                }
            }

        }
        currentImageIndex = 0;
        setImageView(0);
        return files;
    }
    public ArrayList<File> getFilesLocation(String longitude, String latitude) {
        File[] filesArray;
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();

        files.clear();

        File imgFile = new File(path);
        filesArray = imgFile.listFiles();


        if (imgFile.exists()) {
            for (int i = filesArray.length - 1; i >= 0; i--) {
                if (filesArray[i].getName().endsWith(".jpg")) {
                    Log.d("JOSH", "GET FILES VERSION : " + filesArray[i].getName() + "_Longitude");
                    if ((sharedPref.getString(filesArray[i].getName() + "_Longitude", null).contains(longitude)) && (sharedPref.getString(filesArray[i].getName() + "_Latitude", null)).contains(latitude)) {
                        files.add(filesArray[i]);
                        Log.d("yo", "FileName:" + filesArray[i].getName());
                    }
                }
            }

        }
        currentImageIndex = 0;
        setImageView(0);
        return files;
    }

    public void setCaptionView() {

        textView.setText(sharedPref.getString(files.get(currentImageIndex).getName() + "_caption", null));
        timestamp.setText(sharedPref.getString(files.get(currentImageIndex).getName() + "_timeStamp", null));
        Log.d("JOSH", "TIMESTAMP VERSION" + files.get(currentImageIndex).getName() + "_timeStamp");

    }

    public void leftClicked(View view) {
        setImageView(-1);
    }

    public void rightClicked(View view) {
        setImageView(+1);
    }

    public void searchButton(View view) {
        Intent intent = new Intent(this, SearchGalleryActivity.class);
        startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        currTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String imageFileName = currTime + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();

        //Save Timestamp
        editor.putString(imageFileName + "_timeStamp", currTime);
        Log.d("JOSH", "OTHER VERSION TIME: " + imageFileName + "_timeStamp");
        editor.apply();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, MY_LOCATION_CODE);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        Log.d("josh", "LOCATION: " + location);
                        if (location != null) {
                            // Logic to handle location object
//                            Log.d("josh", "RAN");
                            Log.d("JOSH", "OTHER VERSION: " + image.getName()+ "_Longitude");
                            editor.putString(image.getName() + "_Longitude", "" + location.getLongitude());
                            editor.apply();
                            editor.putString(image.getName() + "_Latitude", "" + location.getLatitude());
                            editor.apply();

                        }
                    }
                });

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
            Log.d("josh", "ACTIVITYRAN");
            Log.d("josh", "" + resultCode);

            if(data.getStringExtra(SearchGalleryActivity.LONGITUDE_REPLY)!= null){
                String longitude = (data.getStringExtra(SearchGalleryActivity.LONGITUDE_REPLY));
                String latitude = (data.getStringExtra(SearchGalleryActivity.LATITUDE_REPLY));
                Log.d("josh", "latitude");
                Log.d("josh", latitude);
                getFilesLocation(longitude, latitude);

            }else if(data.getStringExtra(SearchGalleryActivity.BEFORE_REPLY)!= null){
                String before = (data.getStringExtra(SearchGalleryActivity.BEFORE_REPLY));
                String after = (data.getStringExtra(SearchGalleryActivity.AFTER_REPLY));
                Log.d("josh", "before");
                Log.d("josh", after);
                getFiles(before, after);

            }else if(data.getStringExtra(SearchGalleryActivity.KEYWORD_REPLY)!= null){
                Log.d("josh", "keyword");
                String word = (data.getStringExtra(SearchGalleryActivity.KEYWORD_REPLY));
                getFiles(word);
            }
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            getFiles();
        }
        else if (requestCode == LOCATION_CODE && resultCode == RESULT_OK) {
            String longitude = (data.getStringExtra(SearchGalleryActivity.LONGITUDE_REPLY));
            String latitude = (data.getStringExtra(SearchGalleryActivity.LATITUDE_REPLY));
            getFilesLocation(longitude, latitude);

        }
    }

    public void saveCaption(View view) {
//        Log.d("yo", String.valueOf(captionEditText.getText()));
        editor.putString(files.get(currentImageIndex).getName() + "_caption", captionEditText.getText().toString());
        editor.apply();

        captionEditText.setText("");
        setCaptionView();
    }
}