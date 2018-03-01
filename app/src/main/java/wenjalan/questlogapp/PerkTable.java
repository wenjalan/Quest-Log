package wenjalan.questlogapp;
// Represents the User's Perks and their values

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

public class PerkTable implements Serializable {

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
        Log.d("QuestLog.System", "Created new PerkTable for User " + user.getName());
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
    }

    // adds points to a Perk
    public void addPoints(String perk, int points) {
        // check if the user had enough unused points
        if (points <= getPointsIn(Perks.UNUSED)) {
            // add points to the perk
            this.perks.put(perk, this.perks.get(perk) + points);
            // consume used points
            this.perks.put(Perks.UNUSED, perks.get(Perks.UNUSED) - points);

            Log.d("QuestLog.System", "Added " + points + " points to " + perk);
        }
        else {
            Log.d("QuestLog.System", "Failed to add " + points + " points to " + perk + ", insufficient unused points.");
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

    // returns the bonus percent granted by a given Perk
    public int getBonusPercent(String perk) {
        double points = this.perks.get(perk);
        int percent = (int) ((points * QuestLog.PERK_BONUS_MULTIPLIER) * 100);
        return percent;
    }

}
