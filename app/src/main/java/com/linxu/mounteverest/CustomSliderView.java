package com.linxu.mounteverest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by lin xu on 15.12.2016.
 */

public class CustomSliderView extends View{
    private Rect rectangle;
    private Paint paint;
    private GestureDetector gestureDetector;

    public CustomSliderView(Context context) {
        super(context);
        init();
    }

    public CustomSliderView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init();
    }

    public void init(){
        int x = 50;
        int y = 50;
        int sideLength = 200;

        // create a rectangle that we'll draw later
        rectangle = new Rect(x, y, sideLength, sideLength);

        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        gestureDetector = new GestureDetector(getContext(), new mListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = gestureDetector.onTouchEvent(event);
        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // User is done scrolling, it's now safe to do things like autocenter
                //stopScrolling();
                result = true;
            }
        }
        return result;
        //int eventAction = event.getAction();
//
        //// you may need the x/y location
        //int x = (int)event.getX();
        //int y = (int)event.getY();
//
        //// put your code in here to handle the event
        //switch (eventAction) {
        //    case MotionEvent.ACTION_DOWN:
        //        Toast.makeText(getContext(), "action down", Toast.LENGTH_SHORT).show();
        //        break;
        //    case MotionEvent.ACTION_UP:
        //        Toast.makeText(getContext(), "action up", Toast.LENGTH_SHORT).show();
        //        break;
        //    case MotionEvent.ACTION_MOVE:
        //        Toast.makeText(getContext(), "action move", Toast.LENGTH_SHORT).show();
        //        break;
        //}
//
        //// tell the View to redraw the Canvas
        //invalidate();
//
        //// tell the View that we handled the event
        //return true;
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
        canvas.drawRect(rectangle, paint);
    }
}
