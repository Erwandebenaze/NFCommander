package com.example.erfive.nfcommander;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Button button = (Button) findViewById(R.id.button);
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(DetailsActivity.this);
        button.setText(preferences.getString("TagName", "NoTagName"));
    }
}
