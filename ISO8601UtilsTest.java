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
import java.util.TimeZone;
import org.junit.Test;

@SuppressWarnings("MemberName") // class name is a legacy name from a fork
public class ISO8601UtilsTest {

  private static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");

  /** Creates a new GregorianCalendar in the UTC timezone, with its time cleared. */
  private static GregorianCalendar createUtcCalendar() {
    GregorianCalendar calendar = new GregorianCalendar(UTC_TIMEZONE);
    // A new Calendar is created with the current time, so we must clear it
    calendar.clear();
    return calendar;
  }

  /** Creates a new Date object in the UTC timezone from the given components. */
  private static Date createUtcDate(
      int year, int month, int day, int hour, int minute, int second, int millisecond) {
    GregorianCalendar calendar = createUtcCalendar();
    calendar.set(year, month, day, hour, minute, second);
    calendar.set(Calendar.MILLISECOND, millisecond);
    return calendar.getTime();
  }

  // --- Formatting Tests ---

  @Test
  public void format_dateWithoutTime_producesUtcDateTimeAtMidnight() {
    Date date = createUtcDate(2018, Calendar.JUNE, 25, 0, 0, 0, 0);
    String formattedDate = ISO8601Utils.format(date);
    assertThat(formattedDate).isEqualTo("2018-06-25T00:00:00Z");
  }

  @Test
  public void format_withMilliseconds_includesMillisecondsInOutput() {
    // This date corresponds to the timestamp 1530209176870L
    Date date = createUtcDate(2018, Calendar.JUNE, 28, 18, 6, 16, 870);
    String formattedDate = ISO8601Utils.format(date, true);
    assertThat(formattedDate).isEqualTo("2018-06-28T18:06:16.870Z");
  }

  @Test
  public void format_withSpecificTimezone_usesTimezoneOffset() {
    // This date corresponds to the timestamp 1530209176870L
    Date date = createUtcDate(2018, Calendar.JUNE, 28, 18, 6, 16, 870);
    TimeZone brazilTimeZone = TimeZone.getTimeZone("Brazil/East"); // UTC-3

    String formattedDate = ISO8601Utils.format(date, true, brazilTimeZone);
    assertThat(formattedDate).isEqualTo("2018-06-28T15:06:16.870-03:00");
  }

  // --- Parsing Tests ---

  @Test
  public void parse_dateOnlyString_usesDefaultTimezone() throws ParseException {
    String dateStr = "2018-06-25";
    Date actualDate = ISO8601Utils.parse(dateStr, new ParsePosition(0));

    // This test is inherently dependent on the default timezone of the system.
    // To make the test stable, the expected date is constructed using the same default timezone.
    TimeZone defaultTimeZone = TimeZone.getDefault();
    GregorianCalendar calendar = new GregorianCalendar(defaultTimeZone);
    calendar.clear();
    calendar.set(2018, Calendar.JUNE, 25);
    Date expectedDate = calendar.getTime();

    assertThat(actualDate).isEqualTo(expectedDate);
  }

  @Test
  public void parse_stringWithTimezoneOffset_producesCorrectUtcTime() throws ParseException {
    String dateStr = "2018-06-25T00:00:00-03:00";
    Date actualDate = ISO8601Utils.parse(dateStr, new ParsePosition(0));

    // Expected date is 2018-06-25 00:00:00 in -03:00, which is 03:00:00 in UTC
    Date expectedDate = createUtcDate(2018, Calendar.JUNE, 25, 3, 0, 0, 0);
    assertThat(actualDate).isEqualTo(expectedDate);
  }

  @Test
  public void parse_stringWithUnusualTimezoneOffset_producesCorrectUtcTime() throws ParseException {
    String dateStr = "2018-06-25T00:02:00-02:58";
    Date actualDate = ISO8601Utils.parse(dateStr, new ParsePosition(0));

    // The local time is 00:02:00 with a timezone offset of -02:58.
    // To convert to UTC, we subtract the offset: 00:02:00 - (-02:58) = 03:00:00 UTC.
    Date expectedDate = createUtcDate(2018, Calendar.JUNE, 25, 3, 0, 0, 0);
    assertThat(actualDate).isEqualTo(expectedDate);
  }

  // --- Invalid Format Parsing Tests ---

  @Test
  public void parse_invalidDay_throwsParseException() {
    String dateStr = "2022-12-33";
    ParseException e =
        assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateStr, new ParsePosition(0)));
    assertThat(e).hasMessageThat().contains(dateStr);
  }

  @Test
  public void parse_invalidMonth_throwsParseException() {
    String dateStr = "2022-14-30";
    assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateStr, new ParsePosition(0)));
  }

  @Test
  public void parse_invalidTime_throwsParseException() {
    String dateStr = "2018-06-25T61:60:62-03:00";
    assertThrows(ParseException.class, () -> ISO8601Utils.parse(dateStr, new ParsePosition(0)));
  }
}