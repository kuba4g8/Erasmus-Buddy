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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * HomeFragment serves as the main entry point of the application.
 * It handles user registration by saving their name into SharedPreferences
 * and provides navigation to the challenges section.
 */
public class HomeFragment extends Fragment {

    private EditText inputName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating the layout specifically designed for the Home experience
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Binding UI elements to local variables for manipulation
        inputName = view.findViewById(R.id.etNameCity);
        final TextView welcomeText = view.findViewById(R.id.tvWelcome);
        Button startButton = view.findViewById(R.id.btnStart);

        // Accessing SharedPreferences to check for existing user data.
        // MODE_PRIVATE ensures this data is only accessible by this application.
        final SharedPreferences prefs = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        // Retrieving the saved name; defaults to an empty string if first run.
        String savedName = prefs.getString("user_name", "");

        // If the user has already registered, we adjust the UI to a "Welcome Back" state
        // instead of showing the registration form.
        if (!savedName.isEmpty()){
            inputName.setVisibility(View.GONE);
            welcomeText.setVisibility(View.VISIBLE);
            welcomeText.setText(getString(R.string.home_welcome_back, savedName));
        }

        // Setting up the click listener for the transition to the main features.
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentName = inputName.getText().toString();
                
                // If this is the first time the user is interacting and they provided a name,
                // we persist it immediately using apply() (asynchronous save).
                if(savedName.isEmpty() && !currentName.isEmpty()){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("user_name", currentName);
                    editor.apply();
                }

                // Navigating to the ChallengesFragment. 
                // We use replace() to ensure we don't stack multiple fragments unnecessarily.
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ChallengesFragment())
                            .commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (inputName != null) {
            outState.putString("saved_input_name", inputName.getText().toString());
        }
    }

    /**
     * Restores the UI state after a configuration change.
     * @param view The fragment's root view.
     * @param savedInstanceState The bundle containing saved data.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Restoring the draft name so the user doesn't have to re-type it after a rotation.
        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString("saved_input_name");
            if (inputName != null && savedText != null) {
                inputName.setText(savedText);
            }
        }
    }
}