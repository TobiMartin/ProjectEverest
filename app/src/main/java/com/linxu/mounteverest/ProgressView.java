package com.linxu.mounteverest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * Created by lin xu on 21.01.2017.
 */

public class ProgressView extends View{

    private Rect progressBar;

    private ArrayList<Rect> ladderMarkers;
    private Paint progressBarPaint;
    private Paint ladderPaint;
    private DateBaseHandler db;

    public ProgressView(Context context) {
        super(context);
        init();
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        int x = 400;
        int y = 150;
        int sideWidth = 200;
        int sideHeight = 1300;


        String[] dbs = getContext().databaseList();
        for(String database: dbs){
            Log.d("DELETE DB", database);
            getContext().deleteDatabase(database);
        }

        progressBar = new Rect(x, y, x + sideWidth, y + sideHeight);
        ladderMarkers = new ArrayList<>();

        float diff = (progressBar.bottom - progressBar.top)/11; //11 = i + 1;
        for(int i = 0; i < 10; i++){
            ladderMarkers.add(new Rect(progressBar.left, (int)(progressBar.bottom - diff * (i + 1)  - 10 ), progressBar.right, (int)(progressBar.bottom - diff * (i + 1) + 10)));
        }

        progressBarPaint = new Paint();
        progressBarPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        ladderPaint = new Paint();
        ladderPaint.setColor(Color.BLACK);

        db = new DateBaseHandler(getContext());
    }

   /* @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(progressBar, progressBarPaint);

        for(Rect ladderMarker : ladderMarkers){
            canvas.drawRect(ladderMarker, ladderPaint);
        }
    } */

    public Rect getProgressBar(){
        return progressBar;
    }

    public Paint getProgressBarPaint(){
        return progressBarPaint;
    }

    public ArrayList<Rect> getLadderMarks(){
        return ladderMarkers;
    }

    public Paint getLadderPaint(){
        return ladderPaint;
    }

}
