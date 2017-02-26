package com.drone.imavis.services.flyplan.mvc.controller;

import android.graphics.Canvas;
import android.widget.Button;

import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.data.waypoint.Waypoint;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.simple.Text;

import java.util.List;

/**
 * Created by adigu on 24.02.2017.
 */

public class FlyPlanDrawController implements IFlyPlanDraw {

    @Override
    public void onAddText(Canvas canvas, Coordinate coordinate, String content) {
        Text text = new Text<Waypoint>(Waypoint.class, coordinate, content);
        text.draw(canvas);
    }

    @Override
    public void onAddNode(Canvas canvas, Node node) {
        node.getShape().draw(canvas);
    }

    @Override
    public void onToggleSpeedSlider(Canvas canvas) {

    }

    @Override
    public void onToogleHeightSlider(Canvas canvas) {

    }

    @Override
    public void onAddButtons(Canvas canvas, List<Button> buttons) {

    }

    @Override
    public void onAddActionButtons(Canvas canvas, List<Button> buttons) {

    }



}
