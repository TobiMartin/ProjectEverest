package com.linxu.mounteverest;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    //LinearLayout ll;
    //Bundle extras;
    private ProgressView progressView;
    //private pl.droidsonroids.gif.GifTextView climber;
    private Main_Activity_Layout mainActivityLayout;
    //private Animator mCurrentAnimator;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityLayout = new Main_Activity_Layout(this);
        setContentView(mainActivityLayout);

    }




    private void openDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.check_learning_steps_dialog);
        dialog.setTitle("Check Learning Steps");

        TextView text = (TextView)dialog.findViewById(R.id.check_learning_step_text);
        // text.setText("Do you want to set learning step on " + date +" ?");

        final TextView note = (TextView)dialog.findViewById(R.id.learning_step_note2);

        final TextView title = (TextView)dialog.findViewById(R.id.learning_step_title2);

        Button done = (Button)dialog.findViewById(R.id.learning_step_dialog_done_button);
        Button edit = (Button)dialog.findViewById(R.id.learning_step_dialog_edit_button);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* LearningStep learningStep = new LearningStep(date, String.valueOf(title.getText()), String.valueOf(note.getText()));
                db.addLearningStep(learningStep);
                List<LearningStep> learningStepList = db.getAllLearningSteps();

                addProject.upDateLearningSteps(learningStepList); */
                dialog.dismiss();
                // TODO: animation should move to next learning step
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProject.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainActivityLayout.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainActivityLayout.resume();
    }


    /* progressView = (ProgressView)findViewById(R.id.progress_view);
        progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomViewFromThumb();
            }
        }); */

       // snow = (pl.droidsonroids.gif.GifTextView)findViewById(R.id.snow);
        /*climber = (pl.droidsonroids.gif.GifTextView)findViewById(R.id.climber_gif_text_view);
        int mm = 4;
        Drawable d = getResources().getDrawable(R.drawable.climber_transparent);
        int h = d.getIntrinsicHeight();
        int w = d.getIntrinsicWidth();
        Log.d("width","" + w);

        RelativeLayout.LayoutParams parm = new RelativeLayout.LayoutParams(w/mm, h/mm);
        climber.setLayoutParams(parm);

        climber.setX(380f);
        climber.setY(1000f);*/
        //progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        //extras = getIntent().getExtras();
        //validateEmptyProjectData();

    //}

    private void zoomViewFromThumb() {
        //todo: do zoom in animation
    }

    //private void validateEmptyProjectData() {
    //    if(extras == null){
    //        Intent intent = new Intent(MainActivity.this, AddProject.class);
    //        startActivity(intent);
    //    }else{
    //        bindButton();
    //    }
    //}
//
    //private int[] extractLearningStepPercentage() {
    //    int[] learningStepPercentage;
    //    learningStepPercentage = new int[extras.size()];
    //    for(int i = 0; i < extras.size(); i++){
    //        learningStepPercentage[i] = Integer.parseInt(extras.getString("learningStepPercent" + (i+1)));
    //    }
    //    Log.d("learning Percentage: " , " " + Arrays.toString(learningStepPercentage));
    //    return learningStepPercentage;
    //}

    //private void bindButton() {
    //    ll = (LinearLayout)findViewById(R.id.button_layoout);
    //    for(int i = 0; i < extractLearningStepPercentage().length; i++){
    //        final Button button = new Button(this);
    //        button.setId(i+1);
    //        button.setText("learningStepPercentage" + (i+1));
    //        ll.addView(button);
    //        final int finalI = i;
    //        button.setOnClickListener(new View.OnClickListener() {
    //            int sum;
    //            @Override
    //            public void onClick(View view) {
    //                for(int i = 0; i<button.getId(); i++){
    //                    sum += extractLearningStepPercentage()[i];
    //                }
    //                progressBar.setProgress(sum);
    //                Toast.makeText(MainActivity.this, "sum: "+ sum, Toast.LENGTH_SHORT).show();
    //            }
    //        });
    //    }
    //}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                startActivity(new Intent(this, Search.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(this, SignInFirebaseActivity.class));
                return true;
            case R.id.add_project:
                startActivity(new Intent(this, AddProject.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Context getContext() {
        return context;
    }
}
