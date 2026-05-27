package com.example.erasmusbuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databaseMeneger.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment responsible for managing the user's "Survival Challenges".
 * It provides an interface to view, add, and delete tasks stored in a SQLite database.
 * This version uses RecyclerView for efficient list management as per modern Android standards.
 */
public class ChallengesFragment extends Fragment implements ChallengeAdapter.OnTaskActionListener {

    private RecyclerView rvTasks;
    private ChallengeAdapter adapter;
    private EditText etNewTask;
    private DataBaseController dbController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating the main layout for challenges
        View view = inflater.inflate(R.layout.activity_challenges, container, false);

        // Initializing the Database Controller with the Application Context.
        // We use getApplicationContext() to avoid memory leaks that can happen
        // if we hold a reference to an Activity's short-lived Context.
        dbController = new DataBaseController(getContext().getApplicationContext());
        
        // Locating UI components
        rvTasks = view.findViewById(R.id.rvTasks);
        etNewTask = view.findViewById(R.id.etNewTask);
        Button btnAdd = view.findViewById(R.id.btnAddTask);

        // Setting up the RecyclerView
        rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChallengeAdapter(new ArrayList<>(), this);
        rvTasks.setAdapter(adapter);

        // Fetching initial data from the database
        refreshTasks();

        // Logical flow for adding a new task
        btnAdd.setOnClickListener(v -> {
            String taskText = etNewTask.getText().toString().trim();
            if (!taskText.isEmpty()) {
                // Database operations are done in a separate thread to prevent crashes
                new Thread(() -> {
                    dbController.insertTask(taskText);

                    // Re-fetching the full list to ensure data integrity
                    List<Task> tasks = dbController.getTaskList();

                    // We must return to the UI (Main) thread to update any view elements
                    if (getActivity() != null && isAdded()) {
                        getActivity().runOnUiThread(() -> {
                            adapter.updateTasks(tasks);
                            etNewTask.setText("");
                        });
                    }
                }).start();
            }
        });

        return view;
    }

    /**
     * Retrieves the current list of tasks from the database in the background.
     * Updates the RecyclerView adapter upon completion.
     */
    private void refreshTasks() {
        // Performing background fetch to keep the UI thread smooth
        new Thread(() -> {
            List<Task> tasks = dbController.getTaskList();
            
            // Checking if the fragment is still active before updating UI to avoid crashes
            if (getActivity() != null && isAdded()) {
                getActivity().runOnUiThread(() -> adapter.updateTasks(tasks));
            }
        }).start();
    }

    /**
     * Implementation of the delete action from the Adapter.
     */
    @Override
    public void onTaskDelete(Task task, int position) {
        new Thread(() -> {
            // Asynchronous deletion from the persistent SQLite database
            dbController.deleteAtID(task.id);
            // Refreshing the list to sync UI with the current database state
            refreshTasks();
        }).start();
    }

    /**
     * Implementation of the status change action from the Adapter.
     */
    @Override
    public void onTaskStatusChanged(Task task, boolean isChecked) {
        new Thread(() -> {
            // Persistent update of the 'isDone' field in the background
            dbController.updateAtID(task.id, task.taskName, isChecked ? 1 : 0);
        }).start();
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
