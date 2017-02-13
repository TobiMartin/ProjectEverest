package com.linxu.mounteverest;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Vanessa on 13.02.2017.
 */

public class Main_Activity_Layout extends SurfaceView implements SurfaceHolder.Callback {

    //Thread thread = null;
    boolean canDraw = false;
    Canvas canvas;
    private PanelThread thread;
    private ProgressView progressView;

    Bitmap bg_mountain;

    SurfaceHolder surfaceHolder;

    private Rect progressBar;

    private ArrayList<Rect> ladderMarkers;
    private Paint progressBarPaint;
    private Paint ladderPaint;
    private DateBaseHandler db;
    private Bitmap gif;
    private Bitmap gif2;
    private int gifX;
    private int gifY;
    private Dialog dialog;
    private Path animationPath;
    private Button done, edit;
    private boolean doneWasClicked = false;
    private Handler h;
    boolean nextLadderStep = false;
    private Context context;

    int x = 400;
    int y = 150;
    int sideWidth = 200;
    int sideHeight = 1300;

    int x1, y1;



    public Main_Activity_Layout(Context context) {
        super(context);
        surfaceHolder = getHolder();

        init();

        //progressView = new ProgressView(this);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.check_learning_steps_dialog);
        dialog.setTitle("Check Learning Steps");

        bg_mountain = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        bg_mountain = Bitmap.createScaledBitmap(bg_mountain, Resources.getSystem().getDisplayMetrics().widthPixels,  Resources.getSystem().getDisplayMetrics().heightPixels, true);

        gif = BitmapFactory.decodeResource(getResources(), R.drawable.climb);
       // gif2 = BitmapFactory.decodeResource(getResources(), R.drawable.climb2);
        /*BitmapDrawable drawable = (BitmapDrawable)context.getResources()
                .getDrawable(R.drawable.climbing);
        gif = drawable.getBitmap(); */

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.check_learning_steps_dialog);
        dialog.setTitle("Check Learning Steps");

        final TextView note = (TextView)dialog.findViewById(R.id.learning_step_note2);

        final TextView title = (TextView)dialog.findViewById(R.id.learning_step_title2);

        done = (Button)dialog.findViewById(R.id.learning_step_dialog_done_button);
        edit = (Button)dialog.findViewById(R.id.learning_step_dialog_edit_button);
        // gif = Bitmap.createScaledBitmap(gif, (int)(progressBar.bottom), (int) (progressBar.left), true);
        gifX =  x + (sideWidth/2)-30;
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

        getHolder().addCallback(this);
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
                if((ladderMarkers.get(0) != null) && ladderMarkers.get(0).contains(x1,y1) && !(gifY < ladderMarkers.get(1).centerY())){
                    dialog.show();
                }

                int i = 1;
                while(ladderMarkers.size() > i) {
                    //  for (int i = 0; i < ladderMarkers.size(); i++) {
                    if ((ladderMarkers.get(i) != null) && (ladderMarkers.get(i).contains(x1,y1))) {
                        //todo: generalize the learning steps
                        // openDialog();
                        if(gifY == ladderMarkers.get((i-1)%ladderMarkers.size()).centerY()-1) {
                            dialog.show();
                        }
                        else if(gifY == ladderMarkers.get((i-1)%ladderMarkers.size()).centerY()-1){
                            Toast.makeText(context,"You should first finish another step", Toast.LENGTH_SHORT).show();
                        }
                    }
                    i++;
                }
                //  ladderMarkers.get(i) = null;


                // break;
        }
        return true;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bg_mountain, 0, 0, null);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                doneWasClicked = true;
            }

        });

        if (gifY > ladderMarkers.get(0).centerY() && (gifY <= gifY) && doneWasClicked == true) {
            canvas.save();
            canvas.scale(2.0f, 2.0f, x1,y1);
        }
        int a = 0;
        while(ladderMarkers.size() > a) {
            //if (listIterator.hasNext()) {
            if ((gifY > ladderMarkers.get((a + 1) % ladderMarkers.size()).centerY()) && (gifY < ladderMarkers.get(a).centerY()) && doneWasClicked == true) {
                canvas.save();
                canvas.scale(2.0f, 2.0f, x1, y1);
            }
            a++;
        }
        canvas.drawRect(progressBar, progressBarPaint);



        for (Rect ladderMarker : ladderMarkers) {
            canvas.drawRect(ladderMarker, ladderPaint);
        }


        gif = Bitmap.createScaledBitmap(gif, (ladderMarkers.get(0).width() /3), (ladderMarkers.get(0).width()/3), true);
        // gif2 = Bitmap.createScaledBitmap(gif2, (ladderMarkers.get(0).width() /3), (ladderMarkers.get(0).width()/3), true);


        if (gifY > ladderMarkers.get(0).centerY() && (gifY <= gifY) && doneWasClicked == true) {
            gifX = gifX;
            gifY -= 1;
        } else if (gifY == ladderMarkers.get(0).centerY()) {
            doneWasClicked = false;
            gifY = gifY - 1;
        }

        int j = 0;
        while(ladderMarkers.size() > j) {
            if ((gifY > ladderMarkers.get((j + 1) % ladderMarkers.size()).centerY()) && (gifY < ladderMarkers.get(j).centerY()) && doneWasClicked == true) {
                gifX = gifX;
                gifY -= 1;
            } else if (gifY == (ladderMarkers.get((j + 1) % ladderMarkers.size()).centerY())) {
                doneWasClicked = false;
                gifY = gifY - 1;
            }
            j++;
        }

        canvas.drawBitmap(gif, gifX, gifY, null);
        // canvas.drawBitmap(gif2, gifX, gifY, null);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        setWillNotDraw(false);
        thread = new PanelThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (thread != null) {
            boolean retry = true;
            while (retry) {
                try {
                    thread.setRunning(false);
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {

                }
            }
            thread = null;
        }

    }
}
