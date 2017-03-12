package com.example.architecturalpatterns.contracts;

import com.example.architecturalpatterns.models.Task;

/**
 * Created by Anril on 12.03.2017.
 */

public interface EditTaskContract {

    interface Presenter {
        void editTask(long id, String title, String desc);
        void deleteTask(long id);

        void loadEditableTask(long id);

        void goToTaskListActivity();
    }

    interface View {
        void displayEditableTask(String title, String desc);

        void goToTaskListActivity();

        void showMessageTitleEmpty();
        void showMessageTaskUpdated();
        void showMessageTaskDeleted();
    }
}
