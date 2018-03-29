package wenjalan.questlogapp.questlog;
// A timed Quest, needs to be completed before a certain Timestamp

public class TimedQuest extends SideQuest {

    public TimedQuest(QuestList questList, String name, String description, int expReward, String perkCategory, Task... tasks) {
        super(questList, name, description, expReward, perkCategory, tasks);
    }

}
