package com.drone.imavis.mvp.util;

import com.drone.imavis.mvp.util.constants.classes.CFlyPlan.UnitOfLength;

/**
 * Created by Adrian on 28.11.2016.
 */
public class UnityConverterUtil {
    private static UnityConverterUtil ourInstance = new UnityConverterUtil();

    public static UnityConverterUtil getInstance() {
        return ourInstance;
    }

    private UnityConverterUtil() {
    }

    public double UnitOfLength(UnitOfLength sourceUnit, UnitOfLength destinationUnit, double value) {
        //Pair<UnitOfLength, UnitOfLength> unitOfLengthPair = new Pair<>(sourceUnit, destinationUnit);

        if(sourceUnit == UnitOfLength.Meter && destinationUnit == UnitOfLength.Yard)
            value *= 1.0936;

        if(sourceUnit == UnitOfLength.Yard && destinationUnit == UnitOfLength.Meter)
            value /= 1.0936;

        return value;
    }
}
