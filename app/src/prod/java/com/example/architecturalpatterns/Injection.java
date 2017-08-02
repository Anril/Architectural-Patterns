package com.example.architecturalpatterns;

import android.content.Context;

import com.example.architecturalpatterns.sources.TaskRepository;

public final class Injection {
    public static TaskRepository provideTaskRepository(Context context) {
        return TaskRepository.getInstance(context);
    }
}
