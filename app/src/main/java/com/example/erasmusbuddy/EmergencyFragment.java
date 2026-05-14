package com.example.erasmusbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

/*
 * This is the Emergency Numbers Screen.
 */
public class EmergencyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // This line loads the "activity_emergency" layout which contains the phone numbers
        return inflater.inflate(R.layout.activity_emergency, container, false);
    }
}
