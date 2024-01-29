package com.luxx.seed.util;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
public class DateTimeUtil {
    private static final DateTimeFormatter YYYY_MM = DateTimeFormat.forPattern("yyyy_MM");
    public static final DateTimeFormatter YMD_HMS_SSS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter YMD_HMS_Z = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    public static final DateTimeFormatter YMD_HMS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter YMD_H = DateTimeFormat.forPattern("yyyy-MM-dd HH");
    public static final DateTimeFormatter YMD_HM = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter YMD = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final DateTimeFormatter YMDHMS = DateTimeFormat.forPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter YMDHMSF = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");

    public static String currentYM() {
        return YYYY_MM.print(DateTime.now());
    }

    public static String getCurrentTimeForFormat(DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.print(DateTime.now());
    }

    public static String getPastTimeForFormat(int pastDay, DateTimeFormatter dateTimeFormatter) {
        DateTime now = new DateTime();
        return dateTimeFormatter.print(now.minusDays(pastDay));
    }

    public static String getTimeString(String format, long time) {
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(format);
        return timeFormatter.print(time);
    }

    public static Date getTime(String format, String time) {
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(format);
        return timeFormatter.parseDateTime(time).toDate();
    }

    public static String getEsString(long timestamp) {
        return YMD_HMS_Z.print(timestamp);
    }

    public static String getDateTimeString(long timestamp) {
        return YMDHMS.print(timestamp);
    }

    public static DateTime convertToDateTime(String timestamp) {
        return YMD_HMS_SSS.parseDateTime(timestamp);
    }

    public static String getToday() {
        return YMD.print(new DateTime());
    }

    public static String getYesterday() {
        DateTime now = new DateTime();
        return YMD_HMS.print(now.minusDays(1));
    }

    public static String getMonthStart() {
        DateTime now = new DateTime();
        return YMD.print(now.dayOfMonth().withMinimumValue()) + " 00:00:00";
    }

    public static String getMonthEnd() {
        DateTime now = new DateTime();
        return YMD.print(now.dayOfMonth().withMaximumValue()) + " 23:59:59";
    }

    public static String getWeekStart() {
        DateTime now = new DateTime();
        return YMD.print(now.dayOfWeek().withMinimumValue()) + " 00:00:00";
    }

    public static String getWeekEnd() {
        DateTime now = new DateTime();
        return YMD.print(now.dayOfWeek().withMaximumValue()) + " 23:59:59";
    }

    public static String getFormatDateTime(long timestamp, DateTimeFormatter format) {
        Date date = new Date();
        date.setTime(timestamp);
        return format.print(timestamp);
    }

    public static Date getDateFromTimeStr(String timeStr) {
        return YMD_HMS.parseDateTime(timeStr).toDate();
    }

    public static long convertDateTimeStr(String timeStr) {
        return YMD_HMS_SSS.parseDateTime(timeStr).toDate().getTime();
    }

    public static long getSecondsForHourTime(int hourTime, int minuteTime, int secondTime) {
        return hourTime * 60L * 60L + minuteTime * 60L + (long) secondTime;
    }

    public static Date getMinTimestampByAmtToSub(LocalDate now, long amountToSubtract) {
        return Date.from(LocalDateTime.of(now.minus(amountToSubtract, ChronoUnit.DAYS), LocalTime.MIN)
                .toInstant(ZoneOffset.ofHours(8)));
    }

    public static Date getMaxTimestampByAmtToSub(LocalDate now, long amountToSubtract) {
        return Date.from(LocalDateTime.of(now.minus(amountToSubtract, ChronoUnit.DAYS), LocalTime.MAX)
                .toInstant(ZoneOffset.ofHours(8)));
    }

    /**
     * 根据日期获取当天是周几,周日为1，其他类推
     *
     * @param datetime 日期
     * @return 周几
     */
    public static int dateToWeek(Date datetime) {
        DateTime time = new DateTime(datetime);
        return time.getDayOfWeek();
    }

    /**
     * 返回当前几号
     *
     * @param datetime
     * @return
     */
    public static int dateToDay(Date datetime) {
        DateTime time = new DateTime(datetime);
        return time.getDayOfMonth();
    }

}
