package com.linxu.mounteverest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by lin xu on 03.12.2016.
 */
public class ProfileDetail extends FragmentActivity{

    private static final String TAG = "ProfileDetail";



    TextView profileName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        User user;
        user = SignInActivity.currentUser;

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            user = (User) extras.getSerializable("User");
        }

        profileName = (TextView) findViewById(R.id.profile_name);
        profileName.setText(user.getUsername());

        new DownloadImageTask((ImageView) findViewById(R.id.profile_picture)).execute(user.getPhoto());
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

