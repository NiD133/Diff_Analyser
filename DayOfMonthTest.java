/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.threeten.extra;

// ... (imports remain unchanged) ...

/**
 * Test class for {@link DayOfMonth}.
 */
public class TestDayOfMonth {

    private static final int MAX_LENGTH = 31;
    private static final DayOfMonth TEST = DayOfMonth.of(12);
    private static final ZoneId PARIS = ZoneId.of("Europe/Paris");

    // Helper class for testing TemporalField behavior
    private static class TestingField implements TemporalField {
        // ... (implementation remains unchanged) ...
    }

    //-----------------------------------------------------------------------
    // Interface Implementation Tests
    //-----------------------------------------------------------------------
    @Test
    public void class_implements_required_interfaces() {
        assertTrue(Serializable.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(Comparable.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(TemporalAdjuster.class.isAssignableFrom(DayOfMonth.class));
        assertTrue(TemporalAccessor.class.isAssignableFrom(DayOfMonth.class));
    }

    //-----------------------------------------------------------------------
    // Serialization Tests
    //-----------------------------------------------------------------------
    @Test
    public void serialization_roundtrip() throws IOException, ClassNotFoundException {
        DayOfMonth test = DayOfMonth.of(1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(test);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            assertEquals(test, ois.readObject(), "Deserialized object should match original");
        }
    }

    //-----------------------------------------------------------------------
    // now() Tests
    //-----------------------------------------------------------------------
    @RetryingTest(100)
    public void now_usesSystemDefaultZone() {
        assertEquals(LocalDate.now().getDayOfMonth(), DayOfMonth.now().getValue());
    }

    @RetryingTest(100)
    public void now_withZoneId_usesSpecifiedZone() {
        ZoneId zone = ZoneId.of("Asia/Tokyo");
        assertEquals(LocalDate.now(zone).getDayOfMonth(), DayOfMonth.now(zone).getValue());
    }

    //-----------------------------------------------------------------------
    // of() Factory Method Tests
    //-----------------------------------------------------------------------
    @Test
    public void of_validDays_returnsSingletonInstances() {
        for (int i = 1; i <= MAX_LENGTH; i++) {
            DayOfMonth test = DayOfMonth.of(i);
            assertEquals(i, test.getValue(), "Day value should match input");
            assertSame(test, DayOfMonth.of(i), "Should return singleton instance");
        }
    }

    @Test
    public void of_valueBelow1_throwsException() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(0));
    }

    @Test
    public void of_valueAbove31_throwsException() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.of(32));
    }

    //-----------------------------------------------------------------------
    // from() Factory Method Tests
    //-----------------------------------------------------------------------
    @Test
    public void from_TemporalAccessor_nonLeapYear_allDays() {
        LocalDate date = LocalDate.of(2007, 1, 1);
        for (int month = 1; month <= 12; month++) {
            int daysInMonth = YearMonth.of(2007, month).lengthOfMonth();
            for (int day = 1; day <= daysInMonth; day++) {
                assertEquals(day, DayOfMonth.from(date).getValue(), 
                    "Day should match for date: " + date);
                date = date.plusDays(1);
            }
        }
    }

    @Test
    public void from_TemporalAccessor_leapYear_february29() {
        LocalDate date = LocalDate.of(2008, 1, 1);
        // Test through March to include February 29th
        for (int i = 0; i < 31 + 29 + 31; i++) {
            assertEquals(date.getDayOfMonth(), DayOfMonth.from(date).getValue(),
                "Day should match for date: " + date);
            date = date.plusDays(1);
        }
    }

    @Test
    public void from_TemporalAccessor_DayOfMonth_returnsSameInstance() {
        DayOfMonth dom = DayOfMonth.of(6);
        assertSame(dom, DayOfMonth.from(dom), "Should return same instance");
    }

    @Test
    public void from_TemporalAccessor_nonIsoChronology() {
        LocalDate date = LocalDate.now();
        JapaneseDate japaneseDate = JapaneseDate.from(date);
        assertEquals(date.getDayOfMonth(), DayOfMonth.from(japaneseDate).getValue());
    }

    @Test
    public void from_unsupportedTemporalType_throwsException() {
        assertThrows(DateTimeException.class, () -> DayOfMonth.from(LocalTime.NOON));
    }

    @Test
    public void from_nullTemporalAccessor_throwsException() {
        assertThrows(NullPointerException.class, () -> DayOfMonth.from((TemporalAccessor) null));
    }

    @Test
    public void from_parsedCharSequence() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d");
        assertEquals(DayOfMonth.of(3), formatter.parse("3", DayOfMonth::from));
    }

    //-----------------------------------------------------------------------
    // isSupported() Tests
    //-----------------------------------------------------------------------
    @Test
    public void isSupported_returnsFalseForUnsupportedFields() {
        assertFalse(TEST.isSupported((TemporalField) null));
        
        // Time-based fields should be unsupported
        assertFalse(TEST.isSupported(NANO_OF_SECOND));
        assertFalse(TEST.isSupported(MICRO_OF_SECOND));
        assertFalse(TEST.isSupported(MILLI_OF_SECOND));
        assertFalse(TEST.isSupported(SECOND_OF_MINUTE));
        assertFalse(TEST.isSupported(MINUTE_OF_HOUR));
        assertFalse(TEST.isSupported(HOUR_OF_AMPM));
        assertFalse(TEST.isSupported(CLOCK_HOUR_OF_AMPM));
        assertFalse(TEST.isSupported(HOUR_OF_DAY));
        assertFalse(TEST.isSupported(CLOCK_HOUR_OF_DAY));
        assertFalse(TEST.isSupported(AMPM_OF_DAY));
        
        // Date-based fields except DAY_OF_MONTH should be unsupported
        assertFalse(TEST.isSupported(DAY_OF_WEEK));
        assertFalse(TEST.isSupported(ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertFalse(TEST.isSupported(ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertFalse(TEST.isSupported(DAY_OF_YEAR));
        assertFalse(TEST.isSupported(EPOCH_DAY));
        assertFalse(TEST.isSupported(ALIGNED_WEEK_OF_MONTH));
        assertFalse(TEST.isSupported(ALIGNED_WEEK_OF_YEAR));
        assertFalse(TEST.isSupported(MONTH_OF_YEAR));
        assertFalse(TEST.isSupported(PROLEPTIC_MONTH));
        assertFalse(TEST.isSupported(YEAR_OF_ERA));
        assertFalse(TEST.isSupported(YEAR));
        assertFalse(TEST.isSupported(ERA));
        assertFalse(TEST.isSupported(INSTANT_SECONDS));
        assertFalse(TEST.isSupported(OFFSET_SECONDS));
        assertFalse(TEST.isSupported(IsoFields.DAY_OF_QUARTER));
        
        // Custom field should be supported
        assertTrue(TEST.isSupported(TestingField.INSTANCE));
    }

    //-----------------------------------------------------------------------
    // range() Tests
    //-----------------------------------------------------------------------
    @Test
    public void range_supportedField_returnsValidRange() {
        assertEquals(DAY_OF_MONTH.range(), TEST.range(DAY_OF_MONTH));
    }

    @Test
    public void range_unsupportedField_throwsException() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> TEST.range(MONTH_OF_YEAR));
    }

    @Test
    public void range_nullField_throwsException() {
        assertThrows(NullPointerException.class, () -> TEST.range((TemporalField) null));
    }

    //-----------------------------------------------------------------------
    // get() and getLong() Tests
    //-----------------------------------------------------------------------
    @Test
    public void get_supportedField_returnsValue() {
        assertEquals(12, TEST.get(DAY_OF_MONTH));
    }

    @Test
    public void get_unsupportedField_throwsException() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> TEST.get(MONTH_OF_YEAR));
    }

    @Test
    public void get_nullField_throwsException() {
        assertThrows(NullPointerException.class, () -> TEST.get((TemporalField) null));
    }

    @Test
    public void getLong_supportedField_returnsValue() {
        assertEquals(12L, TEST.getLong(DAY_OF_MONTH));
    }

    @Test
    public void getLong_customField_returnsValue() {
        assertEquals(12L, TEST.getLong(TestingField.INSTANCE));
    }

    @Test
    public void getLong_unsupportedField_throwsException() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> TEST.getLong(MONTH_OF_YEAR));
        assertThrows(UnsupportedTemporalTypeException.class, () -> TEST.getLong(IsoFields.DAY_OF_QUARTER));
    }

    @Test
    public void getLong_nullField_throwsException() {
        assertThrows(NullPointerException.class, () -> TEST.getLong((TemporalField) null));
    }

    //-----------------------------------------------------------------------
    // isValidYearMonth() Tests
    //-----------------------------------------------------------------------
    @Test
    public void isValidYearMonth_day31_validForMonthsWith31Days() {
        DayOfMonth test = DayOfMonth.of(31);
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 1)), "January should have 31 days");
        assertFalse(test.isValidYearMonth(YearMonth.of(2012, 2)), "February should not have 31 days");
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 3)), "March should have 31 days");
        assertFalse(test.isValidYearMonth(YearMonth.of(2012, 4)), "April should not have 31 days");
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 5)), "May should have 31 days");
        assertFalse(test.isValidYearMonth(YearMonth.of(2012, 6)), "June should not have 31 days");
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 7)), "July should have 31 days");
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 8)), "August should have 31 days");
        assertFalse(test.isValidYearMonth(YearMonth.of(2012, 9)), "September should not have 31 days");
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 10)), "October should have 31 days");
        assertFalse(test.isValidYearMonth(YearMonth.of(2012, 11)), "November should not have 31 days");
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 12)), "December should have 31 days");
    }

    @Test
    public void isValidYearMonth_day30_validForAllMonthsExceptFebruary() {
        DayOfMonth test = DayOfMonth.of(30);
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(2012, month);
            boolean expected = month != 2; // February has less than 30 days
            assertEquals(expected, test.isValidYearMonth(yearMonth),
                "Day 30 should be valid for month " + month);
        }
    }

    @Test
    public void isValidYearMonth_day29_validInLeapYearFebruary() {
        DayOfMonth test = DayOfMonth.of(29);
        assertTrue(test.isValidYearMonth(YearMonth.of(2012, 2)), "2012 is leap year");
        assertFalse(test.isValidYearMonth(YearMonth.of(2011, 2)), "2011 is not leap year");
    }

    @Test
    public void isValidYearMonth_day28_validForAllMonths() {
        DayOfMonth test = DayOfMonth.of(28);
        for (int month = 1; month <= 12; month++) {
            assertTrue(test.isValidYearMonth(YearMonth.of(2012, month)), 
                "Day 28 should be valid for all months");
        }
    }

    @Test
    public void isValidYearMonth_nullYearMonth_returnsFalse() {
        assertFalse(TEST.isValidYearMonth(null));
    }

    //-----------------------------------------------------------------------
    // query() Tests
    //-----------------------------------------------------------------------
    @Test
    public void query_returnsChronology() {
        assertEquals(IsoChronology.INSTANCE, TEST.query(TemporalQueries.chronology()));
    }

    @Test
    public void query_unsupportedTypes_returnNull() {
        assertNull(TEST.query(TemporalQueries.localDate()));
        assertNull(TEST.query(TemporalQueries.localTime()));
        assertNull(TEST.query(TemporalQueries.offset()));
        assertNull(TEST.query(TemporalQueries.precision()));
        assertNull(TEST.query(TemporalQueries.zone()));
        assertNull(TEST.query(TemporalQueries.zoneId()));
    }

    //-----------------------------------------------------------------------
    // adjustInto() Tests
    //-----------------------------------------------------------------------
    @Test
    public void adjustInto_validDays_success() {
        LocalDate base = LocalDate.of(2007, 1, 1);
        for (int i = 1; i <= MAX_LENGTH; i++) {
            Temporal result = DayOfMonth.of(i).adjustInto(base);
            assertEquals(LocalDate.of(2007, 1, i), result, 
                "Should adjust to day " + i + " of January");
        }
    }

    @Test
    public void adjustInto_april31_throwsException() {
        LocalDate base = LocalDate.of(2007, 4, 1);
        DayOfMonth test = DayOfMonth.of(31);
        assertThrows(DateTimeException.class, () -> test.adjustInto(base));
    }

    @Test
    public void adjustInto_february29_nonLeapYear_throwsException() {
        LocalDate base = LocalDate.of(2007, 2, 1);
        DayOfMonth test = DayOfMonth.of(29);
        assertThrows(DateTimeException.class, () -> test.adjustInto(base));
    }

    @Test
    public void adjustInto_nonIsoChronology_throwsException() {
        assertThrows(DateTimeException.class, () -> TEST.adjustInto(JapaneseDate.now()));
    }

    @Test
    public void adjustInto_nullTemporal_throwsException() {
        assertThrows(NullPointerException.class, () -> TEST.adjustInto((Temporal) null));
    }

    //-----------------------------------------------------------------------
    // atMonth() Tests
    //-----------------------------------------------------------------------
    @Test
    public void atMonth_withMonthEnum_day31_adjustsToLastDayForShortMonths() {
        DayOfMonth test = DayOfMonth.of(31);
        assertEquals(MonthDay.of(1, 31), test.atMonth(JANUARY));
        assertEquals(MonthDay.of(2, 29), test.atMonth(FEBRUARY)); // Leap year adjustment
        assertEquals(MonthDay.of(3, 31), test.atMonth(MARCH));
        assertEquals(MonthDay.of(4, 30), test.atMonth(APRIL));    // Adjusts to 30
        assertEquals(MonthDay.of(5, 31), test.atMonth(MAY));
        assertEquals(MonthDay.of(6, 30), test.atMonth(JUNE));     // Adjusts to 30
        assertEquals(MonthDay.of(7, 31), test.atMonth(JULY));
        assertEquals(MonthDay.of(8, 31), test.atMonth(AUGUST));
        assertEquals(MonthDay.of(9, 30), test.atMonth(SEPTEMBER)); // Adjusts to 30
        assertEquals(MonthDay.of(10, 31), test.atMonth(OCTOBER));
        assertEquals(MonthDay.of(11, 30), test.atMonth(NOVEMBER)); // Adjusts to 30
        assertEquals(MonthDay.of(12, 31), test.atMonth(DECEMBER));
    }

    @Test
    public void atMonth_withMonthEnum_day28_validForAllMonths() {
        DayOfMonth test = DayOfMonth.of(28);
        for (Month month : Month.values()) {
            assertEquals(MonthDay.of(month, 28), test.atMonth(month),
                "Should create MonthDay for 28th of " + month);
        }
    }

    @Test
    public void atMonth_nullMonth_throwsException() {
        assertThrows(NullPointerException.class, () -> TEST.atMonth((Month) null));
    }

    @Test
    public void atMonth_withMonthInt_day31_adjustsToLastDayForShortMonths() {
        DayOfMonth test = DayOfMonth.of(31);
        assertEquals(MonthDay.of(1, 31), test.atMonth(1));
        assertEquals(MonthDay.of(2, 29), test.atMonth(2)); // Leap year adjustment
        assertEquals(MonthDay.of(3, 31), test.atMonth(3));
        assertEquals(MonthDay.of(4, 30), test.atMonth(4));    // Adjusts to 30
        assertEquals(MonthDay.of(5, 31), test.atMonth(5));
        assertEquals(MonthDay.of(6, 30), test.atMonth(6));     // Adjusts to 30
        assertEquals(MonthDay.of(7, 31), test.atMonth(7));
        assertEquals(MonthDay.of(8, 31), test.atMonth(8));
        assertEquals(MonthDay.of(9, 30), test.atMonth(9));     // Adjusts to 30
        assertEquals(MonthDay.of(10, 31), test.atMonth(10));
        assertEquals(MonthDay.of(11, 30), test.atMonth(11));   // Adjusts to 30
        assertEquals(MonthDay.of(12, 31), test.atMonth(12));
    }

    @Test
    public void atMonth_invalidMonth_throwsException() {
        assertThrows(DateTimeException.class, () -> TEST.atMonth(0));
        assertThrows(DateTimeException.class, () -> TEST.atMonth(13));
    }

    //-----------------------------------------------------------------------
    // atYearMonth() Tests
    //-----------------------------------------------------------------------
    @Test
    public void atYearMonth_day31_adjustsToLastDayForShortMonths() {
        DayOfMonth test = DayOfMonth.of(31);
        assertEquals(LocalDate.of(2012, 1, 31), test.atYearMonth(YearMonth.of(2012, 1)));
        assertEquals(LocalDate.of(2012, 2, 29), test.atYearMonth(YearMonth.of(2012, 2))); // Leap year
        assertEquals(LocalDate.of(2012, 3, 31), test.atYearMonth(YearMonth.of(2012, 3)));
        assertEquals(LocalDate.of(2012, 4, 30), test.atYearMonth(YearMonth.of(2012, 4))); // Adjusts to 30
        assertEquals(LocalDate.of(2011, 2, 28), test.atYearMonth(YearMonth.of(2011, 2))); // Non-leap year
    }

    @Test
    public void atYearMonth_day28_validForAllYearMonths() {
        DayOfMonth test = DayOfMonth.of(28);
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(2012, month);
            assertEquals(LocalDate.of(2012, month, 28), test.atYearMonth(yearMonth),
                "Should create date for 28th of " + yearMonth);
        }
    }

    @Test
    public void atYearMonth_nullYearMonth_throwsException() {
        assertThrows(NullPointerException.class, () -> TEST.atYearMonth(null));
    }

    //-----------------------------------------------------------------------
    // compareTo() Tests
    //-----------------------------------------------------------------------
    @Test
    public void compareTo_allDayCombinations_correctOrdering() {
        for (int i = 1; i <= MAX_LENGTH; i++) {
            DayOfMonth a = DayOfMonth.of(i);
            for (int j = 1; j <= MAX_LENGTH; j++) {
                DayOfMonth b = DayOfMonth.of(j);
                int cmp = Integer.compare(i, j);
                assertEquals(cmp, a.compareTo(b), 
                    "Comparison mismatch: " + i + " vs " + j);
            }
        }
    }

    @Test
    public void compareTo_null_throwsException() {
        DayOfMonth test = DayOfMonth.of(1);
        assertThrows(NullPointerException.class, () -> test.compareTo(null));
    }

    //-----------------------------------------------------------------------
    // equals() and hashCode() Tests
    //-----------------------------------------------------------------------
    @Test
    public void equals_and_hashCode_consistent() {
        EqualsTester tester = new EqualsTester();
        for (int i = 1; i <= MAX_LENGTH; i++) {
            tester.addEqualityGroup(DayOfMonth.of(i), DayOfMonth.of(i));
        }
        tester.testEquals();
    }

    //-----------------------------------------------------------------------
    // toString() Tests
    //-----------------------------------------------------------------------
    @Test
    public void toString_returnsDescriptiveString() {
        for (int i = 1; i <= MAX_LENGTH; i++) {
            assertEquals("DayOfMonth:" + i, DayOfMonth.of(i).toString());
        }
    }

    //-----------------------------------------------------------------------
    // now(Clock) Tests
    //-----------------------------------------------------------------------
    @Test
    public void now_withFixedClock_returnsExpectedDay() {
        for (int i = 1; i <= 31; i++) {
            Instant instant = LocalDate.of(2008, 1, i).atStartOfDay(PARIS).toInstant();
            Clock clock = Clock.fixed(instant, PARIS);
            assertEquals(i, DayOfMonth.now(clock).getValue(), 
                "Should return day " + i + " for fixed clock");
        }
    }
}