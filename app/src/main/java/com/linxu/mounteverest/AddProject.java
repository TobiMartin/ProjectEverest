package com.linxu.mounteverest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by lin xu on 15.12.2016.
 */
public class AddProject extends AppCompatActivity {
    private CustomSliderView customSliderView;

    private TextView startTextView;
    private TextView endTextView;

    private List<TextView> learingSteps;

    private boolean startDateSet = false;
    private boolean endDateSet = false;

    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);
        customSliderView = (CustomSliderView)findViewById(R.id.slider);
        startTextView = (TextView)findViewById(R.id.startDate);
        endTextView = (TextView)findViewById(R.id.endDate);
        customSliderView.register(this);
    }

    public void changeStartDate(String date) {
        startTextView.setText(date);
        startDateSet = true;
    }

    public void changeEndDate(String date){
        endTextView.setText(date);
        endDateSet = true;
    }



    public boolean isStartDateSet(){

        return startDateSet;
    }
    public boolean isEndDateSet(){

        return endDateSet;
    }
}
