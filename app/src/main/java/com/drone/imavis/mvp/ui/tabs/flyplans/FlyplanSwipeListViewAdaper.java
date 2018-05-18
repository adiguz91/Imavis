package com.drone.imavis.mvp.ui.tabs.flyplans;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.data.model.TaskStatus;
import com.drone.imavis.mvp.ui.modelviewer.ModelViewerActivity;
import com.drone.imavis.mvp.util.swipelistview.SwipeItemListViewAdapter;
import com.drone.imavis.mvp.util.swipelistview.SwipeItemOnClickListener;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by adigu on 29.07.2017.
 */

public class FlyplanSwipeListViewAdaper extends SwipeItemListViewAdapter<FlyPlan> {

    private Context context;

    public FlyplanSwipeListViewAdaper(Context context, SwipeItemOnClickListener<FlyPlan> onItemClickListener) {
        super(context, onItemClickListener, R.id.flyplanItemSwipe, R.layout.activity_flyplan_listview_item,
                R.id.iconTextViewFlyplanListViewItemEdit, R.id.iconTextViewFlyplanListViewItemDelete);
        this.context = context;
    }

    @Override
    public void fillValues(int position, View convertView) {
        super.fillValues(position, convertView);
        //ButterKnife.bind(this, convertView); //ButterKnife.bind(context, convertView);

        BadgeDrawable badgeCreatedAt = new BadgeDrawable.Builder()
                .type(BadgeDrawable.TYPE_WITH_TWO_TEXT_COMPLEMENTARY)
                .badgeColor(R.color.colorPrimary) //0xffCC9933
                .text1("CREATED")
                .text2("")
                .strokeWidth(2)
                .build();

        BadgeDrawable badgeStatus = new BadgeDrawable.Builder()
                .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                .badgeColor(R.color.colorPrimary)
                .text1("UNKNOWN")
                .build();

        BadgeDrawable badgeDuration = new BadgeDrawable.Builder()
                .type(BadgeDrawable.TYPE_WITH_TWO_TEXT_COMPLEMENTARY)
                .badgeColor(R.color.colorPrimary)
                .text1("3D DURATION")
                .text2("")
                .strokeWidth(2)
                .build();

        BadgeDrawable badgeImageCount = new BadgeDrawable.Builder()
                .type(BadgeDrawable.TYPE_WITH_TWO_TEXT_COMPLEMENTARY)
                .badgeColor(R.color.colorPrimary)
                .text1("NO IMAGES")
                .text2("-")
                .strokeWidth(2)
                .build();

        FlyPlan item = this.getItem(position);
        TextView textViewFlyplanName = convertView.findViewById(R.id.textViewFlyplanListViewItemFlyplanname);
        TextView textViewStatus = convertView.findViewById(R.id.textViewFlyplanListViewItemStatus);
        TextView textViewCreationDate = convertView.findViewById(R.id.textViewFlyplanListViewItemCreatedDate);
        //TextView textView3DLastStartDate = layout3dModel.findViewById(R.id.textViewFlyplanListViewItemFlyplanLastStartDate);
        TextView textView3DLastDuration = convertView.findViewById(R.id.textViewFlyplanListViewItemFlyplanLastDuration);
        FloatingActionButton imageView3D = convertView.findViewById(R.id.flyPlanListView3dImage);
        textViewFlyplanName.setText(item.getName());

        SpannableString spannableString = new SpannableString("");

        if (item.getTask() != null) {
            //textViewStatus.setText(item.getTask().getStatusString());
            badgeStatus.setText1(item.getTask().getStatusString()); // flyplan.getTask().getStatus().name()
            // https://stackoverflow.com/questions/31590714/getcolorint-id-deprecated-on-android-6-0-marshmallow-api-23
            badgeStatus.setBadgeColor(ContextCompat.getColor(context, item.getTask().getTaskStatus().getRessourceColor()));
            badgeImageCount.setText1("IMAGES");
            badgeImageCount.setText2(String.valueOf(item.getTask().getImagesCount()));
            badgeCreatedAt.setText2(item.getTask().getCreatedAtString());

            if (item.getTask().getTaskStatus() != TaskStatus.UNTOUCHED) {
                imageView3D.setVisibility(View.VISIBLE);

                spannableString = new SpannableString(TextUtils.concat(
                        badgeImageCount.toSpannable(), " ",
                        badgeStatus.toSpannable()));

                //textView3DLastStartDate.setText();
                badgeDuration.setText2(item.getTask().getProcessingTimeString());
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
                textView3DLastDuration.setVisibility(View.GONE);
                imageView3D.setVisibility(View.GONE);
                textViewCreationDate.setVisibility(View.GONE);
                //badgeStatus.setText1("UNTOUCHED"); // flyplan.getTask().getStatus().name()
                badgeImageCount.setText1("NO IMAGES");
                badgeImageCount.setText2(" - ");

                spannableString = new SpannableString(badgeStatus.toSpannable());
            }
        }

        if (textViewStatus != null) {
            textViewStatus.setText(spannableString);
        }

        SpannableString spannableStringCratedAt = new SpannableString(badgeCreatedAt.toSpannable());
        if (textViewCreationDate != null) {
            textViewCreationDate.setText(spannableStringCratedAt);
        }

        //SpannableString spannableStringDurationWithImage = toSpannable(badgeDuration);
        if (textViewCreationDate != null) {
            textView3DLastDuration.setText(badgeDuration.toSpannable());
        }
    }

    private SpannableString toSpannable(BadgeDrawable badgeDrawable) {
        SpannableString spanStr = new SpannableString("  ");
        Drawable icon = new IconDrawable(context, FontAwesomeIcons.fa_clock_o).colorRes(R.color.icons).actionBarSize();
        icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        ImageSpan spanImage = new ImageSpan(icon, 0);
        spanStr.setSpan(spanImage, 0, 1, 33); // Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        spanStr.setSpan(new ImageSpan(badgeDrawable, 0), 1, 2, 33);
        badgeDrawable.setBounds(0, 0, badgeDrawable.getIntrinsicWidth(), badgeDrawable.getIntrinsicHeight());
        //SpannableString custom = new SpannableString(TextUtils.concat(spannableStringDurationWithImage, badgeDuration.toSpannable()));
        return spanStr;
    }

    public void goToActivity(Context activity, Class nextActivity, Bundle bundleData) {
        Intent intent = new Intent(activity, nextActivity);
        intent.putExtras(bundleData); //Put your data to your next Intent
        activity.startActivity(intent);
    }
}
