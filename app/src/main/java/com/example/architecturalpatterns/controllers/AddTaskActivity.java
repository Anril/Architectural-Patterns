package com.example.architecturalpatterns.controllers;

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

import com.example.architecturalpatterns.R;
import com.example.architecturalpatterns.models.Task;
import com.example.architecturalpatterns.models.TaskRepository;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = AddTaskActivity.class.getSimpleName();

    private TaskRepository taskRepo = TaskRepository.getInstance(this);

    private TextInputEditText taskTitleEditText;
    private TextInputEditText taskDescEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_task);

        ActionBar actionBar = getSupportActionBar();
        if( actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton doneFab = (FloatingActionButton) findViewById(R.id.fab_done);
        doneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskTitleEditText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.error_title_empty),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Task newTask = new Task();
                newTask.setTitle(taskTitleEditText.getText().toString());
                newTask.setDesc(taskDescEditText.getText().toString());

                long answer = taskRepo.insertTask(newTask);

                if (answer != -1) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.message_task_added),
                            Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(getApplicationContext(), TasksListActivity.class);
                startActivity(intent);
            }
        });

        taskTitleEditText = (TextInputEditText) findViewById(R.id.et_task_title);
        taskDescEditText = (TextInputEditText) findViewById(R.id.et_task_desc);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), TasksListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

