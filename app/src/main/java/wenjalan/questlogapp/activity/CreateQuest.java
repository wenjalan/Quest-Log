package wenjalan.questlogapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.zip.Inflater;

import wenjalan.questlogapp.R;

public class CreateQuest extends AppCompatActivity {

    public static final int DEFAULT_TASK_FIELDS = 3;

    private ArrayList<View> taskViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);
        start();
    }

    // starts the CreateQuest activity with some values
    public void start() {
        init();
    }

    // initialization
    public void init() {
        // create a new array to store references to the task views
        this.taskViews = new ArrayList<View>(2);

        createTaskFields();
        createPerkSpinner();
    }

    // creates 3 empty task fields, references stored in taskViews[]
    private void createTaskFields() {
        // inflate 3 empty tasks
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

// Listeners //
    // called when the user taps the Add Task button
    public void addTaskView(View view) {
        addTaskView();
    }
    public void addTaskView() {
        // Get the LinearLayout and Inflater and inflate a view
        LinearLayout taskList = findViewById(R.id.questTasksLinearLayout);
        LayoutInflater inflater = getLayoutInflater();
        View taskView = inflater.inflate(R.layout.fragment_create_task, taskList, false);

        // add this view to the ArrayList
        this.taskViews.add(taskView);

        // display the recent most view in taskViews
        taskList.addView(taskViews.get(taskViews.size() - 1));
    }

    // called when the user taps a Delete Button on a Task Field
    public void destroyTaskField(View view) {
        // Get the list of Tasks Views
        ViewGroup tasksList = (ViewGroup) findViewById(R.id.questTasksLinearLayout);
        // Grab the entire task field the button's from
        View field = (View) view.getParent().getParent();
        // Delete the parent of the button that called
        tasksList.removeView(field);
    }

}
