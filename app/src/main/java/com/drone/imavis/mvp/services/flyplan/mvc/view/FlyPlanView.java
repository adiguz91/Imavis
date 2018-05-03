package com.drone.imavis.mvp.services.flyplan.mvc.view;

import android.app.Activity;
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

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.model.FlyPlan;
import com.drone.imavis.mvp.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.mvp.services.flyplan.mvc.view.listener.GestureListener;
import com.drone.imavis.mvp.services.flyplan.mvc.view.listener.ScaleListener;
import com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity;
import com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.FlyplannerFragment;
import com.drone.imavis.mvp.util.constants.classes.CFlyPlan;
import com.drone.imavis.mvp.util.constants.classes.CMap;

import javax.inject.Inject;

public class FlyPlanView extends View {

    private static final String TAG = "FlyPlanView";
    private static Coordinate globalMoveCoordinate;
    private static ScaleGestureDetector scaleDetector;
    private static SparseArray<Node> nodes;
    private static boolean isHandledTouch;
    private static boolean isLocked = false;
    @Inject
    ScaleListener scaleListener;
    // draw outside
    Rect newRect = new Rect();
    private GestureDetector gestureDetector;
    private Rect viewRect;
    private boolean isLoading = true;
    private FlyplannerFragment flyplannerFragment;
    private SheetFab actionSheetMenu;
    private Coordinate dragCoordinate;

    public FlyPlanView(final Context context) {
        super(context);
    }

    public FlyPlanView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public FlyPlanView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    public static SparseArray<Node> getNodes() {
        return nodes;
    }

    public static boolean getIsHandledTouch() {
        return isHandledTouch;
    }

    public static float getScaleFactor() {
        if (scaleDetector != null)
            return scaleDetector.getScaleFactor();
        return CMap.SCALE_FACTOR_DEFAULT;
    }

    public static Coordinate getGlobalMoveCoordinate() {
        return globalMoveCoordinate;
    }

    public static void setGlobalMoveCoordinate(Coordinate coordinate) {
        globalMoveCoordinate = coordinate;
    }

    public static boolean isLocked() {
        return isLocked;
    }

    public static void setIsLocked(boolean isLocked) {
        FlyPlanView.isLocked = isLocked;
    }

    public static boolean actionUp(MotionEvent event) {

        int pointerCount = event.getPointerCount();
        boolean isHandled = true;
        for (int actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
            //pointerId = event.getPointerId(actionIndex);
            Coordinate coordinateTouched = new Coordinate(event.getX(actionIndex), event.getY(actionIndex));
            Node touchedNode = FlyPlanController.getTouchedNode();
            if (touchedNode != null) {
                touchedNode.getShape().setCoordinate(coordinateTouched);
                FlyPlanController.getInstance().getFlyPlan().getPoints().editNode(touchedNode);
                break;
            } else {
                // drag map
                isHandled &= false;
                break;
            }
        }
        return isHandled;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ((FlyplannerActivity) getContext()).activityComponent().inject(this);

        nodes = new SparseArray<Node>(CFlyPlan.MAX_WAYPOINTS_SIZE + CFlyPlan.MAX_POI_SIZE);
        gestureDetector = new GestureDetector(getContext(), new GestureListener(FlyPlanView.this));
        scaleDetector = new ScaleGestureDetector(getContext(), scaleListener);
    }

    public SheetFab getActionSheetMenu() {
        if (actionSheetMenu == null)
            actionSheetMenu = ((Activity) getContext()).findViewById(R.id.fabSheet);
        return actionSheetMenu;
    }

    public void setFlyPlan(FlyPlan flyPlan) {
        FlyPlanController.getInstance().setFlyPlan(flyPlan);
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        // draw outside
        /*canvas.getClipBounds(newRect);
        newRect.inset(-20, -20);  //make the rect larger
        canvas.clipRect(newRect, Region.Op.REPLACE);*/

        canvas.save();
        // mainActivity.Zoom(mScaleFactor); // GoogleMap
        canvas.scale(getScaleFactor(), getScaleFactor());
        if (dragCoordinate != null)
            canvas.translate(dragCoordinate.getX(), dragCoordinate.getY());
        FlyPlanController.getInstance().draw(canvas);
        canvas.restore();
    }

    public boolean isIsLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean doOnTouch(MotionEvent event) {
        if (isIsLoading())
            return false;

        if (isLocked)
            return super.onTouchEvent(event);

        Log.w(TAG, "onTouchEvent: " + event);
        //int actionIndex; // event.getActionIndex()
        isHandledTouch = false;

        // onTouch trigger events
        isHandledTouch = scaleDetector.onTouchEvent(event);
        isHandledTouch = gestureDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                isHandledTouch = actionMove(event);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (!isHandledTouch) {
                    isHandledTouch = actionUp(event);
                }
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

    public boolean actionMove(MotionEvent event) {
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
                if (globalMoveCoordinate != null) {
                    dragCoordinate = new Coordinate(globalMoveCoordinate.getX() - coordinateTouched.getX(),
                            globalMoveCoordinate.getY() - coordinateTouched.getY());
                    FlyPlanController.getInstance().getFlyPlan().setDragCoordinate(dragCoordinate);
                }


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
