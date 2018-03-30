package wenjalan.questlogapp.questlog;
// A timed Quest, needs to be completed before a certain Timestamp

import android.util.Log;

public class TimedQuest extends SideQuest {

// Fields //
    private Timestamp completeByDate;

// Constructor //
    public TimedQuest(QuestList questList, String name, String description, int expReward, String perkCategory, Timestamp completeByTimestamp, Task... tasks) {
        super(questList, name, description, expReward, perkCategory, tasks);
        this.completeByDate = completeByTimestamp;
    }

// Methods //
    // check if this quest is expired before completing
    @Override
    protected void complete() {
        // check if this quest is expired
        if (this.isExpired()) {
            Log.d("QuestLog.System", "Couldn't complete SideQuest " + this.getName() + ", it's expired");
            return;
        }
        // complete this SideQuest
        super.complete();
    }

// Getters //
    // returns if this TimeQuest is expired
    public boolean isExpired() {
        // get now's timestamp
        Timestamp currentDate = new Timestamp();
        // check if the currentDate is greater than the completeByDate
        if (currentDate.getYear() <= completeByDate.getYear()) {
            if (currentDate.getMonth() <= completeByDate.getMonth()) {
                if (currentDate.getDay() <= completeByDate.getDay()) {
                    if (currentDate.getMinute() <= completeByDate.getMinute()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
