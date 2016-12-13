package com.linxu.mounteverest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by lin xu on 02.12.2016.
 */
public class AddProject extends AppCompatActivity {
    private Button save;
    private Button addLearningStep;
    private EditText learningStepPercent1;
    private EditText learningStepPercent2;
    private EditText learningStepPercent3;
    private TableLayout tableLayout;
    private TableRow learningStepRow;
    private TextView deadLine;
    int n = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);
        learningStepPercent1 = (EditText)findViewById(R.id.learningStepPercent1);
        learningStepPercent2 = (EditText)findViewById(R.id.learningStepPercent2);
        learningStepPercent3 = (EditText)findViewById(R.id.learningStepPercent3);

        tableLayout = (TableLayout) findViewById(R.id.learningStepTable);

        deadLine = (TextView)findViewById(R.id.deadLine);

        save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProject();
            }
        });

        addLearningStep = (Button)findViewById(R.id.addLearningStep);
        addLearningStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLearningStep();
            }
        });
    }

    private void addLearningStep() {
        learningStepRow = new TableRow(this);
        TableRow.LayoutParams learningStepLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        learningStepRow.setLayoutParams(learningStepLayoutParams);


        EditText learningStepName = new EditText(this);
        TableRow.LayoutParams learningStepNameParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 4);
        learningStepName.setLayoutParams(learningStepNameParams);
        learningStepName.setHint("learningStep" + n);
        n++;

        EditText learningStepPercent = new EditText(this);
        TableRow.LayoutParams learningStepPercentParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2);
        learningStepPercent.setLayoutParams(learningStepPercentParams);
        learningStepPercent.setHint("Days");

        learningStepRow.addView(learningStepName);
        learningStepRow.addView(learningStepPercent);
        tableLayout.addView(learningStepRow);

    }

    private void saveProject() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("learningStepPercent1", learningStepPercent1.getText().toString());
        intent.putExtra("learningStepPercent2", learningStepPercent2.getText().toString());
        intent.putExtra("learningStepPercent3", learningStepPercent3.getText().toString());
        startActivity(intent);
    }
}
