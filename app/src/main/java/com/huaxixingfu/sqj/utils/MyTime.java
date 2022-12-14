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

    public static String geTime_yyyyMMdd(Date date) {
        if(date == null){
            date = new Date(System.currentTimeMillis());
        }
        SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
        return formate.format(date);
    }


    public static String geTime_yyyyMMddString(String string)  {
        try {
            SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");

            SimpleDateFormat formateResult = new SimpleDateFormat("yyyy-MM-dd");
            return formateResult.format(formate.parse(string));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";

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
     * ????????????????????????????????????
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
        SimpleDateFormat formate = new SimpleDateFormat("yyyy???MM???dd???");
        return formate.format(date);
    }

    public static String getTime1() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("yyyy???MM???");
        return formate.format(date);
    }

    public static String getTimeYYYYMMDD() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formate = new SimpleDateFormat("yyyy???MM???dd???");
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

    // string???????????????long??????
    // strTime????????????String???????????????
    // formatType????????????
    // strTime????????????????????????formatType???????????????????????????
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date;
        if (TextUtils.isEmpty(strTime)) {
            date = new Date(System.currentTimeMillis());
        } else {
            date = stringToDate(strTime, formatType); // String????????????date??????
        }
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date????????????long??????
            return currentTime;
        }
    }

    public static long StringToLong2(String strTime, String formatType) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        //???????????????????????????????????????
        Long time;
        Date date = null;
        try {
            date = format.parse(strTime);

            //?????????Date???
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

    // string???????????????date??????
// strTime????????????string??????????????????formatType??????????????????yyyy-MM-dd HH:mm:ss//yyyy???MM???dd???
// HH???mm???ss??????
// strTime?????????????????????????????????formatType?????????????????????
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // date???????????????long??????
    // date????????????date???????????????
    public static long dateToLong(Date date) {
        return date.getTime();
    }


    /**
     * ??????1???????????????2???????????????3???????????????4???????????????5???????????????6???????????????7????????????
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
     * ??????time?????????from???to??????/???
     *
     * @param time   ????????????
     * @param ??????from ????????????
     * @param to     ????????????
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
     * ??????time?????????from???to??????/???
     *
     * @param time ????????????
     * @param from ????????????
     * @param to   ????????????
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
     * ??????time?????????from???
     *
     * @param time ????????????
     * @param from ????????????
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
     * ?????????n???????????????n????????? by ym
     *
     * @param distanceDay ????????? ????????????7???????????????-7??????????????????7?????????7
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
     * ?????????????????? ??????????????????YYYY-MM-dd???
     * 2004-2-30 ????????????
     * 2003-2-29 ????????????
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
