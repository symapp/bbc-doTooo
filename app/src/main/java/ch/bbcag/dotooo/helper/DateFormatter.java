package ch.bbcag.dotooo.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    public static String format(Date date) {
        Calendar filter_calendar= Calendar.getInstance();
        filter_calendar.setTime(date);

        int day = filter_calendar.get(Calendar.DAY_OF_MONTH);
        String dayS;
        switch (day > 20 ? (day % 10) : day) {
            case 1:  dayS = day + "st";
                break;
            case 2:  dayS = day + "nd";
                break;
            case 3:  dayS = day + "rd";
                break;
            default:  dayS = day + "th";
                break;
        }

        String month = filter_calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year = filter_calendar.get(Calendar.YEAR);

        return dayS + " " + month + " " + year;
    }

    public static String makeDateString(int day, int month, int year) {
        String dayS;
        switch (day > 20 ? (day % 10) : day) {
            case 1:  dayS = day + "st";
                break;
            case 2:  dayS = day + "nd";
                break;
            case 3:  dayS = day + "rd";
                break;
            default:  dayS = day + "th";
                break;
        }

        return dayS + " " + getMonthFormat(month) + " " + year;
    }

    public static String getMonthFormat(int month) {
        if (month == 1)
            return "January";
        if (month == 2)
            return "February";
        if (month == 3)
            return "March";
        if (month == 4)
            return "April";
        if (month == 5)
            return "May";
        if (month == 6)
            return "June";
        if (month == 7)
            return "Juli";
        if (month == 8)
            return "August";
        if (month == 9)
            return "September";
        if (month == 10)
            return "October";
        if (month == 11)
            return "November";
        if (month == 12)
            return "December";

        //default should never happen
        return "January";
    }
}
