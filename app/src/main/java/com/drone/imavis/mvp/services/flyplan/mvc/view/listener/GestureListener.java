package com.drone.imavis.mvp.services.flyplan.mvc.view.listener;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.poi.PointOfInterest;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity;

/**
 * Created by adigu on 23.02.2017.
 */

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    private Coordinate touchCoordinate;
    private Node touchedNode;
    private int pointerId;
    private FlyPlanView parentView;
    private LinearLayout.LayoutParams fabSheetCardViewLayout;
    private Context context;

    public GestureListener(FlyPlanView parentView) {
        this.parentView = parentView;
        this.context = parentView.getContext();
    }

    @Override
    public boolean onDown(MotionEvent event) {
        pointerId = event.getPointerId(0);
        touchCoordinate = new Coordinate(event.getX(), event.getY());
        touchedNode = parentView.getController().getTouchedNode(touchCoordinate);

        LinearLayout nodeHeightLayout = ((FlyplannerActivity) parentView.getContext()).findViewById(R.id.nodeHeightLayout);
        if (touchedNode == null) {
            parentView.setGlobalMoveCoordinate(touchCoordinate);
            nodeHeightLayout.setVisibility(View.INVISIBLE);
        } else
            nodeHeightLayout.setVisibility(View.VISIBLE);

        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d("TOUCH", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        pointerId = event.getPointerId(0);
        touchCoordinate = new Coordinate(event.getX(), event.getY());
        boolean isObtained = parentView.getController().obtainTouchedNode(Waypoint.class, touchCoordinate, touchedNode);
        return touchedNode != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onLongPress(MotionEvent event) {
        //super.onLongPress(event);

        if (parentView.scalingIsInProgress())
            return;

        pointerId = event.getPointerId(0);
        touchCoordinate = new Coordinate(event.getX(), event.getY());

        Coordinate textLineRectCoor = parentView.getController().isTouchedTextRect(touchCoordinate);

        if (textLineRectCoor != null) {
            // ##################################################
            // MainFlyplanner.addActionButtons(textLineRectCoor);
            // ##################################################
        } else {
            boolean isObtained = parentView.getController().obtainTouchedNode(PointOfInterest.class, touchCoordinate, touchedNode);
            if (touchedNode != null) {
                if (!isObtained) {
                    // ACTION BUTTONs open

                    // correct coordinates with drag vector
                    Coordinate centeredCoordinate = new Coordinate(touchedNode.getShape().getCoordinate().getX() + parentView.getDragCoordinate().getX(),
                            touchedNode.getShape().getCoordinate().getY() + parentView.getDragCoordinate().getY());

                    CardView fabSheedCardViewWaypoint = parentView.getFlyplannerFragment().getFlightPlannerNodeActionMenu().getActionFabSheetCardViewWaypoint();
                    fabSheetCardViewLayout = (LinearLayout.LayoutParams) fabSheedCardViewWaypoint.getLayoutParams();

                    parentView.getFlyplannerFragment().getFlightPlannerNodeActionMenu().getActionFabSheetWaypoint().setVisibility(View.VISIBLE);
                    parentView.getFlyplannerFragment().getFlightPlannerNodeActionMenu().getActionFabSheetMenuWaypoint().showFab(centeredCoordinate.getX(), centeredCoordinate.getY());

                    TextView closeTextView = fabSheedCardViewWaypoint.findViewById(R.id.fabSheetItemCloseTextWaypoint);
                    if (((FlyplannerActivity) parentView.getContext()).getFlyplan().getIsClosed())
                        closeTextView.setText("Open");
                    else
                        closeTextView.setText("Close");

                    LinearLayout fabSheetItemClose = fabSheedCardViewWaypoint.findViewById(R.id.fabSheetItemCloseWaypoint);
                    if (touchedNode.getClass().equals(Waypoint.class))
                        fabSheetItemClose.setVisibility(View.VISIBLE);
                    else
                        fabSheetItemClose.setVisibility(View.INVISIBLE);

                    LinearLayout.LayoutParams layoutParams = fabSheetCardViewLayout;
                    Point layoutSize = null;
                    if (touchedNode.getClass().equals(Waypoint.class))
                        layoutSize = new Point(getPixelsFromDPs(100), getPixelsFromDPs(76));
                    else
                        layoutSize = new Point(getPixelsFromDPs(100), getPixelsFromDPs(38));
                    centeredCoordinate = correctBoundsSheetCoordinates(layoutSize, centeredCoordinate);

                    //layoutParams = new LinearLayout.LayoutParams(layoutSize.x, layoutSize.y);
                    layoutParams.setMargins((int) centeredCoordinate.getX(), (int) centeredCoordinate.getY(), 0, 0);
                    fabSheedCardViewWaypoint.setLayoutParams(layoutParams);
                    //fabSheedCardViewWaypoint.invalidate();
                    //fabSheedCardViewWaypoint.requestLayout();

                    parentView.getFlyplannerFragment().getFlightPlannerNodeActionMenu().getActionFabSheetMenuWaypoint().showSheet();
                    parentView.setSelectedActionMenuNode(touchedNode);
                    parentView.setIsEnabledActionMenu(true);
                }
            }
        }
    }

    // Method for converting DP/DIP value to pixels
    // https://android--code.blogspot.co.at/2015/09/android-how-to-convert-dp-to-pixels.html
    // or https://stackoverflow.com/questions/4605527/converting-pixels-to-dp
    public int getPixelsFromDPs(int dps) {
        int px = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, context.getResources().getDisplayMetrics()));
        return px;
        //float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
    }

    private Point getCorrectedScreenDimension() {
        Display display = ((FlyplannerActivity) parentView.getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        size.set(size.x, size.y - parentView.getStatusBarHeight()); // size.x == width
        return size;
    }

    private Coordinate correctBoundsSheetCoordinates(Point layoutSize, Coordinate coordinate) {
        Point screenDimension = getCorrectedScreenDimension();
        if ((coordinate.getX() + layoutSize.x) >= screenDimension.x) // correction x
            coordinate.setCoordinate(coordinate.getX() - layoutSize.x, coordinate.getY());
        if ((coordinate.getY() + layoutSize.y) >= screenDimension.y) // correction y
            coordinate.setCoordinate(coordinate.getX(), coordinate.getY() - layoutSize.y);
        return coordinate;
    }


}