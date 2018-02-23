package wenjalan.questlogapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import wenjalan.questlogapp.Level;
import wenjalan.questlogapp.PerkTable;
import wenjalan.questlogapp.R;
import wenjalan.questlogapp.User;

public class Profile extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        start();
    }

    // runs upon startup
    private void start() {
        init();
        render();
    }

    // initialization
    private void init() {
        // get a reference to the User
        this.user = Home.questLog.getUser();
    }

    // renders the activity with the required data
    private void render() {
        // update the user information fields
        refreshName();
        refreshLevel();
        refreshPerks();
    }

    // refreshes the user's username on the activity
    private void refreshName() {
        TextView userNameView = findViewById(R.id.profileUserName);
        String userName = user.getName();
        userNameView.setText(userName);
    }

    // refreshes the user's Level aspects, the level text and the bar
    private void refreshLevel() {
        // TextView field
        TextView levelTextView = findViewById(R.id.profileUserLevel);
        int userLevel = user.getLevel().getLevel();
        levelTextView.setText("Level " + userLevel);

        // EXP Bar
        ProgressBar expBar = findViewById(R.id.profileUserExpBar);
        // TODO: Make this a get() method in Level
        Level level = user.getLevel();
        int progress = (int) ((double) level.getExp() / (double) level.getExpToNextLevel());

        expBar.setProgress(progress);
    }

    // refreshes the user's Perk aspects, including percentages and unused points
    private void refreshPerks() {
        // unused points text
        TextView unusedPointsView = findViewById(R.id.profileUnusedPerkPoints);
        int points = user.getPerkTable().getPointsIn(PerkTable.Perks.UNUSED);
        unusedPointsView.setText("Unused Perk Points: " + points);

        // percentages
        TextView physicalPercentView = findViewById(R.id.profilePhysicalPointText);
        TextView mentalPercentView = findViewById(R.id.profileMentalPointText);
        TextView socialPercentView = findViewById(R.id.profileSocialPointText);

        // TODO: Make this a get() inside PerkTable
        int physicalPercent = 0;
        int mentalPercent = 0;
        int socialPercent = 0;

        // Set the percent in the views
        physicalPercentView.setText("+" + physicalPercent + "%");
        mentalPercentView.setText("+" + mentalPercent + "%");
        socialPercentView.setText("+" + socialPercent + "%");
    }

}
