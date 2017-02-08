package com.linxu.mounteverest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lin xu on 15.12.2016.
 */

public class CustomSliderView extends View {
    private Rect slider;
    private Paint sliderPaint;
    private GestureDetector gestureDetector;

    private List<Rect> eventMarkers;
    private Rect draggedMarker = null;
    private final int eventMarkerHeight = 20;
    private Paint eventPaint;

    private Rect[] discreteMarkers;
    private Paint discreteMarkerPaint;

    private Rect startDateRegion;
    private Rect endDateRegion;
    private Paint dateRegionPaint;

    private AddProject addProject;
    private boolean isOkayClicked;

    private int yearStr;
    private int monthStr;
    private int dayStr;

    private String startDate = "";
    private String endDate = "";

    private boolean checkStartDate;
    private Long startDateInMillis;
    private Long endDateInMillis;

    private float dayCount = 0f;

    private boolean slideDiscreteable;

    private int day;

    private static List<LearningStep> learningStepList;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLearningStepsDatabaseReference;
    private DatabaseReference mUserDatabaseRefercen;

    private LearningStepAdapter mLearningStepAdapter;



    public CustomSliderView(Context context) {
        super(context);
        init();
    }

    public CustomSliderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public void init() {
        int x = 100;
        int y = 150;
        int sideWidth = 200;
        int sideHeight = 1000;

        //delete Database

        String[] dbs = getContext().databaseList();
        for(String database: dbs){
            Log.d("DELETE DB", database);
            getContext().deleteDatabase(database);
        }

        // create a slider that we'll draw later
        slider = new Rect(x, y, x + sideWidth, y + sideHeight);

        eventMarkers = new ArrayList<>();

        // create the Paint and set its color
        sliderPaint = new Paint();
        sliderPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        eventPaint = new Paint();
        eventPaint.setColor(Color.BLACK);

        discreteMarkerPaint = new Paint();
        discreteMarkerPaint.setColor(Color.GRAY);

        dateRegionPaint = new Paint();
        dateRegionPaint.setColor(Color.CYAN);

        startDateRegion = new Rect(slider.left, slider.bottom + 10, slider.right, slider.bottom + 100);
        endDateRegion = new Rect(slider.left, slider.top - 100, slider.right, slider.top - 10);


        //firebase database initialize
        mFirebaseDatabase = SignInActivity.getmFirebaseDatabase();
        
        //mLearningStepsDatabaseReference = mFirebaseDatabase.getReference().child("User").child(SignInActivity.getCurrentUser().getId()).child("learning steps");
        //mUserDatabaseRefercen = SignInActivity.getmUserDatabaseReference();
        //mLearningStepsDatabaseReference = mUserDatabaseRefercen.child("User").child(SignInActivity.getCurrentUser().getId()).child("learning steps");

        mLearningStepsDatabaseReference = mFirebaseDatabase.getReference().child("User").child(SignInActivity.currentUser.getId()).child("learning steps");

        learningStepList = new ArrayList<>();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:

                if (startDateRegion.contains(x, y)) {
                    checkStartDate = true;
                    showDatePicker();
                    break;
                }

                if (endDateRegion.contains(x, y)) {
                    checkStartDate = false;
                    showDatePicker();
                    break;
                }

                if (slider.contains(x, y)) {
                    for (Rect eventMarker : eventMarkers) {
                        if (eventMarker.contains(x, y)) {
                            draggedMarker = eventMarker;
                        }
                    }

                    if (draggedMarker == null) {
                        eventMarkers.add(new Rect(slider.left, y - eventMarkerHeight / 2,
                                slider.right, y + eventMarkerHeight / 2));
                        draggedMarker = eventMarkers.get(eventMarkers.size() - 1);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(draggedMarker != null){
                    //todo: if draggedMarker does not lie on discrete marker, one has to drag it to the nearest discrete marker.
                    openDialog();
                }
                draggedMarker = null;

                break;

            case MotionEvent.ACTION_MOVE:
                if (draggedMarker != null) {
                    for(int i = 0; i < discreteMarkers.length; i++){
                        if(discreteMarkers[i] != null && discreteMarkers[i].contains(x,y) ){
                            draggedMarker.top = discreteMarkers[i].top;
                            draggedMarker.bottom= discreteMarkers[i].bottom;
                            day = i;
                        }
                    }
                }
                break;
        }

        // tell the View to redraw the Canvas
        invalidate();

        // tell the View that we handled the event
        return true;
    }

    private void openDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.set_learning_steps_dialog);
        dialog.setTitle("Set Learning Steps");

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDateInMillis);
        cal.add(Calendar.DATE, day);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final String date = sdf.format(cal.getTime());

        TextView text = (TextView)dialog.findViewById(R.id.set_learning_step_text);
        text.setText("Do you want to set learning step on " + date +" ?");

        final EditText note = (EditText)dialog.findViewById(R.id.learning_step_note);

        final EditText title = (EditText)dialog.findViewById(R.id.learning_step_title);

        Button ok = (Button)dialog.findViewById(R.id.learning_step_dialog_ok_button);
        Button cancel = (Button)dialog.findViewById(R.id.learning_step_dialog_cancel_button);

        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LearningStep learningStep = new LearningStep(date, String.valueOf(title.getText()), String.valueOf(note.getText()));
                //mLearningStepsDatabaseReference.child("learning_step").push().setValue(learningStep);
                learningStepList.add(learningStep);

                Collections.sort(learningStepList, new Comparator<LearningStep>() {
                    @Override
                    public int compare(LearningStep l1, LearningStep l2) {
                        return l1.getDate().compareToIgnoreCase(l2.getDate());
                    }
                });
                addProject.upDateLearningSteps(learningStepList);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: get rid of the chosen step, eventmarker should disappear.
                //dismissEventMarker(day);
                dialog.dismiss();
            }
        });
        dialog.show();
    }




    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();

        yearStr = c.get(Calendar.YEAR);
        monthStr = c.get(Calendar.MONTH);
        dayStr = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                if (isOkayClicked) {
                    if (checkStartDate) {
                        startDate = Integer.toString(selectedYear) + "/" + Integer.toString(selectedMonth + 1) + "/" + Integer.toString(selectedDay);
                        addProject.changeStartDate(startDate);
                    } else {
                        endDate = Integer.toString(selectedYear) + "/0" + Integer.toString(selectedMonth + 1) + "/" + Integer.toString(selectedDay);
                        addProject.changeEndDate(endDate);
                    }

                    yearStr = selectedYear;
                    monthStr = selectedMonth;
                    dayStr = selectedDay;

                }
                isOkayClicked = false;
            }
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), datePickerListener,
                yearStr, monthStr, dayStr);

        if (checkStartDate) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            Log.d("currentTimeMillis: ", Long.toString(System.currentTimeMillis()));


        } else {
            datePickerDialog.getDatePicker().setMinDate(startDateInMillis);
        }

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.cancel();
                            isOkayClicked = false;
                        }
                    }
                });

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            isOkayClicked = true;
                            DatePicker datePicker = datePickerDialog.getDatePicker();
                            datePickerListener.onDateSet(datePicker,
                                    datePicker.getYear(),
                                    datePicker.getMonth(),
                                    datePicker.getDayOfMonth());
                            c.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                            c.set(Calendar.MONTH, datePicker.getMonth());
                            c.set(Calendar.YEAR, datePicker.getYear());

                            if(checkStartDate){
                                startDateInMillis = c.getTimeInMillis();
                                Log.d("start date after ok", Long.toString(startDateInMillis));
                            }else{
                                endDateInMillis = c.getTimeInMillis();
                                Log.d("end date after ok", Long.toString(endDateInMillis));
                            }

                            if(addProject.isEndDateSet() && addProject.isStartDateSet()){
                                discreteSlider();
                            }
                            invalidate();
                        }
                    }
                });

        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    public static List<LearningStep> getLearningStepList() {
        return learningStepList;
    }

    private void discreteSlider() {
        dayCount = (endDateInMillis - startDateInMillis)/(1000 * 60 * 60 * 24);
        int days = (int)dayCount + 1;

        discreteMarkers = new Rect[days];

        float diff = (slider.bottom - slider.top)/ (days + 1);
        for(int i = 0 ; i < discreteMarkers.length; i++){
            discreteMarkers[i] = new Rect(slider.left, (int)(slider.bottom - diff * (i + 1)  - 10 ), slider.right, (int)(slider.bottom - diff * (i + 1) + 10));
        }
        slideDiscreteable = true;
    }


    private class mListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            Toast.makeText(getContext(), "gesture detector down", Toast.LENGTH_SHORT).show();
            return super.onDown(e);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(slider, sliderPaint);

        Log.d("isStartDateSet", ""+addProject.isStartDateSet());

        if(slideDiscreteable) {
            for (Rect discreteMarker : discreteMarkers) {
                canvas.drawRect(discreteMarker, discreteMarkerPaint);
            }
        }

        for (Rect eventMarker : eventMarkers) {
            canvas.drawRect(eventMarker, eventPaint);
        }

        canvas.drawRect(startDateRegion, dateRegionPaint);
        canvas.drawRect(endDateRegion, dateRegionPaint);
    }

    public void register(AddProject project) {
        this.addProject = project;
    }


}
