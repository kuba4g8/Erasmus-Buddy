package com.example.erasmusbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.CheckBox;
import androidx.fragment.app.Fragment;

public class ChallengesFragment extends Fragment {

    private LinearLayout containerTasks;
    private EditText etNewTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_challenges, container, false);

        // 1. Declaring elements
        containerTasks = view.findViewById(R.id.containerTasks);
        etNewTask = view.findViewById(R.id.etNewTask);
        Button btnAdd = view.findViewById(R.id.btnAddTask);

        // 2. Action on ADD TASK
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskText = etNewTask.getText().toString();
                if (!taskText.isEmpty()) {
                    addNewTask(taskText); // Adding new checkbox to containerTasks
                    etNewTask.setText(""); // Clearing the etNewTask, for next text
                }
            }
        });

        return view;
    }

    // This function adds new checkbox
    private void addNewTask(String text) {
        // Create new line in item_challenge.xml
        View taskView = getLayoutInflater().inflate(R.layout.item_challange, null);

        // Setting checkbox text
        CheckBox cb = taskView.findViewById(R.id.cbChallenge);
        cb.setText(text);

        // DeleteTask (X) definiton
        Button btnDelete = taskView.findViewById(R.id.btnDeleteTask);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerTasks.removeView(taskView); // Deletion of whole checkbox from conteiner
            }
        });

        // Addition of new checkbox
        containerTasks.addView(taskView);
    }
}