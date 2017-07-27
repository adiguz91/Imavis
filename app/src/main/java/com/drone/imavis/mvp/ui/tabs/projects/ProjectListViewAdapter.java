package com.drone.imavis.mvp.ui.tabs.projects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.Project;
import com.drone.imavis.mvp.data.model.Projects;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adigu on 08.05.2017.
 */

public class ProjectListViewAdapter extends BaseSwipeAdapter {

    //@Inject Context mContext;
    private Context context;
    private List<Project> projectList;

    //@BindView(R.id.textViewProjectListViewItemProjectname) TextView textViewProjectname;
    //@BindView(R.id.textViewProjectListViewItemDescription) TextView textViewDescription;

    //@Inject
    public ProjectListViewAdapter(Context context) {
        this.context = context;
        this.projectList = new ArrayList<>();
    }

    public void setProjects(List<Project> projectList) {
        this.projectList = projectList;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.projectItemSwipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_project_listview_item, null);
        SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.iconTextViewProjectListViewItemDelete));
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.iconTextViewProjectListViewItemDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "click delete", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.iconTextViewProjectListViewItemEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "click edit", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
        Project project = projectList.get(position);
        //ButterKnife.bind(context, convertView);
        TextView textViewProjectname = (TextView)convertView.findViewById(R.id.textViewProjectListViewItemProjectname);
        TextView textViewDescription = (TextView)convertView.findViewById(R.id.textViewProjectListViewItemDescription);
        textViewProjectname.setText(project.getName());
        textViewDescription.setText(project.getDescription());
    }

    @Override
    public int getCount() {
        return projectList.size();
    }

    @Override
    public Project getItem(int position) {
        return projectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}