package wenjalan.questlogapp.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import wenjalan.questlogapp.R;

public class Home extends AppCompatActivity {

    private LinearLayout questListLinearLayout;
    private LayoutInflater inflater;
    private ViewGroup questListScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getLayoutReferences();
    }

    // get references to any required views, called by onCreate()
    private void getLayoutReferences() {
        questListLinearLayout = (LinearLayout) findViewById(R.id.questListLinearLayout);
        inflater = (LayoutInflater) getLayoutInflater();
        questListScrollView = findViewById(R.id.questListScrollView);
    }

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
