package com.example.architecturalpatterns.contracts;

import com.example.architecturalpatterns.models.Task;

/**
 * Created by Anril on 12.03.2017.
 */

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
