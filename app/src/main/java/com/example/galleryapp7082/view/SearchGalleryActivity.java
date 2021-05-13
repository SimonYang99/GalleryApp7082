package com.example.galleryapp7082.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.galleryapp7082.R;

public class SearchGalleryActivity extends AppCompatActivity {

    private EditText editTextKeyword;
    private EditText beforeTime;
    private EditText afterTime;
    private EditText Longitude;
    private EditText Latitude;
    public static final String BEFORE_REPLY = "BEFORE_REPLY";
    public static final String AFTER_REPLY = "AFTER_REPLY";
    public static final String EXTRA_REPLY = "KEYWORD_REPLY";
    public static final String LONGITUDE_REPLY = "LONG_REPLY";
    public static final String LATITUDE_REPLY = "LAT_REPLY";

    public static final int TIME_RESULT = 201;
    public static final int KEYWORD_RESULT = 202;
    public static final int LOCATION_RESULT = 203;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editTextKeyword = findViewById(R.id.keyWordEditText);
        beforeTime = findViewById(R.id.startDate);
        afterTime = findViewById(R.id.endDate);
        Longitude = findViewById(R.id.LongitudeEditText);
        Latitude = findViewById(R.id.LatitudeEditText);

        final Button DateButton = findViewById(R.id.SearchMenuButton);
        DateButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(beforeTime.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String before = beforeTime.getText().toString();
                String after = afterTime.getText().toString();

                replyIntent.putExtra(BEFORE_REPLY, before);
                replyIntent.putExtra(AFTER_REPLY, after);
                setResult(TIME_RESULT, replyIntent);
            }
            finish();
        });

        final Button KeywordButton = findViewById(R.id.SearchKeyword);
        KeywordButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editTextKeyword.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String keyword = editTextKeyword.getText().toString();

                replyIntent.putExtra(EXTRA_REPLY, keyword);
                setResult(KEYWORD_RESULT, replyIntent);
            }
            finish();
        });

        final Button longLatButton = findViewById(R.id.searchLocation);
        longLatButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(Longitude.getText()) || TextUtils.isEmpty(Latitude.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String longitudeString = Longitude.getText().toString();
                String latitudeString = Latitude.getText().toString();

                replyIntent.putExtra(LONGITUDE_REPLY, longitudeString);
                replyIntent.putExtra(LATITUDE_REPLY, latitudeString);
                setResult(LOCATION_RESULT, replyIntent);
            }
            finish();
        });
    }


    public void filterResults(View view) {

    }

    public void goBackHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}