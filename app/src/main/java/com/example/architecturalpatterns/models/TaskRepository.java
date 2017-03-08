package com.example.architecturalpatterns.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.architecturalpatterns.models.TasksDbHelper.TasksTable.*;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private static final String TAG = TaskRepository.class.getSimpleName();

    private static TaskRepository INSTANCE = null;

    private TasksDbHelper dbOpenHelper;

    private TaskRepository(Context context) {

        this.dbOpenHelper = new TasksDbHelper(context);
    }

    public static TaskRepository getInstance(Context context) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        return new TaskRepository(context);
    }

    @Nullable
    public Task getTaskById(long id) {
        List<Task> tasks = null;
        String queryClause = "WHERE " + COLUMN_ID + "=" + String.valueOf(id);
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
        return getTasksWithClause("WHERE " + COLUMN_COMPLETED + "=1");
    }

    public List<Task> getActiveTasks() {
        return getTasksWithClause("WHERE " + COLUMN_COMPLETED + "=0");
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
            db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, task.getTitle());
            values.put(COLUMN_DESCRIPTION, task.getDesc());
            values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
            rowId = db.insert(TABLE_NAME, null, values);
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
            db = dbOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, task.getTitle());
            values.put(COLUMN_DESCRIPTION, task.getDesc());
            values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
            countAffectedRows = db.update(TABLE_NAME, values,
                    COLUMN_ID + "=?", new String[]{String.valueOf(id)});
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
            db = dbOpenHelper.getWritableDatabase();
            countDeletedRows = db.delete(TABLE_NAME, COLUMN_ID + "=?",
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

        String query = "SELECT * FROM " + TABLE_NAME;
        if (clause != null) {
            query += " " + clause;
        }
        query += ";";

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbOpenHelper.getReadableDatabase();

            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
                int descIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                int completedIndex = cursor.getColumnIndex(COLUMN_COMPLETED);
                do {
                    Task tmpTask = new Task();
                    tmpTask.setId(cursor.getLong(idIndex));
                    tmpTask.setTitle(cursor.getString(titleIndex));
                    tmpTask.setDesc(cursor.getString(descIndex));
                    tmpTask.setCompleted((cursor.getInt(completedIndex) == 1));
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




