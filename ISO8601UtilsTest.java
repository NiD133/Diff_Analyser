package com.google.gson.internal.bind.util;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.Test;

/**
 * Unit tests for the ISO8601Utils class.
 */
public class ISO8601UtilsTest {

  /**
   * Returns the UTC time zone.
   */
  private static TimeZone getUtcTimeZone() {
    return TimeZone.getTimeZone("UTC");
  }

  /**
   * Creates a GregorianCalendar instance set to UTC time zone with cleared fields.
   */
  private static GregorianCalendar createUtcCalendar() {
    TimeZone utc = getUtcTimeZone();
    GregorianCalendar calendar = new GregorianCalendar(utc);
    calendar.clear(); // Clear the calendar to remove current time fields
    return calendar;
  }

  /**
   * Tests formatting a date to ISO 8601 string without milliseconds.
   */
  @Test
  public void testFormatDateToIso8601String() {
    GregorianCalendar calendar = new GregorianCalendar(getUtcTimeZone(), Locale.US);
    calendar.clear();
    calendar.set(2018, Calendar.JUNE, 25);
    Date date = calendar.getTime();
    
    String formattedDate = ISO8601Utils.format(date);
    String expectedDate = "2018-06-25T00:00:00Z";
    
    assertThat(formattedDate).isEqualTo(expectedDate);
  }

  /**
   * Tests formatting a date to ISO 8601 string with milliseconds.
   */
  @Test
  public void testFormatDateWithMilliseconds() {
    long timestamp = 1530209176870L;
    Date date = new Date(timestamp);
    
    String formattedDate = ISO8601Utils.format(date, true);
    String expectedDate = "2018-06-28T18:06:16.870Z";
    
    assertThat(formattedDate).isEqualTo(expectedDate);
  }

  /**
   * Tests formatting a date to ISO 8601 string with a specific time zone.
   */
  @Test
  public void testFormatDateWithTimezone() {
    long timestamp = 1530209176870L;
    Date date = new Date(timestamp);
    
    String formattedDate = ISO8601Utils.format(date, true, TimeZone.getTimeZone("Brazil/East"));
    String expectedDate = "2018-06-28T15:06:16.870-03:00";
    
    assertThat(formattedDate).isEqualTo(expectedDate);
  }

  /**
   * Tests parsing a date string with default time zone.
   */
  @Test
  public void testParseDateWithDefaultTimezone() throws ParseException {
    String dateString = "2018-06-25";
    
    Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
    Date expectedDate = new GregorianCalendar(2018, Calendar.JUNE, 25).getTime();
    
    assertThat(parsedDate).isEqualTo(expectedDate);
  }

  /**
   * Tests parsing an invalid date string with an invalid day.
   */
  @Test
  public void testParseInvalidDay() {
    String invalidDateString = "2022-12-33";
    
    assertThrows(ParseException.class, () -> ISO8601Utils.parse(invalidDateString, new ParsePosition(0)));
  }

  /**
   * Tests parsing an invalid date string with an invalid month.
   */
  @Test
  public void testParseInvalidMonth() {
    String invalidDateString = "2022-14-30";
    
    assertThrows(ParseException.class, () -> ISO8601Utils.parse(invalidDateString, new ParsePosition(0)));
  }

  /**
   * Tests parsing a date string with a specific time zone.
   */
  @Test
  public void testParseDateWithTimezone() throws ParseException {
    String dateString = "2018-06-25T00:00:00-03:00";
    
    Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
    GregorianCalendar calendar = createUtcCalendar();
    calendar.set(2018, Calendar.JUNE, 25, 3, 0);
    Date expectedDate = calendar.getTime();
    
    assertThat(parsedDate).isEqualTo(expectedDate);
  }

  /**
   * Tests parsing a date string with a special time zone offset.
   */
  @Test
  public void testParseDateWithSpecialTimezone() throws ParseException {
    String dateString = "2018-06-25T00:02:00-02:58";
    
    Date parsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
    GregorianCalendar calendar = createUtcCalendar();
    calendar.set(2018, Calendar.JUNE, 25, 3, 0);
    Date expectedDate = calendar.getTime();
    
    assertThat(parsedDate).isEqualTo(expectedDate);
  }

  /**
   * Tests parsing an invalid date string with invalid time.
   */
  @Test
  public void testParseInvalidTime() {
    String invalidDateString = "2018-06-25T61:60:62-03:00";
    
    assertThrows(ParseException.class, () -> ISO8601Utils.parse(invalidDateString, new ParsePosition(0)));
  }
}