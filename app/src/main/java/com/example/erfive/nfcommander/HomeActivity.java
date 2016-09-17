package com.example.erfive.nfcommander;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

/**
 * The main activity of the app
 */
public class HomeActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private NfcAdapter mNfcAdapter;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfCommander";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the NfcAdapter field
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // Stop here, we need NFC to be available on the device
        if (mNfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Go to the DetailsFragmentActivity on click of the "Programmer" button
        Button submit = (Button) findViewById(R.id.program_tag);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, DetailsFragmentActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(HomeActivity.this).edit();
        PreferenceManager
                .getDefaultSharedPreferences(HomeActivity.this).registerOnSharedPreferenceChangeListener(this);

        // Set up the activity on the foreground on NFC detection
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        // Disable the activity to be set on the foreground on NFC detection
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    /**
     * Event fired with a new intent from a dected NFC Tag
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     * Handles the intent from a detectd NFC Tag
     * @param intent
     */
    private void handleIntent(Intent intent) {
        // Get the action intended
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                // Get the NFC Tag and send it to a NdefReaderTask AsyncTask
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask(HomeActivity.this).execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        }

//        // Get all apps installed example code
//        final PackageManager pm = getPackageManager();
//        // Get a list of installed apps.
//        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//
//        for (ApplicationInfo packageInfo : packages) {
//            String appName = (String) packageInfo.loadLabel(pm);
//            Log.d(TAG, "Installed package :" + packageInfo.packageName);
//        }
//
//        // Phone call example code
//        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0618709069"));
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        startActivity(intentCall);
//
//        // Launch app example code
//        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.sec.android.app.popupcalculator");
//        startActivity(launchIntent);
//
//        // SMS sending example code
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage("0618709069", null, "Contenu du SMS", null, null);
    }

    /**
     * Sets an activity to put itsef in the foreground in reaction to a NFC Tag detection
     * @param activity The activity to put in the foreground
     * @param adapter
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        // Create the itent based on the activity to handle
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Create pending intent from the activity's intent
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        // Prepare intent filters and techlist arrays
        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Set up the same filters as in the manifest
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        // Enable the foreground dispatch of the activity
        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * Disable an activity to put itsef in the foreground in reaction to a NFC Tag detection
     * @param activity The activity to disable
     * @param adapter
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    /**
     * Event reacting to a change in the sharedPreferences
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("LatestTag")) {
            Toast.makeText(this, "YOUPI, je suis dans latestTag : " + sharedPreferences.getString(key, "NONNNNN Y'A RIEN !!!"), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Tag : " + sharedPreferences.getString(key, "NONNNNN Y'A RIEN !!!"), Toast.LENGTH_SHORT).show();

    }
}
