package com.example.galleryapp7082.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.example.galleryapp7082.R;
import com.example.galleryapp7082.models.Image;
import com.example.galleryapp7082.models.ImageFactory;
import com.example.galleryapp7082.models.ImageInterface;
import com.example.galleryapp7082.presenter.MainActivityPresenter;
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
    private static final int MY_READ_LOCATION_CODE = 102;
    private final String SD_PATH = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
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
    private ImageFactory imageFactory;
    String currTime = null;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private FusedLocationProviderClient fusedLocationClient;


    //test
    public MainActivityPresenter manager;

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
        imageFactory = new ImageFactory();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        manager = new MainActivityPresenter(textView, timestamp, imageView);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
//        } else {
//            getFiles();
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_READ_LOCATION_CODE);
//        }

    }

    public void onShare(View view) {
        manager.printImageList();
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
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "denied",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }else if (requestCode == MY_READ_LOCATION_CODE){

        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

//    private void setImageView(int next) {
//        if (currentImageIndex + next < files.size() && currentImageIndex + next >= 0) {
//            if (files.size() > 0) {
////                Log.d("yo", "currentImage: "+ currentImageIndex);
//                currentImageIndex += next;
//                setCaptionView();
//                Bitmap myBitmap = BitmapFactory.decodeFile(files.get(currentImageIndex).getAbsolutePath());
//                imageView.setImageBitmap(myBitmap);
////                Log.d("yo", "currentImage: "+ currentImageIndex);
//            } else {
//                imageView.setImageBitmap(null);
//            }
//
//        }
//    }

    public ArrayList<File> getFiles() {
        File[] filesArray;
        files.clear();
        manager.clearImages();

        File imgFile = new File(SD_PATH);
        filesArray = imgFile.listFiles();

        if (imgFile.exists()) {
            for (int i = filesArray.length - 1; i >= 0; i--) {
                if (filesArray[i].getName().endsWith(".png") || filesArray[i].getName().endsWith(".jpg")) {
                    files.add(filesArray[i]);

                    ImageInterface img = imageFactory.createImage(filesArray[i],
                            sharedPref.getString(filesArray[i].getName(), null),
                            sharedPref.getString(filesArray[i].getName() + "_time", null),
                            editor);
                    manager.imageList.add(img);
                }
            }
        }
        currentImageIndex = 0;
        manager.nextImage(0);
        return files;
    }

    public ArrayList<File> getFiles(String filter) {
        File[] filesArray;
        files.clear();
        manager.clearImages();

        File imgFile = new File(SD_PATH);
        filesArray = imgFile.listFiles();

        if (imgFile.exists()) {
            for (int i = filesArray.length - 1; i >= 0; i--) {
                if (filesArray[i].getName().contains(filter)) {
                    files.add(filesArray[i]);
                    ImageInterface img = new Image(filesArray[i],
                            sharedPref.getString(filesArray[i].getName(), null),
                            sharedPref.getString(filesArray[i].getName() + "_time", null),
                            editor);
                    manager.imageList.add(img);

                }
            }
        }
        currentImageIndex = 0;
        manager.nextImage(0);
        return files;
    }

    public ArrayList<File> getFiles(String beforeTime, String afterTime) {
        File[] filesArray;

        files.clear();
        manager.clearImages();

        File imgFile = new File(SD_PATH);
        filesArray = imgFile.listFiles();


        if (imgFile.exists()) {
            for (int i = filesArray.length - 1; i >= 0; i--) {
                if (filesArray[i].getName().endsWith(".png") || filesArray[i].getName().endsWith(".jpg")) {
                    if (sharedPref.getString(filesArray[i].getName() + "_time", null) != null) {
                        if (sharedPref.getString(filesArray[i].getName() + "_time", null).compareTo(beforeTime) == 1 && sharedPref.getString(filesArray[i].getName() + "_time", null).compareTo(afterTime) == 0)  {
                            files.add(filesArray[i]);
                            ImageInterface img = new Image(filesArray[i],
                                    sharedPref.getString(filesArray[i].getName(), null),
                                    sharedPref.getString(filesArray[i].getName() + "_time", null),
                                    editor);
                            manager.imageList.add(img);
                        }
                    }
                }
            }
        }
        currentImageIndex = 0;
        manager.nextImage(0);
        return files;
    }

    public ArrayList<File> getFilesLocationFilter(String longitude, String latitude) {
        File[] filesArray;

        files.clear();
        manager.clearImages();

        File imgFile = new File(SD_PATH);
        filesArray = imgFile.listFiles();


        if (imgFile.exists()) {
            for (int i = filesArray.length - 1; i >= 0; i--) {
                if (filesArray[i].getName().endsWith(".png") || filesArray[i].getName().endsWith(".jpg")) {
                    Log.d("josh", "long" + sharedPref.getString(filesArray[i].getName() + "_longitude", null));
                    Log.d("josh", "lat" + sharedPref.getString(filesArray[i].getName() + "_latitude", null));
                    if(sharedPref.getString(filesArray[i].getName() + "_longitude", null) != null
                            && sharedPref.getString(filesArray[i].getName() + "_latitude", null) !=null){
                        if(sharedPref.getString(filesArray[i].getName() + "_longitude", null).startsWith(longitude)
                                && sharedPref.getString(filesArray[i].getName() + "_latitude", null).startsWith(latitude)){
                            files.add(filesArray[i]);
                            ImageInterface img = new Image(filesArray[i],
                                    sharedPref.getString(filesArray[i].getName(), null),
                                    sharedPref.getString(filesArray[i].getName() + "_time", null),
                                    editor);
                            manager.imageList.add(img);

                        }
                    }
                }
            }
        }

        currentImageIndex = 0;
        manager.nextImage(0);
        return files;
    }
    public void setCaptionView() {
        manager.updateViewInfo();
    }

    public void leftClicked(View view) {
        manager.nextImage(-1);
    }

    public void rightClicked(View view) {
        manager.nextImage(+1);
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

        editor.putString(image.getName() + "_time", currTime);
        editor.apply();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_READ_LOCATION_CODE);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            editor.putString(image.getName() + "_latitude", String.valueOf(location.getLatitude()));
                            editor.putString(image.getName() + "_longitude", String.valueOf(location.getLongitude()));
                            editor.apply();
                            Log.d("josh", "SUBMIT: " + sharedPref.getString(image.getName() + "_longitude", null));
                            Log.d("josh", "SUBMIT: " + sharedPref.getString(image.getName() + "_latitude", null));
                        }
                        else{
                            Log.d("josh", "LOCATION IS NULL");
                        }
                    }
                });

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
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_READ_LOCATION_CODE);
                }else{
                    photoFile = createImageFile();
                }
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
        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == SearchGalleryActivity.KEYWORD_RESULT) {

            String word = (data.getStringExtra(SearchGalleryActivity.EXTRA_REPLY));
            getFiles(word);
        }
        else if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == SearchGalleryActivity.TIME_RESULT) {
            String beforeTime = (data.getStringExtra(SearchGalleryActivity.BEFORE_REPLY));
            String afterTime = (data.getStringExtra(SearchGalleryActivity.AFTER_REPLY));
            getFiles(beforeTime, afterTime);
        }
        else if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == SearchGalleryActivity.LOCATION_RESULT) {

            String longitude = (data.getStringExtra(SearchGalleryActivity.LONGITUDE_REPLY));
            String latitude = (data.getStringExtra(SearchGalleryActivity.LATITUDE_REPLY));
            getFilesLocationFilter(longitude, latitude);
        }

        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);

            getFiles();

        }

    }

    public void saveCaption(View view) {
        manager.getCurrentImage().setCaption(captionEditText.getText().toString());
        captionEditText.setText("");
        setCaptionView();

    }
}