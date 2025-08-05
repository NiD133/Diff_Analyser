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

@SuppressWarnings("MemberName")
public class ISO8601UtilsTest {
    // Timezone constants
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    private static final TimeZone BRAZIL_EAST = TimeZone.getTimeZone("Brazil/East");
    
    // Test data constants
    private static final int TEST_YEAR = 2018;
    private static final int TEST_MONTH = Calendar.JUNE;
    private static final int TEST_DAY = 25;
    private static final long TEST_TIME_MILLIS = 1530209176870L; // 2018-06-28T18:06:16.870Z

    private static GregorianCalendar createUtcCalendar() {
        GregorianCalendar calendar = new GregorianCalendar(UTC);
        calendar.clear(); // Reset time components
        return calendar;
    }

    private static Date createDate(int year, int month, int day, TimeZone tz) {
        GregorianCalendar calendar = new GregorianCalendar(tz);
        calendar.clear();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    @Test
    public void format_dateOnly_utc() {
        Date date = createDate(TEST_YEAR, TEST_MONTH, TEST_DAY, UTC);
        String formatted = ISO8601Utils.format(date);
        assertThat(formatted).isEqualTo("2018-06-25T00:00:00Z");
    }

    @Test
    @SuppressWarnings("JavaUtilDate")
    public void format_dateWithMilliseconds_utc() {
        Date date = new Date(TEST_TIME_MILLIS);
        String formatted = ISO8601Utils.format(date, true);
        assertThat(formatted).isEqualTo("2018-06-28T18:06:16.870Z");
    }

    @Test
    @SuppressWarnings("JavaUtilDate")
    public void format_dateWithMilliseconds_customTimezone() {
        Date date = new Date(TEST_TIME_MILLIS);
        String formatted = ISO8601Utils.format(date, true, BRAZIL_EAST);
        assertThat(formatted).isEqualTo("2018-06-28T15:06:16.870-03:00");
    }

    @Test
    @SuppressWarnings("UndefinedEquals")
    public void parse_dateOnly_defaultTimezone() throws ParseException {
        String dateStr = "2018-06-25";
        Date parsed = ISO8601Utils.parse(dateStr, new ParsePosition(0));
        
        // Expected date in default timezone (same as test environment)
        Date expected = createDate(TEST_YEAR, TEST_MONTH, TEST_DAY, TimeZone.getDefault());
        assertThat(parsed).isEqualTo(expected);
    }

    @Test
    public void parse_invalidDay_throwsException() {
        String invalidDate = "2022-12-33";
        assertThrows(ParseException.class, 
            () -> ISO8601Utils.parse(invalidDate, new ParsePosition(0)));
    }

    @Test
    public void parse_invalidMonth_throwsException() {
        String invalidDate = "2022-14-30";
        assertThrows(ParseException.class, 
            () -> ISO8601Utils.parse(invalidDate, new ParsePosition(0)));
    }

    @Test
    @SuppressWarnings("UndefinedEquals")
    public void parse_dateWithTimezoneOffset_convertsToUtc() throws ParseException {
        String dateStr = "2018-06-25T00:00:00-03:00";
        Date parsed = ISO8601Utils.parse(dateStr, new ParsePosition(0));
        
        // Expected UTC time: 00:00 -03:00 = 03:00 UTC
        GregorianCalendar expectedCal = createUtcCalendar();
        expectedCal.set(TEST_YEAR, TEST_MONTH, TEST_DAY, 3, 0);
        Date expected = expectedCal.getTime();
        
        assertThat(parsed).isEqualTo(expected);
    }

    @Test
    @SuppressWarnings("UndefinedEquals")
    public void parse_dateWithNonStandardOffset_convertsToUtc() throws ParseException {
        String dateStr = "2018-06-25T00:02:00-02:58";
        Date parsed = ISO8601Utils.parse(dateStr, new ParsePosition(0));
        
        // Expected UTC time: 00:02 -02:58 = 03:00 UTC
        GregorianCalendar expectedCal = createUtcCalendar();
        expectedCal.set(TEST_YEAR, TEST_MONTH, TEST_DAY, 3, 0);
        Date expected = expectedCal.getTime();
        
        assertThat(parsed).isEqualTo(expected);
    }

    @Test
    public void parse_invalidTimeComponents_throwsException() {
        String invalidTime = "2018-06-25T61:60:62-03:00";
        assertThrows(ParseException.class, 
            () -> ISO8601Utils.parse(invalidTime, new ParsePosition(0)));
    }
}