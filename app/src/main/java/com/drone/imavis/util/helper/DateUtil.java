package com.drone.imavis.util.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by adigu on 06.05.2017.
 */

public class DateUtil {

    private static Locale locale = Locale.GERMANY;
    private static String dateFormat = "dd.MM.yyyy";
    private static SimpleDateFormat simpleDateFormat;

    public static Date getDateNow() {
        Calendar calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat(dateFormat, locale);
        try {
            return simpleDateFormat.parse(calendar.getTime().toString());
        }
        catch (ParseException e) {
            return null;
        }
    }
}
