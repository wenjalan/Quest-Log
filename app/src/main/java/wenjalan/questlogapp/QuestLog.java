package wenjalan.questlogapp;
// Represents an instance of the app
// Owns User

import android.util.Log;

public class QuestLog {
// Global Fields //
    public static final boolean DEBUG = true;   // sets the app to debug mode

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
