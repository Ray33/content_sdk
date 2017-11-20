package io.mobitech.content_ui.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.mobitech.content_ui.R;

public class PrettyTime {

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(String dateString, Context ctx) {
        SimpleDateFormat taboolaDateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");

        Date convertedDate = new Date();

        try {
            convertedDate = taboolaDateFormat.parse(dateString);
            Log.v("date", convertedDate.toString());
        } catch (ParseException e) {
            if (TextUtils.isDigitsOnly(dateString)){
                convertedDate = new Date(Long.parseLong(dateString));   // parse date from start
            }else{
                convertedDate = new Date(System.currentTimeMillis());
            }
        }

        return getTimeAgo(convertedDate, ctx);
    }

    public static String getTimeAgo(Date date, Context ctx) {

        SimpleDateFormat monthDateFormatter = new SimpleDateFormat("dd MMM");
        SimpleDateFormat fullDateFormatter = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat timeDateFormatter = new SimpleDateFormat("HH:mm");

        if (date == null) {
            return null;
        }

        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int dim = getTimeDistanceInMinutes(time);

        String timeAgo = fullDateFormatter.format(date);

        if (dim == 0) {
            return "< 1 " + ctx.getResources().getString(R.string.date_util_unit_minute) ;//+ " " + ctx.getResources().getString(R.string.date_util_suffix);
        } else if (dim == 1) {
            return "1 " + ctx.getResources().getString(R.string.date_util_unit_minute);
        } else if (dim >= 2 && dim <= 44) {
            //44 mins ago
            return dim + " " + ctx.getResources().getString(R.string.date_util_unit_minutes);
        } else if (fullDateFormatter.format(date).equals(fullDateFormatter.format(curDate))) {
            //same day
            return timeDateFormatter.format(date);
        } else if (dim <= 525599) {
            //same year
            return monthDateFormatter.format(date);
        }

        return timeAgo;
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }
}
