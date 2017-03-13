package com.example.architecturalpatterns;

import android.content.Context;

import com.example.architecturalpatterns.models.TaskRepository;

public final class Injection {
    public static TaskRepository provideTaskRepository(Context context) {
        return TaskRepository.getInstance(context);
    }
}
