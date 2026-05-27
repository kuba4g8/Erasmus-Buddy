package com.example.erasmusbuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

/**
 * CityGuideFragment provides information about the local city and important landmarks.
 * It features interactive elements such as external map links to assist users in navigation.
 */
public class CityGuideFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the city guide layout defined in XML
        View view = inflater.inflate(R.layout.activity_cityguide, container,false);

        // Locating the map button to attach navigation logic
        Button btnMap = view.findViewById(R.id.btnMap);

        // Setting a click listener to trigger an external map application
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Short link for the Rotunda location
                String url = "https://maps.app.goo.gl/x4RXDM9L7Ai1S2yW9";

                // We use an Intent with ACTION_VIEW to delegate the navigation task to a dedicated map app.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                // Starting the activity; Android will automatically show a chooser if multiple apps can handle the URL.
                startActivity(intent);

            }
        });
        return view;
    }
}
