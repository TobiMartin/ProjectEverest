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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    LinearLayout ll;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        extras = getIntent().getExtras();
        validateEmptyProjectData();
    }

    private void validateEmptyProjectData() {
        if(extras == null){
            Intent intent = new Intent(MainActivity.this, AddProject.class);
            startActivity(intent);
        }else{
            bindButton();
        }
    }

    private int[] extractLearningStepPercentage() {
        int[] learningStepPercentage;
        learningStepPercentage = new int[extras.size()];
        for(int i = 0; i < extras.size(); i++){
            learningStepPercentage[i] = Integer.parseInt(extras.getString("learningStepPercent" + (i+1)));
        }
        Log.d("learning Percentage: " , " " + Arrays.toString(learningStepPercentage));
        return learningStepPercentage;
    }

    private void bindButton() {
        ll = (LinearLayout)findViewById(R.id.button_layoout);
        for(int i = 0; i < extractLearningStepPercentage().length; i++){
            Button button = new Button(this);
            button.setId(i+1);
            button.setText("learningStepPercentage" + (i+1));
            ll.addView(button);
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar.setProgress(extractLearningStepPercentage()[finalI]);
                }
            });
        }
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
