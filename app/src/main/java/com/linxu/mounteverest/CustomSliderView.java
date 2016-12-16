package com.linxu.mounteverest;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lin xu on 15.12.2016.
 */

public class CustomSliderView extends View{
    private Rect slider;
    private Paint sliderPaint;
    private GestureDetector gestureDetector;

    private List<Rect> eventMarkers;
    private Rect draggedMarker = null;
    private final int eventMarkerHeight = 80;
    private Paint eventPaint;

    private Rect startDateRegion;
    private Paint dateRegionPaint;

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    public CustomSliderView(Context context) {
        super(context);
        init();
    }

    public CustomSliderView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init();
    }

    public void init(){
        int x = 100;
        int y = 150;
        int sideWidth = 200;
        int sideHeight = 1500;



        // create a slider that we'll draw later
        slider = new Rect(x, y, x+sideWidth, y+sideHeight);

        eventMarkers = new ArrayList<>();

        // create the Paint and set its color
        sliderPaint = new Paint();
        sliderPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        eventPaint = new Paint();
        eventPaint.setColor(Color.BLACK);

        startDateRegion = new Rect(slider.left, slider.top - 100, slider.right, slider.top);
        dateRegionPaint = new Paint();
        dateRegionPaint.setColor(Color.CYAN);
        //gestureDetector = new GestureDetector(getContext(), new mListener());



        onDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar.getInstance().set(Calendar.YEAR, year);
                Calendar.getInstance().set(Calendar.MONTH, month);
                Calendar.getInstance().set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       // boolean result = gestureDetector.onTouchEvent(event);
       // if (!result) {
       //     if (event.getAction() == MotionEvent.ACTION_UP) {
       //         // User is done scrolling, it's now safe to do things like autocenter
       //         //stopScrolling();
       //         result = true;
       //     }
       // }
       // return result;
        int eventAction = event.getAction();

        // you may need the x/y location
        int x = (int)event.getX();
        int y = (int)event.getY();






        // put your code in here to handle the event
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                //double heightFraction = (double)(y - slider.top) / slider.height();
                //Toast.makeText(getContext(), "down", Toast.LENGTH_SHORT).show();
                if (startDateRegion.contains(x, y)) {
                    openDatePickerDialog(onDateSetListener);
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
                draggedMarker = null;

                break;

            case MotionEvent.ACTION_MOVE:
                if (draggedMarker != null) {
                    draggedMarker.top = y - eventMarkerHeight / 2;
                    draggedMarker.bottom = y + eventMarkerHeight / 2;
                }
                break;
        }


        // tell the View to redraw the Canvas
        invalidate();
//
        // tell the View that we handled the event
        return true;
//
    }

    private class mListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            Toast.makeText(getContext(), "gesture detector down", Toast.LENGTH_SHORT).show();
            return super.onDown(e);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(slider, sliderPaint);

        for (Rect eventMarker : eventMarkers) {
            canvas.drawRect(eventMarker, eventPaint);
        }

        canvas.drawRect(startDateRegion, dateRegionPaint);


    }

    private void openDatePickerDialog(DatePickerDialog.OnDateSetListener dateSetListener) {
        new DatePickerDialog(getContext(), dateSetListener, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
    }
}
