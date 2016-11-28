package extensions.converter;

import android.util.Pair;
import com.drone.imavis.constants.classes.CFlyPlan.UnitOfLength;

/**
 * Created by Adrian on 28.11.2016.
 */
public class Converter {
    private static Converter ourInstance = new Converter();

    public static Converter getInstance() {
        return ourInstance;
    }

    private Converter() {
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
