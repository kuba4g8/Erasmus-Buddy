package com.example.erasmusbuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

/*
 * This is the City Guide Screen.
 */
public class CityGuideFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Load the layout
        View view = inflater.inflate(R.layout.activity_cityguide, container,false);

        // Load button
        Button btnMap = view.findViewById(R.id.btnMap);

        // Set action at btn click
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Link for Rotunda location
                String url = "https://maps.app.goo.gl/x4RXDM9L7Ai1S2yW9";

                // Create intent opening url
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                startActivity(intent);

            }
        });
        return view;
    }
}
