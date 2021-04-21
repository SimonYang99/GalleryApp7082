package com.example.galleryapp7082;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SearchGalleryActivity extends AppCompatActivity {

    private EditText editText;
    public static final String EXTRA_REPLY = "yo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editText = findViewById(R.id.keyWord);
        final Button button = findViewById(R.id.SearchMenuButton);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editText.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String word = editText.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY, word);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

    public void filterResults(View view) {

    }
}