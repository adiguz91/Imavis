package com.drone.imavis.flyplan.mvc.controller;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import com.drone.imavis.constants.classes.CFlyPlan;
import com.drone.imavis.flyplan.mvc.model.flyplan.FlyPlan;
import com.drone.imavis.flyplan.mvc.model.flyplan.map.Map;
import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.flyplan.mvc.model.extensions.dimension.Size;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoint;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.data.waypoint.WaypointData;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric.Circle;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.geometric.GeometricShape;
import com.drone.imavis.flyplan.mvc.view.flyplan.FlyPlanView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;

import extensions.file.FileExtension;

/**
 * Created by adigu on 23.02.2017.
 */
public class FlyPlanController implements IFlyPlan {

    private Paint paintNode; // draw nodes
    private Paint paintText; // draw text
    private Paint paintPath; // draw line / path

    private java.util.Map<Integer, Node> nodes = new LinkedHashMap();
    private SparseArray<Node> nodePointers = new SparseArray(CFlyPlan.MAX_NODES);

    private static FlyPlanController flyPlanController;
    private FlyPlan flyPlan;
    //private Paint paint;

    // SINGLETON PATTERN
    public static FlyPlanController getInstance() {
        if (flyPlanController == null)
            flyPlanController = new FlyPlanController();
        return flyPlanController;
    }

    public FlyPlanController() {
        Coordinate mapCoordinate = new Coordinate(0,0);
        Size mapSize = new Size(50, 50);
        Map map = new Map(mapCoordinate, mapSize);
        this.flyPlan = new FlyPlan(map);
    }
    public FlyPlanController(Map map) {
        this.flyPlan = new FlyPlan(map);
    }
    public FlyPlanController(FlyPlan flyPlan) {
        this.flyPlan = flyPlan;
    }

    public void init(final Context context) {
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
        paintPath.setColor(Color.parseColor("#805386E4"));
        paintPath.setStrokeWidth(10);
        paintPath.setStyle(Paint.Style.STROKE);
        float height = 10f;
    }

    public Paint getPaintNode() {
        return paintNode;
    }
    public Paint getPaintText() {
        return paintText;
    }
    public Paint getPaintPath() {
        return paintPath;
    }

    public Coordinate getScaledCoordinate(Coordinate originalPoint, float scalingFactor) {
        float newX, newY;
        newX = originalPoint.getX() / scalingFactor;
        newY = originalPoint.getY() / scalingFactor;
        return new Coordinate(newX, newY);
    }

    private float angleBetweenPoints(GeometricShape nodeA, GeometricShape nodeB) {
        float angleInDegrees = (float) (Math.atan2(nodeB.getCoordinate().getY() - nodeA.getCoordinate().getY(),
                nodeB.getCoordinate().getX() - nodeA.getCoordinate().getX()) * 180f / Math.PI);
        if(angleInDegrees < 0)
            angleInDegrees += 360;
        return angleInDegrees;
    }

    public void drawArcExtended(Canvas canvas, GeometricShape shape, GeometricShape lastShape) {

        Paint paint = new Paint();
        final RectF rect = new RectF();
        float angleDirection = angleBetweenPoints(shape, lastShape);
        float angleDistance = 60;

        float anglePoint1 = angleDirection - (angleDistance/2);
        float anglePoint2 = angleDirection + (angleDistance/2);

        float radius = shape.getSize();
        float distance = radius + shape.getBorder() + 10;

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

    public void drawText(Canvas canvas, String content, Coordinate coordinate) {
        float textWidth = paintText.measureText(content) / 2;
        float textSize = paintText.getTextSize(); // textheight
        paintText.setAntiAlias(true);
        paintText.setTextAlign(Paint.Align.CENTER);
        Coordinate originalCoordinate = new Coordinate(coordinate.getX(), coordinate.getY() + textSize/2 - 10);
        Coordinate scaledCoordinate = getScaledCoordinate(originalCoordinate, getScaleFactor());
        canvas.drawText(content, scaledCoordinate.getX(), scaledCoordinate.getY(), paintText);
    }

    public void drawPath(Canvas canvas, Coordinate fromCoordinate, Coordinate toCoordinate) {
        Coordinate fromScaledCoordinate = getScaledCoordinate(fromCoordinate, getScaleFactor());
        Coordinate toScaledCoordinate = getScaledCoordinate(toCoordinate, getScaleFactor());
        Path path = new Path();
        path.moveTo(fromScaledCoordinate.getX(), fromScaledCoordinate.getY());
        path.lineTo(toScaledCoordinate.getX(),toScaledCoordinate.getY());
        canvas.drawPath(path,paintPath);
    }

    public void drawLineArrow(Canvas canvas, Coordinate fromCoordinate, Coordinate toCoordinate) {
        paintNode.setStyle(Paint.Style.STROKE);
        paintNode.setStrokeWidth(5);
        paintNode.setColor(Color.parseColor("#CC5386E4"));
        canvas.drawLine(fromCoordinate.getX(), fromCoordinate.getY(),
                        toCoordinate.getX(), toCoordinate.getY(), paintNode);
    }

    /**
     * Search and creates new (if needed) node based on touch area
     *
     * @param touchCoordinate
     *
     * @return obtained {@link Node}
     */
    public Node obtainTouchedNode(Coordinate touchCoordinate) {
        Node touchedNode = getTouchedNode(touchCoordinate);

        if (touchedNode == null) {

            Circle shape = new Circle(touchCoordinate);
            WaypointData nodeData = new WaypointData();
            touchedNode = new Waypoint(shape, nodeData);
            touchedNode.getShape().setCoordinate(touchCoordinate);

            if (nodes.size() == CFlyPlan.MAX_NODES) {
                //Log.w(TAG, "Clear all circles, size is " + nodes.size());
                // remove first node
                nodes.clear();
            }
            //Log.w(TAG, "Added node " + touchedNode);
            nodes.put(nodes.size(), touchedNode);
        }

        touchedNode.getShape().setCoordinate(touchCoordinate);
        return touchedNode;
    }

    /**
     * Determines touched node
     *
     * @param touchCoordinate
     *
     * @return {@link Node} touched node or null if no node has been touched
     */
    private Node getTouchedNode(Coordinate touchCoordinate) {
        Node touched = null;
        Node node;
        for (java.util.Map.Entry<Integer, Node> entry : nodes.entrySet()) {
            node = entry.getValue();
            if ((node.getShape().getCoordinate().getX() - touchCoordinate.getX()) *
                    (node.getShape().getCoordinate().getX() - touchCoordinate.getX()) +
                    (node.getShape().getCoordinate().getY() - touchCoordinate.getY()) *
                            (node.getShape().getCoordinate().getY() - touchCoordinate.getY()) <=
                    node.getShape().getSize() * node.getShape().getSize()) {
                touched = node;
                break;
            }
        }

        if(touched != null)
            touched.getShape().setCoordinate(touchCoordinate);
        return touched;
    }

    public float getScaleFactor() {
        return FlyPlanView.getScaleFactor();
    }

    @Override
    public FlyPlan getFlyPlan() {
        return flyPlan;
    }

    @Override
    public FlyPlan onPlanCreateNew() {
        return null;
    }



    @Override
    public FlyPlan onPlanLoad(File file) {
        try
        {
            return FlyPlan.loadFromJsonFile(FileExtension.readFile(file));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onPlanSave() {
        File root = Environment.getExternalStorageDirectory();
        String absoluteFilePath = root.getAbsolutePath() + flyPlan.getTitle() + ".json";
        return FileExtension.writeToFile(absoluteFilePath, flyPlan.saveToJsonFile());
    }

    @Override
    public boolean onPlanDelete() {
        return false;
    }

    @Override
    public boolean onNodeAdd(Coordinate coordinate) {
        return false;
    }

    @Override
    public Node onNodeSelect(Coordinate coordinate) {
        return null;
    }

    @Override
    public boolean onNodeDelete(Node node) {
        return false;
    }

    @Override
    public boolean onNodeSelectAction(Node node) {
        return false;
    }
}
