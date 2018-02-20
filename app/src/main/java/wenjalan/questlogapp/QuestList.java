package wenjalan.questlogapp;
// Represents a User's list of quests

import android.util.Log;

import java.util.ArrayList;

public class QuestList {

// References //
    private User user;

// Fields //
    private ArrayList<SideQuest> questList;

// Constructor //
    public QuestList(User user) {
        init();
        this.user = user;

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Created new QuestList for User " + user.getName());
        }
    }

// Methods //
    // initialization
    private void init() {
        questList = new ArrayList<>();
    }

    // grants exp to the user
    // called from a SideQuest upon completion
    public void rewardUserFor(SideQuest quest) {
        int exp = quest.getExpReward();
        String perk = quest.getPerkCategory();
        if (quest.isComplete()) {
            user.getLevel().addExp(exp, perk);

            // debug
            if (QuestLog.DEBUG) {
                Log.d("QuestLogApp", "Rewarding user " + user.getName() + " for SideQuest " + quest.getName() + "...");
            }
        }
        else {
            if (QuestLog.DEBUG) {
                Log.d("QuestLogApp", "Tried to reward user " + user.getName() + " for SideQuest " + quest.getName() + ", but it wasn't completed");
            }
        }
    }

// Setters //
    // adds a SideQuest given a SideQuest
    public void addQuest(SideQuest quest) {
        questList.add(quest);

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Added SideQuest " + quest.getName() + " to " + user.getName() + "'s QuestList");
        }
    }

    // removes a SideQuest given a SideQuest
    public void removeQuest(SideQuest quest) {
        questList.remove(quest);

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Removed SideQuest " + quest.getName() + " from " + user.getName() + "'s QuestList");
        }
    }

    // removes a SideQuest given an index
    public void removeQuest(int index) {
        questList.remove(index);

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Removed SideQuest at index " + index + " from " + user.getName() + "'s QuestList");
        }
    }

// Getters //
    // returns a SideQuest given an index
    public SideQuest getQuest(int index) {
        return questList.get(index);
    }

    // returns the owner of this QuestList
    public User getOwner() {
        return this.user;
    }

    // toString, meant for debugging purposes only
    @Override
    public String toString() {
        if (questList.size() == 0) {
            return "NO QUESTS ADDED";
        }
        String r = "";
        // iterate through the questList
        for (int i = 0; i < questList.size(); i++) {
            r += questList.get(i).toString() + "\n";
        }
        return r;
    }

}
