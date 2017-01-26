package com.linxu.mounteverest;

import android.content.Context;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;


/**
 * Created by Vanessa on 23.01.2017.
 */

public class GIFView extends View {

    private Movie mMovie;
    private long movieStart;

    public GIFView(Context context) {
        super(context);
        initializeView();
    }

    public GIFView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public GIFView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView();
    }

    private void initializeView() {
        InputStream is = getContext().getResources().openRawResource(
                R.drawable.climber_transparent);
        mMovie = Movie.decodeStream(is);
    }
}
