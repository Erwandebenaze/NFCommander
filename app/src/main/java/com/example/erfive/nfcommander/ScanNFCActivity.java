package com.example.erfive.nfcommander;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ScanNFCActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_nfc);

        TextView tagText = (TextView) findViewById(R.id.TagText);
        Bundle b = getIntent().getExtras();
        tagText.setText(b.getString("tel", "No tel"));
    }
}
