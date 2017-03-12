package com.example.architecturalpatterns.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.architecturalpatterns.Injection;
import com.example.architecturalpatterns.R;
import com.example.architecturalpatterns.models.Task;
import com.example.architecturalpatterns.models.TaskRepository;

public class EditTaskActivity extends AppCompatActivity{

    private static final String TAG = EditTaskActivity.class.getSimpleName();

    private Task editTask;
    private TextInputEditText taskTitleEditText;
    private TextInputEditText taskDescEditText;

    private TaskRepository taskRepo = Injection.provideTaskRepository(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_task);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton doneFab = (FloatingActionButton) findViewById(R.id.fab_done);
        doneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskTitleEditText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.error_title_empty),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    editTask.setTitle(taskTitleEditText.getText().toString());
                    editTask.setDesc(taskDescEditText.getText().toString());

                    boolean answer = taskRepo.updateTask(editTask.getId(), editTask);
                    if (answer) {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.message_task_updated),
                                Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(getApplicationContext(), TasksListActivity.class);
                    startActivity(intent);
                }
            }
        });

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 0);

        editTask = taskRepo.getTaskById(id);

        taskTitleEditText = (TextInputEditText) findViewById(R.id.et_task_title);
        taskTitleEditText.setText(editTask.getTitle());
        taskDescEditText = (TextInputEditText) findViewById(R.id.et_task_desc);
        taskDescEditText.setText(editTask.getDesc());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete:
                boolean answer = taskRepo.removeTask(editTask.getId());
                if (answer) {
                    Toast.makeText(this,
                            getResources().getString(R.string.message_task_deleted),
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), TasksListActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                intent = new Intent(getApplicationContext(), TasksListActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
