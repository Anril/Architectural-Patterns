package com.example.architecturalpatterns.edit_task;

public interface EditTaskContract {

    interface Presenter {
        void editTask(long id, String title, String desc);

        void deleteTask(long id);

        void loadEditableTask(long id);

        void backButtonClicked();
    }

    interface View {
        void showEditableTask(String title, String desc);

        void goToBack();

        void showMessageTitleEmpty();

        void showMessageTaskUpdated();

        void showMessageTaskDeleted();
    }
}
