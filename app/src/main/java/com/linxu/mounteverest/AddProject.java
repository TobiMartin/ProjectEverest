package com.linxu.mounteverest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private EditText startDate;
    private EditText deadLine;
    private Calendar start_myCalendar;
    private Calendar end_myCalendar;
    private DatePickerDialog.OnDateSetListener start_date;
    private DatePickerDialog.OnDateSetListener end_date;
    private boolean startDateSet;
    private boolean endDateSet;
    private SeekBar projectSlider;
    int n = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);
        learningStepPercent1 = (EditText)findViewById(R.id.learningStepPercent1);
        learningStepPercent2 = (EditText)findViewById(R.id.learningStepPercent2);
        learningStepPercent3 = (EditText)findViewById(R.id.learningStepPercent3);

        tableLayout = (TableLayout) findViewById(R.id.learningStepTable);

        startDate = (EditText)findViewById(R.id.startDate);
        deadLine = (EditText)findViewById(R.id.deadLine);

        startDateSet = false;
        endDateSet = false;

        start_myCalendar = Calendar.getInstance();
        start_date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                start_myCalendar.set(Calendar.YEAR, year);
                start_myCalendar.set(Calendar.MONTH, month);
                start_myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        end_myCalendar = Calendar.getInstance();
        end_date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                end_myCalendar.set(Calendar.YEAR, year);
                end_myCalendar.set(Calendar.MONTH, month);
                end_myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(start_date, start_myCalendar);
                startDateSet = true;
            }
        });

        deadLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(end_date, end_myCalendar);
                endDateSet = true;
                if(startDateSet&& endDateSet){
                    setProjectSlider();
                }
            }
        });


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

    private void setProjectSlider() {

        projectSlider = (SeekBar)findViewById(R.id.projectSlider);
        int duration = caculateTotalProjectDuration();
        projectSlider.setMax(duration);
        Toast.makeText(AddProject.this, "max: " + duration, Toast.LENGTH_SHORT).show();
    }

    private void openDatePickerDialog(DatePickerDialog.OnDateSetListener dateSetListener, Calendar cal) {
        new DatePickerDialog(AddProject.this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
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
        formValidate();
        caculateTotalProjectDuration();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("learningStepPercent1", learningStepPercent1.getText().toString());
        intent.putExtra("learningStepPercent2", learningStepPercent2.getText().toString());
        intent.putExtra("learningStepPercent3", learningStepPercent3.getText().toString());
        startActivity(intent);
    }

    private int caculateTotalProjectDuration() {
        long diff = end_myCalendar.getTimeInMillis() - start_myCalendar.getTimeInMillis();
        int duration = (int)diff/(1000 * 60 * 60 * 24);
        Toast.makeText(AddProject.this, "duration: " + duration, Toast.LENGTH_SHORT).show();
        return duration;
    }

    private void formValidate() {
        //Todo: to check if all the blanks are filled with proper data
    }

    private void updateLabel(){
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        startDate.setText(sdf.format(start_myCalendar.getTime()));
        deadLine.setText(sdf.format(end_myCalendar.getTime()));
    }
}
