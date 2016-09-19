package com.android.finki.mpip.footballdreamteam.utility;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Borce on 30.07.2016.
 */
public class DateUtilsTest {

    private int year = 2016, month = 8, day = 10, hour = 9, minute = 6, second = 3;
    private String sDate = String.format("%02d-%02d-%01d %02d:%02d:%02d",
            year, month, day, hour, minute, second);
    private String sDayNameDate = String.format("%02d.%02d.%02d at %02d:%02d:%02d", day,
            month, year % 100, hour, minute, second);
    private Calendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute, second);

    /**
     * Test that format method correctly format the date.
     */
    @Test
    public void testFormat() {
        String result = DateUtils.format(calendar.getTime());
        assertEquals(sDate, result);
    }

    /**
     * Test the behavior on format method called with null param.
     */
    @Test
    public void testFormatOnNull() {
        assertNull(DateUtils.format(null));
    }

    /**
     * Test that parse method correctly parse the date.
     */
    @Test
    public void testParse() {
        Date date = DateUtils.parse(sDate);
        assertNotNull(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(calendar.get(Calendar.YEAR), year);
        assertEquals(calendar.get(Calendar.MONTH), month - 1);
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), day);
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), hour);
        assertEquals(calendar.get(Calendar.MINUTE), minute);
        assertEquals(calendar.get(Calendar.SECOND), second);
    }

    /**
     * Test the behavior on parse method called with null param.
     */
    @Test
    public void testParseOnNull() {
        assertNull(DateUtils.parse(null));
    }

    /**
     * Test the behavior on dayNameFormat called with null param.
     */
    @Test
    public void testDayNameFormatOnNull() {
        assertNull(DateUtils.dayNameFormat(null));
    }

    /**
     * Test that dayNameFormat returns the correct string for the given date.
     */
    @Test
    public void testDayNameFormat() {
        String result = DateUtils.dayNameFormat(calendar.getTime());
        assertNotNull(result);
        assertTrue(result.contains(sDayNameDate));
    }

    /**
     * Test the behavior on getYearDiff called with null prams.
     */
    @Test
    public void testGetYearDiffCalledWithNullParams() {
        assertEquals(0, DateUtils.getYearDiff(null, null));
    }

    /**
     * Test the result on getYearDiff when date 1 month is bigger than date 2 month.
     */
    @Test
    public void testGetYearDiffOnDate1MonthIsBiggerThanDate2Month() {
        Calendar cal1 = new GregorianCalendar(2016, 8, 20);
        Calendar cal2 = new GregorianCalendar(2013, 7, 23);
        int diff = DateUtils.getYearDiff(cal1.getTime(), cal2.getTime());
        assertEquals(3, diff);
    }

    /**
     * Test the result on getYearDiff when date 2 month is bigger than date 1 month.
     */
    @Test
    public void testGetYearDiffOnDate2MonthBiggerThanDate1Month() {
        Calendar cal1 = new GregorianCalendar(2016, 8, 20);
        Calendar cal2 = new GregorianCalendar(2013, 9, 23);
        int diff = DateUtils.getYearDiff(cal1.getTime(), cal2.getTime());
        assertEquals(2, diff);
    }

    /**
     * Test the result on getYearDiff when date months are equal and date 1 day is
     * bigger that date 2 day.
     */
    @Test
    public void testGetYearDiffOnEqualMonthAndDate1DayBiggerThanDate2Date() {
        Calendar cal1 = new GregorianCalendar(2016, 8, 20);
        Calendar cal2 = new GregorianCalendar(2013, 8, 19);
        int diff = DateUtils.getYearDiff(cal1.getTime(), cal2.getTime());
        assertEquals(3, diff);
    }

    /**
     * Test the result on getYearDiff when months are equal and date 2 day is bigger
     * that date 1 date..
     */
    @Test
    public void testGetYearDIffOnEqualMonthsAndDay2DayBiggerThenDay1Day() {
        Calendar cal1 = new GregorianCalendar(2016, 8, 20);
        Calendar cal2 = new GregorianCalendar(2013, 8, 23);
        int diff = DateUtils.getYearDiff(cal1.getTime(), cal2.getTime());
        assertEquals(2, diff);
    }

    /**
     * Test the result on getYearDiff when date 2 is bigger then date 1.
     */
    @Test
    public void testGetYearDiffOnDate2BiggerThenDate1() {
        Calendar cal1 = new GregorianCalendar(2013, 10, 10);
        Calendar cal2 = new GregorianCalendar(2016, 9, 10);
        int diff = DateUtils.getYearDiff(cal1.getTime(), cal2.getTime());
        assertEquals(-3, diff);
    }
}
