package com.linxu.mounteverest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lin xu on 15.12.2016.
 */

public class CustomSliderView extends View {
    private Rect sliderRef;
    private Rect slider;

    private Paint sliderPaint;
    private GestureDetector gestureDetector;

    private List<EventMarker> eventMarkers;
    private EventMarker draggedMarker = null;
    private final double eventMarkerRelativeHeight = 0.94;
    private Paint eventPaint;

    private float referenceHeight = 1300.0f;
    private float referenceWidth = 800.0f;

    private int currentWidth = 800;
    private int currentHeight = 1300;   //1300

    private Rect[] discreteMarkers;
    private Rect[] discreteMarkersRef;
    private Paint discreteMarkerPaint;

    private Rect startDateRegion;
    private Rect startDateRegionRef;

    private Rect endDateRegion;
    private Rect endDateRegionRef;

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

    private boolean slideDiscreteable;

    private static List<LearningStep> learningStepList;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLearningStepsDatabaseReference;
    private DatabaseReference mUserDatabaseRefercen;




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
        int sideHeight = 600;

        //delete Database

        String[] dbs = getContext().databaseList();
        for(String database: dbs){
            Log.d("DELETE DB", database);
            getContext().deleteDatabase(database);
        }

        // create a slider that we'll draw later
        sliderRef = new Rect(x, y, x + sideWidth, y + sideHeight);
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

        startDateRegionRef = new Rect(slider.left, slider.bottom + 10, slider.right, slider.bottom + 100);
        startDateRegion = new Rect(slider.left, slider.bottom + 10, slider.right, slider.bottom + 100);

        endDateRegionRef = new Rect(slider.left, slider.top - 100, slider.right, slider.top - 10);
        endDateRegion = new Rect(slider.left, slider.top - 100, slider.right, slider.top - 10);


        //firebase database initialize
        mFirebaseDatabase = SignInActivity.getmFirebaseDatabase();

        //mLearningStepsDatabaseReference = mFirebaseDatabase.getReference().child("User").child(SignInActivity.getCurrentUser().getId()).child("learning steps");
        //mUserDatabaseRefercen = SignInActivity.getmUserDatabaseReference();
        //mLearningStepsDatabaseReference = mUserDatabaseRefercen.child("User").child(SignInActivity.getCurrentUser().getId()).child("learning steps");

        mLearningStepsDatabaseReference = mFirebaseDatabase.getReference().child("User").child(SignInActivity.currentUser.getId()).child("learning steps");

        learningStepList = new ArrayList<>();

        Log.d("Init", "was called!");
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
                    for (EventMarker eventMarker : eventMarkers) {
                        if (eventMarker.getRect().contains(x, y)) {
                            draggedMarker = eventMarker;
                        }
                    }

                    if (draggedMarker == null && datesSet()) {
                        int day = yToMarkerIndex(y);
                        eventMarkers.add(
                                new EventMarker(discreteMarkers[day],
                                        //new EventMarker(new Rect(slider.left, y - eventMarkerHeight / 2,
                                        //slider.right, y + eventMarkerHeight / 2),
                                        null));
                        draggedMarker = eventMarkers.get(eventMarkers.size() - 1);
                        draggedMarker.setDay(day);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(draggedMarker != null){
                    //todo: if draggedMarker does not lie on discrete marker, one has to drag it to the nearest discrete marker.
                    if (draggedMarker.getLearningStep() == null) {
                        openDialog(draggedMarker);
                    } else {
                        draggedMarker.getLearningStep().setDate(dayToDate(draggedMarker.getDay()));
                        addProject.upDateLearningSteps(learningStepList);
                    }
                }
                draggedMarker = null;

                break;

            case MotionEvent.ACTION_MOVE:
                if (draggedMarker != null && discreteMarkers != null) {
//                    for(int i = 0; i < discreteMarkers.length; i++){
//                        if(discreteMarkers[i] != null && discreteMarkers[i].contains(x,y) ){
//                            draggedMarker.getRect().top = discreteMarkers[i].top;
//                            draggedMarker.getRect().bottom = discreteMarkers[i].bottom;
//                            draggedMarker.setDay(i);
//                        }
//                    }
                    int day = yToMarkerIndex(y);
                    draggedMarker.setRect(discreteMarkers[day]);
                    draggedMarker.setDay(day);
                }
                break;
        }

        // tell the View to redraw the Canvas
        invalidate();

        // tell the View that we handled the event
        return true;
    }

    private int yToMarkerIndex(int y) {
        int sliderHeight = slider.bottom - slider.top;
        double fraction = 1 - 1.0*(y - slider.top) / sliderHeight;
        return (int) Math.max(0, Math.min(Math.floor(fraction * discreteMarkers.length),
                discreteMarkers.length-1));
    }

    private boolean datesSet() {
        return startDateInMillis != null && endDateInMillis != null;
    }

    private void openDialog(final EventMarker eventMarker) {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.set_learning_steps_dialog);
        dialog.setTitle("Set Learning Steps");

        final String date = dayToDate(eventMarker.getDay());

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
                        return l2.getDate().compareToIgnoreCase(l1.getDate());
                    }
                });
                addProject.upDateLearningSteps(learningStepList);
                eventMarker.setLearningStep(learningStep);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventMarker.getLearningStep() == null) {
                    eventMarkers.remove(eventMarker);
                }
                invalidate();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String dayToDate(int day) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDateInMillis);
        cal.add(Calendar.DATE, day);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(cal.getTime());
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
            if(addProject.isStartDateSet()){
                datePickerDialog.getDatePicker().setMinDate(startDateInMillis);
            }
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
        //double dayCount = (endDateInMillis - startDateInMillis)/(1000 * 60 * 60 * 24);

        int nDays = Math.round((endDateInMillis - startDateInMillis)/(1000 * 60 * 60 * 24));
        int nMarkers = nDays + 1;

        discreteMarkersRef = new Rect[nMarkers];

        double diff = (sliderRef.bottom - sliderRef.top)/ nMarkers;
        for(int i = 0 ; i < discreteMarkersRef.length; i++){
            double centerY = sliderRef.bottom - (i + 0.5)*diff;
            discreteMarkersRef[i] = new Rect(
                    sliderRef.left,
                    (int)(centerY - diff*eventMarkerRelativeHeight/2),
                    sliderRef.right,
                    (int)(centerY + diff*eventMarkerRelativeHeight/2));
        }
        slideDiscreteable = true;

        updateRectSizes();
    }


    private class mListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            Toast.makeText(getContext(), "gesture detector down", Toast.LENGTH_SHORT).show();
            return super.onDown(e);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("size changed: ", "w: " + w + ", h: " + h );
        currentWidth = w;
        currentHeight = h;

        updateRectSizes();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void updateRectSizes() {
        double factorX = currentWidth / referenceWidth;
        double factorY = currentHeight / referenceHeight;

        slider = scaleRect(sliderRef, factorX, factorY);
        startDateRegion = scaleRect(startDateRegionRef, factorX, factorY);
        endDateRegion = scaleRect(endDateRegionRef, factorX, factorY);

        Log.d("discreteMarkersRef", "" + discreteMarkersRef);

        if (discreteMarkersRef != null) {
            discreteMarkers = new Rect[discreteMarkersRef.length];
            for (int i = 0; i < discreteMarkersRef.length; i++) {
                discreteMarkers[i] = scaleRect(discreteMarkersRef[i], factorX, factorY);
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {


        canvas.drawRect(slider, sliderPaint);

        if(slideDiscreteable) {
            for (Rect discreteMarker : discreteMarkers) {
                canvas.drawRect(discreteMarker, discreteMarkerPaint);
            }
        }

        for (EventMarker eventMarker : eventMarkers) {
            canvas.drawRect(eventMarker.getRect(), eventPaint);
        }

        canvas.drawRect(startDateRegion, dateRegionPaint);
        canvas.drawRect(endDateRegion, dateRegionPaint);
    }

    private Rect scaleRect(Rect original, double factorX, double factorY) {
        return new Rect((int) Math.rint(factorX*original.left),
                (int) Math.rint(factorY*original.top),
                (int) Math.rint(factorX*original.right),
                (int) Math.rint(factorY*original.bottom));
    }

    public void register(AddProject project) {
        this.addProject = project;
    }


}
