package com.example.architecturalpatterns.tasks_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.architecturalpatterns.R;
import com.example.architecturalpatterns.data.Task;

import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private static final String TAG = TaskAdapter.class.getSimpleName();

    private List<Task> tasks;

    private TasksListContract.Presenter presenter;

    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox completedCheckBox;
        private TextView titleTextView;
        private TextView descTextView;

        private ViewHolder(View view) {
            super(view);

            completedCheckBox = (CheckBox) view.findViewById(R.id.chk_task_state);
            titleTextView = (TextView) view.findViewById(R.id.tv_task_title);
            descTextView = (TextView) view.findViewById(R.id.tv_task_desc);
        }
    }

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
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.completedCheckBox.setOnCheckedChangeListener(null);
        holder.completedCheckBox.setChecked(tasks.get(position).isCompleted());
        holder.titleTextView.setText(tasks.get(position).getTitle());
        holder.descTextView.setText(tasks.get(position).getDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.goToEditTaskActivity(tasks.get(position).getId());
            }
        });

        holder.completedCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.changeTaskState(tasks.get(position).getId(),
                        !tasks.get(position).isCompleted() );
            }
        });
    }
}
