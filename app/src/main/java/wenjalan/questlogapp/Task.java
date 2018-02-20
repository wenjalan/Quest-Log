package wenjalan.questlogapp;
// Represents a single subTask of a SideQuest

public class Task implements Completable {

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
