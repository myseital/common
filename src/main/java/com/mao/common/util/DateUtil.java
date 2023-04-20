package com.mao.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mao.common.domain.NaturalMonth;
import com.mao.common.domain.NaturalWeek;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * 时间工具类
 *
 * @author myseital
 * @date 2022/9/6
 */
public class DateUtil {

    public static final Map<String, String> SHORT_IDS;
    static {
        Map<String, String> map = Maps.newHashMap();
        map.put("ACT", "Australia/Darwin");
        map.put("AET", "Australia/Sydney");
        map.put("AGT", "America/Argentina/Buenos_Aires");
        map.put("ART", "Africa/Cairo");
        map.put("AST", "America/Anchorage");
        map.put("BET", "America/Sao_Paulo");
        map.put("BST", "Asia/Dhaka");
        map.put("CAT", "Africa/Harare");
        map.put("CNT", "America/St_Johns");
        map.put("CST", "America/Chicago");
        map.put("CTT", "Asia/Shanghai");
        map.put("EAT", "Africa/Addis_Ababa");
        map.put("ECT", "Europe/Paris");
        map.put("IET", "America/Indiana/Indianapolis");
        map.put("IST", "Asia/Kolkata");
        map.put("JST", "Asia/Tokyo");
        map.put("MIT", "Pacific/Apia");
        map.put("NET", "Asia/Yerevan");
        map.put("NST", "Pacific/Auckland");
        map.put("PLT", "Asia/Karachi");
        map.put("PNT", "America/Phoenix");
        map.put("PRT", "America/Puerto_Rico");
        map.put("PST", "America/Los_Angeles");
        map.put("SST", "Pacific/Guadalcanal");
        map.put("VST", "Asia/Ho_Chi_Minh");
        map.put("EST", "-05:00");
        map.put("MST", "-07:00");
        map.put("HST", "-10:00");
        map.put("PDT", "-07:00");
        map.put("CDT", "-05:00");

        SHORT_IDS = Collections.unmodifiableMap(map);
    }

    /**
     * 时间 默认格式 yyyy-MM-dd HH:mm:ss
     */
    public static String PATTERN_DATE_TIME_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间ISO格式
     */
    public static String PATTERN_DATE_TIME_ISO = "yyyy-MM-dd'T'HH:mm:ss";
    /**
     * 日期 默认格式yyyy-MM-dd
     */
    public static String PATTERN_DATE_DEFAULT = "yyyy-MM-dd";
    /**
     * 月份 默认格式yyyy-MM
     */
    public static String PATTERN_MONTH_DEFAULT = "yyyy-MM";
    /**
     * 时间开始时间 默认格式yyyy-MM-dd 00:00:00
     */
    public static String PATTERN_DATE_START = "yyyy-MM-dd 00:00:00";
    /**
     * 时间结束时间 默认格式yyyy-MM-dd 23:59:59
     */
    public static String PATTERN_DATE_END = "yyyy-MM-dd 23:59:59";
    /**
     * 时区时间 默认格式yyyy-MM-dd'T'HH:mm:ssZ
     */
    public static String PATTERN_DATE_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ssZ";
    /**
     * 时间ISO8601格式 （世界标准时间格式、UTC时间格式）
     */
    public static String PATTERN_DATE_TIME_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    /**
     * 一周天数
     */
    public static Integer DAY_NUM_OF_WEEK = 7;
    /**
     * 自然周开始
     */
    public static TemporalAdjuster FIRST_OF_WEEK = TemporalAdjusters.ofDateAdjuster(localDate ->
            localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue()));
    /**
     * 自然周结束
     */
    public static TemporalAdjuster LAST_OF_WEEK = TemporalAdjusters.ofDateAdjuster(localDate ->
            localDate.plusDays(DayOfWeek.SUNDAY.getValue() - localDate.getDayOfWeek().getValue()));

    /**
     * 获取当前时间戳 精确到秒
     */
    public static Integer getCurrentSecond() {
        return new Long(System.currentTimeMillis() / 1000).intValue();
    }

    /**
     * 获取当前时间
     */
    public static Date currentDate() {
        return new Date();
    }

    /** ============= 转 String start =================================**/

    /**
     * 将 Date 类型转为字符串
     */
    public static String format(Date date) {
        return format(date, PATTERN_DATE_TIME_DEFAULT, false);
    }

    /**
     * 将 Date 类型转为字符串
     */
    public static String format(Date date, String pattern) {
        return format(date, pattern, false);
    }

    /**
     * 将 Date 类型转为字符串
     * @param date 日期对象
     * @param pattern 格式类型
     * @param zoneFormat 是否为时区格式 false 非时区格式 true 时区格式
     * @return
     */
    public static String format(Date date, String pattern, boolean zoneFormat) {
        String result = "";
        if (date != null) {
            if (zoneFormat) {
                ZonedDateTime zonedDateTime = data2ZonedDateTime(date);
                result = dateTime2Str(zonedDateTime, pattern);
            } else {
                LocalDateTime localDateTime = data2LocalDateTime(date);
                result = dateTime2Str(localDateTime, pattern);
            }
        }

        return result;
    }

    /**
     * 将 java.time.LocalDateTime 转为指定格式的时间字串
     * 将 java.time.ZonedDateTime 转为指定格式的时间字串
     */
    public static String dateTime2Str(TemporalAccessor dateTime, String pattern) {
        if (dateTime == null || pattern == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(dateTime);
    }
    /**
     * 将 java.util.Date 转为指定格式的时间字串
     * 格式必须是日期格式不能携带时间信息
     * @param pattern 默认 yyyy-MM-dd
     */
    public static String date2Str(Date date, String pattern,String zoneId) throws IllegalFormatException {
        if (date == null || pattern == null) {
            return "";
        }
        LocalDateTime localDate = date.toInstant().atZone(ZoneId.of(zoneId)).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(localDate);
    }
    /**
     * 将 java.time.LocalDate 转为指定格式的时间字串
     * 格式必须是日期格式不能携带时间信息
     * @param pattern 默认 yyyy-MM-dd
     */
    public static String localDate2Str(LocalDate localDate, String pattern) throws IllegalFormatException {
        if (localDate == null || pattern == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            return formatter.format(localDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("LocalDate format error! pattern: " + pattern, e);
        }
    }
    /**
     * 时间戳转日期格式字符串
     *
     * @param timestamp milliseconds
     */
    public static String timestamp2Str(Long timestamp, String pattern) {
        return timestamp2Str(timestamp, pattern, false);
    }

    /**
     * 时间戳转日期格式字符串
     *
     * @param timestamp milliseconds
     * @param zoneFormat 是否为时区格式 false 非时区格式 true 时区格式
     * @return
     */
    public static String timestamp2Str(Long timestamp, String pattern, boolean zoneFormat) {
        String result = "";
        if (timestamp == null || pattern == null) {
            return result;
        }
        if (zoneFormat) {
            ZonedDateTime zonedDateTime = timestamp2ZonedDateTime(timestamp);
            result = dateTime2Str(zonedDateTime, pattern);
        } else {
            LocalDateTime localDateTime = timestamp2LocalDateTime(timestamp);
            result = dateTime2Str(localDateTime, pattern);
        }

        return result;
    }

    /**
     * 传入Data类型日期，返回字符串类型时间（ISO8601标准时间）
     *
     * @param date
     * @return
     */
    public static String formatISO8601(Date date) {
        ZonedDateTime zonedDateTime = data2ZonedDateTime(date);
        ZonedDateTime utcDate = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        return dateTime2Str(utcDate, PATTERN_DATE_TIME_ISO8601);
    }

    /** ============= 转 String end ====================================**/

    /** ============= 转 ZonedDateTime LocalDateTime start ==================**/
    /**
     * 将 Date 转为 LocalDateTime
     */
    private static LocalDateTime data2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }

    /**
     * 将 Date 转为 ZonedDateTime
     */
    private static ZonedDateTime data2ZonedDateTime(Date date) {
        ZoneId id = ZoneId.systemDefault();
        return ZonedDateTime.ofInstant(date.toInstant(), id);
    }

    /**
     * 将时间字串转为 LocalDateTime，时间字串的格式请用 pattern 指定
     */
    public static LocalDateTime str2LocalDateTime(String datetimeStr, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

        return LocalDateTime.parse(datetimeStr, dateTimeFormatter);
    }

    /**
     * 将时间字串转为 LocalDate，时间字串的格式请用 pattern 指定
     */
    public static LocalDate str2LocalDate(String dateStr, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

        return LocalDate.parse(dateStr, dateTimeFormatter);
    }

    /**
     * 将时间字串转为 LocalDate，时间字串的格式请用 pattern 指定
     */
    public static LocalDate localDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param timestamp milliseconds
     */
    public static LocalDateTime timestamp2LocalDateTime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 时间戳转ZonedDateTime
     *
     * @param timestamp milliseconds
     */
    public static ZonedDateTime timestamp2ZonedDateTime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return ZonedDateTime.ofInstant(instant, zone);
    }

    /**
     * 获取月份 默认 yyyy-MM
     */
    public static YearMonth getYearMonth(String month) {
        return YearMonth.parse(month, DateTimeFormatter.ofPattern(PATTERN_MONTH_DEFAULT));
    }

    /**
     * 获取月份
     */
    public static YearMonth getYearMonth(String month, String pattern) {
        return YearMonth.parse(month, DateTimeFormatter.ofPattern(pattern));
    }

    /** ============= 转 ZonedDateTime LocalDateTime end ===========================**/

    /** ============= 转 Date Start ===========================**/

    /**
     * 时间字符串转date  默认 yyyy-MM-dd HH:mm:ss
     * @param datetimeStr
     * @return
     */
    public static Date datetimeStr2Date(String datetimeStr) {
        return datetimeStr2Date(datetimeStr, PATTERN_DATE_TIME_DEFAULT);
    }

    /**
     * 时间字符串转date
     * @param datetimeStr
     * @param pattern 默认 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date datetimeStr2Date(String datetimeStr, String pattern) {
        if (null == pattern || pattern.isEmpty()) {
            pattern = PATTERN_DATE_TIME_DEFAULT;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = str2LocalDateTime(datetimeStr, pattern);
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 时间字符串转date
     * @param datetimeStr
     * @param pattern 默认 yyyy-MM-dd HH:mm:ss
     * @param zoneId 时区
     * @return
     */
    public static Date datetimeStr2Date(String datetimeStr, String pattern, ZoneId zoneId) {
        if (null == pattern || pattern.isEmpty()) {
            pattern = PATTERN_DATE_TIME_DEFAULT;
        }
        LocalDateTime localDateTime = str2LocalDateTime(datetimeStr, pattern);
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 日期字符串转date
     * @param dateStr
     * @param pattern 默认 yyyy-MM-dd
     * @return
     */
    public static Date dateStr2Date(String dateStr, String pattern) {
        if (null == pattern || pattern.isEmpty()) {
            pattern = PATTERN_DATE_DEFAULT;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = str2LocalDate(dateStr, pattern);
        ZonedDateTime zdt = localDate.atStartOfDay().atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 日期字符串转date 默认 yyyy-MM-dd
     * @param dateStr
     * @return
     */
    public static Date dateStr2Date(String dateStr) {
        return dateStr2Date(dateStr, PATTERN_DATE_DEFAULT);
    }

    /**
     * offsetDateTime字符串 转 Date
     */
    public static Date offsetDateTimeStr2Date(String offsetDateTimeStr) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(offsetDateTimeStr);
        LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
        ZonedDateTime zonedDateTime = localDateTime.atZone(offsetDateTime.getOffset());
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }
    /** ============= 转 Date end ==============================**/


    /**
     * 将 Date 转为 OffsetDateTime
     */
    public static OffsetDateTime parseOffsetDateTime(Date date){
        LocalDateTime localDateTime = Instant.ofEpochSecond(date.getTime()/1000)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return OffsetDateTime.of(localDateTime, getOFFSET_ID());
    }

    /**
     * 获取系统时区
     */
    public static ZoneOffset getOFFSET_ID(){
        return LocalDateTime.now(ZoneOffset.systemDefault()).atZone(ZoneOffset.systemDefault()).getOffset();
    }


    /**
     * 获取今日起始时间
     *
     * @return
     */
    public static Date getTodayOfStart() {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = currentDate().toInstant();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        ZonedDateTime zdt = localDate.atStartOfDay().atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 获取今日结束时间
     *
     * @return
     */
    public static Date getTodayOfEnd() {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = currentDate().toInstant();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        LocalTime localTime = LocalTime.of(23, 59, 59);
        ZonedDateTime zdt = localDate.atTime(localTime).atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /** ============= 日期计算 start ==============================**/

    /**
     * 获取当前时间几天前的时间
     *
     * @param day
     * @return
     */
    public static Date getNowBefore(int day) {
        return getDateBefore(currentDate(), day);
    }

    /**
     * 获取指定时间几天前的时间
     * @param date 时间
     * @param day 相差天数
     */
    public static Date getDateBefore(Date date, int day) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        LocalDateTime beforeDayTime = localDateTime.minusDays(day);
        ZonedDateTime zdt = beforeDayTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 获取指定时间几天后的时间
     * @param date 时间
     * @param day 相差天数
     */
    public static Date getDateAfter(Date date, int day) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        LocalDateTime beforeDayTime = localDateTime.plusDays(day);
        ZonedDateTime zdt = beforeDayTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 获取当前时间几分钟前的时间
     *
     * @param minutes
     * @return
     */
    public static Date getNowBeforeMinutes(int minutes) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = currentDate().toInstant();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        LocalDateTime beforeDayTime = localDateTime.minusMinutes(minutes);
        ZonedDateTime zdt = beforeDayTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }
    /**
     * 计算两个日期相差分钟
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static Long getMinuteDuration(Date beginTime, Date endTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(beginTime.toInstant(), zoneId);
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endTime.toInstant(), zoneId);
        Duration duration = Duration.between(startLocalDateTime, endLocalDateTime);
        return duration.toMinutes();
    }

    /**
     * 计算两个日期相差小时
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static Long getHourDuration(Date beginTime, Date endTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(beginTime.toInstant(), zoneId);
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endTime.toInstant(), zoneId);
        Duration duration = Duration.between(startLocalDateTime, endLocalDateTime);
        return duration.toHours();
    }

    /**
     * 计算两个日期相差天数
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static Long getDayDistance(Date beginTime, Date endTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(beginTime.toInstant(), zoneId);
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endTime.toInstant(), zoneId);
        Duration duration = Duration.between(startLocalDateTime, endLocalDateTime);
        return duration.toDays();
    }

    /**
     * 获取时间间隔总天数
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static Long getTotalDay(Date beginTime, Date endTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(beginTime.toInstant(), zoneId);
        LocalDateTime endLocalDateTime = LocalDateTime.ofInstant(endTime.toInstant(), zoneId);
        Duration duration = Duration.between(startLocalDateTime, endLocalDateTime);
        return duration.toDays() + 1;
    }
    public static List<String> getDatesInRange(String startDateStr,String endDateStr){
        List<String> dateStrList = new ArrayList<>();
        Date startDate = DateUtil.dateStr2Date(startDateStr,DateUtil.PATTERN_DATE_DEFAULT);
        Date endDate = DateUtil.dateStr2Date(endDateStr,DateUtil.PATTERN_DATE_DEFAULT);
        if(startDate.after(endDate)){
            return null;
        }
        dateStrList.add(startDateStr);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        while (endDate.after(startCalendar.getTime())) {
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            dateStrList.add(DateUtil.format(startCalendar.getTime(),DateUtil.PATTERN_DATE_DEFAULT));
        }
        return dateStrList;
    }
    public static List<String> getDatesInRange(Date startDate,Date endDate,String pattern){
        List<String> dateStrList = new ArrayList<>();
        if(startDate.after(endDate)){
            return null;
        }
        dateStrList.add(DateUtil.format(startDate,pattern));
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        while (endDate.after(startCalendar.getTime())) {
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            dateStrList.add(DateUtil.format(startCalendar.getTime(),pattern));
        }
        return dateStrList;
    }
    /**
     * 根据开始时间跟结束时间获取自然周列表
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 自然周列表
     */
    public static List<NaturalWeek> getNaturalWeek(LocalDate startDate, LocalDate endDate) {
        return getNaturalWeek(startDate, endDate, "——");
    }

    /**
     * 根据开始时间跟结束时间获取自然周列表
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param separator 分隔符
     * @return 开始时间到结束时间之间自然周列表
     */
    public static List<NaturalWeek> getNaturalWeek(LocalDate startDate, LocalDate endDate, String separator) {
        List<NaturalWeek> naturalWeekList = new ArrayList<>();
        if (Objects.isNull(startDate) || Objects.isNull(endDate) || startDate.isAfter(endDate)) {
           throw new IllegalArgumentException("参数异常导致获取自然周数据失败");
        }
        // 开始周开始日期
        LocalDate startFirstWeek = startDate.with(FIRST_OF_WEEK);
        // 开始周结束日期
        LocalDate endFirstWeek = startDate.with(LAST_OF_WEEK);
        // 结束周
        LocalDate startLastWeek = endDate.with(FIRST_OF_WEEK);
        //将第一周添加
        if (startFirstWeek.with(LAST_OF_WEEK).equals(startLastWeek.with(LAST_OF_WEEK))) {
            naturalWeekList.add(new NaturalWeek(startDate, endDate, separator));
            return naturalWeekList;
        }
        naturalWeekList.add(new NaturalWeek(startDate, endFirstWeek, separator));
        while (true) {
            startFirstWeek = startFirstWeek.plusDays(DAY_NUM_OF_WEEK);
            if (startFirstWeek.with(LAST_OF_WEEK).equals(startLastWeek.with(LAST_OF_WEEK))) {
                break;
            } else {
                naturalWeekList.add(new NaturalWeek(startFirstWeek.with(FIRST_OF_WEEK), startFirstWeek.with(LAST_OF_WEEK), separator));
            }
        }

        naturalWeekList.add(new NaturalWeek(startLastWeek, endDate, separator));

        return naturalWeekList;
    }

    /**
     * 自然月计算
     * @param startDate 开始时间
     * @param endDate 结束时间
     */
    public static List<NaturalMonth> getNaturalMonth(LocalDate startDate, LocalDate endDate) {
        List<NaturalMonth> monthList = Lists.newArrayList();
        // 开始月的第一天
        LocalDate startFirstMonth = startDate.with(TemporalAdjusters.firstDayOfMonth());
        // 开始月的最后一天
        LocalDate endFirstMonth = startDate.with(TemporalAdjusters.lastDayOfMonth());
        // 结束月的第一天
        LocalDate startLastMonth = endDate.with(TemporalAdjusters.firstDayOfMonth());
        if (startFirstMonth.equals(startLastMonth)) {
            NaturalMonth naturalMonth = new NaturalMonth(startDate, endDate);
            monthList.add(naturalMonth);
            return monthList;
        }
        monthList.add(new NaturalMonth(startDate, endFirstMonth));
        while (true) {
            startFirstMonth = startFirstMonth.plusMonths(1);
            if (startFirstMonth.with(TemporalAdjusters.firstDayOfMonth())
                    .equals(startLastMonth) || startLastMonth.isBefore(startFirstMonth)) {
                break;
            } else {
                monthList.add(new NaturalMonth(startFirstMonth.with(TemporalAdjusters.firstDayOfMonth()),
                        startFirstMonth.with(TemporalAdjusters.lastDayOfMonth())));
            }
        }

        monthList.add(new NaturalMonth(startLastMonth, endDate));

        return monthList;
    }


    /** ============= 日期计算 end ==============================**/
}
