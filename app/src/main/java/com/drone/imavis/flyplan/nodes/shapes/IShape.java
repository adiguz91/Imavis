package com.drone.imavis.flyplan.nodes.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.drone.imavis.flyplan.dimension.Size;

/**
 * Created by adigu on 03.02.2017.
 */

public interface IShape {

    void draw(Canvas canvas, Paint paint, float scalingFactor);
}
