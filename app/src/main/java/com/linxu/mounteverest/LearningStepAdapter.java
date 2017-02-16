package com.linxu.mounteverest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lin xu on 20.01.2017.
 */

public class LearningStepAdapter extends ArrayAdapter<LearningStep>{

    private final Context context;
    private final List<LearningStep> learningStepList;

    public LearningStepAdapter(Context context, List<LearningStep> learningStepList){
        super(context, -1, learningStepList);
        this.context = context;
        this.learningStepList = learningStepList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.list_items, parent, false);
        TextView dateView = (TextView)rowView.findViewById(R.id.date_view);
        TextView titleView = (TextView)rowView.findViewById(R.id.title_view);
        TextView noteView = (TextView)rowView.findViewById(R.id.note_view);
        dateView.setText("date: " + learningStepList.get(position).getDate());
        titleView.setText("title: " + learningStepList.get(position).getTitle());
        noteView.setText("note: " + learningStepList.get(position).getNote());

        return rowView;
    }
}
