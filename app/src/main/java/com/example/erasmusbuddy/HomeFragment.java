package com.example.erasmusbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/*
 * This is the Home Screen. It is a "Fragment", which is like a smaller
 * screen that fits inside the Main Activity.
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 1. Tell the app which layout file (XML) to use for this screen
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 2. Find the elements by its ID from the XML file
        final EditText inputName = view.findViewById(R.id.etNameCity);
        final TextView welcomeText = view.findViewById(R.id.tvWelcome);
        Button startButton = view.findViewById(R.id.btnStart);

        // 3. Open the "temporary DB" (SharedPreferences) so we can save name of user
        // Jakub Need to change it later for saving in DB
        final SharedPreferences prefs = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        // 4. Checking if name is saved in UserData
        String savedName = prefs.getString("user_name", "");

        // 5. If name is saved, hide input, show text
        if (!savedName.isEmpty()){
            inputName.setVisibility(View.GONE);
            welcomeText.setVisibility(View.VISIBLE);
            welcomeText.setText("Welcome " + savedName + "!");
        }

        // 6. Tell the button what to do when someone clicks it
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If we don't have username saved, we need to take it from input and save it
                String currentName = inputName.getText().toString();
                if(savedName.isEmpty() && !currentName.isEmpty()){
                    // editor is permamently saving username in phone memory
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("user_name", currentName);
                    editor.apply();
                }
                // When clicked, we swap the current fragment with the Challenges screen
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ChallengesFragment())
                            .commit();
                }
            }
        });

        // 7. Send the finished view to the phone screen
        return view;
    }
}