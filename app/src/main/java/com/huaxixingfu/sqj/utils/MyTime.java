package com.huaxixingfu.sqj.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MyTime {


    // 06-12 08:32:33

    public static String geTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmm");
        return formate.format(date);
    }

    public static String geTime_HHmmss() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("HHmmss");
        return formate.format(date);
    }

    public static String geTime_yyyyMMdd() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
        return formate.format(date);
    }

    public static String geTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat formate = new SimpleDateFormat("MM-dd HH:mm:ss");
        return formate.format(date);
    }

    public static String getTime_FileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
        return formate.format(date);
    }

    public static String getTime_() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formate.format(date);
    }

    public static String getTime_current() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return formate.format(date);
    }

    public static Date parseServerTime(String serverTime, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINESE);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        Date date = null;
        try {
            date = sdf.parse(serverTime);
        } catch (Exception e) {
//            Timber.e(e, "");
            e.printStackTrace();
        }
        return date;
    }

    public static String strToDate2(String strDate) {
//        DateFormat.parse(strDate);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = formatter.parse(strDate);
            return formatter2.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 日期格式字符串转换时间戳
     *
     * @param date
     * @param format
     * @return
     */
    public static String date2TimeStamp(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String date2TimeStampYMDHMS(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            return String.valueOf(sdf.parse(date).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String date2TimeStampYMDH(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return String.valueOf(sdf.parse(date).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getTime_1(long time) {
        Date date = new Date(time);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return formate.format(date);
    }


    public static String getTime_(long time) {
        Date date = new Date(time);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formate.format(date);
    }

    public static String getTime1(long time) {
        Date date = new Date(time);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy年MM月dd日");
        return formate.format(date);
    }

    public static String getTime1() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("yyyy年MM月");
        return formate.format(date);
    }

    public static String getTimeYYYYMMDD() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("yyyy年MM月dd日");
        return formate.format(date);
    }

    public static String getTime2() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("MM-dd");
        return formate.format(date);
    }


    public static String getTime_2(long time) {
        Date date = new Date(time);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy/MM/dd");
        return formate.format(date);
    }

    public static String getTime_3(long time) {
        Date date = new Date(time);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
        return formate.format(date);
    }

    public static String getTime_yyyyMMdd(long time) {
        Date date = new Date(time);
        SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
        return formate.format(date);
    }

    public static String getTime_yyyyMMdd() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
        return formate.format(date);
    }

    public static String getTime_yyyyMMddHHmmssSSS() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return formate.format(date);
    }

    public static String getTime_yyyyMM(long time) {
        Date date = new Date(time);
        SimpleDateFormat formate = new SimpleDateFormat("yyyyMM");
        return formate.format(date);
    }

    public static String getTime_HHmm() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("HH:mm");
        return formate.format(date);
    }
    public static String getTime_HHmmss() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("HH:mm:ss");
        return formate.format(date);
    }

    public static String getTime_yyyy_MM_dd(long time) {
        Date date = new Date(time);
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
        return formate.format(date);
    }

    public static String strToDate(String strDate) {
//        DateFormat.parse(strDate);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = formatter.parse(strDate);
            return formatter2.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式类型和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date;
        if (TextUtils.isEmpty(strTime)) {
            date = new Date(System.currentTimeMillis());
        } else {
            date = stringToDate(strTime, formatType); // String类型转成date类型
        }
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static long StringToLong2(String strTime, String formatType) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        //设置要读取的时间字符串格式
        Long time;
        Date date = null;
        try {
            date = format.parse(strTime);

            //转换为Date类
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            time = stringToLong(strTime, formatType);
        }
        return time;
    }

    public static String parseDate(String dateStr) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        Date result;
        result = df.parse(dateStr);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(result);
    }


    public static String parseDateCouponTime(String oldDate) {
        Date date1 = null;
        DateFormat df2 = null;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = df.parse(oldDate);
            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            date1 = df1.parse(date.toString());
            df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {

            e.printStackTrace();
        }

        return df2.format(date1);
    }

    // string类型转换为date类型
// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
// HH时mm分ss秒，
// strTime的时间格式类型必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }


    /**
     * 返回1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六
     *
     * @param
     * @return
     */

    public static int getDayofweek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 判断time是否在from，to之内/后
     *
     * @param time   指定日期
     * @param 、、from 开始日期
     * @param to     结束日期
     * @return
     */
    public static boolean belongCalendar(Date time, /*Date from,*/ Date to) {
        Calendar date = Calendar.getInstance();
        date.setTime(time);
/*
        Calendar after = Calendar.getInstance();
        after.setTime(from);*/

        Calendar before = Calendar.getInstance();
        before.setTime(to);

//        if (date.after(after) && date.before(before)) {
        if (date.after(before)/* && date.before(before)*/) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断time是否在from，to之内/后
     *
     * @param time 指定日期
     * @param from 开始日期
     * @param to   结束日期
     * @return
     */
    public static boolean belongCalendar(Date time, Date from, Date to) {
        Calendar date = Calendar.getInstance();
        date.setTime(time);
        Calendar after = Calendar.getInstance();
        after.setTime(from);
        Calendar before = Calendar.getInstance();
        before.setTime(to);
        if (date.after(after) && date.before(before)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断time是否在from后
     *
     * @param time 指定日期
     * @param from 开始日期
     * @return
     */
    public static boolean afterCalendar(Date time, Date from) {
        Calendar date = Calendar.getInstance();
        date.setTime(time);
        Calendar after = Calendar.getInstance();
        after.setTime(from);
        if (date.after(after)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取前n天日期、后n天日期 by ym
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    /**
     * 判断时间格式 格式必须为“YYYY-MM-dd”
     * 2004-2-30 是无效的
     * 2003-2-29 是无效的
     *
     * @param sDate
     * @return
     */
    public static boolean isLegalDate(String sDate) {
        int legalLen = 10;
        if ((sDate == null) || (sDate.length() != legalLen)) {
            return false;
        }

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

}
