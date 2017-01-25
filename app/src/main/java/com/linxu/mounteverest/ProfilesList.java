package com.linxu.mounteverest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class ProfilesList extends ListFragment {

    private static final String TAG = "ProfilesList";
    private List<User> userList;

    ArrayList<String> userNames = new ArrayList<String>();
    ArrayList<String> userPhotos = new ArrayList<String>();
    ArrayList<String> userID = new ArrayList<String>();

    private void getUserInfo () {
        userList = ViewProfiles.getUserList();

        if (userList != null) {
            for (User element : userList) {
                if (!userID.contains(element.getId())) {
                    userNames.add(element.getUsername());
                    userPhotos.add(element.getPhoto());
                    userID.add(element.getId());
                }
            }
        } else
            Log.d(TAG, "NULL");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        getUserInfo ();
        for (String element : userID)
            Log.d(TAG, element);

        // Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i = 0; i < userID.size(); i++){
            HashMap<String, String> hm = new HashMap<String,String>();

            Uri uri =  Uri.parse(userPhotos.get(i));

            hm.put("txt", userNames.get(i));
//            hm.put("flag", String.valueOf(uri));

            aList.add(hm);
        }

        String[] from = { /*"photoURL",*/"txt"};
        int[] to = {/*R.id.flag,*/R.id.txt};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(ProfilesList.this.getActivity(), ProfileDetail.class);
        intent.putExtra("User", userList.get(position));
        startActivity(intent);
    }
}
