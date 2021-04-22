package com.example.galleryapp7082;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SearchGalleryActivity extends AppCompatActivity {

    private EditText editText;
    private EditText beforeTime;
    public static final String EXTRA_REPLY = "yo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editText = findViewById(R.id.keyWord);
        beforeTime = findViewById(R.id.startDate);
        final Button button = findViewById(R.id.SearchMenuButton);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editText.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String word = editText.getText().toString();

                if(!beforeTime.getText().toString().isEmpty()) {
                    word = beforeTime.getText().toString();
                }
                replyIntent.putExtra(EXTRA_REPLY, word);
                setResult(RESULT_OK, replyIntent);
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