package wenjalan.questlogapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import wenjalan.questlogapp.QuestLog;
import wenjalan.questlogapp.R;

public class CreateUser extends AppCompatActivity {

// Constants //
    private static final int USERNAME_MAX_CHAR_LENGTH = 10;

// Fields //
    private Intent intent;
    private QuestLog questLog;

// Android Events //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("QuestLog.Android", "CREATED activity CreateUser");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        intent = this.getIntent();
    }

// Button Listeners //
    // called when the user clicks the "Create User" button
    public void createUser(View view) {
        // get the user's name
        EditText field = findViewById(R.id.userNameField);
        String username = field.getText().toString();

        // check for errors
        boolean problematic = checkNameForErrors(username);

        // if the username was problematic
        if (problematic) {
            // show an error
            // TODO: that
            return;
        }

        // create the user and put it in an intent
        this.questLog = new QuestLog(username);
        Intent rIntent = new Intent();
        rIntent.putExtra("QuestLog", this.questLog);

        // send back the new QuestLog
        setResult(RESULT_OK, rIntent);
        finish();
    }

    // checks if the username is error prone, returns true if there was an error
    private boolean checkNameForErrors(String name) {
        if (name.length() > USERNAME_MAX_CHAR_LENGTH) {
            Log.d("QuestLog.Android", "Error parsing username: Username is too long");
            return true;
        }
        else {
            return false;
        }
    }


}
