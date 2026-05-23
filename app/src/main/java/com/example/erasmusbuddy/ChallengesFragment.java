package com.example.erasmusbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.databaseMeneger.*;
import java.util.List;

/**
 * Fragment responsible for managing the user's "Survival Challenges".
 * It provides an interface to view, add, and delete tasks stored in a SQLite database.
 * To maintain a smooth user experience, all database operations are executed in background threads.
 */
public class ChallengesFragment extends Fragment {

    private LinearLayout containerTasks;
    private EditText etNewTask;
    private DataBaseController dbController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.activity_challenges, container, false);

        // Initialize the Database Controller with Application Context to prevent memory leaks
        dbController = new DataBaseController(getContext().getApplicationContext());
        
        // Find and bind UI elements from the layout
        containerTasks = view.findViewById(R.id.containerTasks);
        etNewTask = view.findViewById(R.id.etNewTask);
        Button btnAdd = view.findViewById(R.id.btnAddTask);

        // Fetch and display existing tasks from the database
        refreshTasks();

        // Listener for the "ADD TASK" button
        btnAdd.setOnClickListener(v -> {
            String taskText = etNewTask.getText().toString().trim();
            if (!taskText.isEmpty()) {
                // Perform DB insertion in a background thread to keep UI responsive
                new Thread(() -> {
                    dbController.insertTask(taskText);
                    
                    // Refresh the UI list after successful insertion
                    refreshTasks();

                    // Clear the input field on the Main (UI) Thread
                    if (getActivity() != null && isAdded()) {
                        getActivity().runOnUiThread(() -> etNewTask.setText(""));
                    }
                }).start();
            }
        });

        return view;
    }

    /**
     * Retrieves the current list of tasks from the database in the background.
     * Triggers a UI update once the data is fetched.
     */
    private void refreshTasks() {
        new Thread(() -> {
            List<Task> tasks = dbController.getTaskList();
            // Always check if fragment is still attached before updating the UI
            if (getActivity() != null && isAdded()) {
                getActivity().runOnUiThread(() -> displayTasks(tasks));
            }
        }).start();
    }

    /**
     * Clears the current list in the UI and renders the provided list of tasks.
     */
    private void displayTasks(List<Task> tasks) {
        containerTasks.removeAllViews();
        for (Task task : tasks) {
            addNewTask(task);
        }
    }

    /**
     * Inflates a new task item view and adds it to the list container.
     * Sets up listeners for checkbox changes and task deletion.
     */
    private void addNewTask(Task task) {
        // Inflate the layout for a single task row
        View taskView = getLayoutInflater().inflate(R.layout.item_challange, null);

        // Bind data to the CheckBox
        CheckBox cb = taskView.findViewById(R.id.cbChallenge);
        cb.setText(task.taskName);
        cb.setChecked(task.isDone == 1);

        // Update database when the checkbox state changes
        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            new Thread(() -> {
                dbController.updateAtID(task.id, task.taskName, isChecked ? 1 : 0);
            }).start();
        });

        // Set up the delete button functionality
        Button btnDelete = taskView.findViewById(R.id.btnDeleteTask);
        btnDelete.setOnClickListener(v -> {
            new Thread(() -> {
                // Delete from DB in background
                dbController.deleteAtID(task.id);
                // Remove from UI in the foreground
                if (getActivity() != null && isAdded()) {
                    getActivity().runOnUiThread(() -> containerTasks.removeView(taskView));
                }
            }).start();
        });

        // Add the completed task view to our main list container
        containerTasks.addView(taskView);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (etNewTask != null) {
            outState.putString("draft_task_text", etNewTask.getText().toString());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            String draftText = savedInstanceState.getString("draft_task_text");
            if (etNewTask != null && draftText != null) {
                etNewTask.setText(draftText);
            }
        }
    }
}
