package com.drone.imavis.util.helper;

import com.drone.imavis.util.constants.Constants;
import com.drone.imavis.util.constants.classes.CDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by adigu on 06.05.2017.
 */

public class DateUtil {

    public static Date getDateNow() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CDate.DATE_FORMAT, CDate.DATE_LOCALE);
        try {
            return simpleDateFormat.parse(calendar.getTime().toString());
        }
        catch (ParseException e) {
            return null;
        }
    }
}
