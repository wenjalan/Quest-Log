package wenjalan.questlogapp;
// Represents a User's list of quests
/**
 * Created by Alan on 2/17/2018.
 */
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
    }

// Methods //
    // initialization
    private void init() {
        questList = new ArrayList<>();

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Created new QuestList for User " + user.getName());
        }
    }

    // grants exp to the user
    // called from a SideQuest upon completion
    public void rewardUserFor(SideQuest quest) {
        // TODO: write this
    }

// Setters //
    // adds a SideQuest given a SideQuest
    public void addQuest(SideQuest quest) {
        questList.add(quest);

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Added SideQuest " + quest.getName() + " to " + user.getName() + "'s QuestList.");
        }
    }

    // removes a SideQuest given a SideQuest
    public void removeQuest(SideQuest quest) {
        questList.remove(quest);

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Removed SideQuest " + quest.getName() + " from " + user.getName() + "'s QuestList.");
        }
    }

    // removes a SideQuest given an index
    public void removeQuest(int index) {
        questList.remove(index);

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Removed SideQuest at index " + index + " from " + user.getName() + "'s QuestList.");
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
