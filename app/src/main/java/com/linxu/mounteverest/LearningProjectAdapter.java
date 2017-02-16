package com.linxu.mounteverest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lin xu on 16.02.2017.
 */

public class LearningProjectAdapter extends ArrayAdapter<LearningProject>{

    private final Context context;
    private final List<LearningProject> learningProjectList;

    public LearningProjectAdapter(Context context, List<LearningProject> learningProjectList){
        super(context, -1, learningProjectList);
        this.context = context;
        this.learningProjectList = learningProjectList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.project_list_item, parent, false);
        TextView nameView = (TextView)rowView.findViewById(R.id.name_view);
        nameView.setText(learningProjectList.get(position).getName());
        return rowView;
    }
}
