package com.linxu.mounteverest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText learningStep1Percent;
    private EditText learningStep2Percent;
    private TextView learningStep1;
    private TextView learningStep2;
    LinearLayout ll;
    int[] progress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        ll = (LinearLayout)findViewById(R.id.button_layoout);
        getProgressBarProgress();

        //learningStep1Percent = (EditText) findViewById(R.id.learningStep1Percent);
        //learningStep2Percent = (EditText) findViewById(R.id.learningStep2Percent);
        //learningStep1 = (TextView) findViewById(R.id.learningStep1);
        //learningStep2 = (TextView) findViewById(R.id.learningStep2);

        //addFocusChangeListener(learningStep1Percent);
        //addFocusChangeListener(learningStep2Percent);
    }

    private void getProgressBarProgress() {
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            //String learningProgress1 = extras.getString("learningStepPercent1");
            //String learningProgress2 = extras.getString("learningStepPercent2");
            //String learningProgress3 = extras.getString("learningStepPercent3");


            for(int i = 0; i < extras.size(); i++){
                progress = new int[extras.size()];
                progress[i]= Integer.parseInt(extras.getString("learningStepPercent"+Integer.toString(i+1)));
                Button button = new Button(this);
                button.setId(i+1);
                final int id_ = button.getId();
                button.setText("learningStep"+ (i+1));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "button clicked index: "+ id_ + " progress: " + progress[id_-1] + " progress id: " + (id_ -1), Toast.LENGTH_SHORT).show();
                        progressBar.setProgress(progress[id_-1]);
                    }
                });
                ll.addView(button);
            }
        }else{
            progressBar.setProgress(0);
        }
    }

    private void addFocusChangeListener(final EditText learningStepPercent) {
        learningStepPercent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!learningStepPercent.getText().toString().equals("")){
                    int percent = Integer.parseInt(learningStepPercent.getText().toString());
                    progressBar.setProgress(percent);
                }else{
                    progressBar.setProgress(0);
                }
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
