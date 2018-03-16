package wenjalan.questlogapp;
// Represents a single subTask of a SideQuest

import android.util.Log;

import java.io.Serializable;

public class Task implements Completable, Serializable {

// Fields //
    private String description;
    private boolean isComplete;

// Constructor //
    public Task(String description) {
        init();
        this.description = description;
    }

// Methods //
    // initialization
    private void init() {
        this.isComplete = false;
    }

// Setters //
    // completes this Task
    public void complete() {
        this.isComplete = true;
        Log.d("QuestLog.System", "Completed Task \"" + this.description + "\"");

    }

    // uncompletes this TAsk
    public void uncomplete() {
        this.isComplete = false;
        Log.d("QuestLog.System", "Uncompleted Task \"" + this.description + "\"");
    }

// Getters //
    // returns whether this Task is complete
    @Override
    public boolean isComplete() {
        return this.isComplete;
    }

    // returns the description of this SideQuest
    public String getDescription() {
        return this.description;
    }
}
