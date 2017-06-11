package com.drone.flyplanner.util.flyplan.draw;

import android.graphics.Canvas;
import android.widget.Button;

import com.drone.flyplanner.data.model.flyplan.nodes.Node;
import com.drone.flyplanner.data.model.flyplan.nodes.shapes.simple.Text;
import com.drone.flyplanner.data.model.flyplan.nodes.types.waypoint.Waypoint;
import com.drone.flyplanner.util.models.coordinates.Coordinate;

import java.util.List;

/**
 * Created by adigu on 24.02.2017.
 */

public class FlyPlanDrawUtil implements IFlyPlanDrawUtil {

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
