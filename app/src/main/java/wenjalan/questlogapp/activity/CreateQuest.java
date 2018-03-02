package wenjalan.questlogapp.activity;
// Activity used to create new SideQuests
// Creates a new SideQuest given user-generated fields and returns to Home

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.zip.Inflater;

import wenjalan.questlogapp.PerkTable;
import wenjalan.questlogapp.QuestLog;
import wenjalan.questlogapp.R;
import wenjalan.questlogapp.SideQuest;
import wenjalan.questlogapp.Task;
import wenjalan.questlogapp.User;

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

        // check if we need to load a quest to edit
        Intent i = this.getIntent();
        int editQuestIndex = i.getIntExtra("LoadQuestAtIndex", -1);
        if (editQuestIndex >= 0) {
            loadQuest(editQuestIndex);
        }
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
        TextView createQuestTitle = findViewById(R.id.createQuestTitle);
        createQuestTitle.setText("Edit Quest");

        // get the quest
        SideQuest quest = this.user.getQuestList().getQuest(index);

        // set the fields
        // title
        EditText titleField = (EditText) findViewById(R.id.questTitleField);
        titleField.setText(quest.getName());

        // description
        EditText descField = (EditText) findViewById(R.id.questDescField);
        descField.setText(quest.getDescription());

        // exp
        EditText expField = (EditText) findViewById(R.id.questExpField);
        int expReward = quest.getExpReward();
        expField.setText("" + expReward);

        // perk
        Spinner perkSpinner = (Spinner) findViewById(R.id.perkSpinner);
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
        EditText taskDescField = (EditText) taskView.findViewById(R.id.createTaskDesc);
        taskDescField.setText(tasks[0].getDescription());
        for (int taskIndex = 1; taskIndex < tasks.length; taskIndex++) {
            addTaskView();
            taskView = (View) this.taskViews.get(taskIndex);
            taskDescField = (EditText) taskView.findViewById(R.id.createTaskDesc);
            taskDescField.setText(tasks[taskIndex].getDescription());
        }
        Log.d("QuestLog.Android", "Loaded SideQuest " + quest.getName() + " for editing");
    }

// Listeners //
    // called when the user taps the Add Task button
    public void addTaskView(View view) {
        addTaskView();
    } // to catch the button
    public void addTaskView() {
        // Get the LinearLayout and Inflater and inflate a view
        LinearLayout taskList = findViewById(R.id.questTasksLinearLayout);
        LayoutInflater inflater = getLayoutInflater();
        View taskView = inflater.inflate(R.layout.fragment_create_task, taskList, false);

        // add this view to the ArrayList
        this.taskViews.add(taskView);

        // display the recent most view in taskViews
        taskList.addView(taskViews.get(taskViews.size() - 1));

        // log
        Log.d("QuestLog.Android", "Added a Task field");
    }

    // called when the user taps a Delete Button on a Task Field
    public void destroyTaskField(View view) {
        // Get the list of Tasks Views
        ViewGroup tasksList = (ViewGroup) findViewById(R.id.questTasksLinearLayout);
        // Grab the entire task field the button's from
        View field = (View) view.getParent().getParent();
        // Remove the field from the taskViews list
        taskViews.remove(field);
        // Delete the parent of the button that called
        tasksList.removeView(field);

        // log
        Log.d("QuestLog.Android", "Removed a Task field");
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
        EditText titleField = findViewById(R.id.questTitleField);
        questTitle = titleField.getText().toString();

        // check if the title is empty
        if (questTitle.isEmpty()) {
            Log.d("QuestLog.Android", "Failed to create quest: Invalid title");
            return;
        }

        // description
        EditText descField = findViewById(R.id.questDescField);
        questDesc = descField.getText().toString();

        // check if the description is empty
        if (questDesc.isEmpty()) {
            Log.d("QuestLog.Android", "Failed to create quest: Invalid description");
            return;
        }

        // exp
        EditText expField = findViewById(R.id.questExpField);
        try {
            questExp = Integer.valueOf(expField.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("QuestLog.Android", "Failed to create quest: non-integer value " + expField.getText().toString() + " as an EXP reward");
            return;
        }

        // check if the amount of exp is negative
        if (questExp < 0) {
            Log.d("QuestLog.Android", "Failed to create quest: EXP reward < 0");
            return;
        }

        // check if the amount of exp is greater than one level's worth of exp
        if (questExp < this.user.getLevel().getExpToNextLevel()) {
            Log.d("QuestLog.Android", "Failed to create quest: EXP reward exceeds max cap");
            return;
        }

        // perk
        Spinner perkField = findViewById(R.id.perkSpinner);
        questPerk = getPerkFromField(perkField);

        // tasks
        try {
            questTasks = getTasksFromFields();
        } catch (IllegalArgumentException e) {
            Log.d("QuestLog.Android", "Failed to create quest: Task field is empty");
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
        for (int i = 0; i < taskViews.size(); i++) {
            // get a taskView
            View task = taskViews.get(i);
            // get its desc field
            EditText field = task.findViewById(R.id.createTaskDesc);
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
