package com.example.architecturalpatterns.tasks_list;

import com.example.architecturalpatterns.data.Task;
import com.example.architecturalpatterns.sources.TaskRepository;

import java.util.List;

public class TasksListPresenter implements TasksListContract.Presenter {

    private TasksListContract.View view;

    private TaskRepository taskRepo;

    private TaskFilterType filterType = TaskFilterType.ALL_TASKS;

    public TasksListPresenter(TasksListContract.View view, TaskRepository taskRepo) {
        this.view = view;
        this.taskRepo = taskRepo;
    }

    @Override
    public void setFilterType(TaskFilterType filterType) {
        this.filterType = filterType;
    }

    public TaskFilterType getFilterType() {
        return filterType;
    }

    @Override
    public void displayAllTasks() {
        filterType = TaskFilterType.ALL_TASKS;
        List<Task> tasks = taskRepo.getAllTasks();
        view.changeFilterTypeLabel(TaskFilterType.ALL_TASKS);
        view.displayTasks(tasks);
    }

    @Override
    public void displayActiveTasks() {
        filterType = TaskFilterType.ACTIVE_TASKS;
        List<Task> tasks = taskRepo.getActiveTasks();
        view.changeFilterTypeLabel(TaskFilterType.ACTIVE_TASKS);
        view.displayTasks(tasks);
    }

    @Override
    public void displayCompetedTasks() {
        filterType = TaskFilterType.COMPLETED_TASKS;
        List<Task> tasks = taskRepo.getCompletedTasks();
        view.changeFilterTypeLabel(TaskFilterType.COMPLETED_TASKS);
        view.displayTasks(tasks);
    }

    @Override
    public void refreshTaskList() {
        switch (filterType) {
            case ALL_TASKS:
                view.displayTasks(taskRepo.getAllTasks());
                break;
            case ACTIVE_TASKS:
                view.displayTasks(taskRepo.getActiveTasks());
                break;
            case COMPLETED_TASKS:
                view.displayTasks(taskRepo.getCompletedTasks());
                break;
        }
    }

    @Override
    public void changeTaskState(long id, boolean completed) {
        Task editedTask = taskRepo.getTaskById(id);
        if (editedTask != null) {
            editedTask.setCompleted(completed);
            taskRepo.updateTask(id, editedTask);
        }
    }

    @Override
    public void goToAddTaskActivity() {
        view.goToAddTaskActivity();
    }

    @Override
    public void goToEditTaskActivity(long editTaskId) {
        view.goToEditTaskActivity(editTaskId);
    }
}
