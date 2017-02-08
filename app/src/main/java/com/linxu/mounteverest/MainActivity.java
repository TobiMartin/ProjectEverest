package com.linxu.mounteverest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.CollapsibleActionView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ProgressView progressView;
    private pl.droidsonroids.gif.GifTextView climber;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String mUsername;
    private String mPhotoUrl;
    private GoogleApiClient googleApiClient;

    public static final String ANONYMOUS = "anonymous";
    private float currentPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        // Set default username is anonymous.
        mUsername = ANONYMOUS;


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
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
        if(AddProject.addProjectDoneBoolean == false){
            openAddProjectDialog();
            AddProject.addProjectDoneBoolean = true;
        }

        progressView = (ProgressView)findViewById(R.id.progress_view);

        progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomViewFromThumb();
            }
        });

        climber = (pl.droidsonroids.gif.GifTextView)findViewById(R.id.climber_gif_text_view);
        int mm = 4;
        Drawable d = getResources().getDrawable(R.drawable.climber_transparent);
        int h = d.getIntrinsicHeight();
        int w = d.getIntrinsicWidth();
        Log.d("width","" + w);

        RelativeLayout.LayoutParams parm = new RelativeLayout.LayoutParams(w/mm, h/mm);
        climber.setLayoutParams(parm);

        climber.setX(380f);
        climber.setY(1300f);
        climber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
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

    private void openAddProjectDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("hi");
        alertDialog.setMessage("set your learningsteps!");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this, AddProject.class);
                startActivity(intent);
            }
        });
        alertDialog.show();
    }

    private void openDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("hi");
        alertDialog.setMessage("this is my app");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveToNextLadder(climber);
            }
        });
        alertDialog.show();
    }

    private void zoomViewFromThumb() {
        //todo: do zoom in animation
    }

    private void moveToNextLadder(View view) {
        Animation animation = new TranslateAnimation(0, 0, currentPos, getNextLadder(climber.getY()));
        currentPos += getNextLadder(climber.getY());
        animation.setDuration(200);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    private float getNextLadder(float y) {
        List difference = new ArrayList();
        for(int i =0; i< ProgressView.getLadderMarkers().size(); i++){
            if(y - ProgressView.getLadderMarkers().get(i).top > 0){
                difference.add(y - ProgressView.getLadderMarkers().get(i).top);
            }
        }
        float differencePosition = -(float)Collections.min(difference);
        Log.d("differencePosition:  ","" + differencePosition);
        Log.d("y: ", "" + y);
        climber.setY(y + differencePosition);
        return differencePosition;
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
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(googleApiClient);
                mUsername = ANONYMOUS;
                AddProject.addProjectDoneBoolean = false;
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
