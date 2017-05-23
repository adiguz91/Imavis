package com.drone.imavis.mvp.ui.tabs.flyplans;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
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

import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by adigu on 08.05.2017.
 */

public class FlyplanListViewAdapter extends BaseSwipeAdapter {

    //@Inject Context mContext;
    private Context context;
    private List<Flyplan> flyplanList;



    final BadgeDrawable badgeStatus = new BadgeDrawable.Builder()
            .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
            .badgeColor(0xff336699)
            .text1("UNKNOWN")
            .build();

    final BadgeDrawable badgeImageCount =
            new BadgeDrawable.Builder()
                    .type(BadgeDrawable.TYPE_WITH_TWO_TEXT_COMPLEMENTARY)
                    .badgeColor(0xffCC9933)
                    .text1("IMAGES")
                    .text2("593")
                    .padding(4, 4, 4, 4, 4)
                    .strokeWidth(2)
                    .build();

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

        TextView textViewStatus = (TextView) convertView.findViewById(R.id.textViewFlyplanListViewItemStatus);
        //TextView textViewCreationDate = (TextView) convertView.findViewById(R.id.textViewFlyplanListViewItemCreatedDate);
        TextView textViewFlyplanname = (TextView)convertView.findViewById(R.id.textViewFlyplanListViewItemFlyplanname);

        textViewFlyplanname.setText(flyplan.getName());

        badgeStatus.setText1("COMPLETED"); // flyplan.getTask().getStatus().name()
        badgeImageCount.setText1("IMAGES");
        badgeImageCount.setText2(String.valueOf(flyplan.getTask().getImagesCount()));
        SpannableString spannableString =
                new SpannableString(TextUtils.concat( badgeStatus.toSpannable(), " ",
                                    badgeImageCount.toSpannable() ));

        if (textViewStatus != null) {
            textViewStatus.setText(spannableString);
        }
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