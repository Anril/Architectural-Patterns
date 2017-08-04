package com.example.architecturalpatterns.tasks_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.architecturalpatterns.R;
import com.example.architecturalpatterns.data.Task;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TaskViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.chk_task_state)
    CheckBox completedCheckBox;
    @BindView(R.id.tv_task_title)
    TextView titleTextView;
    @BindView(R.id.tv_task_desc)
    TextView descTextView;

    public TaskViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Task task, final TasksListContract.Presenter presenter) {
        completedCheckBox.setOnCheckedChangeListener(null);
        completedCheckBox.setChecked(task.isCompleted());
        titleTextView.setText(task.getTitle());
        descTextView.setText(task.getDesc());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onTaskItemClicked(task.getId());
            }
        });

        completedCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        presenter.onTaskStateChanged(task.getId(), !task.isCompleted());
                    }
                });
    }
}
