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
import com.example.databaseMeneger.*;
import java.util.List;

public class ChallengesFragment extends Fragment {

    private LinearLayout containerTasks;
    private EditText etNewTask;
    private DataBaseController dbController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_challenges, container, false);

        // 1. Declaring elements
        dbController = new DataBaseController(getContext());
        containerTasks = view.findViewById(R.id.containerTasks);
        etNewTask = view.findViewById(R.id.etNewTask);
        Button btnAdd = view.findViewById(R.id.btnAddTask);

        loadTasksFromDB();

        // 2. Action on ADD TASK
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskText = etNewTask.getText().toString();
                if (!taskText.isEmpty()) {
                    new Thread(() -> {
                        dbController.insertTask(taskText);// Adding new checkbox to containerTasks
                        getActivity().runOnUiThread(() -> {
                            loadTasksFromDB();
                            etNewTask.setText("");
                        });
                    }).start();
                        
                }
            }
        });

        return view;
    }

    // Downloads all of the tasks from DB and puts it into UI
    private void loadTasksFromDB(){
        containerTasks.removeAllViews(); // We need to clear whole list before downloading new tasks
        List<Task> tasks = dbController.getTaskList();
        for (Task task : tasks){
            addNewTask(task);
        }
    }
    // This function adds new checkbox
    private void addNewTask(Task task) {
        // Create new line in item_challenge.xml
        View taskView = getLayoutInflater().inflate(R.layout.item_challange, null);

        // Setting checkbox text and isDone
        CheckBox cb = taskView.findViewById(R.id.cbChallenge);
        cb.setText(task.taskName);
        cb.setChecked(task.isDone == 1);

        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dbController.updateAtID(task.id, task.taskName, isChecked ? 1:0);
        });

        // DeleteTask (X) definiton
        Button btnDelete = taskView.findViewById(R.id.btnDeleteTask);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbController.deleteAtID(task.id);
                containerTasks.removeView(taskView); // Deletion of whole checkbox from conteiner
            }
        });

        // Addition of new checkbox
        containerTasks.addView(taskView);
    }
}