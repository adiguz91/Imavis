package com.drone.imavis.mvp.util;

import com.drone.imavis.mvp.util.constants.classes.CDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by adigu on 06.05.2017.
 */

public final class DateUtil {

    public static Date getDateNow() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CDate.DATE_FORMAT, CDate.DATE_LOCALE);
        try {
            return simpleDateFormat.parse(calendar.getTime().toString());
        } catch (ParseException e) {
            return null;
        }
    }
}
