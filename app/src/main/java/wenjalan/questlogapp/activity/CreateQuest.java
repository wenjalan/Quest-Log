package wenjalan.questlogapp.activity;
// Activity used to create new SideQuests
// Creates a new SideQuest given user-generated fields and returns to Home

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import wenjalan.questlogapp.questlog.PerkTable;
import wenjalan.questlogapp.questlog.QuestLog;
import wenjalan.questlogapp.R;
import wenjalan.questlogapp.questlog.SideQuest;
import wenjalan.questlogapp.questlog.Task;
import wenjalan.questlogapp.questlog.User;
import wenjalan.questlogapp.animation.AddTaskAnimation;
import wenjalan.questlogapp.animation.RemoveTaskAnimation;

public class CreateQuest extends AppCompatActivity {

// Constants //
    public static final int DEFAULT_TASK_FIELDS = 1;

// Fields //
    private ArrayList<View> taskViews;
    private QuestLog questLog;
    private User user;

// Android Events //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("QuestLog.Android", "CREATED activity CreateQuest");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);
        start();
    }

    @Override
    protected void onPause() {
        Log.d("QuestLog.Android", "PAUSED activity CreateQuest");
        super.onPause();
        save();
    }

    @Override
    protected void onResume() {
        Log.d("QuestLog.Android", "RESUMED activity CreateQuest");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d("QuestLog.Android", "DESTROYED activity CreateQuest");
        super.onDestroy();
    }

// Methods //
    // starts the CreateQuest activity with some values
    private void start() {
        init();
        render();
    }

    // initialization
    private void init() {
        this.questLog = Home.questLog;
        this.user = questLog.getUser();
        this.taskViews = new ArrayList<View>(DEFAULT_TASK_FIELDS);
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

    // renders the activity
    private void render() {
        // create field views not already present
        createTaskFields();
        createPerkSpinner();

        // update the max exp reward text
        EditText expField = findViewById(R.id.expField);
        int maxExp = questLog.getUser().getLevel().getExpToNextLevel();
        expField.setHint("" + maxExp);

        // check if we need to load a quest to edit
        Intent i = this.getIntent();
        int editQuestIndex = i.getIntExtra("LoadQuestAtIndex", -1);
        if (editQuestIndex >= 0) {
            loadQuest(editQuestIndex);
        }

        // set the focus to the title EditText
        findViewById(R.id.titleField).requestFocus();
    }

    // creates empty task fields, references stored in taskViews[]
    private void createTaskFields() {
        // inflate empty tasks
        for (int i = 0; i < DEFAULT_TASK_FIELDS; i++) {
            addTaskView();
        }
    }

    // initializes the perkSpinner, based on tutorial at
    // https://developer.android.com/guide/topics/ui/controls/spinner.html
    private void createPerkSpinner() {
        Spinner perkSpinner = findViewById(R.id.perkSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.perks_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        perkSpinner.setAdapter(adapter);
    }

    // loads a SideQuest into the fields
    private void loadQuest(int index) {
        // change the title of the activity to "Edit Quest"
        TextView createQuestTitle = this.findViewById(R.id.title);
        createQuestTitle.setText("Edit Quest");

        // get the quest
        SideQuest quest = this.user.getQuestList().getQuest(index);

        // set the fields
        // title
        EditText titleField = (EditText) this.findViewById(R.id.titleField);
        titleField.setText(quest.getName());

        // description
        EditText descField = (EditText) this.findViewById(R.id.descriptionField);
        descField.setText(quest.getDescription());

        // exp
        EditText expField = (EditText) this.findViewById(R.id.expField);
        int expReward = quest.getExpReward();
        expField.setText("" + expReward);

        // perk
        Spinner perkSpinner = (Spinner) this.findViewById(R.id.perkSpinner);
        String perk = quest.getPerkCategory();

        if (perk == null) {
            perkSpinner.setSelection(0);
        }
        else if (perk.equals(PerkTable.Perks.PHYSICAL)) {
            perkSpinner.setSelection(1);
        }
        else if (perk.equals(PerkTable.Perks.MENTAL)) {
            perkSpinner.setSelection(2);
        }
        else if (perk.equals(PerkTable.Perks.SOCIAL)) {
            perkSpinner.setSelection(3);
        }

        // tasks
        Task[] tasks = quest.getTasks();
        // add the first task because its view already exists
        View taskView = (View) this.taskViews.get(0);
        EditText taskDescField = (EditText) taskView.findViewById(R.id.taskField);
        taskDescField.setText(tasks[0].getDescription());
        for (int taskIndex = 1; taskIndex < tasks.length; taskIndex++) {
            addTaskView();
            taskView = (View) this.taskViews.get(taskIndex);
            taskDescField = (EditText) taskView.findViewById(R.id.taskField);
            taskDescField.setText(tasks[taskIndex].getDescription());
        }
        Log.d("QuestLog.Android", "Loaded SideQuest " + quest.getName() + " for editing");
    }

// Listeners //
    // called when the user presses the up button
    public void navigationUp(View view) {
        finish();
    }

    // called when the user taps the Add Task button
    public void addTaskView(View view) {
        addTaskView();
    } // to catch the button
    public void addTaskView() {
        // Get the LinearLayout and Inflater and inflate a view
        LinearLayout taskList = findViewById(R.id.taskFieldsLayout);
        LayoutInflater inflater = getLayoutInflater();
        View taskView = inflater.inflate(R.layout.create_quest_task, taskList, false);

        // add this view to the ArrayList
        this.taskViews.add(taskView);

        // add the recent most view in the LinearLayout
        taskList.addView(taskViews.get(taskViews.size() - 1));

        // set the visibility to no
        taskView.setVisibility(View.GONE);

        // animate it
        animateAddTaskView(taskView);

        // change the task's id to match the arraylist
        updateTaskId(taskView);

        // set the focus to the new task's field
        taskView.findViewById(R.id.taskField).requestFocus();

        // log
        Log.d("QuestLog.Android", "Added a Task field");
    }

    // animates the adding of a Task View
    private void animateAddTaskView(View view) {
        AddTaskAnimation a = new AddTaskAnimation(view);
        view.startAnimation(a);
    }

    // called when the user taps a Delete Button on a Task Field
    public void destroyTaskField(View view) {
        // Get the LinearLayout of Tasks Views
        LinearLayout tasksList = (LinearLayout) findViewById(R.id.taskFieldsLayout);
        // if this view isn't the last view in the list
        if (taskViews.size() > 1) {
            // Grab the entire task field the button's from
            View taskView = (View) view.getParent();
            // Get the index of the Task we're removing
            int id = taskViews.indexOf(taskView);
            // Remove the field from the taskViews ArrayList (using the view itself)
            taskViews.remove(taskView);
            // animate it, also removes it from taskFieldsLayout afterwards and updates IDs
            animateRemoveTaskView(taskView);
            // Log
            Log.d("QuestLog.Android", "Removed Task View " + id);
        }
        else {
            Log.d("QuestLog.Android", "Couldn't remove Task View, it's the last one");
        }
    }

    // animates the removal of a Task View
    private void animateRemoveTaskView(final View view) {
        RemoveTaskAnimation a = new RemoveTaskAnimation(view);
        // remove the view from the layout and update the ids once the animation finishes
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // remove it from the taskFieldsLayout
                // TODO: Fix?
                ((LinearLayout) findViewById(R.id.taskFieldsLayout)).removeView((View) view.getParent());
                // update the task ids
                updateTaskIds();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // start the animation
        view.startAnimation(a);
    }

    // updates the ids of all tasks current in the layout
    private void updateTaskIds() {
        LinearLayout taskFieldsLayout = this.findViewById(R.id.taskFieldsLayout);
        // for all task views
        for (int i = 0; i < taskFieldsLayout.getChildCount(); i++) {
            // get the current task view
            View taskView = taskFieldsLayout.getChildAt(i);
            // update that child
            updateTaskId(taskView);
        }
    }

    // updates an id of a task view
    private void updateTaskId(View taskView) {
        // get the id textview and the id of this task
        TextView taskIdView = taskView.findViewById(R.id.taskId);
        int id = this.taskViews.indexOf(taskView);
        // set the taskid to that id plus one (zero based counting -> one based counting)
        taskIdView.setText("Task " + (id + 1));
    }

    // called when the user taps the Create Quest button
    // if any field is found to be invalid, returns
    // TODO: Add Snackbar to show user the error
    public void createQuest(View view) {
        // the fields the user enters
        String questTitle;
        String questDesc;
        int questExp;
        String questPerk;
        Task[] questTasks;

        // title
        EditText titleField = findViewById(R.id.titleField);
        questTitle = titleField.getText().toString();

        // check if the title is empty
        if (questTitle.isEmpty()) {
            Log.d("QuestLog.Android", "Failed to create quest: Invalid title");
            showError("Invalid title");
            return;
        }

        // description
        EditText descField = findViewById(R.id.descriptionField);
        questDesc = descField.getText().toString();

        // check if the description is empty
        if (questDesc.isEmpty()) {
            Log.d("QuestLog.Android", "Failed to create quest: Invalid description");
            showError("Invalid description");
            return;
        }

        // exp
        EditText expField = findViewById(R.id.expField);
        try {
            questExp = Integer.valueOf(expField.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("QuestLog.Android", "Failed to create quest: non-integer value " + expField.getText().toString() + " as an EXP reward");
            showError("Invalid EXP");
            return;
        }

        // check if the amount of exp is negative or 0
        if (questExp <= 0) {
            Log.d("QuestLog.Android", "Failed to create quest: EXP reward < 0");

            // show error feedback to user
            TextInputLayout layout = (TextInputLayout) this.findViewById(R.id.expFieldLayout);
            layout.setHint(null);
            layout.setError("EXP reward must be more than 0");

            return;
        }

        // check if the amount of exp is greater than one level's worth of exp (minus 1 to avoid animation errors)
        if (questExp > this.user.getLevel().getExpToNextLevel()) {
            Log.d("QuestLog.Android", "Failed to create quest: EXP reward exceeds max cap");

            // show error feedback to user
            TextInputLayout layout = (TextInputLayout) this.findViewById(R.id.expFieldLayout);
            int maxExp = questLog.getUser().getLevel().getExpToNextLevel();
            layout.setHint(null);
            layout.setError("Max: " + maxExp);

            return;
        }

        // perk
        Spinner perkField = findViewById(R.id.perkSpinner);
        questPerk = getPerkFromField(perkField);

        // tasks
        try {
            questTasks = getTasksFromFields();
        } catch (IllegalArgumentException e) {
            Log.d("QuestLog.Android", "Failed to create quest: " + e.getMessage());
            showError("Invalid Task");
            return;
        }

        // create the new SideQuest object and add it to the QuestList
        SideQuest quest = new SideQuest(
                user.getQuestList(),
                questTitle,
                questDesc,
                questExp,
                questPerk,
                questTasks
        );

        // if we were editing a quest
        int editQuestIndex = getIntent().getIntExtra("LoadQuestAtIndex", -1);
        // if the index was found to be a quest
        if (editQuestIndex >= 0) {
            // replace the SideQuest
            user.getQuestList().replaceQuest(editQuestIndex, quest);
        }
        else {
            // add the SideQuest
            user.getQuestList().addQuest(quest);
        }

        // close this activity
        Log.d("QuestLog.Android", "SideQuest" + quest.getName() + " created, closing activity...");
        finish();
    }

    // shows an error to the user via a Toast
    private void showError(String error) {
        Toast toast = Toast.makeText(this, error, Toast.LENGTH_SHORT);
        toast.show();
    }

    // returns the Perk chosen in a given Spinner
    private String getPerkFromField(Spinner spinner) {
        // get the input from the Spinner
        String input = spinner.getSelectedItem().toString();

        // return a Perk based on that input
        if (input.equalsIgnoreCase("PHYSICAL")) {
            return PerkTable.Perks.PHYSICAL;
        }
        else if (input.equalsIgnoreCase("MENTAL")) {
            return PerkTable.Perks.MENTAL;
        }
        else if (input.equalsIgnoreCase("SOCIAL")) {
            return PerkTable.Perks.SOCIAL;
        }
        else {
            return null;
        }
    }

    // returns an ArrayList of Tasks generated by the fields, using the stored taskViews ArrayList
    private Task[] getTasksFromFields() {
        ArrayList<Task> tasks = new ArrayList<>();
        // check if there are 0 tasks
        if (taskViews.size() == 0) {
            throw new IllegalArgumentException("No task fields found");
        }
        for (int i = 0; i < taskViews.size(); i++) {
            // get a taskView
            View task = taskViews.get(i);
            // get its desc field
            EditText field = task.findViewById(R.id.taskField);
            // get the text inside the field
            String desc = field.getText().toString();
            // check if the field is empty
            if (desc.isEmpty()) {
                throw new IllegalArgumentException("Task field is empty");
            }
            // create a new Task from that text
            Task t = new Task(desc);
            // add the new Task to the ArrayList
            tasks.add(t);
        }
        return tasks.toArray(new Task[tasks.size()]);
    }

}
