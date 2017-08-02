package com.example.architecturalpatterns.models;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.architecturalpatterns.data.Task;
import com.example.architecturalpatterns.sources.TaskRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FakeTaskRepository extends TaskRepository {

    private static final String TAG = FakeTaskRepository.class.getSimpleName();

    private static FakeTaskRepository INSTANCE = null;

    private static Map<Long, Task> tasks = new LinkedHashMap<>();

    private int curDatabaseId = 0;

    public FakeTaskRepository(Context context) {
        super(context);
    }

    public static TaskRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FakeTaskRepository(context);
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public Task getTaskById(long id) {
        return tasks.get(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getCompletedTasks() {
        List<Task> competedTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.isCompleted()) {
                competedTasks.add(task);
            }
        }
        return competedTasks;
    }

    @Override
    public List<Task> getActiveTasks() {
        List<Task> activeTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (!task.isCompleted()) {
                activeTasks.add(task);
            }
        }
        return activeTasks;
    }

    @Override
    public long insertTask(Task task) {
        task.setId(getCurDatabaseId());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public boolean updateTask(long id, Task task) {
        if (tasks.containsKey(id)) {
            task.setId(id);
            tasks.put(id, task);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeTask(long id) {
        if (tasks.remove(id) != null) {
            return true;
        }
        return false;
    }

    private int getCurDatabaseId() {
        return curDatabaseId++;
    }

}




