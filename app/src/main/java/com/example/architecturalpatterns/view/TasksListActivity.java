package com.example.architecturalpatterns.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.architecturalpatterns.Injection;
import com.example.architecturalpatterns.R;
import com.example.architecturalpatterns.contracts.TasksListContract;
import com.example.architecturalpatterns.models.Task;
import com.example.architecturalpatterns.models.TaskRepository;
import com.example.architecturalpatterns.presenters.TasksListPresenter;

import java.util.ArrayList;
import java.util.List;

public class TasksListActivity extends AppCompatActivity implements TasksListContract.View {

    private static final String TAG = TasksListActivity.class.getSimpleName();
    private static final String FILTER_KEY = "FILTER";

    private TaskRepository taskRepo = Injection.provideTaskRepository(this);

    private TasksListContract.Presenter presenter = new TasksListPresenter(this, taskRepo);

    private TaskAdapter taskAdapter = new TaskAdapter(new ArrayList<Task>(), presenter);

    private TextView filteringLabelTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tasks_list);

        FloatingActionButton addTaskFab = (FloatingActionButton) findViewById(R.id.fab_add_task);
        addTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.goToAddTaskActivity();
            }
        });

        filteringLabelTextView = (TextView) findViewById(R.id.tv_filtering_label);

        RecyclerView taskListRecView = (RecyclerView) findViewById(R.id.task_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecView.setLayoutManager(layoutManager);
        taskListRecView.setAdapter(taskAdapter);

        final SwipeRefreshLayout refresher = (SwipeRefreshLayout)
                findViewById(R.id.swipe_refresh_layout);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshTaskList();
                refresher.setRefreshing(false);
            }
        });

        if (savedInstanceState != null) {
            presenter.setFilterType((TaskFilterType) savedInstanceState.getSerializable(FILTER_KEY));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (presenter.getFilterType()) {
            case ALL_TASKS:
                presenter.displayAllTasks();
                break;
            case ACTIVE_TASKS:
                presenter.displayActiveTasks();
                break;
            case COMPLETED_TASKS:
                presenter.displayCompetedTasks();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter) {
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(),
                    findViewById(R.id.menu_filter));
            popupMenu.inflate(R.menu.filter_popup);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_all:
                            presenter.displayAllTasks();
                            break;
                        case R.id.menu_active:
                            presenter.displayActiveTasks();
                            break;
                        case R.id.menu_completed:
                            presenter.displayCompetedTasks();
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FILTER_KEY, presenter.getFilterType());
    }


    @Override
    public void displayTasks(List<Task> tasks) {
        taskAdapter.replaceDataSet(tasks);
    }

    @Override
    public void changeFilterTypeLabel(TaskFilterType filterType) {
        switch (filterType) {
            case ALL_TASKS:
                filteringLabelTextView.setText(R.string.label_all_tasks);
                break;
            case ACTIVE_TASKS:
                filteringLabelTextView.setText(R.string.label_active_tasks);
                break;
            case COMPLETED_TASKS:
                filteringLabelTextView.setText(R.string.label_completed_tasks);
                break;
        }
    }

    @Override
    public void goToAddTaskActivity() {
        Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToEditTaskActivity(long editTaskId) {
        Intent intent = new Intent(getApplicationContext(), EditTaskActivity.class);
        intent.putExtra("id", editTaskId);
        startActivity(intent);
    }
}
