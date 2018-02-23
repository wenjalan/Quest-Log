package wenjalan.questlogapp;
// Represents an instance of the app
// Owns the instance of User

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
        // Create a new User
        this.user = new User(username);

        // if in debug mode, start test method
        if (QuestLog.DEBUG) {
            start();
        }
    }

// Methods //
    // loads data from a stored QuestLog app
    public void loadFrom() {
        // TODO: Actually make it load something
        return;
    }

    // debugging method, ran if app is set to DEBUG mode
    private void start() {
        Log.d("QuestLogApp", "*** Setup complete, running start() method ***");

        // get references to the User's things
        QuestList quests = user.getQuestList();
        PerkTable perks = user.getPerkTable();
        Level level = user.getLevel();

        // add 6 unused points
        perks.addUnusedPoints(6);

//        // spend points
//        perks.addPoints(PerkTable.Perks.PHYSICAL, 3);
//        perks.addPoints(PerkTable.Perks.MENTAL, 2);
//        perks.addPoints(PerkTable.Perks.SOCIAL, 1);

        // give some exp
        level.addExp(80, null);

//        // create a new quest to test
//        SideQuest quest = new SideQuest(
//                quests,
//                "Project 34",
//                "Complete your IA for IBCS SL",
//                80,
//                PerkTable.Perks.MENTAL
//        );
//
//        // add a few tasks
//        quest.addTasks(
//            new Task("Plan it"),
//            new Task("Code it"),
//            new Task("Write it")
//        );
//
//        // complete task 0
//        quest.completeTasks(0);
//
//        // add the quest to the questList
//        quests.addQuest(quest);
//        quests.addQuest(quest);
//        quests.addQuest(quest);
//        quests.addQuest(quest);
//        quests.addQuest(quest);
//        quests.addQuest(quest);
//
//        quests.getQuest(0).forceComplete();
    }

// Getters //
    // returns the User of this QuestLog
    public User getUser() {
        return this.user;
    }

}
