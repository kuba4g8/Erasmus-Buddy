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
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.activity_challenges, container, false);

        // Initialize the Database Controller
        dbController = new DataBaseController(getContext().getApplicationContext());
        
        // Find UI elements
        rvTasks = view.findViewById(R.id.rvTasks);
        etNewTask = view.findViewById(R.id.etNewTask);
        Button btnAdd = view.findViewById(R.id.btnAddTask);

        // Setup RecyclerView with LinearLayoutManager and Adapter
        rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChallengeAdapter(new ArrayList<>(), this);
        rvTasks.setAdapter(adapter);

        // Fetch and display existing tasks from the database
        refreshTasks();

        // Listener for the "ADD TASK" button
        btnAdd.setOnClickListener(v -> {
            String taskText = etNewTask.getText().toString().trim();
            if (!taskText.isEmpty()) {
                new Thread(() -> {
                    dbController.insertTask(taskText);

                    List<Task> tasks = dbController.getTaskList();

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
     */
    private void refreshTasks() {
        new Thread(() -> {
            List<Task> tasks = dbController.getTaskList();
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
            // Delete from DB in background
            dbController.deleteAtID(task.id);
            // Refresh the whole list to ensure UI is in sync with DB
            refreshTasks();
        }).start();
    }

    /**
     * Implementation of the status change action from the Adapter.
     */
    @Override
    public void onTaskStatusChanged(Task task, boolean isChecked) {
        new Thread(() -> {
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
