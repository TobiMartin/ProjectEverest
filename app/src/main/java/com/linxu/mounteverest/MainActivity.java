package com.linxu.mounteverest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText learningStep1Percent;
    private EditText learningStep2Percent;
    private TextView learningStep1;
    private TextView learningStep2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        learningStep1Percent = (EditText) findViewById(R.id.learningStep1Percent);
        learningStep2Percent = (EditText) findViewById(R.id.learningStep2Percent);
        learningStep1 = (TextView) findViewById(R.id.learningStep1);
        learningStep2 = (TextView) findViewById(R.id.learningStep2);
        learningStep1Percent.setText("20");
        learningStep2Percent.setText("50");

        onProgress(learningStep1Percent, Integer.parseInt(learningStep1Percent.getText().toString()));
        onProgress(learningStep2Percent, Integer.parseInt(learningStep2Percent.getText().toString()));
    }

    private void onProgress(EditText learningStepPercent, final int percent) {
        learningStepPercent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                progressBar.setProgress(percent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                startActivity(new Intent(this, Search.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(this, Profile.class));
                return true;
            case R.id.add_project:
                startActivity(new Intent(this, AddProject.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
