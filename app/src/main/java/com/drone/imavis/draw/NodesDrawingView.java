package com.drone.imavis.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.drone.imavis.constants.classes.CFlyPlan;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.flyplan.coordinates.Coordinate;
import com.drone.imavis.flyplan.dimension.Size;
import com.drone.imavis.flyplan.nodes.Node;
import com.drone.imavis.flyplan.nodes.data.waypoint.Waypoint;
import com.drone.imavis.flyplan.nodes.data.waypoint.WaypointData;
import com.drone.imavis.flyplan.nodes.shapes.geometric.Circle;
import com.drone.imavis.flyplan.nodes.shapes.geometric.GeometricShape;
import com.drone.imavis.view.MainActivity;

import java.util.LinkedHashMap;
import java.util.Map;

public class NodesDrawingView extends View {

    private static final String TAG = "NodesDrawingView";
    //private Bitmap mBitmap = null; // main bitmap for background
    private Rect viewRect;
    private Paint paintNode; // draw nodes
    private Paint paintText; // draw text
    private Paint paintPath; // draw line / path
    private Map<Integer, Node> nodes = new LinkedHashMap();
    private SparseArray<Node> nodePointers = new SparseArray(CFlyPlan.MAX_NODES);
    //private NodeController nodeController = new NodeController();

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;

    private MainActivity mainActivity = (MainActivity) getContext();

    /**
     * Default constructor
     *
     * @param context {@link android.content.Context}
     */
    public NodesDrawingView(final Context context) {
        super(context);
        init(context);

    }

    public NodesDrawingView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NodesDrawingView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(final Context context) {

        mScaleDetector = new ScaleGestureDetector(context, new ScaleGestureListener());

        // Generate bitmap used for background
        //mBitmap = BitmapFactory.decodeResource(ct.getResources(), R.drawable.up_image);
        paintNode = new Paint();
        paintNode.setAntiAlias(true);
        paintNode.setColor(Color.WHITE);
        //paintNode.setStrokeWidth(40);
        paintNode.setStyle(Paint.Style.FILL);

        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(50);  //set text size

        paintPath = new Paint();
        paintPath.setAntiAlias(true);
        paintPath.setColor(Color.RED);
        paintPath.setStrokeWidth(10);
        paintPath.setStyle(Paint.Style.STROKE);
        float height = 10f;
        paintPath.setShader(new LinearGradient(0,0,0,height,Color.RED,Color.YELLOW, Shader.TileMode.CLAMP));

        /*
        Path pth = new Path();
        pth.moveTo(w*0.27f,0);
        pth.lineTo(w*0.73f,0);
        pth.lineTo(w*0.92f,h);
        pth.lineTo(w*0.08f,h);
        pth.lineTo(w*0.27f,0);
        p.setColor(0xff800000);
        p.setShader(new LinearGradient(0,0,0,h,0xff000000,0xffffffff,Shader.TileMode.CLAMP));
        canvas.drawPath(pth,p);
        */
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        // background bitmap to cover all area
        //canv.drawBitmap(mBitmap, null, viewRect, null);

        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor);
        //mainActivity.Zoom(mScaleFactor);

        // onDraw() code goes here
        float lastX = 0, lastY = 0;
        int counter = 0;
        Node node;
        Node lastNode = null;

        for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
            node = entry.getValue();
            node.getShape().draw(canvas, paintNode, mScaleFactor);

            //canv.drawCircle(node.centerX, node.centerY, node.radius, paintNode);
            drawText(paintNode, canvas, String.valueOf(counter), node.getShape().getCoordinate().getX(), node.getShape().getCoordinate().getY());
            if(counter > 0) {
                drawPath(paintNode, canvas, lastX, lastY, node.getShape().getCoordinate().getX(), node.getShape().getCoordinate().getY());
            }

            if(lastNode != null)
                drawArcExtended(canvas, lastNode.getShape(), node.getShape());

            lastX = node.getShape().getCoordinate().getX();
            lastY = node.getShape().getCoordinate().getY();
            lastNode = node;
            counter++;
        }

        canvas.restore();
    }

    public Coordinate getScaledCoordinate(Coordinate originalPoint, float scalingFactor) {
        float newX, newY;
        newX = originalPoint.getX() / scalingFactor;
        newY = originalPoint.getY() / scalingFactor;
        return new Coordinate(newX, newY);
    }

    private float angleBetweenPoints(GeometricShape nodeA, GeometricShape nodeB) {
        // angle in degrees
        // var angleDeg = Math.atan2(p2.y - p1.y, p2.x - p1.x) * 180 / Math.PI;
        // or
        //
        //float angle = (float) Math.toDegrees(Math.atan2(target.y - y, target.x - x));
        //if(angle < 0){
        //    angle += 360;
        //}
        //return angle;

        float angleInDegrees = (float) (Math.atan2(nodeB.getCoordinate().getY() - nodeA.getCoordinate().getY(),
                        nodeB.getCoordinate().getX() - nodeA.getCoordinate().getX()) * 180f / Math.PI);

        if(angleInDegrees < 0){
            angleInDegrees += 360;
        }
        return angleInDegrees;
    }

    private void drawArcExtended(Canvas canvas, GeometricShape shape, GeometricShape lastShape) {

        Paint paint = new Paint();
        final RectF rect = new RectF();
        float angleDirection = angleBetweenPoints(shape, lastShape);
        float angleDistance = 60;

        float anglePoint1 = angleDirection - (angleDistance/2);
        float anglePoint2 = angleDirection + (angleDistance/2);

        float radius = shape.getSize();
        float distance = radius + shape.getBorder() + 20;

        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        //paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);

        rect.set(shape.getCoordinate().getX() - distance,
                 shape.getCoordinate().getY() - distance,
                 shape.getCoordinate().getX() + distance,
                 shape.getCoordinate().getY() + distance);

        Coordinate peakPoint = PointOnCircle(shape.getCoordinate(), distance+distance/2, angleDirection); // Kreuzpunkt
        Coordinate point1 = PointOnCircle(shape.getCoordinate(), distance, anglePoint1);
        Coordinate point2 = PointOnCircle(shape.getCoordinate(), distance, anglePoint2);

        Path path = new Path();
        path.addArc(rect, anglePoint1, angleDistance);
        //path.moveTo(point2.getX(), point2.getY());
        //path.lineTo(shape.getCoordinate().getX(), shape.getCoordinate().getY()-distance-distance/2);
        path.lineTo(peakPoint.getX(), peakPoint.getY());
        path.lineTo(point1.getX(), point1.getY());

        canvas.drawPath(path, paint);
        //canvas.drawArc(rect, 240, 60, false, paint);
    }

    private Coordinate PointOnCircle(Coordinate coordinate, float radius, float angleInDegrees) {
        // Convert from degrees to radians via multiplication by PI/180
        float x = (float)(radius * Math.cos(angleInDegrees * Math.PI / 180f)) + coordinate.getX();
        float y = (float)(radius * Math.sin(angleInDegrees * Math.PI / 180f)) + coordinate.getY();
        return new Coordinate(x, y);
    }

    private void drawText(Paint paintNode, Canvas canvas, String content, float x, float y) {
        float textWidth = paintText.measureText(content) / 2;
        float textSize = paintText.getTextSize(); // textheight
        //canvas.drawPaint(paintNode);
        paintText.setAntiAlias(true);
        paintText.setTextAlign(Paint.Align.CENTER);
        //paintText.setColor(Color.YELLOW);
        //paintText.setStyle(Paint.Style.FILL);

        Coordinate originalCoordinate = new Coordinate(x, y + textSize/2 - 10);
        Coordinate scaledCoordinate = getScaledCoordinate(originalCoordinate, mScaleFactor);

        canvas.drawText(content, scaledCoordinate.getX(), scaledCoordinate.getY(), paintText);
    }

    private void drawPath(Paint paintNode, Canvas canvas, float fromX, float fromY, float toX, float toY) {

        Coordinate fromCoordinate = new Coordinate(fromX, fromY);
        Coordinate fromScaledCoordinate = getScaledCoordinate(fromCoordinate, mScaleFactor);

        Coordinate toCoordinate = new Coordinate(toX, toY);
        Coordinate toScaledCoordinate = getScaledCoordinate(toCoordinate, mScaleFactor);

        Path path = new Path();
        path.moveTo(fromScaledCoordinate.getX(), fromScaledCoordinate.getY());
        path.lineTo(toScaledCoordinate.getX(),toScaledCoordinate.getY());
        canvas.drawPath(path,paintPath);
    }

    private void drawLineArrow(Paint paintNode, Canvas canvas, float fromX, float fromY, float toX, float toY) {
        paintNode.setStyle(Paint.Style.STROKE);
        paintNode.setStrokeWidth(5);
        paintNode.setColor(Color.RED);
        float startX = fromX;
        float startY = fromY;
        float stopX = toX;
        float stopY = toY;
        canvas.drawLine(startX, startY, stopX, stopY, paintNode);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(event);

        boolean handled = false;

        Node touchedNode;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        // get touch event coordinates and make transparent node from it
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // it's the first pointer, so clear all existing pointers data
                clearCirclePointer();

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

                // check if we've touched inside some node
                touchedNode = obtaintouchedNode(xTouch, yTouch);
                touchedNode.getShape().setCoordinate(new Coordinate(xTouch, yTouch));
                nodePointers.put(event.getPointerId(0), touchedNode);

                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.w(TAG, "Pointer down");
                // It secondary pointers, so obtain their ids and check circles
                pointerId = event.getPointerId(actionIndex);

                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);

                // check if we've touched inside some node
                touchedNode = obtaintouchedNode(xTouch, yTouch);

                nodePointers.put(pointerId, touchedNode);
                touchedNode.getShape().setCoordinate(new Coordinate(xTouch, yTouch));
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();

                Log.w(TAG, "Move");

                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    // Some pointer has moved, search it by pointer id
                    pointerId = event.getPointerId(actionIndex);

                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);

                    touchedNode = nodePointers.get(pointerId);

                    if (null != touchedNode) {
                        touchedNode.getShape().setCoordinate(new Coordinate(xTouch, yTouch));
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
                // not general pointer was up
                pointerId = event.getPointerId(actionIndex);

                nodePointers.remove(pointerId);
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

    /**
     * Clears all CircleArea - pointer id relations
     */
    private void clearCirclePointer() {
        Log.w(TAG, "clearCirclePointer");

        nodePointers.clear();
    }

    /**
     * Search and creates new (if needed) node based on touch area
     *
     * @param xTouch int x of touch
     * @param yTouch int y of touch
     *
     * @return obtained {@link Node}
     */
    private Node obtaintouchedNode(final int xTouch, final int yTouch) {
        Node touchedNode = gettouchedNode(xTouch, yTouch);

        if (null == touchedNode) {

            Circle shape = new Circle(new Coordinate(xTouch, yTouch));
            WaypointData nodeData = new WaypointData();
            touchedNode = new Waypoint(shape, nodeData);

            if (nodes.size() == CFlyPlan.MAX_NODES) {
                Log.w(TAG, "Clear all circles, size is " + nodes.size());
                // remove first node
                nodes.clear();
            }

            Log.w(TAG, "Added node " + touchedNode);
            nodes.put(nodes.size(), touchedNode);
        }

        return touchedNode;
    }

    /**
     * Determines touched node
     *
     * @param xTouch int x touch coordinate
     * @param yTouch int y touch coordinate
     *
     * @return {@link Node} touched node or null if no node has been touched
     */
    private Node gettouchedNode(final int xTouch, final int yTouch) {
        Node touched = null;
        Node node;

        for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {

            node = entry.getValue();

            if ((node.getShape().getCoordinate().getX() - xTouch) *
                (node.getShape().getCoordinate().getX() - xTouch) +
                (node.getShape().getCoordinate().getY() - yTouch) *
                (node.getShape().getCoordinate().getY() - yTouch) <=
                 node.getShape().getSize() * node.getShape().getSize()) {
                touched = node;
                break;
            }
        }

        return touched;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    public class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        //private float mScaleFactor = 1.0f;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            Log.w(TAG, "scaling factor: " + mScaleFactor);

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 2.0f));

            invalidate();
            return true;
        }
    }
}

