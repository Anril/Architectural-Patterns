package com.example.architecturalpatterns.add_task;

public interface AddTaskContract {

    interface Presenter {
        void addTask(String title, String desc);

        void backButtonClicked();
    }

    interface View {
        void goToBack();

        void showMessageTitleEmpty();

        void showMessageTaskAdded();
    }
}
