package wenjalan.questlogapp.activity;

import android.content.Context;
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
import wenjalan.questlogapp.User;

public class Home extends AppCompatActivity {

// Fields //
    private QuestLog questLog;

// References //
    private User user;

// Layout References //
    private LinearLayout questListLinearLayout;
    private LayoutInflater inflater;
    private ViewGroup questListScrollView;

// Methods //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        start();
    }

    // runs on startup
    public void start() {
        init();
        render();
    }

    // initialization
    public void init() {
        // get references to layout items to modify
        getLayoutReferences();

        // create a new instance of QuestLog
        this.questLog = new QuestLog("Alan Wen");

        // load stored info
        this.questLog.loadFrom();

        // get references from the questLog
        this.user = this.questLog.getUser();
    }

    // modifies on-screen content to match internal states
    public void render() {
        // update the user's info bar
        updateUserInfobar();

        // update the list of quests
        // updateQuestList();
    }

    // get references to any required views, called by onCreate()
    private void getLayoutReferences() {
        this.questListLinearLayout = (LinearLayout) findViewById(R.id.questListLinearLayout);
        this.inflater = (LayoutInflater) getLayoutInflater();
        this.questListScrollView = findViewById(R.id.questListScrollView);
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


// Button Listeners //
    // called when the user taps the newQuestButton
    public void newQuest(View view) {
        Log.d("Home", "User tapped newQuestButton");

        View sideQuest = inflater.inflate(R.layout.fragment_sidequest, questListLinearLayout, false);
        questListLinearLayout.addView(sideQuest, questListLinearLayout.getChildCount() - 1);
    }

    // called when the user taps the userProfileButton
    public void loadProfile(View view) {
        Log.d("Home", "User tapped userProfileButton");
    }

}
