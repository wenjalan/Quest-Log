package wenjalan.questlogapp;
// Represents a User's list of quests

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestList implements Serializable {

// References //
    private User user;

// Fields //
    private ArrayList<SideQuest> questList;

// Constructor //
    public QuestList(User user) {
        init();
        this.user = user;

        // Log for debugging purposes
        Log.d("QuestLog.System", "Created new QuestList for User " + user.getName());
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
            // reward exp
            user.getLevel().addExp(exp, perk);
            // remove quest
            questList.remove(quest);
            Log.d("QuestLog.System", "Rewarding user " + user.getName() + " for SideQuest " + quest.getName() + "...");
        }
        else {
            Log.d("QuestLog.System", "Tried to reward user " + user.getName() + " for SideQuest " + quest.getName() + ", but it wasn't completed");
        }
    }

// Setters //
    // adds a SideQuest given a SideQuest
    public void addQuest(SideQuest quest) {
        questList.add(quest);
        Log.d("QuestLog.System", "Added SideQuest " + quest.getName() + " to " + user.getName() + "'s QuestList");
    }

    // replaces a SideQuest at a given index with another SideQuest
    public void replaceQuest(int index, SideQuest quest) {
        questList.set(index, quest);
    }

    // removes a SideQuest given a SideQuest
    public void removeQuest(SideQuest quest) {
        questList.remove(quest);
        Log.d("QuestLog.System", "Removed SideQuest " + quest.getName() + " from " + user.getName() + "'s QuestList");
    }

    // removes a SideQuest given an index
    public void removeQuest(int index) {
        questList.remove(index);
        Log.d("QuestLog.System", "Removed SideQuest at index " + index + " from " + user.getName() + "'s QuestList");
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

    // returns the number of Quests in this QuestList
    public int quests() {
        return this.questList.size();
    }
}
