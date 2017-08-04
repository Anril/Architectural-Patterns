package com.example.architecturalpatterns.tasks_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.architecturalpatterns.Injection;
import com.example.architecturalpatterns.R;
import com.example.architecturalpatterns.add_task.AddTaskActivity;
import com.example.architecturalpatterns.core.BaseActivity;
import com.example.architecturalpatterns.data.Task;
import com.example.architecturalpatterns.edit_task.EditTaskActivity;
import com.example.architecturalpatterns.sources.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TasksListActivity extends BaseActivity implements TasksListContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = TasksListActivity.class.getSimpleName();

    private static final String FILTER_KEY = "FILTER";

    private TaskRepository taskRepo;
    private TasksListContract.Presenter presenter;
    private TaskAdapter taskAdapter;

    @BindView(R.id.tv_filter_label)
    TextView filterLabelTextView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_task_list)
    RecyclerView taskListRecView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tasks_list);

        taskRepo = Injection.provideTaskRepository(this);

        if (savedInstanceState != null) {
            try {
                presenter = new TasksListPresenter(this, taskRepo,
                        TaskFilterType.valueOf(savedInstanceState.getString(FILTER_KEY)));
            } catch (Exception e) {
                e.printStackTrace();
                presenter = new TasksListPresenter(this, taskRepo);
            }
        } else {
            presenter = new TasksListPresenter(this, taskRepo);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecView.setLayoutManager(layoutManager);
        taskAdapter = new TaskAdapter(new ArrayList<Task>(), presenter);
        taskListRecView.setAdapter(taskAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FILTER_KEY, presenter.getTaskFilterType().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter) {
            PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.menu_filter));
            popupMenu.inflate(R.menu.filter_popup);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_all:
                            presenter.onTaskFilterTypeClicked(TaskFilterType.ALL_TASKS);
                            break;
                        case R.id.menu_active:
                            presenter.onTaskFilterTypeClicked(TaskFilterType.ACTIVE_TASKS);
                            break;
                        case R.id.menu_completed:
                            presenter.onTaskFilterTypeClicked(TaskFilterType.COMPLETED_TASKS);
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_add_task)
    void onAddTaskFabClick() {
        presenter.onAddTaskFabClicked();
    }

    @Override
    public void showTasks(List<Task> tasks) {
        taskAdapter.replaceDataSet(tasks);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setFilterTypeLabel(int resId) {
        filterLabelTextView.setText(getString(resId));
    }

    @Override
    public void onRefresh() {
        presenter.onSwipeRefreshLayout();
    }

    @Override
    public void goToAddTaskActivity() {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToEditTaskActivity(long editTaskId) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra(EditTaskActivity.EDITABLE_TASK_ID_KEY, editTaskId);
        startActivity(intent);
    }
}
