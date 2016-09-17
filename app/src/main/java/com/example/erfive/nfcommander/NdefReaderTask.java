package com.example.erfive.nfcommander;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * AsyncTask {@link com.example.erfive.nfcommander.NdefReaderTask} to read a tag contained in an NFC Tag
 */

public class NdefReaderTask extends AsyncTask<Tag, Void, String> {

    public static final String TAG = "NfCommander";

    // The context from which the task was called
    private Context mContext;

    /**
     *
     * @param ctx Context from which the task is called
     */
    public NdefReaderTask (Context ctx){
        mContext = ctx;
    }

    /**
     *
     * @param tags Array containg the tag to read
     * @return A String object containing the tag's content
     */
    @Override
    protected String doInBackground(Tag... tags) {
        Tag tag = tags[0];

        // Get the tag and set a new message in a byte[]
        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            // NDEF is not supported by this Tag.
            Log.e(TAG, "Unsupported tag");
        }

        // Get NdefMessage from recovered tag
        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        // Get the records from the NdefMessage
        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            // Check if the content of the record is well known and if the record contains RTD text
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    // Return the string contained in the record's text
                    return readText(ndefRecord);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Unsupported Encoding", e);
                }
            }
        }

        return "Le tag , ne contient pas de texte ou n'est pas lisible.";
    }

    /**
     *
     * @param record The NdefRecord to get the text from
     * @return A Sring containing the record's content
     * @throws UnsupportedEncodingException
     */
    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // Construct and return the text from the byte array
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            // Change the "LatestTag" key from the shared preference of the calling context
            SharedPreferences.Editor editor = PreferenceManager
                    .getDefaultSharedPreferences(mContext).edit();
            editor.putString("LatestTag", result);
            editor.apply();
        }
    }
}