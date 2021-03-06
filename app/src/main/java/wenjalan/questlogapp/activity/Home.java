package wenjalan.questlogapp.activity;
// The Home screen where the user's quests are displayed
// Acts as the main activity, allowing access to Profile and CreateQuest

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import wenjalan.questlogapp.Level;
import wenjalan.questlogapp.QuestList;
import wenjalan.questlogapp.QuestLog;
import wenjalan.questlogapp.R;
import wenjalan.questlogapp.SideQuest;
import wenjalan.questlogapp.Task;
import wenjalan.questlogapp.animation.QuestLogAnimation;
import wenjalan.questlogapp.animation.RemoveQuestAnimation;

public class Home extends AppCompatActivity {

// Constants //
    private static final int ANIMATION_EXP_BAR_DURATION = 1000;

    // Request codes
    private static final int CREATE_NEW_USER_REQUEST = 0;

// Fields //
    public static QuestLog questLog;
    private boolean animateBar;

// Android Events //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("QuestLog.Android", "CREATED activity Home");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // set the animation flag
        this.animateBar = true;
        start();
    }

    @Override
    protected void onPause() {
        Log.d("QuestLog.Android", "PAUSED activity Home");
        super.onPause();
        save();
    }

    @Override
    protected void onResume() {
        Log.d("QuestLog.Android", "RESUMED activity Home");
        super.onResume();
        this.animateBar = false;
        render();
    }

    @Override
    protected void onDestroy() {
        Log.d("QuestLog.Android", "DESTROYED activity Home");
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // parse the request
        if (requestCode == CREATE_NEW_USER_REQUEST) {
            // if it was successful
            if (resultCode == RESULT_OK) {
                // get the QuestLog from the intent
                Bundle bundle = data.getExtras();
                this.questLog = (QuestLog) bundle.getSerializable("QuestLog");
            }
        }
        else {
            Log.d("QuestLog.Android", "onActivityResult returned an invalid requestCode!");
        }
    }

// Methods //
    // runs on startup
    public void start() {
        load();
        render();
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

    // loads app data from storage, returns true if load was successful
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

            // if there was no questLog
            if (this.questLog == null) {
                throw new Exception("No QuestLog data found");
            }

            // close
            objectInputStream.close();
            fileInputStream.close();

            // log
            Log.d("Home", "Loaded QuestLog from storage");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Home", "Failed to load QuestLog from storage, loading CreateUser activity...");
            createNewUser();
        }
    }

    // creates a new user
    private void createNewUser() {
        Intent i = new Intent(this, CreateUser.class);
        startActivityForResult(i, CREATE_NEW_USER_REQUEST);
        this.questLog = (QuestLog) i.getParcelableExtra("QuestLog");
    }

    // modifies on-screen content to match internal states
    private void render() {
        // check if the QuestLog can be rendered
        if (this.questLog == null) {
            Log.d("QuestLog.Android", "Failed to render activity Home!");
            return;
        }
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
        TextView userLevelTextView = findViewById(R.id.levelText);
        // get a reference to the User's Level
        Level userLevel = this.questLog.getUser().getLevel();
        // get the User's current level
        int level = userLevel.getLevel();
        // update the text
        userLevelTextView.setText("Level " + level);
    }

    // updates the user's EXP bar on the homepage
    private void updateUserEXPBar() {
        // get a reference to the ProgressBar
        ProgressBar expBar = findViewById(R.id.expBar);
        // get the current EXP in the bar
        int barProgress = expBar.getProgress();
        // get the user's current progress towards their next level
        int expProgress = this.questLog.getUser().getLevel().getLevelProgress();

        // If we're not just resuming the activity
        if (animateBar) {
            // if the user leveled up
            if (barProgress >= expProgress) {
                Log.d("QuestLog.Animation", "Animating levelup EXP gain");
                // animate the bar to 100
                animateExpGain(expBar, expProgress);
                animateLevelUp();
            }
            else {
                Log.d("QuestLog.Animation", "Animating regular EXP gain");
                animateExpGain(expBar, expProgress);
            }
        }
    }

    // animates the EXP bar gain
    // TODO: Write about this in your writeup
    private void animateExpGain(final ProgressBar expBar, final int finish) {
        // Create a new animation
        ObjectAnimator animation;
        int current = expBar.getProgress();

        // if this animation should loop back around from 100 to 0
        if (current >= finish) {
            animation = ObjectAnimator.ofInt(expBar, "progress", 100);
            animation.removeAllListeners();
            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {}

                @Override
                public void onAnimationEnd(Animator animator) {
                    // if the bar is full
                    if (expBar.getProgress() >= 100) {
                        expBar.setProgress(0);
                    }
                    animateExpGain(expBar, finish);
                }

                @Override
                public void onAnimationCancel(Animator animator) {}

                @Override
                public void onAnimationRepeat(Animator animator) {}
            });
        }
        else {
            animation = ObjectAnimator.ofInt(expBar, "progress", finish);
            animation.removeAllListeners();
        }

        // start the animation
        animation.setDuration(QuestLogAnimation.DURATION_LONG);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    // plays the animation for a levelup
    private void animateLevelUp() {

    }

    // updates the questListLinearLayout with all SideQuest instances
    private void updateQuestList() {
        // clear the existing list, if it exists
        clearQuestViews();
        // get the User's QuestList
        QuestList questList = this.questLog.getUser().getQuestList();
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
        LinearLayout questList = findViewById(R.id.questsLayout);
        questList.removeAllViews();
    }

    // displays a single sideQuest on the homepage
    private void displayQuest(SideQuest sideQuest) {
        // get a reference to the questListLinearLayout
        LinearLayout questList = findViewById(R.id.questsLayout);
        // get an inflater
        LayoutInflater inflater = getLayoutInflater();
        // create a new View to hold the SideQuest in
        View questView = inflater.inflate(R.layout.home_sidequest, questList, false);

        // title
        displayQuestTitle(questView, sideQuest.getName());

        // description
        displayQuestDesc(questView, sideQuest.getDescription());

        // EXP reward
        displayQuestReward(questView, sideQuest.getExpReward());

        // perk
        displayQuestPerk(questView, sideQuest.getPerkCategory());

        // tasks
        displayQuestTasks(questView, inflater, sideQuest);

        // add the view to the list
        questList.addView(questView);
    }

    // displays the title of a Quest
    private void displayQuestTitle(View questView, String questTitle) {
        TextView title = questView.findViewById(R.id.title);
        title.setText(questTitle);
    }

    // displays the description of a Quest
    private void displayQuestDesc(View questView, String questDesc) {
        TextView desc = questView.findViewById(R.id.description);
        desc.setText(questDesc);
    }

    // displays the EXP reward of a Quest
    private void displayQuestReward(View questView, int questReward) {
        TextView expText = questView.findViewById(R.id.reward);
        expText.setText(questReward + " EXP"); // have to explicitly convert it to String
    }

    // displays the perk of a Quest
    private void displayQuestPerk(View questView, String questPerk) {
        TextView expText = questView.findViewById(R.id.reward);
        String rewardText = expText.getText().toString();
        if (questPerk != null) {
            expText.setText(questPerk + ", " + rewardText);
        }
    }

    // displays the tasks of a Quest
    private void displayQuestTasks(View questView, LayoutInflater inflater, SideQuest quest) {
        // get the task list Linear Layout
        LinearLayout taskList = questView.findViewById(R.id.taskFieldsLayout);
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
        View taskView = inflater.inflate(R.layout.home_sidequest_task, taskList, false);
        // add the task
        taskList.addView(taskView);
        // edit the task's desc
        TextView taskDesc = taskView.findViewById(R.id.description);
        taskDesc.setText(t.getDescription());
        // set the task's status
        CheckBox taskCheckBox = taskView.findViewById(R.id.checkBox);
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
        QuestList questList = this.questLog.getUser().getQuestList();
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
            // animate the removal of the SideQuest
            LinearLayout questsLayout = (LinearLayout) findViewById(R.id.questsLayout);
            View sideQuestView = questsLayout.getChildAt(questIndex);
            animateRemoveSideQuestView(sideQuestView);
            // set the animation flag
            this.animateBar = true;
        }
    }

    // animates the removal of a Side Quest View
    private void animateRemoveSideQuestView(View questView) {
        RemoveQuestAnimation a = new RemoveQuestAnimation(questView);
        // refresh the homepage on animation end
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // refresh the homepage
                render();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        questView.startAnimation(a);
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
    // TODO: Make it less messy
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

    // called when the user wants to edit a SideQuest
    public void editQuest(View button) {
        int questIndex = getQuestIndexFromEditButton(button);
        Intent i = new Intent(this, CreateQuest.class);
        i.putExtra("LoadQuestAtIndex", questIndex);
        startActivity(i);
    }

    // returns the index of the Quest that an Edit button is attached to
    private int getQuestIndexFromEditButton(View view) {
        ConstraintLayout body = (ConstraintLayout) view.getParent();
        LinearLayout sidequest = (LinearLayout) body.getParent();
        LinearLayout questsList = (LinearLayout) sidequest.getParent();
        return questsList.indexOfChild(sidequest);
    }

}
