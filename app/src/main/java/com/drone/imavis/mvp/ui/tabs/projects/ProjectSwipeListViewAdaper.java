package com.drone.imavis.mvp.ui.tabs.projects;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.util.swipelistview.SwipeItemListViewAdapter;
import com.drone.imavis.mvp.util.swipelistview.SwipeItemOnClickListener;

/**
 * Created by adigu on 29.07.2017.
 */

public class ProjectSwipeListViewAdaper extends SwipeItemListViewAdapter<Project> {

    public ProjectSwipeListViewAdaper(Context context, SwipeItemOnClickListener<Project> onItemClickListener) {
        super(context, onItemClickListener, R.id.projectItemSwipe, R.layout.activity_project_listview_item,
                R.id.iconTextViewProjectListViewItemEdit, R.id.iconTextViewProjectListViewItemDelete);
    }

    @Override
    public void fillValues(int position, View convertView) {
        super.fillValues(position, convertView);

        Project item = this.getItem(position);
        //ButterKnife.bind(context, convertView);
        TextView textViewProjectname = convertView.findViewById(R.id.textViewProjectListViewItemProjectname);
        TextView textViewDescription = convertView.findViewById(R.id.textViewProjectListViewItemDescription);
        textViewProjectname.setText(item.getName());
        textViewDescription.setText(item.getDescription());
    }
}
