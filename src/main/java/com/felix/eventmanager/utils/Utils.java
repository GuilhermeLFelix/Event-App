package com.felix.eventmanager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class Utils {
    public static boolean isValidHour(String hour) {
        if (hour == null)
            return false;
        else if (! hour.contains(":"))
            return false;
        else {
            String[] data = hour.split(":");
            int h1, h2;

            try {
                return (h1 = Integer.parseInt(data[0])) <= 23 && h1 >= 0 &&
                        (h2 = Integer.parseInt(data[1])) <= 59 && h2 >= 0;
            } catch (Exception exc) {
                return false;
            }
        }
    }

    public static LocalDateTime getDateWithHour(Date date, String hour) {
        String[] hr = hour.split(":");
        Integer h = hr[0] == null ? null : Integer.parseInt(hr[0]),
                m = hr[1] == null ? null : Integer.parseInt(hr[1]);

        LocalTime time = h == null || m == null ? LocalTime.MIDNIGHT : LocalTime.of(h, m, 0);
        Instant instant = date.toInstant();
        return LocalDateTime.of(instant.atZone(ZoneId.systemDefault()).toLocalDate(), time);
    };

    public static Date strToDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        return formatter.parse(date);
    }

    public static String dateToStr(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        return formatter.format(date);
    }
}
