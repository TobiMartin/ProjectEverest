package com.linxu.mounteverest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.droidsonroids.gif.GifTexImage2D;
import pl.droidsonroids.gif.GifTextView;

/**
 * Created by Vanessa on 21.01.2017.
 */

public class Main_Activity_Layout extends SurfaceView implements Runnable {

    Thread thread = null;
    boolean canDraw = false;
    Canvas canvas;

    Bitmap bg_mountain;

    SurfaceHolder surfaceHolder;

    private Rect progressBar;

    private ArrayList<Rect> ladderMarkers;
    private Paint progressBarPaint;
    private Paint ladderPaint;
    private DateBaseHandler db;
    private Bitmap gif;
    private int gifX;
    private int gifY;
    private Dialog dialog;
    private Path animationPath;
    private Button done, edit;
    private boolean doneWasClicked = false;

    int x = 400;
    int y = 150;
    int sideWidth = 200;
    int sideHeight = 800;

    int x1, y1;

    public Main_Activity_Layout(Context context) {
        super(context);
        surfaceHolder = getHolder();

        bg_mountain = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        bg_mountain = Bitmap.createScaledBitmap(bg_mountain, Resources.getSystem().getDisplayMetrics().widthPixels,  Resources.getSystem().getDisplayMetrics().heightPixels, true);

        gif = BitmapFactory.decodeResource(getResources(), R.drawable.climb);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.check_learning_steps_dialog);
        dialog.setTitle("Check Learning Steps");

        final TextView note = (TextView)dialog.findViewById(R.id.learning_step_note2);

        final TextView title = (TextView)dialog.findViewById(R.id.learning_step_title2);

        done = (Button)dialog.findViewById(R.id.learning_step_dialog_done_button);
        edit = (Button)dialog.findViewById(R.id.learning_step_dialog_edit_button);
       // gif = Bitmap.createScaledBitmap(gif, (int)(progressBar.bottom), (int) (progressBar.left), true);
        gifX =  x + (sideWidth/2) - 30;
        gifY =  y + sideHeight;
    }


    private void init() {

       /* int x = 400;
        int y = 150;
        int sideWidth = 200;
        int sideHeight = 800; */


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

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        x1 = (int) event.getX();
        y1 = (int) event.getY();
        switch (eventAction) {
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < ladderMarkers.size(); i++) {
                    if ((ladderMarkers.get(i) != null) && (ladderMarkers.get(i).contains(x1,y1))) {
                        //todo: generalize the learning steps
                       // openDialog();
                        dialog.show();
                    }
                }
                //ladderMarkers.get(i) = null;

                break;
        }

        // tell the View to redraw the Canvas
        invalidate();

        // tell the View that we handled the event
        return true;
    }
    /*private void openDialog() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.check_learning_steps_dialog);
        dialog.setTitle("Check Learning Steps");

        final TextView note = (TextView)dialog.findViewById(R.id.learning_step_note2);

        final TextView title = (TextView)dialog.findViewById(R.id.learning_step_title2);

       done = (Button)dialog.findViewById(R.id.learning_step_dialog_done_button);
       edit = (Button)dialog.findViewById(R.id.learning_step_dialog_edit_button);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                // TODO: animation should move to next learning step
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProject.class);
                startActivity(intent);

                //// TODO:  change to AddProjectActivity to change and save learning step and update old learning step
                dialog.dismiss();
            }
        });
        dialog.show();
    } */




   /* @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bg_mountain, 0, 0, null);

        canvas.drawRect(progressBar, progressBarPaint);

        for(Rect ladderMarker : ladderMarkers){
            canvas.drawRect(ladderMarker, ladderPaint);
        }
    } */

    @Override
    public void run() {
    //Canvas c;
        while(canDraw = true){
            //c = null;
            if(!surfaceHolder.getSurface().isValid()){
                continue; //start at beginning of while-loop
            }

            canvas = surfaceHolder.lockCanvas();

            canvas.drawBitmap(bg_mountain, 0, 0, null);
            init();

            canvas.drawRect(progressBar, progressBarPaint);

            for(Rect ladderMarker : ladderMarkers){
                canvas.drawRect(ladderMarker, ladderPaint);
            }

            gif = Bitmap.createScaledBitmap(gif, (progressBar.width()/3), (ladderMarkers.get(1).height()) + 50, true);



            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    doneWasClicked = true;
                }

            });

            /*for(int i = 0; i < ladderMarkers.size(); i++) {
                if (gifY > ladderMarkers.get(i).centerY() && doneWasClicked == true) {
                    gifX = gifX;
                    gifY -= 1;

                    if (gifY == ladderMarkers.get(i).centerY()) {
                        gifY = ladderMarkers.get(i).centerY();
                    }
                }
                canvas.drawBitmap(gif, gifX, gifY, null);

            }*/

            if (gifY > ladderMarkers.get(0).centerY()  && doneWasClicked == true) {
             //   while(!(gifY < ladderMarkers.get(1)))
                gifX = gifX;
                gifY -= 1;
                ;
            }

            canvas.drawBitmap(gif, gifX, gifY, null);

            /*else if (gifY > ladderMarkers.get(1).centerY() && doneWasClicked == true) {
                gifX = gifX;
                gifY -= 1;
                canvas.drawBitmap(gif, gifX, gifY, null);
            } */



            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    public void pause(){

        canDraw = false;

        while(true) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        thread = null;
    }

    public void resume(){

        canDraw = true;
        thread = new Thread(this);
        thread.start();
    }



}
