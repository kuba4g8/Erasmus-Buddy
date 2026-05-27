package com.example.erasmusbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.databaseMeneger.Task;
import java.util.List;

/**
 * Adapter class for the RecyclerView in ChallengesFragment.
 * It manages the creation and binding of task items, implementing the ViewHolder pattern.
 * The ViewHolder pattern is crucial for performance as it avoids repetitive calls to findViewById() 
 * by caching view references, allowing for smooth scrolling.
 */
public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {

    private List<Task> taskList;
    private final OnTaskActionListener listener;

    /**
     * Interface to handle interactions within the task items.
     */
    public interface OnTaskActionListener {
        /**
         * Triggered when a task needs to be deleted from the list and database.
         */
        void onTaskDelete(Task task, int position);

        /**
         * Triggered when the completion status of a task changes.
         */
        void onTaskStatusChanged(Task task, boolean isChecked);
    }

    /**
     * Constructor for the adapter.
     */
    public ChallengeAdapter(List<Task> taskList, OnTaskActionListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    /**
     * Updates the internal data set and refreshes the RecyclerView.
     */
    public void updateTasks(List<Task> newTasks) {
        this.taskList = newTasks;
        // notifyDataSetChanged() tells the list to redraw everything. 
        // For larger lists, using DiffUtil is more efficient but this is suitable for smaller sets.
        notifyDataSetChanged();
    }

    /**
     * Creates new view holders when the RecyclerView needs them.
     */
    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We inflate the specific item layout for a single challenge row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_challange, parent, false);
        return new ChallengeViewHolder(view);
    }

    /**
     * Binds data from a specific Task object to a ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task, listener);
    }

    /**
     * Returns the total number of items in the list.
     */
    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    /**
     * ViewHolder class that holds references to the views for a single task item.
     */
    public static class ChallengeViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox cbChallenge;
        private final Button btnDelete;

        public ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            // Caching view references in the constructor
            cbChallenge = itemView.findViewById(R.id.cbChallenge);
            btnDelete = itemView.findViewById(R.id.btnDeleteTask);
        }

        /**
         * Populates the view elements with task data and attaches listeners.
         */
        public void bind(Task task, OnTaskActionListener listener) {
            // It's important to remove the listener before setting values to avoid
            // triggering the 'onChanged' callback during the binding process.
            cbChallenge.setOnCheckedChangeListener(null);
            cbChallenge.setText(task.taskName);
            cbChallenge.setChecked(task.isDone == 1);

            // Attaching the listener to handle user check/uncheck actions
            cbChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
                listener.onTaskStatusChanged(task, isChecked);
            });

            // Handling the deletion request
            btnDelete.setOnClickListener(v -> {
                listener.onTaskDelete(task, getAdapterPosition());
            });
        }
    }
}
