package wenjalan.questlogapp.activity;

import android.content.Context;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);
        start();
        Log.d("CreateQuest", "CREATED activity CreateQuest");
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
        Log.d("CreateQuest", "PAUSED activity CreateQuest");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("CreateQuest", "RESUMED activity CreateQuest");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("CreateQuest", "DESTROYED activity CreateQuest");
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
            Log.d("CreateQuest", "Saved QuestLog to storage");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("CreateQuest", "Failed to save QuestLog to storage");
        }
    }

    // renders the activity
    private void render() {
        // create field views not already present
        createTaskFields();
        createPerkSpinner();
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
        Log.d("CreateQuest", "Added a Task field");
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
        Log.d("CreateQuest", "Removed a Task field");
    }

    // called when the user taps the Create Quest button
    // TODO: Check if the fields are valid before crashing
    public void createQuest(View view) {
        // grab references to all the fields
        EditText titleField = findViewById(R.id.questTitleField);
        EditText descField = findViewById(R.id.questDescField);
        Spinner perkField = findViewById(R.id.perkSpinner);
        EditText expField = findViewById(R.id.questExpField);
        LinearLayout taskFields = findViewById(R.id.questTasksLinearLayout);

        // extract the easy ones
        String questTitle = titleField.getText().toString();
        String questDesc = descField.getText().toString();

        // exp
        int questExp;
        try {
            questExp = Integer.valueOf(expField.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("CreateQuest", "Failed to create quest: non-integer value " + expField.getText().toString() + " as an EXP reward");
            return;
        }

        // extract the harder ones, should be foolproof
        String questPerk = getPerkFromField(perkField);
        Task[] questTasks = getTasksFromFields();

        // create the new SideQuest object and add it to the QuestList
        SideQuest quest = new SideQuest(
                user.getQuestList(),
                questTitle,
                questDesc,
                questExp,
                questPerk,
                questTasks
        );

        // add the SideQuest
        user.getQuestList().addQuest(quest);

        // close this activity
        // debug
        Log.d("CreateQuest", "SideQuest" + quest.getName() + " created, closing activity...");
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
            // create a new Task from that text
            Task t = new Task(desc);
            // add the new Task to the ArrayList
            tasks.add(t);
        }

        return tasks.toArray(new Task[tasks.size()]);
    }

}
