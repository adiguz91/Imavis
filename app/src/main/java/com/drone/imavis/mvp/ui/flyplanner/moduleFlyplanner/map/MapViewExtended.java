package com.drone.imavis.mvp.ui.flyplanner.moduleFlyplanner.map;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.services.flyplan.mvc.model.extensions.coordinates.Coordinate;
import com.drone.imavis.mvp.services.flyplan.mvc.view.FlyPlanView;
import com.drone.imavis.mvp.ui.flyplanner.FlyplannerActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

public class MapViewExtended extends MapView {

    private FlyPlanView flyPlanView;
    private boolean isDraggingEnabled;
    private GoogleMap googleMap;

    public MapViewExtended(Context context) {
        super(context);
    }

    public MapViewExtended(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        isDraggingEnabled = true;
        flyPlanView = ((FlyplannerActivity) getContext()).findViewById(R.id.flyplannerDraw);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("MapViewExtended", String.valueOf(event.getAction()));
        Coordinate touched = new Coordinate(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!isDraggingEnabled)
                    enableMapDragging(true);
                flyPlanView.dragView(touched);
                break;
            case MotionEvent.ACTION_UP:
                flyPlanView.setNewGlobalCoordinate(null);
                enableMapDragging(false);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private void enableMapDragging(boolean enable) {
        isDraggingEnabled = enable;
        if (googleMap == null && ((FlyplannerActivity) getContext()).getFlyplan().getMap() != null)
            googleMap = ((FlyplannerActivity) getContext()).getFlyplan().getMap().getMap();
        if (googleMap != null)
            googleMap.getUiSettings().setScrollGesturesEnabled(isDraggingEnabled);
    }

    /*
    private MapViewExtended.OnTouchListener listener;

    public void setOnTouchListener(MapViewExtended.OnTouchListener listener) {
        this.listener = listener;
    }

    public interface OnTouchListener {
        void onTouch();
    }
    */
}