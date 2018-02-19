package wenjalan.questlogapp;

import android.util.Log;

/**
 * Created by Alan on 2/17/2018.
 */

public class User {

// Fields //
    private String name;
    private QuestList quests;
    private PerkTable perks;
    private Level level;

    public User(String username) {
        init();
        this.name = name;
    }

// Methods //
    // initialization
    private void init() {
        quests = new QuestList(this);
        perks = new PerkTable();
        // level = new Level(this, perks);

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

}
