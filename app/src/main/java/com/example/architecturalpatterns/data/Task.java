package com.example.architecturalpatterns.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {

    private long id;
    private String title;
    private String desc;
    private boolean completed;

    public Task(long id, String title, String desc, boolean completed) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.completed = completed;
    }

    protected Task(Parcel in) {
        id = in.readLong();
        title = in.readString();
        desc = in.readString();
        completed = in.readByte() != 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeByte((byte) (completed ? 1 : 0));
    }
}
