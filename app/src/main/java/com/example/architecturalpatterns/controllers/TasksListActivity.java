package com.example.architecturalpatterns.controllers;

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

import com.example.architecturalpatterns.R;
import com.example.architecturalpatterns.adapters.TaskAdapter;
import com.example.architecturalpatterns.models.Task;
import com.example.architecturalpatterns.models.TaskRepository;

import java.util.ArrayList;

public class TasksListActivity extends AppCompatActivity {

    private static final String TAG = TasksListActivity.class.getSimpleName();
    private static final String FILTER_KEY = "FILTER";

    private enum FilterType {
        ALL_TASKS,
        ACTIVE_TASKS,
        COMPLETED_TASKS;
    }

    private FilterType curFilter = FilterType.ALL_TASKS;

    private TaskRepository taskRepo = TaskRepository.getInstance(this);
    private TaskAdapter taskAdapter = new TaskAdapter(new ArrayList<Task>());

    private TextView filteringLabelTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tasks_list);

        FloatingActionButton addTaskFab = (FloatingActionButton) findViewById(R.id.fab_add_task);
        addTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
                startActivity(intent);
            }
        });

        filteringLabelTextView = (TextView) findViewById(R.id.tv_filtering_label);

        RecyclerView taskListRecView = (RecyclerView) findViewById(R.id.task_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskListRecView.setLayoutManager(layoutManager);
        taskListRecView.setAdapter(taskAdapter);

        final SwipeRefreshLayout swiper = (SwipeRefreshLayout)
                findViewById(R.id.swipe_refresh_layout);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayTasks(curFilter);
                swiper.setRefreshing(false);
            }
        });

        if (savedInstanceState != null) {
            curFilter = (FilterType) savedInstanceState.getSerializable(FILTER_KEY);
        }
        displayTasks(curFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayTasks(curFilter);
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
                            displayTasks(FilterType.ALL_TASKS);
                            break;
                        case R.id.menu_active:
                            displayTasks(FilterType.ACTIVE_TASKS);
                            break;
                        case R.id.menu_completed:
                            displayTasks(FilterType.COMPLETED_TASKS);
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
        outState.putSerializable(FILTER_KEY, curFilter);
    }

    private void displayTasks(FilterType filter) {
        switch (filter) {
            case ALL_TASKS:
                taskAdapter.replaceDataSet(taskRepo.getAllTasks());
                curFilter = FilterType.ALL_TASKS;
                filteringLabelTextView.setText(R.string.label_all_tasks);
                break;
            case ACTIVE_TASKS:
                taskAdapter.replaceDataSet(taskRepo.getActiveTasks());
                curFilter = FilterType.ACTIVE_TASKS;
                filteringLabelTextView.setText(R.string.label_active_tasks);
                break;
            case COMPLETED_TASKS:
                taskAdapter.replaceDataSet(taskRepo.getCompletedTasks());
                curFilter = FilterType.COMPLETED_TASKS;
                filteringLabelTextView.setText(R.string.label_completed_tasks);
                break;
        }
    }
}
