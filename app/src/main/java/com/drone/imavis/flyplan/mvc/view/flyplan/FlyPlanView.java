package com.drone.imavis.flyplan.mvc.view.flyplan;

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

import com.drone.imavis.constants.classes.CFlyPlan;
import com.drone.imavis.constants.classes.CMap;
import com.drone.imavis.draw.CopyNodesDrawingView;
import com.drone.imavis.flyplan.mvc.controller.FlyPlanDrawController;
import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.flyplan.mvc.controller.FlyPlanController;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoint;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.WaypointData;
import com.drone.imavis.flyplan.mvc.view.flyplan.listener.GestureListener;
import com.drone.imavis.flyplan.mvc.view.flyplan.listener.ScaleListener;

import java.util.ListIterator;

public class FlyPlanView extends View {

    private static final String TAG = "NodesDrawingView";
    private Rect viewRect;

    private static ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;
    private Coordinate touchCoordinate = null;

    private SparseArray<Node> nodes = new SparseArray<Node>(CFlyPlan.MAX_NODES);
    //private FlyPlanDrawController flyPlanDrawController = new FlyPlanDrawController();

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

    public static float getScaleFactor() {
        if(scaleDetector != null)
            return scaleDetector.getScaleFactor();
        return CMap.SCALE_FACTOR_DEFAULT;
    }

    public void init(final Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        // FlyPlanController.getInstance().init(context);
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.scale(FlyPlanController.getInstance().getScaleFactor(), FlyPlanController.getInstance().getScaleFactor());
        //mainActivity.Zoom(mScaleFactor);

        // BEGIN onDraw() ----------
        int counter = 1;
        Waypoint node, lastNode = null;
        ListIterator<Waypoint> iterator =  FlyPlanController.getInstance().getFlyPlan().getPoints().getWaypoints().listIterator();

        while (iterator.hasNext()) {
            node = iterator.next();

            if(lastNode != null)
                node.addLine(canvas, lastNode, node);

            node.getShape().draw(canvas);
            node.addText(canvas, String.valueOf(counter));

            if(lastNode != null)
                node.addDirection(canvas, lastNode, node);

            lastNode = node;
            counter++;
        }
        // END onDraw() ----------

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        Log.w(TAG, "onTouchEvent: " + event);
        //scaleDetector.onTouchEvent(event);
        //gestureDetector.onTouchEvent(event);

        boolean handled = false;
        Node touchedNode;
        int pointerId;
        int actionIndex = event.getActionIndex();

        // get touch event coordinates and make transparent node from it
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // it's the first pointer, so clear all existing pointers data
                clearCirclePointer();
                pointerId = event.getPointerId(0);
                touchCoordinate = new Coordinate(event.getX(0), event.getY(0));
                touchedNode = (Waypoint) FlyPlanController.getInstance().obtainTouchedNode(touchCoordinate);
                //FlyPlanController.getInstance().getFlyPlan().getPoints().addNode(touchedNode);
                nodes.put(pointerId, touchedNode);
                invalidate();

                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                Log.w(TAG, "Pointer down");
                // It secondary pointers, so obtain their ids and check circles
                pointerId = event.getPointerId(actionIndex);
                touchCoordinate = new Coordinate(event.getX(actionIndex), event.getY(actionIndex));
                touchedNode = (Waypoint) FlyPlanController.getInstance().obtainTouchedNode(touchCoordinate);
                //touchedNode.getData().setId(pointerId);
                //FlyPlanController.getInstance().getFlyPlan().getPoints().addNode(touchedNode);
                nodes.put(pointerId, touchedNode);
                //touchedNode.getDataY()= xTouch;
                //touchedCircle.centerY = yTouch;
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();

                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    pointerId = event.getPointerId(actionIndex);
                    /*ListIterator iterator = FlyPlanController.getInstance().getFlyPlan().getPoints().getWaypoints().listIterator();
                    while (iterator.hasNext()){
                        Node node = (Waypoint) iterator.next();
                        if(pointerId == node.getData().getId()) {
                            touchedNode = node;
                            break;
                        }
                    }*/
                    pointerId = event.getPointerId(actionIndex);
                    Coordinate coordinateTouched = new Coordinate(event.getX(actionIndex), event.getY(actionIndex));
                    touchedNode = nodes.get(pointerId);
                    if (touchedNode != null) {
                        touchedNode.getShape().setCoordinate(coordinateTouched);
                    }
                }
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                clearCirclePointer();
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                pointerId = event.getPointerId(actionIndex);
                nodes.remove(pointerId);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                handled = true;
                break;

            default:
                // do nothing
                break;
        }

        return super.onTouchEvent(event) || handled;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    private void clearCirclePointer() {
        Log.w(TAG, "clearCirclePointer");
        nodes.clear();
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
