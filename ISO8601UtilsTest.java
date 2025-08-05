/*
 * Copyright (C) 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 * Tests for ISO8601Utils date formatting and parsing functionality.
 * 
 * Tests cover:
 * - Date formatting with and without milliseconds
 * - Date formatting with different timezones
 * - Date parsing with various timezone formats
 * - Error handling for invalid date/time values
 */
@SuppressWarnings("MemberName") // class name
public class ISO8601UtilsTest {

  // Test constants for better readability and maintainability
  private static final long JUNE_28_2018_TIMESTAMP = 1530209176870L; // 2018-06-28T18:06:16.870Z
  private static final String BRAZIL_EAST_TIMEZONE = "Brazil/East";
  private static final String UTC_TIMEZONE = "UTC";
  
  // Expected date strings
  private static final String EXPECTED_DATE_WITHOUT_TIME = "2018-06-25T00:00:00Z";
  private static final String EXPECTED_DATE_WITH_MILLIS_UTC = "2018-06-28T18:06:16.870Z";
  private static final String EXPECTED_DATE_WITH_MILLIS_BRAZIL = "2018-06-28T15:06:16.870-03:00";

  /**
   * Creates a UTC timezone instance for consistent test setup.
   */
  private static TimeZone createUtcTimeZone() {
    return TimeZone.getTimeZone(UTC_TIMEZONE);
  }

  /**
   * Creates a GregorianCalendar configured for UTC timezone with cleared time.
   * This ensures consistent date creation across tests.
   */
  private static GregorianCalendar createUtcCalendar() {
    TimeZone utc = createUtcTimeZone();
    GregorianCalendar calendar = new GregorianCalendar(utc);
    calendar.clear(); // Remove current time to start with epoch
    return calendar;
  }

  /**
   * Creates a Date object for June 25, 2018 at midnight UTC.
   */
  private static Date createJune25Date() {
    GregorianCalendar calendar = new GregorianCalendar(createUtcTimeZone(), Locale.US);
    calendar.clear();
    calendar.set(2018, Calendar.JUNE, 25);
    return calendar.getTime();
  }

  // === DATE FORMATTING TESTS ===

  @Test
  public void formatDate_withoutMilliseconds_shouldReturnISO8601String() {
    // Given: A specific date (June 25, 2018)
    Date inputDate = createJune25Date();
    
    // When: Formatting the date using default format (no milliseconds)
    String actualFormattedDate = ISO8601Utils.format(inputDate);
    
    // Then: Should return ISO8601 format without milliseconds
    assertThat(actualFormattedDate).isEqualTo(EXPECTED_DATE_WITHOUT_TIME);
  }

  @Test
  @SuppressWarnings("JavaUtilDate")
  public void formatDate_withMilliseconds_shouldIncludeMillisecondsInOutput() {
    // Given: A date with specific milliseconds
    Date inputDate = new Date(JUNE_28_2018_TIMESTAMP);
    
    // When: Formatting with milliseconds enabled
    String actualFormattedDate = ISO8601Utils.format(inputDate, true);
    
    // Then: Should include milliseconds in the output
    assertThat(actualFormattedDate).isEqualTo(EXPECTED_DATE_WITH_MILLIS_UTC);
  }

  @Test
  @SuppressWarnings("JavaUtilDate")
  public void formatDate_withCustomTimezone_shouldAdjustTimeAndShowOffset() {
    // Given: A date and a specific timezone (Brazil/East = UTC-3)
    Date inputDate = new Date(JUNE_28_2018_TIMESTAMP);
    TimeZone brazilTimezone = TimeZone.getTimeZone(BRAZIL_EAST_TIMEZONE);
    
    // When: Formatting with custom timezone
    String actualFormattedDate = ISO8601Utils.format(inputDate, true, brazilTimezone);
    
    // Then: Should show time adjusted for timezone with proper offset
    assertThat(actualFormattedDate).isEqualTo(EXPECTED_DATE_WITH_MILLIS_BRAZIL);
  }

  // === DATE PARSING TESTS ===

  @Test
  @SuppressWarnings("UndefinedEquals")
  public void parseDate_withDateOnly_shouldParseToMidnightInDefaultTimezone() throws ParseException {
    // Given: A date string without time component
    String dateString = "2018-06-25";
    
    // When: Parsing the date string
    Date actualParsedDate = ISO8601Utils.parse(dateString, new ParsePosition(0));
    
    // Then: Should parse to midnight of that date in default timezone
    Date expectedDate = new GregorianCalendar(2018, Calendar.JUNE, 25).getTime();
    assertThat(actualParsedDate).isEqualTo(expectedDate);
  }

  @Test
  @SuppressWarnings("UndefinedEquals")
  public void parseDate_withTimezoneOffset_shouldConvertToUTC() throws ParseException {
    // Given: A date string with timezone offset (-03:00)
    String dateStringWithTimezone = "2018-06-25T00:00:00-03:00";
    
    // When: Parsing the date string
    Date actualParsedDate = ISO8601Utils.parse(dateStringWithTimezone, new ParsePosition(0));
    
    // Then: Should convert to UTC (00:00 -03:00 becomes 03:00 UTC)
    GregorianCalendar expectedCalendar = createUtcCalendar();
    expectedCalendar.set(2018, Calendar.JUNE, 25, 3, 0); // 3 AM UTC
    Date expectedDate = expectedCalendar.getTime();
    assertThat(actualParsedDate).isEqualTo(expectedDate);
  }

  @Test
  @SuppressWarnings("UndefinedEquals")
  public void parseDate_withNonStandardTimezoneOffset_shouldHandleCorrectly() throws ParseException {
    // Given: A date string with non-standard timezone offset (-02:58)
    String dateStringWithSpecialTimezone = "2018-06-25T00:02:00-02:58";
    
    // When: Parsing the date string
    Date actualParsedDate = ISO8601Utils.parse(dateStringWithSpecialTimezone, new ParsePosition(0));
    
    // Then: Should correctly calculate UTC time (00:02 + 02:58 = 03:00 UTC)
    GregorianCalendar expectedCalendar = createUtcCalendar();
    expectedCalendar.set(2018, Calendar.JUNE, 25, 3, 0); // 3 AM UTC
    Date expectedDate = expectedCalendar.getTime();
    assertThat(actualParsedDate).isEqualTo(expectedDate);
  }

  // === ERROR HANDLING TESTS ===

  @Test
  public void parseDate_withInvalidDay_shouldThrowParseException() {
    // Given: A date string with invalid day (33rd day of month)
    String invalidDateString = "2022-12-33";
    
    // When & Then: Parsing should throw ParseException
    assertThrows(
        "Should reject invalid day values", 
        ParseException.class, 
        () -> ISO8601Utils.parse(invalidDateString, new ParsePosition(0))
    );
  }

  @Test
  public void parseDate_withInvalidMonth_shouldThrowParseException() {
    // Given: A date string with invalid month (14th month)
    String invalidDateString = "2022-14-30";
    
    // When & Then: Parsing should throw ParseException
    assertThrows(
        "Should reject invalid month values", 
        ParseException.class, 
        () -> ISO8601Utils.parse(invalidDateString, new ParsePosition(0))
    );
  }

  @Test
  public void parseDate_withInvalidTime_shouldThrowParseException() {
    // Given: A date string with invalid time values (61 hours, 60 minutes, 62 seconds)
    String invalidTimeString = "2018-06-25T61:60:62-03:00";
    
    // When & Then: Parsing should throw ParseException
    assertThrows(
        "Should reject invalid time values", 
        ParseException.class, 
        () -> ISO8601Utils.parse(invalidTimeString, new ParsePosition(0))
    );
  }
}