package com.drone.imavis.mvp.ui.flyplans;

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
import com.drone.imavis.mvp.data.model.Flyplan;
import com.drone.imavis.mvp.data.model.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adigu on 08.05.2017.
 */

public class FlyplanListViewAdapter extends BaseSwipeAdapter {

    //@Inject Context mContext;
    private Context context;
    private List<Flyplan> flyplanList;

    //@Inject
    public FlyplanListViewAdapter(Context context) {
        this.context = context;
        this.flyplanList = new ArrayList<>();
    }

    public void setFlyplans(List<Flyplan> flyplanList) {
        this.flyplanList = flyplanList;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.flyplanItemSwipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_flyplan_listview_item, null);
        SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.iconTextViewFlyplanListViewItemDelete));
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.iconTextViewFlyplanListViewItemDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "click delete", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
        Flyplan flyplan = flyplanList.get(position);
        //ButterKnife.bind(context, convertView);
        TextView textViewFlyplanname = (TextView)convertView.findViewById(R.id.textViewFlyplanListViewItemFlyplanname);
        //TextView textViewDescription = (TextView)convertView.findViewById(R.id.textViewFlyplanListViewItemDescription);
        textViewFlyplanname.setText(flyplan.getName());
        //textViewDescription.setText(project.getDescription());
    }

    @Override
    public int getCount() {
        return flyplanList.size();
    }

    @Override
    public Flyplan getItem(int position) {
        return flyplanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}