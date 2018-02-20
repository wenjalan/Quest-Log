package wenjalan.questlogapp;
// Represents a SideQuest within a QuestList

import android.util.Log;

import java.util.ArrayList;

public class SideQuest implements Completable {

// References
    private QuestList questList;

// Fields //
    private String name;                    // the name or title of this SideQuest
    private String description;             // the description of this SideQuest
    private int expReward;                  // how much EXP this quest will yield
    private String perkCategory;            // what Perk this SideQuest belongs to
    private boolean isComplete;             // whether or not this SideQuest is complete
    private ArrayList<Task> tasks;          // the list of Tasks to complete this SideQuest

// Constructor //
    // TODO: Make SideQuest auto-add itself to a QuestList upon creation?
    public SideQuest(QuestList questList, String name, String description, int expReward) {
        init();
        this.questList = questList;
        this.name = name;
        this.description = description;
        this.expReward = expReward;

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Created new SideQuest " + this.name);
        }
    }

    public SideQuest(QuestList questList, String name, String description, int expReward, String perkCategory) {
        init();
        this.questList = questList;
        this.name = name;
        this.description = description;
        this.expReward = expReward;
        this.perkCategory = perkCategory;

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Created new SideQuest " + this.name);
        }
    }

    public SideQuest(QuestList questList, String name, String description, int expReward, String perkCategory, Task... tasks) {
        init();
        this.questList = questList;
        this.name = name;
        this.description = description;
        this.expReward = expReward;
        this.perkCategory = perkCategory;
        this.addTasks(tasks);

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Created new SideQuest " + this.name);
        }
    }

// Methods //
    // initializes this SideQuest with default values
    private void init() {
        this.name = "Side Quest";
        this.description = "A perfectly normal Side Quest.";
        this.expReward = 0;
        this.perkCategory = null;
        this.isComplete = false;
        this.tasks = new ArrayList<Task>();
    }

    // completes this SideQuest
    // should only be called once all Tasks are complete
    public void complete() {
        this.isComplete = true;
        questList.rewardUserFor(this);
    }

    // force completes a SideQuest if in debug mode
    // dev use only
    public void forceComplete() {
        if (QuestLog.DEBUG) {
            complete();
        }
    }

// Setters //
    // adds Task(s) to this SideQuest
    public void addTasks(Task... tasks) {
        for (Task t : tasks) {
            this.tasks.add(t);
        }

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Added task(s) to SideQuest " + this.name);
        }
    }

    // completes Task(s) of this SideQuest
    public void completeTasks(int... indexes) {
        // complete the Tasks requested
        for (int i : indexes) {
            tasks.get(i).complete();
        }

        // if all Tasks in this SideQuest have been completed
        if (allTasksAreComplete()) {
            // complete this SideQuest
            complete();
        }
    }

    // checks whether all Tasks in this SideQuest have been completed
    private boolean allTasksAreComplete() {
        // if the SideQuest has tasks, check
        if (tasks.size() > 0) {
            // iterate through all the tasks
            for (int i = 0; i < tasks.size(); i++) {
                // if a Task is found to be incomplete
                if (!tasks.get(i).isComplete()) {
                    // return false
                    return false;
                }
            }
        }
        // return true if no Tasks were incomplete
        return true;
    }

// Getters //
    // returns a Task of this SideQuest given an index
    public Task getTask(int index) {
        return tasks.get(index);
    }

    // returns the number of Tasks this SideQuest has
    public int tasks() {
        return this.tasks.size();
    }

    // returns the name of this SideQuest
    public String getName() {
        return this.name;
    }

    // returns the description of this SideQuest
    public String getDescription() {
        return this.description;
    }

    // returns the EXP reward for this SideQuest
    public int getExpReward() {
        return this.expReward;
    }

    // returns the Perk this SideQuest is associated with
    // TODO: Ensure it works with no perk category
    public String getPerkCategory() {
        return this.perkCategory;
    }

    // returns whether this SideQuest has been completed (all Tasks completed)
    @Override
    public boolean isComplete() {
        return this.isComplete;
    }

}
