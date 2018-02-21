package wenjalan.questlogapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import wenjalan.questlogapp.R;

public class CreateQuest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // called when the Home button is pressed
    public void homeButton(View view) {
        this.onDestroy();
    }
}
