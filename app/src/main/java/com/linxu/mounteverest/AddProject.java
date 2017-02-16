package com.linxu.mounteverest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lin xu on 15.12.2016.
 */
public class AddProject extends AppCompatActivity {
    private CustomSliderView customSliderView;

    private TextView startTextView;
    private TextView endTextView;

    private ListView listView;

    private LearningStepAdapter learningStepAdapter;

    private boolean startDateSet = false;
    private boolean endDateSet = false;
    private Button addProjectDone;

    private String projectName;
    private TextView projectNameView;


    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        customSliderView = (CustomSliderView)findViewById(R.id.slider);
        startTextView = (TextView)findViewById(R.id.startDate);
        endTextView = (TextView)findViewById(R.id.endDate);
        customSliderView.register(this);
        listView = (ListView)findViewById(R.id.list_view);
        projectNameView = (TextView)findViewById(R.id.project_name);

        FirebaseDatabase mFirebaseDatabase = SignInActivity.getmFirebaseDatabase();
        final DatabaseReference mProjectDatabaseRef = mFirebaseDatabase.getReference().child("User").child(SignInActivity.currentUser.getId()).child("Projects");

        openSetNameDialog();

        addProjectDone = (Button)findViewById(R.id.add_project_done);
        addProjectDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProjectDatabaseRef.push().setValue(
                        new LearningProject(
                            CustomSliderView.getLearningStepList(),
                                projectName
                                ));

                Intent intent = new Intent(AddProject.this, SelectProjectActivity.class);
                startActivity(intent);
            }
        });
        addProjectDone.setEnabled(false);
    }

    private void openSetNameDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(AddProject.this).create();
        alertDialog.setTitle("Set Project Name");
        alertDialog.setMessage("Please give your project a name!");
        final EditText input = new EditText(AddProject.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                projectName = input.getText().toString();
                if (projectName.equals("")) {
                    projectName = "Untitled Project";
                }
                projectNameView.setText(projectName);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void upDateLearningSteps(List learningSteps){
        Collections.sort(learningSteps, new Comparator<LearningStep>() {
            @Override
            public int compare(LearningStep l1, LearningStep l2) {
                return l2.getDate().compareToIgnoreCase(l1.getDate());
            }
        });
        learningStepAdapter = new LearningStepAdapter(AddProject.this, learningSteps);
        listView.setAdapter(learningStepAdapter);
    }

    public void changeStartDate(String date) {
        startTextView.setText(date);
        startDateSet = true;
    }

    public void changeEndDate(String date){
        endTextView.setText(date);
        endDateSet = true;
    }

    public void notifyAddLearningStep() {
        addProjectDone.setEnabled(true);
    }

    public boolean isStartDateSet(){
        return startDateSet;
    }

    public boolean isEndDateSet(){
        return endDateSet;
    }
}
