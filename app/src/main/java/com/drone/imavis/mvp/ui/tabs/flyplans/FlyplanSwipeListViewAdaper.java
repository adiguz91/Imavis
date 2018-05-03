package com.drone.imavis.mvp.ui.tabs.flyplans;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.util.swipelistview.SwipeItemListViewAdapter;
import com.drone.imavis.mvp.util.swipelistview.SwipeItemOnClickListener;

import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by adigu on 29.07.2017.
 */

public class FlyplanSwipeListViewAdaper extends SwipeItemListViewAdapter<FlyPlan> {

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

        textViewFlyplanName.setText(item.getName());

        if (item.getTask() != null) {
            textViewStatus.setText(item.getTask().getStatusString());
            //TextView textViewCreationDate = (TextView) convertView.findViewById(R.id.textViewFlyplanListViewItemCreatedDate);
            badgeStatus.setText1("UNTOUCHED"); // flyplan.getTask().getStatus().name()
            badgeImageCount.setText1("IMAGES");
            badgeImageCount.setText2(String.valueOf(item.getTask().getImagesCount()));
        } else {
            badgeStatus.setText1("UNTOUCHED"); // flyplan.getTask().getStatus().name()
            badgeImageCount.setText1("NO IMAGES");
            badgeImageCount.setText2("0");
        }

        SpannableString spannableString =
                new SpannableString(TextUtils.concat(badgeStatus.toSpannable(), " ",
                        badgeImageCount.toSpannable()));

        if (textViewStatus != null) {
            textViewStatus.setText(spannableString);
        }
    }
}
