package com.example.architecturalpatterns.tasks_list;

import com.example.architecturalpatterns.R;
import com.example.architecturalpatterns.data.Task;
import com.example.architecturalpatterns.sources.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TasksListPresenter implements TasksListContract.Presenter {

    private TasksListContract.View view;

    private TaskRepository taskRepo;

    private TaskFilterType filterType = TaskFilterType.ALL_TASKS;

    public TasksListPresenter(TasksListContract.View view, TaskRepository taskRepo) {
        this.view = view;
        this.taskRepo = taskRepo;
    }

    public TasksListPresenter(TasksListContract.View view, TaskRepository taskRepo,
                              TaskFilterType filterType) {
        this(view, taskRepo);
        this.filterType = filterType;
        view.setFilterTypeLabel(getFilterLabelResId(filterType));
    }

    @Override
    public void onResume() {
        view.showTasks(loadTasks(filterType));
    }

    @Override
    public void onTaskFilterTypeClicked(TaskFilterType filterType) {
        this.filterType = filterType;
        view.showTasks(loadTasks(filterType));
        view.setFilterTypeLabel(getFilterLabelResId(filterType));
    }

    @Override
    public void onSwipeRefreshLayout() {
        view.showTasks(loadTasks(filterType));
    }

    @Override
    public void onTaskStateChanged(long taskId, boolean newState) {
        Task editedTask = taskRepo.getTaskById(taskId);
        if (editedTask != null) {
            editedTask.setCompleted(newState);
            taskRepo.updateTask(taskId, editedTask);
        }
    }

    @Override
    public void onAddTaskFabClicked() {
        view.goToAddTaskActivity();
    }

    @Override
    public void onTaskItemClicked(long editableTaskId) {
        view.goToEditTaskActivity(editableTaskId);
    }

    @Override
    public TaskFilterType getTaskFilterType() {
        return filterType;
    }

    private int getFilterLabelResId(TaskFilterType filterType) {
        switch (filterType) {
            case ALL_TASKS:
                return R.string.label_all_tasks;
            case ACTIVE_TASKS:
                return R.string.label_active_tasks;
            case COMPLETED_TASKS:
                return R.string.label_completed_tasks;
        }
        return 0;
    }

    private List<Task> loadTasks(TaskFilterType filterType) {
        switch (filterType) {
            case ALL_TASKS:
                return taskRepo.getAllTasks();
            case ACTIVE_TASKS:
                return taskRepo.getActiveTasks();
            case COMPLETED_TASKS:
                return taskRepo.getCompletedTasks();
        }
        return new ArrayList<>();
    }
}
