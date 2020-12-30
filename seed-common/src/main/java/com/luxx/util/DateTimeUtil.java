package com.luxx.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;


public class DateTimeUtil {
    public static final String TIME_ZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss+0800";

    public static final long SECOND_TO_MILLS = 1000;

    public static final ZoneId zoneId = ZoneId.systemDefault();

    public static DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static DateTimeFormatter HHmmss = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static DateTimeFormatter STANDARD_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String STANDARD_DATE_TIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime getDayStart(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
    }

    public static LocalDateTime getDayEnd(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
    }

    public static LocalDateTime convertDateToLDT(Date date) {
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    public static Date convertLdtToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    public static LocalDateTime convertLongSecondToLdt(long seconds) {
        return convertLongMillsToLdt(seconds * SECOND_TO_MILLS);
    }

    public static LocalDateTime convertLongMillsToLdt(long mills) {
        return convertDateToLDT(new Date(mills));
    }

    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(zoneId).toInstant().toEpochMilli();
    }

    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(zoneId).toInstant().getEpochSecond();
    }

    public static String formatLocalDateTime(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatLocalDateTime(LocalDateTime time) {
        return time.format(STANDARD_DATE_TIME);
    }

    public static String formatLocalTime(LocalDateTime time) {
        return time.format(HHmmss);
    }

    public static String formatLocalDateTime(LocalDateTime time, DateTimeFormatter formatter) {
        return time.format(formatter);
    }

    public static String formatNow(String pattern) {
        return formatLocalDateTime(LocalDateTime.now(), pattern);
    }

    public static String formatNow(DateTimeFormatter pattern) {
        return formatLocalDateTime(LocalDateTime.now(), pattern);
    }

    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field) {
        return time.minus(number, field);
    }

    public static int formatLocalDay(LocalDateTime localDateTime) {
        return Integer.parseInt(formatLocalDateTime(localDateTime, yyyyMMdd));
    }

    public static LocalDateTime parse(String dataStr) {
        return LocalDateTime.parse(dataStr, STANDARD_DATE_TIME);
    }

    public static LocalDateTime parse(String dataStr, String sourcePattern) {
        return LocalDateTime.parse(dataStr, DateTimeFormatter.ofPattern(sourcePattern));
    }

    public static long betweenTwoTime(
            LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS)
            return period.getYears();
        if (field == ChronoUnit.MONTHS)
            return period.getYears() * 12 + period.getMonths();
        return field.between(startTime, endTime);
    }

    public static boolean compareIsOpening() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) < 9 || calendar.get(Calendar.HOUR_OF_DAY) > 17
                || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        } else {
            return false;
        }
    }
}
