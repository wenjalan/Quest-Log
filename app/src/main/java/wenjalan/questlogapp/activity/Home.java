package wenjalan.questlogapp.activity;
// The Home screen where the user's quests are displayed
// Acts as the main activity, allowing access to Profile and CreateQuest

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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
    public static QuestLog questLog;

// References //
    public User user;

// Android Events //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        start();
        Log.d("QuestLog.Android", "CREATED activity Home");
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
        Log.d("QuestLog.Android", "PAUSED activity Home");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // load();
        render();
        Log.d("QuestLog.Android", "RESUMED activity Home");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("QuestLog.Android", "DESTROYED activity Home");
    }

// Methods //
    // runs on startup
    public void start() {
        init();
        render();
    }

    // initialization
    private void init() {
        // load saved data
        load();
        // get references from the questLog
        this.user = Home.questLog.getUser();
    }

    // saves app data to storage
    private void save() {
        FileOutputStream outputStream;
        ObjectOutputStream objectOutputStream;

        // output the QuestLog to storage
        try {
            // initialize
            outputStream = openFileOutput(QuestLog.DATA_FILE_NAME, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(outputStream);

            // write the QuestLog to the data file
            objectOutputStream.writeObject(this.questLog);

            // close the streams
            objectOutputStream.close();
            outputStream.close();

            // log
            Log.d("Home", "Saved QuestLog to storage");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Home", "Failed to save QuestLog to storage");
        }
    }

    // loads app data from storage
    private void load() {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;

        // load the QuestLog from storage
        try {
            // initialize
            fileInputStream = openFileInput(QuestLog.DATA_FILE_NAME);
            objectInputStream = new ObjectInputStream(fileInputStream);

            // read the QuestLog from storage
            this.questLog = (QuestLog) objectInputStream.readObject();

            // close
            objectInputStream.close();
            fileInputStream.close();

            // log
            Log.d("Home", "Loaded QuestLog from storage");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Home", "Failed to load QuestLog from storage, creating new instance...");
            this.questLog = new QuestLog("AlphaUser");
        }
    }

    // modifies on-screen content to match internal states
    private void render() {
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
        int progress = this.user.getLevel().getLevelProgress();
        // update the progress
        expBar.setProgress(progress);
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
        // add the view to the list
        questList.addView(questView);

        // title
        displayQuestTitle(questView, sideQuest.getName());

        // description
        displayQuestDesc(questView, sideQuest.getDescription());

        // perk
        displayQuestPerk(questView, sideQuest.getPerkCategory());

        // EXP reward
        displayQuestReward(questView, sideQuest.getExpReward());

        // tasks
        displayQuestTasks(questView, inflater, sideQuest);

    }

    // displays the title of a Quest
    private void displayQuestTitle(View questView, String questTitle) {
        TextView title = questView.findViewById(R.id.sideQuestTitle);
        title.setText(questTitle);
    }

    // displays the description of a Quest
    private void displayQuestDesc(View questView, String questDesc) {
        TextView desc = questView.findViewById(R.id.sideQuestDesc);
        desc.setText(questDesc);
    }

    // displays the perk of a Quest
    private void displayQuestPerk(View questView, String questPerk) {
        TextView perk = questView.findViewById(R.id.sideQuestPerk);
        if (questPerk != null) {
            perk.setText(questPerk);
        }
        else {
            perk.setText("");
        }
    }

    // displays the EXP reward of a Quest
    private void displayQuestReward(View questView, int questReward) {
        TextView expText = questView.findViewById(R.id.sideQuestEXP);
        expText.setText("" + questReward + " EXP"); // have to explicitly convert it to String
    }

    // displays the tasks of a Quest
    private void displayQuestTasks(View questView, LayoutInflater inflater, SideQuest quest) {
        // get the task list Linear Layout
        LinearLayout taskList = questView.findViewById(R.id.sideQuestTaskList);
        // add the tasks
        for (int i = 0; i < quest.tasks(); i++) {
            // get the current task
            Task task = quest.getTask(i);
            // display the task
            displayTask(taskList, inflater, task);
        }
    }

    // displays a Task inside a SideQuest view, used only by displayQuestTasks
    private void displayTask(LinearLayout taskList, LayoutInflater inflater, Task t) {
        // inflate a view
        View taskView = inflater.inflate(R.layout.fragment_task, taskList, false);
        // add the task
        taskList.addView(taskView);
        // edit the task's desc
        TextView taskDesc = taskView.findViewById(R.id.taskDesc);
        taskDesc.setText(t.getDescription());
        // set the task's status
        CheckBox taskCheckBox = taskView.findViewById(R.id.taskCheckBox);
        taskCheckBox.setChecked(t.isComplete());
    }


// Listeners //
    // called when the user taps the newQuestButton
    public void newQuest(View view) {
        Intent i = new Intent(this, CreateQuest.class);
        startActivity(i);
    }

    // called when the user taps the userProfileButton
    public void loadProfile(View view) {
        Intent i = new Intent(this, Profile.class);
        startActivity(i);
    }

    // called when the User completes a Task
    public void toggleTaskStatus(View view) {
        // Get the state of the checkbox
        boolean state = ((CheckBox) view).isChecked();

        // get the indexes of the Task and SideQuest
        int taskIndex = getTaskIndex(view);
        int questIndex = getQuestIndex(view);

        // get a reference to the SideQuest
        QuestList questList = user.getQuestList();
        SideQuest sideQuest = questList.getQuest(questIndex);

        // set the Task's status in the backend
        if (state) {
            // complete the Task
            sideQuest.completeTasks(taskIndex);
        }
        else {
            // un-complete the Task
            sideQuest.uncompleteTasks(taskIndex);
        }

        // refresh homepage if SideQuest complete
        if (sideQuest.isComplete()) {
            render();
        }
    }

    // returns the Task index of a Task given its checkbox
    // TODO: Make it less messy
    private int getTaskIndex(View view) {
        // get the index of this Task in the SideQuest and TaskList (should match)
        ConstraintLayout taskConstraintLayout = (ConstraintLayout) view.getParent();
        LinearLayout taskLayout = (LinearLayout) taskConstraintLayout.getParent();
        LinearLayout taskList = (LinearLayout) taskLayout.getParent();
        return taskList.indexOfChild(taskLayout);
    }

    // returns the SideQuest index of a SideQuest given a checkbox of a SideQuest's task
    // TODO: Make it less messy/redundant
    private int getQuestIndex(View view) {
        // return the SideQuest this view belongs to
        ConstraintLayout taskConstraintLayout = (ConstraintLayout) view.getParent();
        LinearLayout taskLayout = (LinearLayout) taskConstraintLayout.getParent();
        LinearLayout taskList = (LinearLayout) taskLayout.getParent();
        ConstraintLayout sideQuestConstraint = (ConstraintLayout) taskList.getParent();
        LinearLayout sideQuestLinearLayout = (LinearLayout) sideQuestConstraint.getParent();
        LinearLayout questListLinearLayout = (LinearLayout) sideQuestLinearLayout.getParent();
        return questListLinearLayout.indexOfChild(sideQuestLinearLayout);
    }

}
