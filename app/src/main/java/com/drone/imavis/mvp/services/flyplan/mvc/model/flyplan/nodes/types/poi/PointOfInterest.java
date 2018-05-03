package com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.types.poi;

import android.graphics.Canvas;
import android.graphics.Color;

import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.mvp.services.flyplan.mvc.model.flyplan.nodes.shapes.simple.Text;
import com.drone.imavis.mvp.util.constants.classes.CColor;
import com.drone.imavis.mvp.util.constants.classes.CShape;

/**
 * Created by adigu on 03.02.2017.
 */

public class PointOfInterest<T> extends Node implements IPointOfInterestDraw {
    public PointOfInterest(Coordinate coordinateTouched) {
        super(PointOfInterest.class, coordinateTouched);
        setShapePaint();
    }

    public void draw(Canvas canvas, String content) {
        this.getShape().draw(canvas);
        addText(canvas, content);
    }

    public void setShapeSelectedPaint() {
        //this.getShape().setBackgroundColor(Color.parseColor(CColor.NODE_SELECTED_CIRCLE));
        this.getShape().setBorderColor(Color.parseColor(CColor.NODE_SELECTED_CIRCLE_BORDER));
    }

    public void setShapePaint() {
        this.getShape().setBorderColor(Color.parseColor(CColor.POI_CIRCLE_BORDER));
        this.getShape().setBorder(CShape.POI_CIRCLE_BORDERSIZE);
    }

    private void addText(Canvas canvas, String content) {
        Text text = new Text<PointOfInterest>(PointOfInterest.class, getShape().getCoordinate(), content);
        text.draw(canvas);
    }
}
