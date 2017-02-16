package com.linxu.mounteverest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.w3c.dom.Text;


public class MountainClimbActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ProgressView progressView;
    private pl.droidsonroids.gif.GifTextView climber;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String mUsername;
    private String mPhotoUrl;
    private GoogleApiClient googleApiClient;

    public static final String ANONYMOUS = "anonymous";
    private float currentPos;
    private int currentStep = 0;

    private TextView projectNameView;

    private static LearningProject currentProject;

    public static void setCurrentProject(LearningProject currentProject) {
        MountainClimbActivity.currentProject = currentProject;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);
        projectNameView = (TextView)findViewById(R.id.project_name_view);
        projectNameView.setText(currentProject.getName());
        projectNameView.bringToFront();
        // Initialize Firebase Auth
        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //to avoid sign in



        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                //.addApi(AppInvite.API)
                .build();

        //if(AddProject.addProjectDoneBoolean == false){
        //    openAddProjectDialog();
        //    AddProject.addProjectDoneBoolean = true;
        //}

        progressView = (ProgressView)findViewById(R.id.progress_view);



        climber = (pl.droidsonroids.gif.GifTextView)findViewById(R.id.climber_gif_text_view);
        int mm = 4;
        Drawable d = getResources().getDrawable(R.drawable.climber_transparent);
        int h = d.getIntrinsicHeight();
        int w = d.getIntrinsicWidth();

        RelativeLayout.LayoutParams parm = new RelativeLayout.LayoutParams(w/mm, h/mm);
        climber.setLayoutParams(parm);

        climber.setX(380f);
        climber.setY(progressView.rungY(0));

        climber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentStep < progressView.getNLearningSteps()) {
                    openDialog();
                }
            }
        });

        CircularImageView circularImageView = (CircularImageView)findViewById(R.id.circle_image_view);
        circularImageView.setX(140f);
        circularImageView.setY(400f);
        circularImageView.setScaleX(2f);
        circularImageView.setScaleY(2f);
        // Set Border
        circularImageView.setBorderColor(Color.LTGRAY);
        circularImageView.setBorderWidth(10);
        // Add Shadow with default param
        circularImageView.addShadow();
        // or with custom param
        circularImageView.setShadowRadius(15);
        circularImageView.setShadowColor(Color.RED);
    }

    //private void openAddProjectDialog() {
    //    AlertDialog alertDialog = new AlertDialog.Builder(MountainClimbActivity.this).create();
    //    alertDialog.setTitle("Hi");
    //    alertDialog.setMessage("Set your learning steps!");
//
    //    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
    //        @Override
    //        public void onClick(DialogInterface dialogInterface, int i) {
    //            Intent intent = new Intent(MountainClimbActivity.this, AddProject.class);
    //            startActivity(intent);
    //        }
    //    });
    //    alertDialog.show();
    //}

    private void openDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MountainClimbActivity.this).create();
        alertDialog.setTitle("Next Step");
        LearningStep learningStep = currentProject.getLearningSteps().get(
                progressView.getNLearningSteps() - 1 - currentStep);

        alertDialog.setMessage("Have you finished your current step for " +
                learningStep.getDate() + "? Title: " +
                learningStep.getTitle() + "? Note: " + learningStep.getNote());

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (currentStep == progressView.getNLearningSteps() - 1) {
                    showSuccessAlert();
                }
                if (currentStep <= progressView.getNLearningSteps() - 1) {
                    moveToNextLadder(climber);
                }
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                schneeSturm();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void schneeSturm() {

    }

    private void showSuccessAlert() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MountainClimbActivity.this).create();
        alertDialog.setTitle("Congratulations!");
        alertDialog.setMessage("You have completed all learning steps!");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MountainClimbActivity.this, SelectProjectActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void moveToNextLadder(View view) {
        currentStep += 1;
        float previousPos = climber.getY();
        currentPos = progressView.rungY(currentStep);

        // On last step, move outside the screen
        if (currentStep == progressView.getNLearningSteps()) {
            currentPos = -climber.getHeight();
        }

        climber.setY(currentPos);
        Animation moveAnimation = new TranslateAnimation(0, 0,
               previousPos - currentPos, 0);
        moveAnimation.setDuration(1000);

        //moveAnimation.setFillAfter(true);
        if (currentStep == progressView.getNLearningSteps()) {
            Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(1000);
            //alphaAnimation.setFillAfter(true);

            AnimationSet comp = new AnimationSet(true);
            // comp.setFillEnabled(true);
            comp.setFillAfter(true);
            comp.addAnimation(alphaAnimation);
            comp.addAnimation(moveAnimation);
            view.startAnimation(comp);
        } else {
            view.startAnimation(moveAnimation);
        }
    }


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
                startSearch();
                return true;
            case R.id.profile:
                startProfile();
                return true;
            case R.id.add_project:
                startActivity(new Intent(this, AddProject.class));
                return true;
            case R.id.select_project:
                startActivity(new Intent(this, SelectProjectActivity.class));
                return true;
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(googleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startProfile() {
        Intent intent = new Intent(this, ProfileDetail.class);
        //intent.putExtra("User", currentUser);
        startActivity(intent);
    }

    private void startSearch() {
        Intent intent = new Intent(this, ViewProfiles.class);
        //intent.putExtra("UserList", (Serializable)userList);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
