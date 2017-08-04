package com.example.architecturalpatterns.tasks_list;

import com.example.architecturalpatterns.data.Task;

import java.util.List;

public interface TasksListContract {

    interface Presenter {

        void onResume();

        void onTaskFilterTypeClicked(TaskFilterType filterType);

        void onSwipeRefreshLayout();

        void onTaskStateChanged(long taskId, boolean newState);

        void onAddTaskFabClicked();

        void onTaskItemClicked(long editTaskId);

        TaskFilterType getTaskFilterType();
    }

    interface View {

        void showTasks(List<Task> tasks);

        void setFilterTypeLabel(int resId);

        void goToAddTaskActivity();

        void goToEditTaskActivity(long editTaskId);
    }
}
