package com.example.architecturalpatterns.tasks_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.architecturalpatterns.R;
import com.example.architecturalpatterns.data.Task;

import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private static final String TAG = TaskAdapter.class.getSimpleName();

    private List<Task> tasks;

    private TasksListContract.Presenter presenter;

    public TaskAdapter(List<Task> tasks, TasksListContract.Presenter presenter) {
        this.tasks = tasks;
        this.presenter = presenter;
    }

    public void replaceDataSet(List<Task> newTasks) {
        tasks = newTasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {
        holder.bind(tasks.get(position), presenter);
    }
}
