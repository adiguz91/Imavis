package com.drone.imavis.flyplan.mvc.controller;

import android.graphics.Canvas;
import android.widget.Button;

import com.drone.imavis.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.flyplan.mvc.model.flyplan.nodes.shapes.simple.Line;

import java.util.List;

/**
 * Created by adigu on 24.02.2017.
 */

public interface IFlyPlanDraw {
    void onAddText(Canvas canvas, Coordinate coordinate, String text);
    void onAddNode(Canvas canvas, Node node);
    //void onAddNodeLine(Canvas canvas);
    //void onAddNodeDirection(Canvas canvas, Node node);

    void onToggleSpeedSlider(Canvas canvas);
    void onToogleHeightSlider(Canvas canvas);

    void onAddButtons(Canvas canvas, List<Button> buttons);
    void onAddActionButtons(Canvas canvas, List<Button> buttons);
}
