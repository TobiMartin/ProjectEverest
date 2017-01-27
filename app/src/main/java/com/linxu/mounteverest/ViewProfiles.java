package com.linxu.mounteverest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

/**
 * Created by lin xu on 26.01.2017.
 */

public class ViewProfiles extends AppCompatActivity{
    private static final String TAG = "ViewProfiles";
    //public static List<User> userList;

    //public static List<User> getUserList() {
    //    return userList;
    //}


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Bundle extras = getIntent().getExtras();
       // if (extras != null)
       // {
            // userList = (List<User>) extras.getSerializable("UserList");

            //for (User element : userList) {
            //    Log.d (TAG, element.getUsername().toString());
            //}
        //}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profiles);
    }
}
