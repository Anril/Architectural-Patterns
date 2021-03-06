package com.example.architecturalpatterns.edit_task;

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
import com.example.architecturalpatterns.sources.TaskRepository;

public class EditTaskActivity extends AppCompatActivity implements EditTaskContract.View {

    public static final String EDITABLE_TASK_ID_KEY = "editable_task_id";

    private static final String TAG = EditTaskActivity.class.getSimpleName();

    private TaskRepository taskRepo = Injection.provideTaskRepository(this);

    private EditTaskContract.Presenter presenter = new EditTaskPresenter(this, taskRepo);

    private long editableTaskId;

    private TextInputEditText taskTitleEditText;
    private TextInputEditText taskDescEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_task);

        taskTitleEditText = (TextInputEditText) findViewById(R.id.et_task_title);
        taskDescEditText = (TextInputEditText) findViewById(R.id.et_task_desc);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton doneFab = (FloatingActionButton) findViewById(R.id.fab_done);
        doneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = taskTitleEditText.getText().toString();
                String newDesc = taskDescEditText.getText().toString();
                presenter.editTask(editableTaskId, newTitle, newDesc);
            }
        });

        Intent intent = getIntent();
        editableTaskId = intent.getLongExtra(EDITABLE_TASK_ID_KEY, 0);
        presenter.loadEditableTask(editableTaskId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                presenter.deleteTask(editableTaskId);
                break;
            case android.R.id.home:
                presenter.backButtonClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showEditableTask(String title, String desc) {
        taskTitleEditText.setText(title);
        taskDescEditText.setText(desc);
    }

    @Override
    public void goToBack() {
        onBackPressed();
    }

    @Override
    public void showMessageTitleEmpty() {
        Toast.makeText(this,
                getResources().getString(R.string.error_title_empty),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessageTaskUpdated() {
        Toast.makeText(this,
                getResources().getString(R.string.message_task_updated),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessageTaskDeleted() {
        Toast.makeText(this,
                getResources().getString(R.string.message_task_deleted),
                Toast.LENGTH_SHORT).show();
    }
}
