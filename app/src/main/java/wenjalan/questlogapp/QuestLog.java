package wenjalan.questlogapp;
// Represents an instance of the app
// Owns the instance of User

import android.util.Log;

import java.io.Serializable;

public class QuestLog implements Serializable {
// App Constants  //
    public static final double PERK_BONUS_MULTIPLIER = 0.05; // percent bonus per perk point
    public static final double NEXT_LEVEL_EXP_MULTIPLIER = 1.20; // how much the exp requirement for a level goes up by each level
    public static final int PERK_POINTS_PER_LEVEL = 1;  // how many perk points to give the user per level up
    public static final String DATA_FILE_NAME = "QuestLog.dat";

// Fields //
    private User user;

// Constructors //
    public QuestLog(String username) {
        // Create a new User
        this.user = new User(username);
    }

// Getters //
    // returns the User of this QuestLog
    public User getUser() {
        return this.user;
    }

}
