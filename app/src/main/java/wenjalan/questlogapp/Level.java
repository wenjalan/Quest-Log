package wenjalan.questlogapp;
// Represents a User's Level and EXP system

import android.util.Log;

import java.io.Serializable;

public class Level implements Serializable {

// References //
    private User user;
    private PerkTable perks;
    private final double PERK_BONUS_MULT = QuestLog.PERK_BONUS_MULTIPLIER;
    private final double NEXT_LEVEL_EXP_MULT = QuestLog.NEXT_LEVEL_EXP_MULTIPLIER;
    private final int PERK_POINTS_PER_LEVEL = QuestLog.PERK_POINTS_PER_LEVEL;

// Fields //
    private int userLevel;                  // user's current level
    private int userExperience;             // user's exp at their current level
    private int nextLevelExperience;        // EXP needed to reach the next level

// Constructors //
    public Level(User user, PerkTable perkTable) {
        init();
        this.user = user;
        this.perks = perkTable;
        Log.d("QuestLog.System", "Created new Level for User " + user.getName());
    }

// Methods //
    // initialization
    private void init() {
        this.userLevel = 1;                 // User starts at level 1
        this.userExperience = 0;            // User starts with 0 EXP
        this.nextLevelExperience = 100;     // EXP to get to next level is 100
    }

    // adds an amount of EXP to this user's Level
    public void addExp(int exp, String perk) {
        // debugging
        int sourceExp = exp;

        // if the EXP comes with a perk, get the bonus
        if (perk != null) {
            exp += getPerkBonusExp(exp, perk);
        }

        // debugging
        int bonusExp = exp - sourceExp;

        // add the experience to the User's total
        this.userExperience += exp;

        // more debugging
        int levelUps = 0;

        // level up the user
        while (this.userExperience >= this.nextLevelExperience) {
            // consume the EXP needed to reach the next level
            userExperience -= nextLevelExperience;
            // level up
            levelUp();

            // debugging
            levelUps++;
        }

        // print the exp gain
        Log.d("QuestLog.System", "Granted user " + user.getName() + " " + exp + " EXP (" + bonusExp + " BONUS)");

        // print the levelups
        while (levelUps > 0) {
            Log.d("QuestLog.System", "User leveled up (" + (this.getLevel() - 1) + " -> " + this.getLevel() + ")");
            levelUps--;
        }
    }

    // returns the amount of exp from the perk bonus
    private int getPerkBonusExp(int exp, String perk) {
        return (int) (exp * (perks.getPointsIn(perk) * PERK_BONUS_MULT));
    }

    // levels up the user
    private void levelUp() {
        // increment user's level by 1
        this.userLevel++;

        // add an unused Perk Point
        perks.addUnusedPoints(PERK_POINTS_PER_LEVEL);

        // increase the EXP needed to reach the next level
        this.nextLevelExperience *= NEXT_LEVEL_EXP_MULT;
    }

// Getters //
    // returns the User's level
    public int getLevel() {
        return this.userLevel;
    }

    // returns the user's experience
    public int getExp() {
        return this.userExperience;
    }

    // returns the required exp for the next level
    public int getExpToNextLevel() { return this.nextLevelExperience; }

    // returns the progress towards the next level as a percent
    public int getLevelProgress() {
        double progress = (double) this.getExp() / (double) this.getExpToNextLevel();
        int percent = (int) (100.0 * progress);
        // prevent animation skips
        if (percent == 0) {
            return 1;
        }
        else {
            return percent;
        }
    }

    // returns the owner of this Level
    public User getOwner() {
        return this.user;
    }

}
