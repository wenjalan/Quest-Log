package wenjalan.questlogapp.activity;
// Activity used to display the user's stats and perks

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import wenjalan.questlogapp.Level;
import wenjalan.questlogapp.PerkTable;
import wenjalan.questlogapp.QuestLog;
import wenjalan.questlogapp.R;
import wenjalan.questlogapp.User;

public class Profile extends AppCompatActivity {

// Fields //
    private QuestLog questLog;
    private User user;

// Android Events //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("QuestLog.Android", "CREATED activity Profile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        start();
    }

    @Override
    protected void onPause() {
        Log.d("QuestLog.Android", "PAUSED activity Profile");
        super.onPause();
        save();
    }

    @Override
    protected void onResume() {
        Log.d("QuestLog.Android", "RESUMED activity Profile");
        super.onResume();
        render();
    }

    @Override
    protected void onDestroy() {
        Log.d("QuestLog.Android", "DESTROYED activity Profile");
        super.onDestroy();
    }

// Methods //
    // runs upon startup
    private void start() {
        init();
        render();
    }

    // initialization
    private void init() {
        this.questLog = Home.questLog;
        this.user = questLog.getUser();
    }

    // saves app data to storage
    // WARNING: Copy-pasted from Home
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
            Log.d("QuestLog.Android", "Saved QuestLog to storage");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("QuestLog.Android", "Failed to save QuestLog to storage");
        }
    }

    // renders the activity with the required data
    private void render() {
        // update the user information fields
        refreshName();
        refreshLevel();
        refreshPerks();
    }

    // refreshes the username field
    private void refreshName() {
        // Get the username
        String username = questLog.getUser().getName();
        // Set the name
        TextView nameField = findViewById(R.id.userName);
        nameField.setText(username);
    }

    // refreshes the user's Level aspects, the level text and the bar
    private void refreshLevel() {
        // Level reference
        Level level = user.getLevel();

        // TextView field
        TextView levelTextView = this.findViewById(R.id.level);
        int userLevel = user.getLevel().getLevel();
        levelTextView.setText("Level " + userLevel);

        // EXP Text
        TextView expText = this.findViewById(R.id.expProgress);
        int cExp = level.getExp();
        int lExp = level.getExpToNextLevel();
        expText.setText("" + cExp + "/" + lExp);

        // EXP Bar
        ProgressBar expBar = this.findViewById(R.id.expBar);
        int progress = level.getLevelProgress();
        expBar.setProgress(progress);
    }

    // refreshes the user's Perk aspects, including percentages and unused points
    private void refreshPerks() {
        // get the PerkTable
        PerkTable perks = user.getPerkTable();
        // unused points text
        TextView unusedPointsView = findViewById(R.id.unusedPoints);
        int points = perks.getPointsIn(PerkTable.Perks.UNUSED);
        unusedPointsView.setText("Unused Points: " + points);

        // percentages
        TextView physicalPercentView = findViewById(R.id.bonusPhysical);
        TextView mentalPercentView = findViewById(R.id.bonusMental);
        TextView socialPercentView = findViewById(R.id.bonusSocial);

        // get the percent bonuses from PerkTable
        int physicalPercent = perks.getBonusPercent(PerkTable.Perks.PHYSICAL);
        int mentalPercent = perks.getBonusPercent(PerkTable.Perks.MENTAL);
        int socialPercent = perks.getBonusPercent(PerkTable.Perks.SOCIAL);

        // Set the percent in the views
        physicalPercentView.setText("+" + physicalPercent + "%");
        mentalPercentView.setText("+" + mentalPercent + "%");
        socialPercentView.setText("+" + socialPercent + "%");
    }

// Listeners //
    // when the user taps the up button
    public void navigationUp(View view) {
        finish();
    }

    // when the user presses an add point button
    public void addPointPhysical(View view) {
        addPoint(PerkTable.Perks.PHYSICAL);
    }

    // when the user presses an add point button
    public void addPointMental(View view) {
        addPoint(PerkTable.Perks.MENTAL);
    }

    // when the user presses an add point button
    public void addPointSocial(View view) {
        addPoint(PerkTable.Perks.SOCIAL);
    }

    // adds a point to a perk
    private void addPoint(String perk) {
        PerkTable perks = user.getPerkTable();
        perks.addPoints(perk, 1);
        refreshPerks();
    }

    // deletes all user data
    public void deleteUserData(View view) {
        // delete the data file
        deleteFile(QuestLog.DATA_FILE_NAME);
        // replace the reference to the old questLog
        Home.questLog = null;
        Log.d("QuestLog.Android", "Deleted user data");
        // restart the app
        System.exit(0);
    }

}
