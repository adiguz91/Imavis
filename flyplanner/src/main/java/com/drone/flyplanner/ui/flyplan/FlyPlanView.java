package com.drone.flyplanner.ui.flyplan;

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

import com.drone.flyplanner.data.model.flyplan.nodes.Node;
import com.drone.flyplanner.ui.flyplan.listener.GestureListener;
import com.drone.flyplanner.ui.flyplan.listener.ScaleListener;
import com.drone.flyplanner.util.constants.classes.CFlyPlan;
import com.drone.flyplanner.util.constants.classes.CMap;
import com.drone.flyplanner.util.flyplan.control.IFlyPlanUtil;
import com.drone.flyplanner.util.models.coordinates.Coordinate;

import javax.inject.Inject;

public class FlyPlanView extends View {

    private GestureDetector gestureDetector;
    private Rect viewRect;

    @Inject
    IFlyPlanUtil flyPlanUtil;

    private final String TAG = "FlyPlanView";
    private ScaleGestureDetector scaleDetector;
    private SparseArray<Node> nodes;
    public SparseArray<Node> getNodes() {
        return nodes;
    }

    private boolean isHandledTouch;
    public boolean getIsHandledTouch() {
        return isHandledTouch;
    }

    //private GoogleMapFragment googleMapFragment;

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

        //final Activity activity = (Activity) context;
        //GoogleMapFragment googleMapFragment = (GoogleMapFragment) activity.fra.findFragmentById(R.id.flyplannerMapView);
    }

    public float getScaleFactor() {
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
        flyPlanUtil.draw(canvas);
        canvas.restore();
    }

    private Node touchedNode;

    private boolean onDown(MotionEvent event) {
        //FlyPlanView.getNodes().clear();
        int pointerId = event.getPointerId(0);
        Coordinate touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
        touchedNode = flyPlanUtil.getTouchedNode(touchCoordinate);
        if(touchedNode == null)
            return false;
        return true;
    }

    private boolean isDown = false;
    public boolean isDown() {
        return isDown;
    }

    public boolean doOnTouch(MotionEvent event) {
        //super.onTouchEvent(event);
        Log.w(TAG, "onTouchEvent: " + event);
        int actionIndex; // event.getActionIndex()

        isHandledTouch = false;

        // onTouch trigger events
        isHandledTouch = scaleDetector.onTouchEvent(event);
        isHandledTouch = gestureDetector.onTouchEvent(event);

        //isDown = false;
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

        if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
            isHandledTouch = true;
        Log.i("LogFlyplan", "isHandled: " + isHandledTouch + " | event: " + event.getActionMasked());

        //flyplannerListener.onCompleteHandling(isHandledTouch, event);

        if(!isHandledTouch)
            return false; //return super.onTouchEvent(event);
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
            Node touchedNode = flyPlanUtil.getTouchedNode();
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
