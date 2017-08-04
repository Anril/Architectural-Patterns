package com.example.architecturalpatterns.add_task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.architecturalpatterns.Injection;
import com.example.architecturalpatterns.R;
import com.example.architecturalpatterns.core.BaseActivity;
import com.example.architecturalpatterns.sources.TaskRepository;

import butterknife.BindView;
import butterknife.OnClick;

public class AddTaskActivity extends BaseActivity implements AddTaskContract.View {

    private static final String TAG = AddTaskActivity.class.getSimpleName();

    private TaskRepository taskRepo = Injection.provideTaskRepository(this);

    private AddTaskContract.Presenter presenter = new AddTaskPresenter(this, taskRepo);

    @BindView(R.id.et_task_title)
    TextInputEditText taskTitleEditText;
    @BindView(R.id.et_task_desc)
    TextInputEditText taskDescEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_task);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.backButtonClicked();
        }
        return super.onOptionsItemSelected(item);
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
    public void showMessageTaskAdded() {
        Toast.makeText(this,
                getResources().getString(R.string.message_task_added),
                Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab_done)
    void onDoneFabClick(View view) {
        String title = taskTitleEditText.getText().toString();
        String desc = taskDescEditText.getText().toString();
        presenter.addTask(title, desc);
    }
}

