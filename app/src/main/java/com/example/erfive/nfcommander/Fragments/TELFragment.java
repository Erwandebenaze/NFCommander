package com.example.erfive.nfcommander.Fragments;

/**
 * Created by Erfive on 15/09/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.erfive.nfcommander.DetailsFragmentActivity;
import com.example.erfive.nfcommander.R;
import com.example.erfive.nfcommander.ScanNFCActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class TELFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static final String ARG_SECTION_NUMBER = "section_number";
    public TELFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TELFragment newInstance(int sectionNumber) {
        TELFragment fragment = new TELFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tel, container, false);
        Button submit = (Button) rootView.findViewById(R.id.tel_validation);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ScanNFCActivity.class);
                Bundle bundle = new Bundle();
                EditText tele = (EditText) rootView.findViewById(R.id.tel_editText);
                bundle.putString("tel", tele.getText().toString());
                i.putExtras(bundle);
                startActivity(i);

            }
        });

        return rootView;
    }

}