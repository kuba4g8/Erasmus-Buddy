package com.example.erasmusbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

/*
 * This is the City Guide Screen.
 */
public class CityGuideFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // This line loads the "activity_cityguide" layout for this screen
        return inflater.inflate(R.layout.activity_cityguide, container, false);
    }
}
