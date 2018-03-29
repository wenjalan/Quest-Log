package wenjalan.questlogapp.questlog;

/**
 * Created by Alan on 3/28/2018.
 */

public class TimedQuest extends SideQuest {

    public TimedQuest(QuestList questList, String name, String description, int expReward, String perkCategory, Task... tasks) {
        super(questList, name, description, expReward, perkCategory, tasks);
    }

}
