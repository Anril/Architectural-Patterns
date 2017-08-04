package com.example.architecturalpatterns.sources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.example.architecturalpatterns.data.Task;
import com.example.architecturalpatterns.sources.TasksDbHelper.TasksTable;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private static final String TAG = TaskRepository.class.getSimpleName();

    private static TaskRepository INSTANCE;

    private TasksDbHelper dbHelper;

    public TaskRepository(Context context) {
        this.dbHelper = new TasksDbHelper(context);
    }

    public static TaskRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TaskRepository(context);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Nullable
    public Task getTaskById(long id) {
        List<Task> tasks = null;
        String queryClause = "WHERE " + TasksTable.COLUMN_ID + "=" + String.valueOf(id);
        tasks = getTasksWithClause(queryClause);
        if (tasks.size() > 0) {
            return tasks.get(0);
        }
        return null;
    }

    public List<Task> getAllTasks() {
        return getTasksWithClause(null);
    }

    public List<Task> getCompletedTasks() {
        return getTasksWithClause("WHERE " + TasksTable.COLUMN_COMPLETED + "=1");
    }

    public List<Task> getActiveTasks() {
        return getTasksWithClause("WHERE " + TasksTable.COLUMN_COMPLETED + "=0");
    }

    /**
     * @param task Task to insert.
     * @return Row id of the inserted task, -1 if an error occurred.
     * @throws SQLException
     */
    public long insertTask(Task task) {
        long rowId = 0;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TasksTable.COLUMN_TITLE, task.getTitle());
            values.put(TasksTable.COLUMN_DESCRIPTION, task.getDesc());
            values.put(TasksTable.COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
            rowId = db.insert(TasksTable.TABLE_NAME, null, values);
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
            } catch (Exception ignoredException) {
                /* Intentionally swallowed exception */
            }
        }
        return rowId;
    }

    public boolean updateTask(long id, Task task) {
        int countAffectedRows = 0;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TasksTable.COLUMN_TITLE, task.getTitle());
            values.put(TasksTable.COLUMN_DESCRIPTION, task.getDesc());
            values.put(TasksTable.COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
            countAffectedRows = db.update(TasksTable.TABLE_NAME, values,
                    TasksTable.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
            } catch (Exception ignoredException) {
                /* Intentionally swallowed exception */
            }
        }
        return countAffectedRows > 0;
    }

    public boolean removeTask(long id) {
        int countDeletedRows = 0;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            countDeletedRows = db.delete(TasksTable.TABLE_NAME, TasksTable.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)});
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
            } catch (Exception ignoredException) {
                /* Intentionally swallowed exception */
            }
        }
        return countDeletedRows > 0;
    }

    /**
     * @param clause This parameter should contains a clause of the SQL query.
     *               For example: "WHERE salary > 3000".
     * @return List of the tasks.
     * @throws SQLException
     */
    private List<Task> getTasksWithClause(@Nullable String clause) {
        List<Task> tasks = new ArrayList<>();

        String query = "SELECT * FROM " + TasksTable.TABLE_NAME;
        if (clause != null) {
            query += " " + clause;
        }
        query += ";";

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();

            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(TasksTable.COLUMN_ID);
                int titleIndex = cursor.getColumnIndex(TasksTable.COLUMN_TITLE);
                int descIndex = cursor.getColumnIndex(TasksTable.COLUMN_DESCRIPTION);
                int completedIndex = cursor.getColumnIndex(TasksTable.COLUMN_COMPLETED);
                do {
                    Task tmpTask = new Task(cursor.getLong(idIndex),
                            cursor.getString(titleIndex),
                            cursor.getString(descIndex),
                            cursor.getInt(completedIndex) == 1);
                    tasks.add(tmpTask);
                } while (cursor.moveToNext());
            }
        } finally {
            try {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception ignoredException) {
                /* Intentionally swallowed exception */
            }
            try {
                if (db != null) {
                    db.close();
                }
            } catch (Exception ignoredException) {
                /* Intentionally swallowed exception */
            }
        }
        return tasks;
    }
}




