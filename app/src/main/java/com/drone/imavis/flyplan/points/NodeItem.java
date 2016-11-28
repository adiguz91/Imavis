package com.drone.imavis.flyplan.points;

import com.drone.imavis.constants.Constants;
import com.drone.imavis.constants.classes.CFlyPlan;
import com.drone.imavis.flyplan.Coordinate;

/**
 * Created by Adrian on 26.11.2016.
 */

public class NodeItem extends PointItem {

    //int id;
    int direction;
    int distanceToNextNode;
    int speedToNextNode = CFlyPlan.MIN_SPEED;
    int flyHeight = CFlyPlan.MIN_FLY_HEIGHT;
    int size; // Ciryle=radius, Square=length

    Type type = Type.Circle;
    Mode mode = Mode.Progressive;
    PointOfInterest poi; // or id reference to POI
    Coordinate coordinate;

    public NodeItem() {}

    public enum Type {
        Circle,
        Square
    }

    public enum Mode {
        Straight,
        Custom,
        Progressive, // direction to next node
        POI
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDistanceToNextNode() {
        return distanceToNextNode;
    }

    public void setDistanceToNextNode(int distanceToNextNode) {
        this.distanceToNextNode = distanceToNextNode;
    }

    public int getSpeedToNextNode() {
        return speedToNextNode;
    }

    public void setSpeedToNextNode(int speedToNextNode) {
        this.speedToNextNode = speedToNextNode;
    }

    public int getFlyHeight() {
        return flyHeight;
    }

    public void setFlyHeight(int flyHeight) {
        this.flyHeight = flyHeight;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public PointOfInterest getPoi() {
        return poi;
    }

    public void setPoi(PointOfInterest poi) {
        this.poi = poi;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
