package com.yao.utils;

import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2014/10/29.
 */
public class DateUtils {

    //时间输出格式
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";

    public static void main(String[] args) {

        dateMove();
    }

    /**
     * 自行输入格式
     *
     * @param forStr
     * @return
     */
    public static String formatCurTime(String forStr) {
        SimpleDateFormat format = new SimpleDateFormat(forStr);
        return format.format(new Date());
    }

    /**
     * 获取当前时间，精确至毫秒
     *
     * @return
     */
    public static String getCurTime() {
        return formatCurTime(YYYY_MM_DD_HH_MM_SS_SSS);
    }

    /**
     * 返回当前日期
     *
     * @return
     */
    public static String getCurDay() {
        return formatCurTime(YYYYMMDD);
    }

    /**
     * 获取时间区间内的日期列表
     *
     * @param startDate
     * @param endDate
     * @return
     */
    private List<Date> listDateInterval(Date startDate, Date endDate) {

        List<Date> dateList = new ArrayList<>();
        if (null == startDate || null == endDate || startDate.getTime() > endDate.getTime())
            return dateList;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while (calendar.getTime().getTime() <= endDate.getTime()) {
            dateList.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }

        return dateList;
    }

    /**
     * 获取两个时间集合的差集
     *
     * @param originStart
     * @param originEnd
     * @param start
     * @param end
     * @return
     */
    private List<Date> listDiffDateInterval(Date originStart, Date originEnd, Date start, Date end) {

        List<Date> dateList = new ArrayList<>();
        if (null == originStart || null == originEnd || null == start || null == end ||
                originEnd.getTime() < originStart.getTime() ||
                end.getTime() < start.getTime() ||
                (originStart.getTime() <= start.getTime() && originEnd.getTime() >= end.getTime()))
            return dateList;

        if (end.getTime() <= originStart.getTime() || start.getTime() >= originEnd.getTime()) {
            dateList = listDateInterval(start, end);
            if (end.getTime() == originStart.getTime())
                dateList.remove(dateList.size() - 1);
            if (start.getTime() == originEnd.getTime())
                dateList.remove(0);
        } else if (start.getTime() < originStart.getTime() && end.getTime() > originEnd.getTime()) {
            dateList = listDateInterval(start, originStart);
            dateList.remove(dateList.size() - 1);
            List<Date> tmp = listDateInterval(originEnd, end);
            tmp.remove(0);
            dateList.addAll(tmp);
        } else {
            dateList = listDateInterval(start.getTime() < originStart.getTime() ? start : originEnd, end.getTime() > originEnd.getTime() ? end : originStart);
            if (originEnd.getTime() == end.getTime())
                dateList.remove(dateList.size() - 1);
            if (originStart.getTime() == start.getTime())
                dateList.remove(0);
        }

        return dateList;
    }

    private static void compareDate() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(2016, 7, 10, 0, 0, 0);
        startDate.set(Calendar.MILLISECOND, 0);
        startDate.add(Calendar.DAY_OF_YEAR, 2);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2016, 7, 12, 0, 0, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSS);
        System.out.println(format.format(startDate.getTime()));
        System.out.println(format.format(endDate.getTime()));
        System.out.println(endDate.compareTo(startDate));
    }

    private static long countDateInterval(Date originDate, Date offsetDate) {

        long offset = 0;

        if (null != originDate && null != offsetDate)
            offset = (offsetDate.getTime() - originDate.getTime()) / (1000 * 60 * 60 * 24);

        return offset;
    }

    private static Date intervalMove(Date intervalStart, Date intervalEnd, Calendar origin) {

        long interval = countDateInterval(
                getOneDayStartTime(intervalStart),
                getOneDayStartTime(intervalEnd)
        );

        Calendar dest = Calendar.getInstance();
        dest.setTime(origin.getTime());
        dest.add(Calendar.DAY_OF_YEAR, (int) interval);

        return dest.getTime();
    }

    private static Date intervalMove(Date intervalStart, Date intervalEnd, Date origin) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(origin);
        return intervalMove(intervalStart, intervalEnd, calendar);
    }

    public static void testIntervalMove() {

        Calendar intervalStart = Calendar.getInstance();
        Calendar intervalEnd = Calendar.getInstance();
        Calendar origin = Calendar.getInstance();

        // future -> now
        intervalStart.set(2016, 7, 20);
        intervalEnd.set(2016, 7, 15);
        origin.set(2016, 7, 21);
        Date now = intervalMove(intervalStart.getTime(), intervalEnd.getTime(), origin);
        Assert.isTrue(16 == now.getDate());


        // now -> now
        intervalStart.set(2016, 7, 20);
        intervalEnd.set(2016, 7, 20);
        origin.set(2016, 7, 21);
        now = intervalMove(intervalStart.getTime(), intervalEnd.getTime(), origin);
        Assert.isTrue(21 == now.getDate());

        // his -> future
        intervalStart.set(2016, 7, 15);
        intervalEnd.set(2016, 7, 20);
        origin.set(2016, 7, 14);
        now = intervalMove(intervalStart.getTime(), intervalEnd.getTime(), origin);
        Assert.isTrue(19 == now.getDate());

    }

    public static Date getOneDayStartTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static void dateMove() {

        Calendar calHis = Calendar.getInstance();
        calHis.set(2016, 6, 21, 0, 0, 0);
        Calendar calNow = Calendar.getInstance();
        calNow.set(2016, 7, 18, 0, 0, 0);
        Calendar calFut = Calendar.getInstance();
        calFut.set(2016, 7, 21, 0, 0, 0);

        System.out.println(countDateInterval(calHis.getTime(), calHis.getTime()));
        System.out.println(countDateInterval(calNow.getTime(), calNow.getTime()));
        System.out.println(countDateInterval(calFut.getTime(), calFut.getTime()));

        System.out.println(countDateInterval(calHis.getTime(), calNow.getTime()));
        System.out.println(countDateInterval(calNow.getTime(), calNow.getTime()));
        System.out.println(countDateInterval(calFut.getTime(), calNow.getTime()));


        Calendar calHis2 = Calendar.getInstance();
        calHis2.set(2016, 6, 22, 0, 0, 0);
        System.out.println(intervalMove(calHis.getTime(), calNow.getTime(), calHis.getTime()));
        System.out.println(intervalMove(calNow.getTime(), calNow.getTime(), calNow.getTime()));
        System.out.println(intervalMove(calFut.getTime(), calNow.getTime(), calHis2.getTime()));

        calHis.add(Calendar.DAY_OF_YEAR, (int) countDateInterval(calHis.getTime(), calNow.getTime()));
        System.out.println(calHis.getTime());
        calNow.add(Calendar.DAY_OF_YEAR, (int) countDateInterval(calNow.getTime(), calNow.getTime()));
        System.out.println(calNow.getTime());
        calHis2.add(Calendar.DAY_OF_YEAR, (int) countDateInterval(calFut.getTime(), calNow.getTime()));
        System.out.println(calHis2.getTime());
    }
}
