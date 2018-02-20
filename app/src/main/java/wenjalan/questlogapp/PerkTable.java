package wenjalan.questlogapp;
// Represents the User's Perks and their values

import android.util.Log;

import java.util.HashMap;

public class PerkTable {

// References //
    private User user;

// Fields //
    private HashMap<String, Integer> perks;

// Subclasses //
    // the Perks that exist in the perks HashMap
    // never use raw Strings as a key in the perks table, always use one of these constants
    public class Perks {
        public static final String UNUSED = "UNUSED";
        public static final String PHYSICAL = "PHYSICAL";
        public static final String MENTAL = "MENTAL";
        public static final String SOCIAL = "SOCIAL";
    }

// Constructor //
    public PerkTable(User user) {
        init();
        this.user = user;
    }

// Methods //
    // initialization
    private void init() {
        this.perks = new HashMap<String, Integer>();
        // set the starting value of all perks to 0
        this.perks.put(Perks.UNUSED, 0);
        this.perks.put(Perks.PHYSICAL, 0);
        this.perks.put(Perks.MENTAL, 0);
        this.perks.put(Perks.SOCIAL, 0);

        // Log for debugging purposes
        if (QuestLog.DEBUG) {
            Log.d("QuestLogApp", "Created new PerkTable for User " + user.getName());
        }
    }

    // adds points to a Perk
    public void addPoints(String perk, int points) {
        // check if the user had enough unused points
        if (points <= getPointsIn(Perks.UNUSED)) {
            // add points to the perk
            this.perks.put(perk, this.perks.get(perk) + points);
            // consume used points
            this.perks.put(Perks.UNUSED, perks.get(Perks.UNUSED) - points);

            // debugging
            if (QuestLog.DEBUG) {
                Log.d("QuestLogApp", "Added " + points + " points to " + perk);
            }
        }
        else {
            // debugging
            if (QuestLog.DEBUG) {
                Log.d("QuestLogApp", "Failed to add " + points + " points to " + perk + ", insufficient unused points.");
            }
        }
    }

    // adds unused Perk Points
    public void addUnusedPoints(int points) {
        this.perks.put(Perks.UNUSED, this.perks.get(Perks.UNUSED) + points);
    }

// Getters //
    // returns the amount of points in a given Perk
    public int getPointsIn(String perk) {
        return this.perks.get(perk);
    }

}
