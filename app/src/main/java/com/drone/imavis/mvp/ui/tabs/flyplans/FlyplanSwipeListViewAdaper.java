package com.drone.imavis.mvp.ui.tabs.flyplans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.ui.modelviewer.ModelViewerActivity;
import com.drone.imavis.mvp.util.swipelistview.SwipeItemListViewAdapter;
import com.drone.imavis.mvp.util.swipelistview.SwipeItemOnClickListener;

import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by adigu on 29.07.2017.
 */

public class FlyplanSwipeListViewAdaper extends SwipeItemListViewAdapter<FlyPlan> {

    private final BadgeDrawable badgeStatus = new BadgeDrawable.Builder()
            .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
            .badgeColor(0xff336699)
            .text1("UNKNOWN")
            .build();
    private final BadgeDrawable badgeImageCount =
            new BadgeDrawable.Builder()
                    .type(BadgeDrawable.TYPE_WITH_TWO_TEXT_COMPLEMENTARY)
                    .badgeColor(0xffCC9933)
                    .text1("NO IMAGES")
                    .text2("-")
                    .padding(4, 4, 4, 4, 4)
                    .strokeWidth(2)
                    .build();

    public FlyplanSwipeListViewAdaper(Context context, SwipeItemOnClickListener<FlyPlan> onItemClickListener) {
        super(context, onItemClickListener, R.id.flyplanItemSwipe, R.layout.activity_flyplan_listview_item,
                R.id.iconTextViewFlyplanListViewItemEdit, R.id.iconTextViewFlyplanListViewItemDelete);
    }

    @Override
    public void fillValues(int position, View convertView) {
        super.fillValues(position, convertView);

        FlyPlan item = this.getItem(position);
        //ButterKnife.bind(context, convertView);
        TextView textViewFlyplanName = convertView.findViewById(R.id.textViewFlyplanListViewItemFlyplanname);
        TextView textViewStatus = convertView.findViewById(R.id.textViewFlyplanListViewItemStatus);
        TextView textViewCreationDate = convertView.findViewById(R.id.textViewFlyplanListViewItemCreatedDate);
        LinearLayout layout3dModel = convertView.findViewById(R.id.flyplanListView3dModelLayout);
        //TextView textView3DLastStartDate = layout3dModel.findViewById(R.id.textViewFlyplanListViewItemFlyplanLastStartDate);
        TextView textView3DLastDuration = layout3dModel.findViewById(R.id.textViewFlyplanListViewItemFlyplanLastDuration);
        ImageView imageView3D = layout3dModel.findViewById(R.id.flyPlanListView3dImage);

        textViewFlyplanName.setText(item.getName());

        if (item.getTask() != null) {
            textViewStatus.setText(item.getTask().getStatusString());
            badgeStatus.setText1(item.getTask().getStatusString()); // flyplan.getTask().getStatus().name()
            badgeImageCount.setText1("IMAGES");
            badgeImageCount.setText2(String.valueOf(item.getTask().getImagesCount()));
            textViewCreationDate.setText(item.getTask().getCreatedAtString());

            layout3dModel.setVisibility(View.VISIBLE);
            //textView3DLastStartDate.setText();
            textView3DLastDuration.setText(item.getTask().getProcessingTimeString());
            imageView3D.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("PROJECT_ID", String.valueOf(item.getTask().getProject()));
                    bundle.putString("TASK_ID", item.getTask().getId());
                    goToActivity(convertView.getContext(), ModelViewerActivity.class, bundle);
                }
            });
        } else {
            layout3dModel.setVisibility(View.GONE);
            badgeStatus.setText1("UNTOUCHED"); // flyplan.getTask().getStatus().name()
            badgeImageCount.setText1("NO IMAGES");
            badgeImageCount.setText2("-");
        }

        SpannableString spannableString =
                new SpannableString(TextUtils.concat(badgeStatus.toSpannable(), " ",
                        badgeImageCount.toSpannable()));

        if (textViewStatus != null) {
            textViewStatus.setText(spannableString);
        }
    }

    public void goToActivity(Context activity, Class nextActivity, Bundle bundleData) {
        Intent intent = new Intent(activity, nextActivity);
        intent.putExtras(bundleData); //Put your data to your next Intent
        activity.startActivity(intent);
    }
}
