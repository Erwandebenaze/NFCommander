package com.example.erfive.nfcommander.Fragments;

/**
 * Created by Erfive on 15/09/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.erfive.nfcommander.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SMSFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static final String ARG_SECTION_NUMBER = "section_number";
    public SMSFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SMSFragment newInstance(int sectionNumber) {
        SMSFragment fragment = new SMSFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sms, container, false);


        return rootView;
    }

}