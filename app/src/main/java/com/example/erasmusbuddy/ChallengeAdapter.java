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
 * It manages the creation and binding of task items, implementing the ViewHolder pattern
 * for efficient view recycling.
 */
public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {

    private List<Task> taskList;
    private final OnTaskActionListener listener;

    /**
     * Interface to handle interactions within the task items.
     */
    public interface OnTaskActionListener {
        void onTaskDelete(Task task, int position);
        void onTaskStatusChanged(Task task, boolean isChecked);
    }

    public ChallengeAdapter(List<Task> taskList, OnTaskActionListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    public void updateTasks(List<Task> newTasks) {
        this.taskList = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_challange, parent, false);
        return new ChallengeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task, listener);
    }

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
            cbChallenge = itemView.findViewById(R.id.cbChallenge);
            btnDelete = itemView.findViewById(R.id.btnDeleteTask);
        }

        public void bind(Task task, OnTaskActionListener listener) {
            // Remove listener before setting state to avoid triggering it during binding
            cbChallenge.setOnCheckedChangeListener(null);
            cbChallenge.setText(task.taskName);
            cbChallenge.setChecked(task.isDone == 1);

            cbChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
                listener.onTaskStatusChanged(task, isChecked);
            });

            btnDelete.setOnClickListener(v -> {
                listener.onTaskDelete(task, getAdapterPosition());
            });
        }
    }
}
