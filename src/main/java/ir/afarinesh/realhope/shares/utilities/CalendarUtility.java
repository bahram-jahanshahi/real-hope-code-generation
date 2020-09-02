package ir.afarinesh.realhope.shares.utilities;

import com.github.mfathi91.time.PersianDate;
import ir.afarinesh.realhope.core.domain.JavaDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtility {
    public static String format(Date date, String locale) {
        if (date == null) {
            return "";
        } else {
            if (locale.equals("en")) {
                return gregorianFormat(date);
            }
            if (locale.equals("fa")) {
                return persianFormat(date);
            }
        }
        return "";
    }

    /**
     * Gregorian format of date
     * ریخت میلادی تاریخ
     *
     * @param date
     * @return
     */
    public static String gregorianFormat(Date date) {
        if (date == null) {
            return "";
        } else {
            Calendar calendar = Calendar
                    .getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return year + "/" + (month + 1) + "/" + day;
        }
    }

    /**
     * Persian format of date
     * ریخت هجری شمسی تاریخ
     *
     * @param date
     * @return
     */
    public static String persianFormat(Date date) {
        if (date == null) {
            return "";
        } else {

            Calendar calendar = Calendar
                    .getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            PersianDate persianDate = PersianDate.fromGregorian(LocalDate.of(year, month, day));
            return persianDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        }
    }

    public static Date xMonthLater(Date date, int count) {
        long aMonth = 1000L * 60L * 60L * 24L * 30L;
        return new Date(date.getTime() + count * aMonth);
    }

    public static int compareWithToday(Date date) {
        if (DateUtils.isToday(date)) {
            return 0;
        }

        if (DateUtils.isBeforeDay(date, new Date())) {
            return -1;
        }

        if (DateUtils.isAfterDay(date, new Date())) {
            return 1;
        }

        return 0;
    }

    public static Date endOfTheDay(Date date) {
        if (date == null) {
            return null;
        }
        long oneDay = 1000 * 60 * 60 * 24;
        long time = date.getTime() + oneDay - 1;
        return new Date(time);
    }

    public static JavaDate getJavaDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar
                .getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new JavaDate(year, month, day);
    }

    public static Date getDate(JavaDate javaDate) {
        if (javaDate == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(javaDate.getYear(), javaDate.getMonth(), javaDate.getDay());
        return calendar.getTime();
    }
}
