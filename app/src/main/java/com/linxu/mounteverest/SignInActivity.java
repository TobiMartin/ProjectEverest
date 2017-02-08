package com.linxu.mounteverest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lin xu on 25.01.2017.
 */
public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private static FirebaseDatabase mFirebaseDatabase;
    private static DatabaseReference mUserDatabaseReference;

    public static FirebaseDatabase getmFirebaseDatabase() {
        return mFirebaseDatabase;
    }
    public static DatabaseReference getmUserDatabaseReference() {
        return mUserDatabaseReference;
    }



    public static List<User> userList;
    public static User currentUser;
    public static List<List<LearningStep>> learningStepsPerUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);



        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);



        userList = new ArrayList<User>();
        learningStepsPerUser = new ArrayList<List<LearningStep>>();



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDatabaseReference = mFirebaseDatabase.getReference();



        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        Log.d("signInOptions", "" + gso);



        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        new GetData("https://mounteverest-13ffd.firebaseio.com/User/.json").execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("result: ", "" + result.getStatus());
            if(result.isSuccess()){
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else{
                Log.e(TAG, "Google Sign in failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle: " + acct.getId());



        User user = new User(acct.getId(), acct.getDisplayName(), acct.getEmail(), String.valueOf(acct.getPhotoUrl()));
        currentUser = user;



        mUserDatabaseReference.child("User").child(user.getId()).setValue(user);



        addUserToList(user);



        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential: onComplete: " + task.isSuccessful());
                //if sign in fails, display a message to the user. If sign in succeeds
                //the auth state listener will be notified and logic to handle the
                //signed in user can be handled in the listener.
                if(!task.isSuccessful()){
                    Log.w(TAG, "signInWithCredential", task.getException());
                    Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void addUserToList(User user) {
        boolean doit = true;
        for(User element: userList){
            if(element.getId().equals(user.getId())){
                doit = false;
            }
        }
        if(doit){
            userList.add(user);
        }
    }

//    public void fetchCurrentUser() {
//        if (mFirebaseAuth.getCurrentUser() != null) {
//            Log.d("CURRENT", "" + mFirebaseAuth.getCurrentUser());
//        }
//        else {
//            Log.d("CURRENT", "NULL");
//        }
//    }

    public class GetData extends AsyncTask<Object, Object, String> {

        HttpURLConnection urlConnection;
        String jsonString = "";
        String url_ = "";

        public GetData(String urlString) {
            super();
            url_ = urlString;
        }

        @Override
        protected String doInBackground(Object... args) {

            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(url_);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                in.close();
                jsonString = result.toString();


            }catch( Exception e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }

            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            try {

                JSONObject obj = new JSONObject(jsonString);

                for (int i = 0; i < obj.names().length(); i++) {
                    Log.d("IDS1", "" + obj.names().get(i));

                    JSONObject userObj = obj.getJSONObject((String) obj.names().get(i));
                    Log.d("IDS2", "" + userObj);

                    User user = new User((String)userObj.get("id"), (String)userObj.get("username"),
                            (String)userObj.get("email"), (String)userObj.get("photo"));
                    userList.add(user);



                    JSONObject learningStep = new JSONObject();
                    JSONObject learningSteps = new JSONObject();
                    if (userObj.has("learning steps")) {
                        learningStep = userObj.getJSONObject("learning steps");
                    }
                    else {
                        learningStepsPerUser.add(null);
                    }
                    if (learningStep.has("learning_step")) {
                        learningSteps = learningStep.getJSONObject("learning_step");

                        List<LearningStep> steps = new ArrayList<LearningStep>();
                        for (int j = 0; j < learningSteps.names().length(); j++) {
                            JSONObject singleStep = learningSteps.getJSONObject((String) learningSteps.names().get(j));
                            LearningStep step = new LearningStep((String)singleStep.get("date"), (String)singleStep.get("title"),
                                    (String)singleStep.get("note"));
                            steps.add(step);
                        }
                        Log.d("STEPS! ARRAY", "" + steps.toString());
                        learningStepsPerUser.add(steps);
                        Log.d("STEPS! ARRAY", "" + learningStepsPerUser.toString());
                        Log.d("STEPS! ARRAY", "" + learningStepsPerUser.size());
                    }


                    //TODO
                    // currentUser abfragen

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
