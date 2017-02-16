package com.linxu.mounteverest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin xu on 15.02.2017.
 */

public class SelectProject extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient googleApiClient;

    private ListView projectListView;
    private LearningProjectAdapter learningProjectAdapter;
    private List<LearningProject> projectList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_project);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //projectList = new ArrayList<>();
        //projectList.add(new LearningProject(null, "meinTextProject1"));
        projectList = loadProjectsFromFirebase();
        projectListView = (ListView)findViewById(R.id.project_list_view);
        learningProjectAdapter = new LearningProjectAdapter(this, projectList);
        projectListView.setAdapter(learningProjectAdapter);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                //.addApi(AppInvite.API)
                .build();

    }

    private List<LearningProject> loadProjectsFromFirebase() {
        return new ArrayList<>();
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
            case R.id.profile:
                startProfile();
                return true;
            case R.id.add_project:
                startActivity(new Intent(this, AddProject.class));
                return true;
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(googleApiClient);
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
