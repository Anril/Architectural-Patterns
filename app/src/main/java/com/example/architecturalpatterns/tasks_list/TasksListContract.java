package com.example.architecturalpatterns.tasks_list;

import com.example.architecturalpatterns.data.Task;

import java.util.List;

public interface TasksListContract {

    interface Presenter {
        void setFilterType(TaskFilterType filterType);
        TaskFilterType getFilterType();

        void displayAllTasks();
        void displayActiveTasks();
        void displayCompetedTasks();

        void refreshTaskList();

        void changeTaskState(long id ,boolean completed);

        void goToAddTaskActivity();
        void goToEditTaskActivity(long editTaskId);
    }

    interface View {
        void displayTasks(List<Task> tasks);

        void changeFilterTypeLabel(TaskFilterType filterType);

        void goToAddTaskActivity();
        void goToEditTaskActivity(long editTaskId);
    }
}
