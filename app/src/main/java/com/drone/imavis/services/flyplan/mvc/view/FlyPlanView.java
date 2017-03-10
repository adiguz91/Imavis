package com.drone.imavis.services.flyplan.mvc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drone.imavis.R;
import com.drone.imavis.activities.MainActivity;
import com.drone.imavis.constants.classes.CFlyPlan;
import com.drone.imavis.constants.classes.CMap;
import com.drone.imavis.extensions.flyplan.math.FlyPlanMath;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.services.flyplan.mvc.model.extensions.dimension.Size;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.services.flyplan.mvc.view.listener.GestureListener;
import com.drone.imavis.services.flyplan.mvc.view.listener.ScaleListener;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class FlyPlanView extends View {

    private GestureDetector gestureDetector;
    private Coordinate touchCoordinate;
    private Rect viewRect;

    private static final String TAG = "FlyPlanView";
    private static ScaleGestureDetector scaleDetector;
    private static SparseArray<Node> nodes;
    public static SparseArray<Node> getNodes() {
        return nodes;
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
        boolean handled = false;
        Node touchedNode;
        int actionIndex; // event.getActionIndex()
        int pointerId;

        // onTouch trigger events
        handled = scaleDetector.onTouchEvent(event);
        handled = gestureDetector.onTouchEvent(event);

        // get touch event coordinates and make transparent node from it
        switch (event.getActionMasked()) {
            /*
            case MotionEvent.ACTION_DOWN:
                clearCirclePointer();
                pointerId = event.getPointerId(0);
                touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
                touchedNode = (Waypoint) FlyPlanController.getInstance().obtainTouchedNode(touchCoordinate);
                nodes.put(pointerId, touchedNode);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                clearCirclePointer();
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                Log.w(TAG, "Pointer down");
                // It secondary pointers, so obtain their ids and check circles
                pointerId = event.getPointerId(actionIndex);
                touchCoordinate = new Coordinate(event.getX(actionIndex), event.getY(actionIndex));
                touchedNode = (Waypoint) FlyPlanController.getInstance().obtainTouchedNode(touchCoordinate);
                nodes.put(pointerId, touchedNode);
                touchedNode.getShape().setCoordinate(touchCoordinate);
                invalidate();
                handled = true;
                break;
*/
            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();

                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    pointerId = event.getPointerId(actionIndex);
                    Coordinate coordinateTouched = new Coordinate(event.getX(actionIndex), event.getY(actionIndex));
                    //touchedNode = getNodes().get(pointerId);
                    touchedNode = FlyPlanController.getTouchedNode();
                    if (touchedNode != null) {
                        touchedNode.getShape().setCoordinate(coordinateTouched);
                    } else {
                        // drag map
                        handled = false;
                    }
                    MainActivity.removeActionButtons();
                }
                invalidate();
                handled = true;
                break;
/*
            case MotionEvent.ACTION_POINTER_UP:
                pointerId = event.getPointerId(actionIndex);
                nodes.remove(pointerId);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                handled = true;
                break;
*/
            default:
                // do nothing
                break;
        }

        //return super.onTouchEvent(event);
        return super.onTouchEvent(event) || handled;
        //return true;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    /*
    private void initGestureListener(final Context context) {

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                // code...
            }
        });

        scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            public boolean onScale(ScaleGestureDetector detector) {
                // do scaling here
                scaleFactor *= detector.getScaleFactor();
                Log.w(TAG, "scaling factor: " + scaleFactor);
                scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 2.0f));
                FlyPlanController.getInstance().setScaleFactor(scaleFactor);
                invalidate(); // ?
                return true;
            }
        });

    }
    */
}
