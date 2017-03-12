package com.example.architecturalpatterns.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.architecturalpatterns.Injection;
import com.example.architecturalpatterns.R;
import com.example.architecturalpatterns.contracts.AddTaskContract;
import com.example.architecturalpatterns.models.TaskRepository;
import com.example.architecturalpatterns.presenters.AddTaskPresenter;

public class AddTaskActivity extends AppCompatActivity implements AddTaskContract.View {

    private static final String TAG = AddTaskActivity.class.getSimpleName();

    private TaskRepository taskRepo = Injection.provideTaskRepository(this);

    private AddTaskContract.Presenter presenter = new AddTaskPresenter(this, taskRepo);

    private TextInputEditText taskTitleEditText;
    private TextInputEditText taskDescEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_task);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton doneFab = (FloatingActionButton) findViewById(R.id.fab_done);
        doneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = taskTitleEditText.getText().toString();
                String desc = taskDescEditText.getText().toString();
                presenter.addTask(title, desc);
            }
        });

        taskTitleEditText = (TextInputEditText) findViewById(R.id.et_task_title);
        taskDescEditText = (TextInputEditText) findViewById(R.id.et_task_desc);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.goToTaskListActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void goToTaskListActivity() {
        Intent intent = new Intent(getApplicationContext(), TasksListActivity.class);
        startActivity(intent);
    }

    @Override
    public void showMessageTitleEmpty() {
        Toast.makeText(getApplicationContext(),
                getResources().getString(R.string.error_title_empty),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessageTaskAdded() {
        Toast.makeText(getApplicationContext(),
                getResources().getString(R.string.message_task_added),
                Toast.LENGTH_SHORT).show();
    }

}

