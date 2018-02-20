package wenjalan.questlogapp;
// Represents an instance of the app
// Owns User

import android.util.Log;

public class QuestLog {
// App Constants  //
    public static final boolean DEBUG = true;   // sets the app to debug mode
    public static final double PERK_BONUS_MULTIPLIER = 0.05; // percent bonus per perk point
    public static final double NEXT_LEVEL_EXP_MULTIPLIER = 1.20; // how much the exp requirement for a level goes up by each level
    public static final int PERK_POINTS_PER_LEVEL = 1;  // how many perk points to give the user per level up

// Fields //
    private User user;

// Constructors //
    public QuestLog(String username) {
        this.user = new User(username);
        if (QuestLog.DEBUG) {
            start();
        }
    }

// Initialization //
    private void init() {
        // TODO: initialization
    }

// Methods //
    public void start() {
        Log.d("QuestLogApp", "Setup complete, running start() method.");
    }

}
