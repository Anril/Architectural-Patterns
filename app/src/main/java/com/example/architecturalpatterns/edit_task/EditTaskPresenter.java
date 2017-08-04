package com.example.architecturalpatterns.edit_task;

import com.example.architecturalpatterns.data.Task;
import com.example.architecturalpatterns.sources.TaskRepository;

public class EditTaskPresenter implements EditTaskContract.Presenter {

    private EditTaskContract.View view;

    private TaskRepository taskRepo;

    public EditTaskPresenter(EditTaskContract.View view, TaskRepository taskRepo) {
        this.view = view;
        this.taskRepo = taskRepo;
    }

    @Override
    public void editTask(long id, String title, String desc) {
        if (title.isEmpty()) {
            view.showMessageTitleEmpty();
        }
        Task newTask = taskRepo.getTaskById(id);
        if (newTask == null) {
            return;
        }
        newTask.setTitle(title);
        newTask.setDesc(desc);
        if (taskRepo.updateTask(id, newTask)) {
            view.showMessageTaskUpdated();
        }
        view.goToBack();
    }

    @Override
    public void deleteTask(long id) {
        if (taskRepo.removeTask(id)) {
            view.showMessageTaskDeleted();
        }
        view.goToBack();
    }

    @Override
    public void loadEditableTask(long id) {
        Task editableTask = taskRepo.getTaskById(id);
        if (editableTask == null) {
            return;
        }
        view.showEditableTask(editableTask.getTitle(), editableTask.getDesc());
    }

    @Override
    public void backButtonClicked() {
        view.goToBack();
    }
}
