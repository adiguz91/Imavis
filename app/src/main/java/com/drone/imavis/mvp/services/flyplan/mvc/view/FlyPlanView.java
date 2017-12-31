package com.drone.imavis.mvp.services.flyplan.mvc.view;

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

import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.mvp.services.flyplan.mvc.view.listener.GestureListener;
import com.drone.imavis.mvp.services.flyplan.mvc.view.listener.ScaleListener;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.FlyplannerFragment;
import com.drone.imavis.mvp.util.constants.classes.CFlyPlan;
import com.drone.imavis.mvp.util.constants.classes.CMap;

public class FlyPlanView extends View {

    //@Inject FlyplanController flyplanController;
    private ScaleListener scaleListener;

    private GestureDetector gestureDetector;
    private Rect viewRect;

    private FlyplannerFragment flyplannerFragment;

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
        //flyplannerFragment = (FlyplannerFragment) getParent();
        nodes = new SparseArray<Node>(CFlyPlan.MAX_WAYPOINTS_SIZE + CFlyPlan.MAX_POI_SIZE);
        gestureDetector = new GestureDetector(context, new GestureListener());
        scaleDetector = new ScaleGestureDetector(context, scaleListener);
    }

    public static float getScaleFactor() {
        if(scaleDetector != null)
            return scaleDetector.getScaleFactor();
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

    private static boolean isLocked = false;
    public static boolean isLocked() {
        return isLocked;
    }
    public static void setIsLocked(boolean isLocked) {
        FlyPlanView.isLocked = isLocked;
    }

    public boolean doOnTouch(MotionEvent event) {
        if(isLocked)
            return super.onTouchEvent(event);
        Log.w(TAG, "onTouchEvent: " + event);
        //int actionIndex; // event.getActionIndex()
        isHandledTouch = false;

        // onTouch trigger events
        isHandledTouch = scaleDetector.onTouchEvent(event);

        isHandledTouch = gestureDetector.onTouchEvent(event);

        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_MOVE:
                isHandledTouch = actionMove(event);
                invalidate();
                break;
            default:
                break;
        }

        //if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
        //    isHandledTouch = true;
        Log.i("LogFlyplan", "isHandled: " + isHandledTouch + " | event: " + event.getActionMasked());
        return isHandledTouch;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        return doOnTouch(event);
    }

    public static boolean actionMove(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        boolean isHandled = true;
        for (int actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
            //pointerId = event.getPointerId(actionIndex);
            Coordinate coordinateTouched = new Coordinate(event.getX(actionIndex), event.getY(actionIndex));
            Node touchedNode = FlyPlanController.getTouchedNode();
            if (touchedNode != null) {
                touchedNode.getShape().setCoordinate(coordinateTouched);
                //isHandled = true;
                break;
            } else {
                // drag map
                isHandled &= false;
                break;
            }
        }
        //MainFlyplanner.removeActionButtons(); #### !!!! IMPORTANT UUUUSEEEEEE, callback!?=!=
        return isHandled;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }
}
