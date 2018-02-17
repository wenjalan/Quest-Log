package wenjalan.questlogapp;

/**
 * Created by Alan on 2/17/2018.
 */

public class User {

// Fields //
    private String name;
    private QuestList quests;
    private PerkTable perks;
    private Level level;

    public User(String username) {
        init();
        this.name = name;
    }

// Methods //
    // initialization
    public void init() {
        quests = new QuestList(this);
        perks = new PerkTable();
        // level = new Level(this, perks);
    }

}
