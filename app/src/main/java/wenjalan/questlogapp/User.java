package wenjalan.questlogapp;

import android.util.Log;

public class User {

// Fields //
    private String name;
    private QuestList quests;
    private PerkTable perks;
    private Level level;

// Constructor //
    public User(String username) {
        // set name before init because init uses name
        this.name = username;
        init();
    }

// Methods //
    // initialization
    private void init() {
        quests = new QuestList(this);
        perks = new PerkTable(this);
        level = new Level(this, perks);

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Created new User " + name);
        }
    }

// Getters //
    // returns the name of this User
    public String getName() {
        return this.name;
    }

    // returns this User's QuestList
    public QuestList getQuestList() {
        return this.quests;
    }

    // returns this User's Level
    public Level getLevel() {
        return this.level;
    }

    // returns this User's PerkTable
    public PerkTable getPerkTable() {
        return this.perks;
    }

}
