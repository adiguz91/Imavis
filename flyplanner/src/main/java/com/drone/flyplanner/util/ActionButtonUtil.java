package com.drone.flyplanner.util;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.drone.flyplanner.util.models.coordinates.Coordinate;
import com.drone.flyplanner.util.models.dimension.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adigu on 10.03.2017.
 */

public class ActionButtonUtil {

    private ViewGroup viewGroup;
    private Button nodeDeleteButton;

    public ActionButtonUtil(ViewGroup viewGroup) {
        initListOfActionNodes();
        this.viewGroup = viewGroup;
    }

    public List<Button> getActionButtons(Coordinate coordinate) {
        Coordinate correctCoordinate = getCheckedActionButtonCoordinate(coordinate);
        List<Button> buttons = new ArrayList<>();
        buttons.add(getNodeDeleteButton(correctCoordinate));
        //buttons.add(getClosingWaypoint(coordinate))
        //MainActivity.addActionButtons(buttons);
        return buttons;
    }

    List<String> listOfActionNodes = new ArrayList<>();
    private void initListOfActionNodes() {
        String nodeDeleteText = "Delete";
        //String waypointCloseText = "Close Circle";
        listOfActionNodes.add(nodeDeleteText);
        //listOfActionNodes.add(waypointCloseText);
    }

    // coordinate is the center of the buttons
    private Coordinate getCheckedActionButtonCoordinate(Coordinate coordinateTouched) {
        int screenPadding = 20;
        int buttonPadding = 10;
        int buttonTextSize = 16;

        int actionButtonWidth = 0;
        int actionButtonHeight = 0;
        for (String actionButtonName : listOfActionNodes) {
            actionButtonWidth += FlyPlanMathUtil.getInstance().getPointOfText(actionButtonName, buttonTextSize).getWidth() + buttonPadding*2;
            actionButtonHeight += FlyPlanMathUtil.getInstance().getPointOfText(actionButtonName, buttonTextSize).getHeight() + buttonPadding*2;
        }
        Size actionButtonSize = new Size(actionButtonWidth, actionButtonHeight);
        Size screenSize = new Size(viewGroup.getWidth(), viewGroup.getHeight());
        Coordinate correctedCoordinate = getCorrectCoordinatesOfActionButtons(coordinateTouched, actionButtonSize, screenSize, screenPadding);
        return correctedCoordinate;
    }

    private Coordinate getCorrectCoordinatesOfActionButtons(Coordinate coordinateElement, Size element, Size screenSize, int padding) {
        int elementBottom = (int) coordinateElement.getY() + element.getHeight();
        int elementTop = (int) coordinateElement.getY() - element.getHeight();
        int elementLeft = (int) coordinateElement.getX() - element.getWidth();
        int elementRight = (int) coordinateElement.getX() + element.getWidth();

        boolean isInsideTheScreen = true;
        if(elementLeft < 0+padding) {
            isInsideTheScreen &= false;
            elementLeft = 0+padding;
            elementRight = elementLeft + element.getWidth();
        }
        if(elementTop < 0+padding) {
            isInsideTheScreen &= false;
            elementTop = 0+padding;
            elementBottom = elementTop + element.getHeight();
        }
        if(elementRight > screenSize.getWidth()-padding) {
            isInsideTheScreen &= false;
            elementRight = screenSize.getHeight()-padding;
            elementLeft = elementRight - element.getWidth();
        }
        if(elementBottom > screenSize.getHeight()-padding) {
            isInsideTheScreen &= false;
            elementBottom = screenSize.getHeight()-padding;
            elementTop = elementBottom - element.getHeight();
        }

        if(!isInsideTheScreen)
            return new Coordinate(elementLeft, elementTop);
        // change the centered coordinateElement to left,top coordinate
        return coordinateElement; //.toLeftTop(element);
    }

    private Button getNodeDeleteButton(Coordinate coordinate) {
        if(nodeDeleteButton == null)
            nodeDeleteButton = new Button(viewGroup.getContext());
        nodeDeleteButton.setX(coordinate.getX());
        nodeDeleteButton.setY(coordinate.getY());
        nodeDeleteButton.setBackgroundColor(Color.YELLOW);
        nodeDeleteButton.setPadding(10, 10, 10, 10);
        nodeDeleteButton.setTextColor(Color.BLACK);
        nodeDeleteButton.setTextSize(16);
        nodeDeleteButton.setText("Delete");
        nodeDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return nodeDeleteButton;
    }
}
