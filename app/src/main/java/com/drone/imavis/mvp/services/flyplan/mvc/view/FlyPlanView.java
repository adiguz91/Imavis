package com.drone.imavis.mvp.services.flyplan.mvc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
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
import com.drone.imavis.mvp.ui.base.BaseActivity;
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

    private GestureDetector gestureDetector;
    private Rect viewRect;
    private boolean isLoading = true;
    private FlyplannerFragment flyplannerFragment;

    private SheetFab actionSheetMenu;
    private Coordinate dragCoordinate = new Coordinate(0, 0);
    private Coordinate dragCoordinatePrev = new Coordinate(0, 0);

    public FlyPlanView(final Context context) {
        super(context);
    }

    public FlyPlanView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public FlyPlanView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlyPlanView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

    private boolean isDragingView = false;

    public static boolean isLocked() {
        return isLocked;
    }

    public static void setIsLocked(boolean isLocked) {
        FlyPlanView.isLocked = isLocked;
    }

    private boolean isPressing = false;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ((BaseActivity) getContext()).activityComponent().inject(this);

        nodes = new SparseArray<Node>(CFlyPlan.MAX_WAYPOINTS_SIZE + CFlyPlan.MAX_POI_SIZE);
        gestureDetector = new GestureDetector(getContext(), new GestureListener(FlyPlanView.this));
        scaleDetector = new ScaleGestureDetector(getContext(), scaleListener);
    }

    public void setFlyplanner(FlyplannerFragment flyplannerFragment) {
        this.flyplannerFragment = flyplannerFragment;
    }

    public FlyplannerFragment getFlyplannerFragment() {
        return flyplannerFragment;
    }

    private Coordinate newGlobalCoordinate;

    public boolean isIsLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public static boolean isCoordinateInsideCircle(Coordinate center, Coordinate point, float radius) {
        // Compare radius of circle with distance of its center from given point
        return (point.getX() - center.getX()) * (point.getX() - center.getX()) +
                (point.getY() - center.getY()) * (point.getY() - center.getY()) <= radius * radius;
    }

    public void setGlobalMoveCoordinate(Coordinate coordinate) {
        dragCoordinatePrev = new Coordinate(dragCoordinate.getX(), dragCoordinate.getY());
        globalMoveCoordinate = coordinate;
    }

    public Coordinate getDragCoordinate() {
        return dragCoordinate;
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(dragCoordinate.getX(), dragCoordinate.getY());
        canvas.scale(getScaleFactor(), getScaleFactor()); // mainActivity.Zoom(mScaleFactor); // GoogleMap
        FlyPlanController.getInstance().draw(canvas);
        canvas.restore();
    }

    private boolean isEnabledActionMenu;

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        //event.setLocation(event.getRawX(), event.getRawY());
        //event.offsetLocation(-dragCoordinate.getX(), -dragCoordinate.getY());
        return doOnTouch(event);
    }

    public void setIsDragingView(boolean isDragingView) {
        this.isDragingView = isDragingView;
    }

    public void dragView(Coordinate coordinateTouched) {
        if (newGlobalCoordinate == null) {
            dragCoordinatePrev = new Coordinate(dragCoordinate.getX(), dragCoordinate.getY());
            newGlobalCoordinate = new Coordinate(coordinateTouched.getX(), coordinateTouched.getY());
        }
        dragCoordinate.setCoordinate(dragCoordinatePrev.getX() + (coordinateTouched.getX() - newGlobalCoordinate.getX()),
                dragCoordinatePrev.getY() + (coordinateTouched.getY() - newGlobalCoordinate.getY()));
        invalidate();
    }

    public void setNewGlobalCoordinate(Coordinate coordinate) {
        newGlobalCoordinate = coordinate;
    }

    public boolean doOnTouch(MotionEvent event) {
        //Log.w(TAG, "onTouchEvent: " + event);

        if (isEnabledActionMenu)
            return false;

        if (isIsLoading())
            return false;

        if (isLocked)
            return super.onTouchEvent(event); // set drag map

        if (!isDragingView)
            event.offsetLocation(-dragCoordinate.getX(), -dragCoordinate.getY());

        isHandledTouch = false;
        // onTouch trigger events
        isHandledTouch = scaleDetector.onTouchEvent(event);
        isHandledTouch = gestureDetector.onTouchEvent(event);

        if (isEnabledActionMenu)
            return false;

        if (event.getActionMasked() == MotionEvent.ACTION_MOVE)
            isHandledTouch = actionMove(event);
        else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (!isHandledTouch) {
                isDragingView = false;
                newGlobalCoordinate = null;
                isHandledTouch = actionUp(event);
            }
        }

        invalidate();
        return isHandledTouch;
    }

    public void setIsEnabledActionMenu(boolean isEnabledActionMenu) {
        this.isEnabledActionMenu = isEnabledActionMenu;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private Node selectedActionMenuNode;

    public Node getSelectedActionMenuNode() {
        return selectedActionMenuNode;
    }

    public void setSelectedActionMenuNode(Node selectedActionMenuNode) {
        this.selectedActionMenuNode = selectedActionMenuNode;
    }

    public boolean actionMove(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        boolean isHandled = true;
        for (int actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
            //pointerId = event.getPointerId(actionIndex);
            Coordinate coordinateTouched = new Coordinate(event.getX(), event.getY());
            Node touchedNode = FlyPlanController.getTouchedNode();
            if (touchedNode != null) {
                isDragingView = false;
                touchedNode.getShape().setCoordinate(coordinateTouched);
            } else {
                if (globalMoveCoordinate != null) { // drag map
                    Coordinate correctCoordinate = new Coordinate(coordinateTouched.getX() - dragCoordinatePrev.getX(),
                            coordinateTouched.getY() - dragCoordinatePrev.getY());
                    if (isDragingView || !isCoordinateInsideCircle(globalMoveCoordinate, coordinateTouched, 40)) {
                        if (isDragingView) { // removes one artefact
                            if (newGlobalCoordinate == null)
                                newGlobalCoordinate = new Coordinate(coordinateTouched.getX(), coordinateTouched.getY());
                            dragCoordinate.setCoordinate(dragCoordinatePrev.getX() + (coordinateTouched.getX() - newGlobalCoordinate.getX()),
                                    dragCoordinatePrev.getY() + (coordinateTouched.getY() - newGlobalCoordinate.getY()));
                        }
                        isDragingView = true;
                    }
                }
                isHandled &= false;
            }
            break;
        }
        return isHandled;
    }

    public boolean actionUp(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        boolean isHandled = true;
        for (int actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
            //pointerId = event.getPointerId(actionIndex);
            Coordinate coordinateTouched = new Coordinate(event.getX(), event.getY()); // event.getY(actionIndex)
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
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }
}
