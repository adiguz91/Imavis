package com.drone.imavis.services.flyplan.mvc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.drone.imavis.activities.MainActivity;
import com.drone.imavis.activities.MainFlyplanner;
import com.drone.imavis.constants.classes.CFlyPlan;
import com.drone.imavis.constants.classes.CMap;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.services.flyplan.mvc.view.listener.GestureListener;
import com.drone.imavis.services.flyplan.mvc.view.listener.ScaleListener;
import com.google.android.gms.maps.GoogleMap;

public class FlyPlanView extends View {

    private GestureDetector gestureDetector;
    private Rect viewRect;

    private static final String TAG = "FlyPlanView";
    private static ScaleGestureDetector scaleDetector;
    private static SparseArray<Node> nodes;
    public static SparseArray<Node> getNodes() {
        return nodes;
    }

    private static boolean isHandledTouch;
    public static boolean getIsHandledTouch() {
        return isHandledTouch;
    }

    public FlyPlanView(final Context context) {
        super(context);
        init(context);
    }
    public FlyPlanView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public FlyPlanView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(final Context context) {
        nodes = new SparseArray<Node>(CFlyPlan.MAX_WAYPOINTS_SIZE + CFlyPlan.MAX_POI_SIZE);
        gestureDetector = new GestureDetector(context, new GestureListener());
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        // FlyPlanController.getInstance().init(context);
    }

    public static float getScaleFactor() {
        if(scaleDetector != null)
            return ScaleListener.getScaleFactor();
        return CMap.SCALE_FACTOR_DEFAULT;
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        // mainActivity.Zoom(mScaleFactor); // GoogleMap
        canvas.scale(getScaleFactor(), getScaleFactor());
        FlyPlanController.getInstance().draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        Log.w(TAG, "onTouchEvent: " + event);
        int actionIndex; // event.getActionIndex()
        isHandledTouch = false;

        // onTouch trigger events
        isHandledTouch = scaleDetector.onTouchEvent(event);
        isHandledTouch = gestureDetector.onTouchEvent(event);

        switch (event.getActionMasked())
        {
            // find Node or Line
            case MotionEvent.ACTION_MOVE:
                isHandledTouch = actionMove(event);
                invalidate();
                break;

            // do nothing
            default:
                break;
        }

        if(isHandledTouch)
            return super.onTouchEvent(event);
        else
            return false;
        //return super.dispatchTouchEvent(event);
        //return super.onTouchEvent(event);
        //return true;
    }

    public static boolean actionMove(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        boolean isHandled = false;
        for (int actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
            //pointerId = event.getPointerId(actionIndex);
            Coordinate coordinateTouched = new Coordinate(event.getX(actionIndex), event.getY(actionIndex));
            Node touchedNode = FlyPlanController.getTouchedNode();
            if (touchedNode != null) {
                touchedNode.getShape().setCoordinate(coordinateTouched);
                isHandled = true;
            } else {
                // drag map
                isHandled = false;
                break;
            }
            MainFlyplanner.removeActionButtons();
        }
        return isHandled;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }
}
