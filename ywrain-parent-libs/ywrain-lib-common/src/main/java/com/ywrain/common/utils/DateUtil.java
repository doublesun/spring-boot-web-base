package com.ywrain.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * 日期时间工具类
 * <br> 使用JDK8的{@link java.time}包下的新日期时间类封装
 * <br> 格式化日期时间字符串使用线程安全的{@link DateTimeFormatter}
 *
 * @author weipengfei@youcheyihou.com
 * @since 1.2.0
 */
public class DateUtil extends DateUtils {

    /**
     * 日期格式化规则(yyyy-MM-dd)
     */
    public final static String DefaultDateFormatPattern = "yyyy-MM-dd";
    /**
     * 日期格式化规则类(yyyy-MM-dd)
     */
    public final static DateTimeFormatter DefaultDateFormat = DateTimeFormatter.ofPattern(DefaultDateFormatPattern);
    /**
     * 日期格式化规则类(yyyy-MM)
     */
    public final static DateTimeFormatter DefaultMonthFormat = DateTimeFormatter.ofPattern("yyyy-MM");
    /**
     * 时间格式化规则(HH:mm:ss)
     */
    public final static String DefaultTimeFormatPattern = "HH:mm:ss";
    /**
     * 时间格式化规则类(HH:mm:ss)
     */
    public final static DateTimeFormatter DefaultTimeFormat = DateTimeFormatter.ofPattern(DefaultTimeFormatPattern);
    /**
     * 完整日期格式话规则(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DefaultDateTimeFormatPattern = DefaultDateFormatPattern + " " + DefaultTimeFormatPattern;
    /**
     * 完整日期格式化类(yyyy-MM-dd HH:mm:ss)
     */
    public final static DateTimeFormatter DefaultDateTimeFormat = DateTimeFormatter.ofPattern(DefaultDateTimeFormatPattern);
    /**
     * 完整日期格式话规则(yyyy-MM-dd HH:mm:ss.SSS)
     */
    public final static String DefaultDateTimeFormatSssPattern = DefaultDateFormatPattern + " " + DefaultTimeFormatPattern + ".SSS";
    /**
     * 完整日期格式化类,带毫秒(yyyy-MM-dd HH:mm:ss.SSS)
     */
    public final static DateTimeFormatter DefaultDateTimeSssFormat = DateTimeFormatter.ofPattern(DefaultDateTimeFormatSssPattern);

    /**
     * 默认时区，和当前系统时区一致
     */
    public final static ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    private enum ModifyType {
        /**
         * Truncation.
         */
        TRUNCATE,

        /**
         * Rounding.
         */
        ROUND,

        /**
         * Ceiling.
         */
        CEILING
    }

    /**
     * 获取当前系统unix时间戳
     *
     * @return unix时间戳 {@link long}
     */
    public static long getCurrentUnixtime() {
        return Instant.now().getEpochSecond();
    }

    /**
     * 获取当前日期时间戳对象
     *
     * @return 日期时间戳对象 {@link java.util.Date}
     */
    public static Date getCurrentTime() {
        return new Date();
    }

    /**
     * 获取指定时间
     *
     * @return 日期时间戳对象 {@link java.util.Date}
     */
    public static Date getDateTime(int year, int month, int day) {
        LocalDateTime time = LocalDateTime.of(year, month, day, 0, 0);
        return toDatetime(time);
    }

    /**
     * 获取指定时间
     *
     * @return 日期时间戳对象 {@link java.util.Date}
     */
    public static Date getDateTime(int year, int month, int day, int hour, int minute, int second) {
        LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute, second);
        return toDatetime(time);
    }

    /**
     * 获取今天0点的时间
     *
     * @return 日期时间对象
     */
    public static Date getTodayZeroDatetime() {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime newTime = time.withHour(0).withMinute(0).withSecond(0).withNano(0);
        return toDatetime(newTime);
    }

    /**
     * 获取当前日期对应前一天的0点
     *
     * @return 日期对象
     */
    public static Date getYesterdayZeroDatetime() {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime newTime = time.withHour(0).withMinute(0).withSecond(0).withNano(0).minusDays(1);
        return toDatetime(newTime);
    }

    /**
     * 获取当前日期对应下一天的0点
     *
     * @return 日期对象
     */
    public static Date getTomorrowZeroDatetime() {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime newTime = time.withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(1);
        return toDatetime(newTime);
    }

    /**
     * 获取指定日期对应的0点
     *
     * @return 日期对象
     */
    public static Date getZeroDatetime(Date date) {
        LocalDateTime time = toLocalDateTime(date);
        LocalDateTime newTime = time.withHour(0).withMinute(0).withSecond(0).withNano(0);
        return toDatetime(newTime);
    }

    /**
     * 获取今日对应的当天最后一刻
     *
     * @return 日期对象
     */
    public static Date getDayLastDatetime() {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime newTime = time.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        return toDatetime(newTime);
    }

    /**
     * 获取指定日期对应的当天最后一刻
     *
     * @return 日期对象
     */
    public static Date getDayLastDatetime(Date date) {
        LocalDateTime time = toLocalDateTime(date);
        LocalDateTime newTime = time.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        return toDatetime(newTime);
    }

    /**
     * 获取指定日期区间的随即一个时间对象
     *
     * @param minDate 区间最小时间对象
     * @param maxDate 区间最大时间对象
     * @return 随机的时间对象
     */
    public static Date getRandomDatetime(Date minDate, Date maxDate) {
        long minUnixtime = minDate.getTime();
        long maxUnixtime = maxDate.getTime();
        long utime = RandomUtil.nextLong(minUnixtime, maxUnixtime);
        return new Date(utime);
    }

    /**
     * 获取日期的星期
     *
     * @param date 日期对象
     * @return 自然语义下星期几，如：星期一为1，星期天为7
     */
    public static int getWeek(Date date) {
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), DateUtil.DEFAULT_ZONE_ID);
        return ldt.getDayOfWeek().getValue();
    }

    /**
     * 计算2个日期时间之间相差的秒数
     *
     * @return 秒数
     */
    public static long calcDiffSeconds(Date date1, Date date2) {
        return Math.abs(date1.getTime() / 1000 - date2.getTime() / 1000);
    }

    /**
     * 计算2个日期时间之间相差的天数
     *
     * @param date1 日期对象
     * @param date2 日期对象
     * @return 天数
     */
    public static int calcDiffDays(Date date1, Date date2) {
        LocalDateTime nowDateTime = LocalDateTime.ofInstant(date1.toInstant(), DEFAULT_ZONE_ID);
        LocalDateTime thirdDateTime = LocalDateTime.ofInstant(date2.toInstant(), DEFAULT_ZONE_ID);
        Long between = ChronoUnit.DAYS.between(nowDateTime, thirdDateTime);
        return Math.abs(between.intValue());
    }

    public static Date addDays(final Date date, final int amount) {
        LocalDateTime time = toLocalDateTime(date);
        return toDatetime(time.plusDays(amount));
    }

    public static Date addHours(final Date date, final int amount) {
        LocalDateTime time = toLocalDateTime(date);
        return toDatetime(time.plusHours(amount));
    }

    public static Date addMinutes(final Date date, final int amount) {
        LocalDateTime time = toLocalDateTime(date);
        return toDatetime(time.plusMinutes(amount));
    }

    public static Date addSeconds(final Date date, final int amount) {
        LocalDateTime time = toLocalDateTime(date);
        return toDatetime(time.plusSeconds(amount));
    }

    public static Date addWeeks(final Date date, final int amount) {
        LocalDateTime time = toLocalDateTime(date);
        return toDatetime(time.plusWeeks(amount));
    }

    public static Date addMonths(final Date date, final int amount) {
        LocalDateTime time = toLocalDateTime(date);
        return toDatetime(time.plusMonths(amount));
    }

    public static Date addYears(final Date date, final int amount) {
        LocalDateTime time = toLocalDateTime(date);
        return toDatetime(time.plusYears(amount));
    }

    public static boolean isBefore(Date date) {
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(toLocalDateTime(date));
    }

    public static boolean isBefore(Date date1, Date date2) {
        return toLocalDateTime(date1).isBefore(toLocalDateTime(date2));
    }

    public static boolean isEqual(Date date) {
        LocalDateTime now = LocalDateTime.now();
        return now.isEqual(toLocalDateTime(date));
    }

    public static boolean isEqual(Date date1, Date date2) {
        return toLocalDateTime(date1).isEqual(toLocalDateTime(date2));
    }

    /**
     * 根据毫秒时间戳，获取日期时间对象
     *
     * @param milliTimestamp 毫秒unix时间戳
     * @return 日期时间对象
     */
    public static Date toDatetime(long milliUnixtime) {
        return new Date(milliUnixtime);
    }

    /**
     * 将新日期是时间对象{@link LocalDateTime}转换为{@link Date}
     *
     * @param localDateTime 新的日期时间类
     * @return 日期时间Date
     */
    public static Date toDatetime(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(DEFAULT_ZONE_ID).toInstant();
        return Date.from(instant);
    }

    /**
     * 将新日期是时间对象{@link LocalDateTime}转换为{@link Date}
     *
     * @param localDate 新的日期对象
     * @return 日期时间Date
     */
    public static Date toDatetime(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(DEFAULT_ZONE_ID).toInstant();
        return Date.from(instant);
    }

    /**
     * 将新日期是时间对象{@link LocalDateTime}转换为{@link Date}
     *
     * @param localDate 新的日期时间类
     * @param localTime 新的日期时间类
     * @return 日期时间Date
     */
    public static Date toDatetime(LocalDate localDate, LocalTime localTime) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return toDatetime(localDateTime);
    }

    /**
     * 将旧日期是时间对象{@link Date}转换为{@link LocalDateTime}
     *
     * @param date 日期时间
     * @return 新的日期时间
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, DEFAULT_ZONE_ID);
    }

    /**
     * 格式化日期对象转换为字符串
     * <br> 使用默认的完整日期格式解析 {@link #DefaultDateTimeFormat}
     *
     * @param date 日期时间
     * @return 格式化字符串
     */
    public static String formatToString(Date date) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        return localDateTime.format(DefaultDateTimeFormat);
    }

    /**
     * 格式化日期对象转换为字符串
     *
     * @param formatter 格式化规则对象
     * @param date 日期时间
     * @return 字符串
     */
    public static String formatToString(DateTimeFormatter formatter, Date date) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        return localDateTime.format(formatter);
    }

    /**
     * 格式化日期对象转换为字符串
     *
     * @param formatterPattern 格式化类型规则
     * @param date 日期时间
     * @return 字符串
     */
    public static String formatToString(String formatterPattern, Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterPattern);
        LocalDateTime dateTime = toLocalDateTime(date);
        return dateTime.format(formatter);
    }

    /**
     * 解析格式化日期字符串，转换为日期对象
     * <br> 默认支持日期字符串格式："2010-05-12"
     *
     * @param formatterPattern 格式化类型规则
     * @param textDatetime 字符串
     * @return 日期对象
     */
    public static Date parseToDate(String textDatetime) {
        LocalDate localDate = LocalDate.parse(textDatetime);
        return toDatetime(localDate);
    }

    /**
     * 解析格式化日期字符串，转换为日期对象
     * <pre>
     *     仅支持字符串是日期类型，以下格式示例：
     *     "yyyyMMdd"
     *     "yyyy-MM-dd"
     *     ...
     *     "dd MM yyyy"
     *     "uuuu-MM-dd"
     * </pre>
     *
     * @param formatterPattern 格式化类型规则
     * @param textDatetime 字符串
     * @return 日期对象，时间字段补0
     */
    public static Date parseToDate(String formatterPattern, String textDatetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterPattern);
        LocalDate localDate = LocalDate.parse(textDatetime, formatter);
        return toDatetime(localDate);
    }

    /**
     * 解析格式化日期字符串，转换为日期对象
     * <br> 使用默认的完整日期格式解析 {@link #DefaultDateTimeFormat}
     *
     * @param textDatetime 字符串
     * @return 日期对象
     */
    public static Date parseToDatetime(String textDatetime) {
        LocalDateTime localDateTime = LocalDateTime.from(DefaultDateTimeFormat.parse(textDatetime));
        return toDatetime(localDateTime);
    }

    /**
     * 解析格式化日期时间字符串，转换为日期对象
     * <pre>
     *     不支持仅有日期或仅有时间的的字符串解析，如：
     *     "2019-04-12"
     *     "10:37:51"
     *     "10:37:51.986"
     *     ...
     * </pre>
     *
     * @param formatterPattern 格式化类型规则
     * @param textDatetime 字符串
     * @return 日期对象
     */
    public static Date parseToDatetime(String formatterPattern, String textDatetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterPattern);
        return parseToDatetime(formatter, textDatetime);
    }

    /**
     * 解析格式化日期时间字符串，转换为日期对象
     * <pre>
     *     日期时间字符串不支持以下格式：
     *     "2019-04-12"
     *     "10:37:51"
     *     "10:37:51.986"
     * </pre>
     *
     * @param formatter 格式化规则对象
     * @param textDatetime 字符串
     * @return 日期对象
     */
    public static Date parseToDatetime(DateTimeFormatter formatter, String textDatetime) {
        LocalDateTime localDateTime = LocalDateTime.parse(textDatetime, formatter);
        return toDatetime(localDateTime);
    }

}
