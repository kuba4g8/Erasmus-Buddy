package com.example.erasmusbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

/*
 * This is the Challenges (Survival Tasks) Screen.
 */
public class ChallengesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // This simple line loads the "activity_challenges" layout for this screen
        return inflater.inflate(R.layout.activity_challenges, container, false);
    }
}
