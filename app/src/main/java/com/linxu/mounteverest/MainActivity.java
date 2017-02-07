package com.linxu.mounteverest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.Serializable;
import java.util.List;



public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    //private ProgressBar progressBar;
    //LinearLayout ll;
    //Bundle extras;
    private ProgressView progressView;
    private pl.droidsonroids.gif.GifTextView climber;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    //private FirebaseDatabase mFirebaseDatabase;
    //private DatabaseReference mLearningProjectDatabaseReference;
    //private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder> mFirebaseAdapter;
    //private FirebaseRemoteConfig mFirebaseRemoteConfig;
    //private FirebaseAnalytics mFirebaseAnalytics;

    private String mUsername;
    private String mPhotoUrl;
    private GoogleApiClient googleApiClient;

    public static final String ANONYMOUS = "anonymous";

    //private FirebaseDateBaseHandler firebaseDateBaseHandler;
    //private User currentUser;
    //private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        //firebaseDateBaseHandler = new FirebaseDateBaseHandler();
        //firebaseDateBaseHandler.Init();

        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mLearningProjectDatabaseReference = mFirebaseDatabase.getReference().child("learning_project");

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

        progressView = (ProgressView)findViewById(R.id.progress_view);

        progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomViewFromThumb();
            }
        });

       // snow = (pl.droidsonroids.gif.GifTextView)findViewById(R.id.snow);
        climber = (pl.droidsonroids.gif.GifTextView)findViewById(R.id.climber_gif_text_view);
        int mm = 4;
        Drawable d = getResources().getDrawable(R.drawable.climber_transparent);
        int h = d.getIntrinsicHeight();
        int w = d.getIntrinsicWidth();
        Log.d("width","" + w);

        RelativeLayout.LayoutParams parm = new RelativeLayout.LayoutParams(w/mm, h/mm);
        climber.setLayoutParams(parm);

        climber.setX(380f);

        climber.setY(1000f);

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

        //progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        //extras = getIntent().getExtras();
        //validateEmptyProjectData();

    }

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
