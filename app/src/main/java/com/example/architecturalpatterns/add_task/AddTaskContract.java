package com.example.architecturalpatterns.add_task;

public interface AddTaskContract {

    interface Presenter {
        void addTask(String title, String desc);

        void goToTaskListActivity();
    }

    interface View {
        void goToTaskListActivity();

        void showMessageTitleEmpty();
        void showMessageTaskAdded();
    }
}
