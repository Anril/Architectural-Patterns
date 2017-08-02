package com.example.architecturalpatterns.add_task;

import com.example.architecturalpatterns.data.Task;
import com.example.architecturalpatterns.sources.TaskRepository;

public class AddTaskPresenter implements AddTaskContract.Presenter {

    private AddTaskContract.View view;

    private TaskRepository taskRepo;

    public AddTaskPresenter(AddTaskContract.View view, TaskRepository taskRepo) {
        this.view = view;
        this.taskRepo = taskRepo;
    }

    @Override
    public void addTask(String title, String desc) {
        if (title.isEmpty()) {
            view.showMessageTitleEmpty();
            return;
        }
        Task newTask = new Task(0,title, desc, false);

        if (taskRepo.insertTask(newTask) != -1) {
            view.showMessageTaskAdded();
            view.goToTaskListActivity();
        }
    }

    @Override
    public void goToTaskListActivity() {
        view.goToTaskListActivity();
    }
}
