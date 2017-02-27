package com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.types.poi;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.drone.imavis.constants.classes.CColor;
import com.drone.imavis.constants.classes.CShape;
import com.drone.imavis.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.Node;
import com.drone.imavis.services.flyplan.mvc.model.flyplan.nodes.shapes.simple.Text;

/**
 * Created by adigu on 03.02.2017.
 */

public class PointOfInterest<T> extends Node implements IPointOfInterestDraw {
    public PointOfInterest(Coordinate coordinateTouched) {
        super(PointOfInterest.class, coordinateTouched);
    }

    public void draw(Canvas canvas, String content) {
        draw(canvas, content, false);
    }

    public void draw(Canvas canvas, String content, boolean selected) {
        this.getShape().draw(canvas, selected);
        addText(canvas, content);
    }

    public Paint getPaint() {
        Paint poiPaint = new Paint();
        poiPaint.setAntiAlias(true);
        //poiPaint.setColor(Color.parseColor(CColor.POI_CIRCLES.get(0)));
        poiPaint.setStyle(Paint.Style.FILL);
        return poiPaint;
    }

    @Override
    public void addText(Canvas canvas, String content) {
        Text text = new Text<PointOfInterest>(PointOfInterest.class, getShape().getCoordinate(), content);
        text.draw(canvas);
    }
}
