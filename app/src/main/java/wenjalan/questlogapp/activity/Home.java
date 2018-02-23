package wenjalan.questlogapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import wenjalan.questlogapp.Level;
import wenjalan.questlogapp.PerkTable;
import wenjalan.questlogapp.QuestList;
import wenjalan.questlogapp.QuestLog;
import wenjalan.questlogapp.R;
import wenjalan.questlogapp.SideQuest;
import wenjalan.questlogapp.Task;
import wenjalan.questlogapp.User;

public class Home extends AppCompatActivity {

// Fields //
    // TODO: Make it a Parcelable instead of a global?
    public static QuestLog questLog;

// References //
    public User user;

// Methods //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        render();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }

    // runs on startup
    public void start() {
        init();
        render();
    }

    // initialization
    private void init() {
        // TODO: Load saved data
        if (QuestLog.CLEAN_BOOT) {
            // create a new instance of QuestLog
            Home.questLog = new QuestLog("Alan Wen");
        }
        else {
            // load QuestLog from storage
            load();
        }

        // get references from the questLog
        this.user = Home.questLog.getUser();
    }

    // modifies on-screen content to match internal states
    private void render() {
        // update the user's info bar
        updateUserInfobar();
        // update the list of quests
        updateQuestList();
    }

    // loads the user's QuestLog from storage
    private void load() {
        // TODO: load data
    }

    // saves the user's QuestLog to storage
    private void save() {
        // TODO: Save data
    }

    // updates the userInfoBar in the layout
    private void updateUserInfobar() {
        updateUserLevelText();
        updateUserEXPBar();
    }

    // updates the user's level text on the homepage
    private void updateUserLevelText() {
        // get a reference to the TextView
        TextView userLevelTextView = findViewById(R.id.userLevelTextView);
        // get a reference to the User's Level
        Level userLevel = this.user.getLevel();
        // get the User's current level
        int level = userLevel.getLevel();
        // update the text
        userLevelTextView.setText("Level " + level);
    }

    // updates the user's EXP bar on the homepage
    private void updateUserEXPBar() {
        // get a reference to the ProgressBar
        ProgressBar expBar = findViewById(R.id.userExpBar);
        // get a reference to the User's Level
        Level userLevel = user.getLevel();
        // get the user's current progress towards their next level
        // TODO: Use Level.getLevelProgress instead
        int progress = (int) ((double) userLevel.getExp() / (double) userLevel.getExpToNextLevel() * 100);
        // update the progress
        expBar.setProgress(progress);

        // debug
        if (QuestLog.DEBUG) {
            Log.d("Home", "Set EXP bar progress to " + progress + " percent");
        }
    }

    // updates the questListLinearLayout with all SideQuest instances
    private void updateQuestList() {
        // clear the existing list, if it exists
        clearQuestViews();
        // get the User's QuestList
        QuestList questList = user.getQuestList();
        // iterate through the list
        for (int i = 0; i < questList.quests(); i++) {
            SideQuest quest = questList.getQuest(i);
            if (!quest.isComplete()) {
                displayQuest(quest);
            }
        }
    }

    // clears the SideQuests off the screen to refresh
    private void clearQuestViews() {
        LinearLayout questList = findViewById(R.id.questListLinearLayout);
        questList.removeAllViews();
    }

    // displays a single sideQuest on the homepage
    private void displayQuest(SideQuest sideQuest) {
        // get a reference to the questListLinearLayout
        LinearLayout questList = findViewById(R.id.questListLinearLayout);
        // get an inflater
        LayoutInflater inflater = getLayoutInflater();

        // create a new View to hold the SideQuest in
        View questView = inflater.inflate(R.layout.fragment_sidequest, questList, false);

        // add the view
        questList.addView(questView);

        // edit the quest's fields to match those in the user's QuestList
        // title
        TextView title = questView.findViewById(R.id.sideQuestTitle);
        title.setText(sideQuest.getName());

        // description
        TextView desc = questView.findViewById(R.id.sideQuestDesc);
        desc.setText(sideQuest.getDescription());

        // perk
        TextView perk = questView.findViewById(R.id.sideQuestPerk);
        String questPerk = sideQuest.getPerkCategory();
        if (questPerk != null) {
            perk.setText(questPerk);
        }
        else {
            perk.setText("");
        }

        // EXP reward
        TextView expText = questView.findViewById(R.id.sideQuestEXP);
        expText.setText("" + sideQuest.getExpReward() + " EXP"); // have to explicitly convert it to String

        // tasks
        // get the task list Linear Layout
        LinearLayout taskList = questView.findViewById(R.id.sideQuestTaskList);
        // add the tasks
        for (int i = 0; i < sideQuest.tasks(); i++) {
            // get the current task
            Task task = sideQuest.getTask(i);
            // inflate a view
            View taskView = inflater.inflate(R.layout.fragment_task, taskList, false);

            // add the task
            taskList.addView(taskView);
            // edit the task's desc
            TextView taskDesc = taskView.findViewById(R.id.taskDesc);
            taskDesc.setText(task.getDescription());
        }

    }


// Button Listeners //
    // called when the user taps the newQuestButton
    public void newQuest(View view) {
        Log.d("Home", "User tapped newQuestButton");
        Intent i = new Intent(this, CreateQuest.class);
        startActivity(i);
    }

    // called when the user taps the userProfileButton
    public void loadProfile(View view) {
        Log.d("Home", "User tapped userProfileButton");
        Intent i = new Intent(this, Profile.class);
        startActivity(i);
    }

    // called when the User completes a Task
    public void toggleTaskStatus(View view) {
        // TODO: Make this less bad
        // get the index of this Task in the SideQuest and TaskList (should match)
        ConstraintLayout taskConstraintLayout = (ConstraintLayout) view.getParent();
        LinearLayout taskLayout = (LinearLayout) taskConstraintLayout.getParent();
        LinearLayout taskList = (LinearLayout) taskLayout.getParent();
        int taskIndex = taskList.indexOfChild(taskLayout);

        // get the index of the SideQuest this Task belongs to
        ConstraintLayout sideQuestConstraint = (ConstraintLayout) taskList.getParent();
        LinearLayout sideQuestLinearLayout = (LinearLayout) sideQuestConstraint.getParent();
        LinearLayout questListLinearLayout = (LinearLayout) sideQuestLinearLayout.getParent();
        int questIndex = questListLinearLayout.indexOfChild(sideQuestLinearLayout);

        // Get the state of the checkbox
        // TODO: Make this better
        CheckBox taskCheckBox = ((View) view.getParent()).findViewById(R.id.taskCheckBox);
        boolean status = taskCheckBox.isChecked();

        // Debug
        Log.d("Home", "Attempting to complete task index " + taskIndex + " of SideQuest index " + questIndex);

        // set the Task's status in the backend
        QuestList questList = user.getQuestList();
        SideQuest sideQuest = questList.getQuest(questIndex);
        sideQuest.completeTasks(taskIndex);

        // check if all tasks are complete
        if (sideQuest.isComplete()) {
            render();
        }
    }

}
