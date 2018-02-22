package wenjalan.questlogapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    // runs on startup
    public void start() {
        init();
        render();
    }

    // initialization
    public void init() {
        // create a new instance of QuestLog
        Home.questLog = new QuestLog("Alan Wen");

        // load stored info
        Home.questLog.loadFrom();

        // get references from the questLog
        this.user = Home.questLog.getUser();
    }

    // modifies on-screen content to match internal states
    public void render() {
        // update the user's info bar
        updateUserInfobar();
        // update the list of quests
        updateQuestList();
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
        // get the User's QuestList
        QuestList questList = user.getQuestList();
        // iterate through the list
        for (int i = 0; i < questList.quests(); i++) {
            SideQuest quest = questList.getQuest(i);
            displayQuest(quest);
        }
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
    }

}
