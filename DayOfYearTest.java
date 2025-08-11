/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 * [License text remains the same...]
 */
package org.threeten.extra;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import com.google.common.testing.EqualsTester;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test DayOfYear.
 */
public class TestDayOfYear {

    // Test data constants
    private static final Year STANDARD_YEAR = Year.of(2007);
    private static final Year LEAP_YEAR = Year.of(2008);
    private static final int DAYS_IN_STANDARD_YEAR = 365;
    private static final int DAYS_IN_LEAP_YEAR = 366;
    private static final int SAMPLE_DAY_VALUE = 12;
    private static final DayOfYear SAMPLE_DAY = DayOfYear.of(SAMPLE_DAY_VALUE);
    private static final ZoneId PARIS_ZONE = ZoneId.of("Europe/Paris");

    /**
     * Custom temporal field for testing field delegation behavior.
     * This field mimics DAY_OF_YEAR behavior to test how DayOfYear handles custom fields.
     */
    private static class CustomDayOfYearField implements TemporalField {
        public static final CustomDayOfYearField INSTANCE = new CustomDayOfYearField();

        @Override
        public TemporalUnit getBaseUnit() {
            return ChronoUnit.DAYS;
        }

        @Override
        public TemporalUnit getRangeUnit() {
            return ChronoUnit.YEARS;
        }

        @Override
        public ValueRange range() {
            return ValueRange.of(1, 365, 366);
        }

        @Override
        public boolean isDateBased() {
            return true;
        }

        @Override
        public boolean isTimeBased() {
            return false;
        }

        @Override
        public boolean isSupportedBy(TemporalAccessor temporal) {
            return temporal.isSupported(ChronoField.DAY_OF_YEAR);
        }

        @Override
        public ValueRange rangeRefinedBy(TemporalAccessor temporal) {
            return range();
        }

        @Override
        public long getFrom(TemporalAccessor temporal) {
            return temporal.getLong(ChronoField.DAY_OF_YEAR);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R extends Temporal> R adjustInto(R temporal, long newValue) {
            return (R) temporal.with(ChronoField.DAY_OF_YEAR, newValue);
        }
    }

    //-----------------------------------------------------------------------
    // Basic interface compliance tests
    //-----------------------------------------------------------------------
    
    @Test
    public void shouldImplementRequiredInterfaces() {
        assertTrue(Serializable.class.isAssignableFrom(DayOfYear.class));
        assertTrue(Comparable.class.isAssignableFrom(DayOfYear.class));
        assertTrue(TemporalAdjuster.class.isAssignableFrom(DayOfYear.class));
        assertTrue(TemporalAccessor.class.isAssignableFrom(DayOfYear.class));
    }

    @Test
    public void shouldSerializeAndDeserializeCorrectly() throws IOException, ClassNotFoundException {
        DayOfYear originalDay = DayOfYear.of(1);
        
        // Serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(originalDay);
        }
        
        // Deserialize
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            DayOfYear deserializedDay = (DayOfYear) ois.readObject();
            assertSame(originalDay, deserializedDay, "Should return same cached instance");
        }
    }

    //-----------------------------------------------------------------------
    // Factory method tests - now()
    //-----------------------------------------------------------------------
    
    @RetryingTest(100)
    public void now_shouldReturnCurrentDayOfYear() {
        int expectedDayOfYear = LocalDate.now().getDayOfYear();
        int actualDayOfYear = DayOfYear.now().getValue();
        assertEquals(expectedDayOfYear, actualDayOfYear);
    }

    @RetryingTest(100)
    public void now_withZoneId_shouldReturnCurrentDayOfYearInSpecifiedZone() {
        ZoneId tokyoZone = ZoneId.of("Asia/Tokyo");
        int expectedDayOfYear = LocalDate.now(tokyoZone).getDayOfYear();
        int actualDayOfYear = DayOfYear.now(tokyoZone).getValue();
        assertEquals(expectedDayOfYear, actualDayOfYear);
    }

    @Test
    public void now_withClock_shouldReturnDayOfYearFromClock_standardYear() {
        LocalDate startDate = LocalDate.of(2007, 1, 1);
        
        for (int expectedDay = 1; expectedDay <= DAYS_IN_STANDARD_YEAR; expectedDay++) {
            LocalDate currentDate = startDate.plusDays(expectedDay - 1);
            Instant instant = currentDate.atStartOfDay(PARIS_ZONE).toInstant();
            Clock fixedClock = Clock.fixed(instant, PARIS_ZONE);
            
            DayOfYear actualDay = DayOfYear.now(fixedClock);
            assertEquals(expectedDay, actualDay.getValue());
        }
    }

    @Test
    public void now_withClock_shouldReturnDayOfYearFromClock_leapYear() {
        LocalDate startDate = LocalDate.of(2008, 1, 1);
        
        for (int expectedDay = 1; expectedDay <= DAYS_IN_LEAP_YEAR; expectedDay++) {
            LocalDate currentDate = startDate.plusDays(expectedDay - 1);
            Instant instant = currentDate.atStartOfDay(PARIS_ZONE).toInstant();
            Clock fixedClock = Clock.fixed(instant, PARIS_ZONE);
            
            DayOfYear actualDay = DayOfYear.now(fixedClock);
            assertEquals(expectedDay, actualDay.getValue());
        }
    }

    //-----------------------------------------------------------------------
    // Factory method tests - of(int)
    //-----------------------------------------------------------------------
    
    @Test
    public void of_shouldCreateDayOfYearForValidValues() {
        for (int dayValue = 1; dayValue <= DAYS_IN_LEAP_YEAR; dayValue++) {
            DayOfYear day = DayOfYear.of(dayValue);
            assertEquals(dayValue, day.getValue());
            assertSame(day, DayOfYear.of(dayValue), "Should return cached instance");
        }
    }

    @Test
    public void of_shouldThrowException_whenValueTooLow() {
        assertThrows(DateTimeException.class, () -> DayOfYear.of(0));
    }

    @Test
    public void of_shouldThrowException_whenValueTooHigh() {
        assertThrows(DateTimeException.class, () -> DayOfYear.of(367));
    }

    //-----------------------------------------------------------------------
    // Factory method tests - from(TemporalAccessor)
    //-----------------------------------------------------------------------
    
    @Test
    public void from_shouldExtractDayOfYear_standardYear() {
        LocalDate startDate = LocalDate.of(2007, 1, 1);
        
        for (int expectedDay = 1; expectedDay <= DAYS_IN_STANDARD_YEAR; expectedDay++) {
            LocalDate currentDate = startDate.plusDays(expectedDay - 1);
            DayOfYear actualDay = DayOfYear.from(currentDate);
            assertEquals(expectedDay, actualDay.getValue());
        }
        
        // Test year rollover
        LocalDate nextYearStart = startDate.plusDays(DAYS_IN_STANDARD_YEAR);
        DayOfYear dayAfterYearEnd = DayOfYear.from(nextYearStart);
        assertEquals(1, dayAfterYearEnd.getValue());
    }

    @Test
    public void from_shouldExtractDayOfYear_leapYear() {
        LocalDate startDate = LocalDate.of(2008, 1, 1);
        
        for (int expectedDay = 1; expectedDay <= DAYS_IN_LEAP_YEAR; expectedDay++) {
            LocalDate currentDate = startDate.plusDays(expectedDay - 1);
            DayOfYear actualDay = DayOfYear.from(currentDate);
            assertEquals(expectedDay, actualDay.getValue());
        }
    }

    @Test
    public void from_shouldReturnSameInstance_whenPassedDayOfYear() {
        DayOfYear originalDay = DayOfYear.of(6);
        DayOfYear extractedDay = DayOfYear.from(originalDay);
        assertEquals(originalDay, extractedDay);
    }

    @Test
    public void from_shouldWorkWithNonIsoChronology() {
        LocalDate isoDate = LocalDate.now();
        JapaneseDate japaneseDate = JapaneseDate.from(isoDate);
        
        DayOfYear dayFromIso = DayOfYear.from(isoDate);
        DayOfYear dayFromJapanese = DayOfYear.from(japaneseDate);
        
        assertEquals(dayFromIso.getValue(), dayFromJapanese.getValue());
    }

    @Test
    public void from_shouldThrowException_whenCannotExtractDayOfYear() {
        assertThrows(DateTimeException.class, () -> DayOfYear.from(LocalTime.NOON));
    }

    @Test
    public void from_shouldThrowException_whenPassedNull() {
        assertThrows(NullPointerException.class, () -> DayOfYear.from((TemporalAccessor) null));
    }

    @Test
    public void from_shouldWorkWithDateTimeFormatter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("D");
        DayOfYear parsedDay = formatter.parse("76", DayOfYear::from);
        assertEquals(DayOfYear.of(76), parsedDay);
    }

    //-----------------------------------------------------------------------
    // Field support tests
    //-----------------------------------------------------------------------
    
    @Test
    public void isSupported_shouldReturnTrue_onlyForDayOfYearField() {
        // Supported field
        assertTrue(SAMPLE_DAY.isSupported(ChronoField.DAY_OF_YEAR));
        assertTrue(SAMPLE_DAY.isSupported(CustomDayOfYearField.INSTANCE));
        
        // Unsupported fields - time-based
        assertFalse(SAMPLE_DAY.isSupported(ChronoField.NANO_OF_SECOND));
        assertFalse(SAMPLE_DAY.isSupported(ChronoField.HOUR_OF_DAY));
        assertFalse(SAMPLE_DAY.isSupported(ChronoField.MINUTE_OF_HOUR));
        
        // Unsupported fields - date-based
        assertFalse(SAMPLE_DAY.isSupported(ChronoField.DAY_OF_WEEK));
        assertFalse(SAMPLE_DAY.isSupported(ChronoField.DAY_OF_MONTH));
        assertFalse(SAMPLE_DAY.isSupported(ChronoField.MONTH_OF_YEAR));
        assertFalse(SAMPLE_DAY.isSupported(ChronoField.YEAR));
        
        // Null field
        assertFalse(SAMPLE_DAY.isSupported((TemporalField) null));
    }

    @Test
    public void range_shouldReturnDayOfYearRange_forSupportedField() {
        ValueRange expectedRange = ChronoField.DAY_OF_YEAR.range();
        ValueRange actualRange = SAMPLE_DAY.range(ChronoField.DAY_OF_YEAR);
        assertEquals(expectedRange, actualRange);
    }

    @Test
    public void range_shouldThrowException_forUnsupportedField() {
        assertThrows(UnsupportedTemporalTypeException.class, 
                    () -> SAMPLE_DAY.range(ChronoField.MONTH_OF_YEAR));
    }

    @Test
    public void range_shouldThrowException_whenPassedNull() {
        assertThrows(NullPointerException.class, 
                    () -> SAMPLE_DAY.range((TemporalField) null));
    }

    @Test
    public void get_shouldReturnDayValue_forSupportedField() {
        assertEquals(SAMPLE_DAY_VALUE, SAMPLE_DAY.get(ChronoField.DAY_OF_YEAR));
    }

    @Test
    public void get_shouldThrowException_forUnsupportedField() {
        assertThrows(UnsupportedTemporalTypeException.class, 
                    () -> SAMPLE_DAY.get(ChronoField.MONTH_OF_YEAR));
    }

    @Test
    public void get_shouldThrowException_whenPassedNull() {
        assertThrows(NullPointerException.class, 
                    () -> SAMPLE_DAY.get((TemporalField) null));
    }

    @Test
    public void getLong_shouldReturnDayValue_forSupportedField() {
        assertEquals(SAMPLE_DAY_VALUE, SAMPLE_DAY.getLong(ChronoField.DAY_OF_YEAR));
    }

    @Test
    public void getLong_shouldDelegateToCustomField() {
        assertEquals(SAMPLE_DAY_VALUE, SAMPLE_DAY.getLong(CustomDayOfYearField.INSTANCE));
    }

    @Test
    public void getLong_shouldThrowException_forUnsupportedField() {
        assertThrows(UnsupportedTemporalTypeException.class, 
                    () -> SAMPLE_DAY.getLong(ChronoField.MONTH_OF_YEAR));
        assertThrows(UnsupportedTemporalTypeException.class, 
                    () -> SAMPLE_DAY.getLong(IsoFields.DAY_OF_QUARTER));
    }

    @Test
    public void getLong_shouldThrowException_whenPassedNull() {
        assertThrows(NullPointerException.class, 
                    () -> SAMPLE_DAY.getLong((TemporalField) null));
    }

    //-----------------------------------------------------------------------
    // Year validation tests
    //-----------------------------------------------------------------------
    
    @Test
    public void isValidYear_shouldReturnFalse_forDay366InStandardYear() {
        DayOfYear day366 = DayOfYear.of(366);
        assertFalse(day366.isValidYear(2011), "Day 366 should be invalid in standard year 2011");
        assertFalse(day366.isValidYear(2013), "Day 366 should be invalid in standard year 2013");
    }

    @Test
    public void isValidYear_shouldReturnTrue_forDay366InLeapYear() {
        DayOfYear day366 = DayOfYear.of(366);
        assertTrue(day366.isValidYear(2012), "Day 366 should be valid in leap year 2012");
    }

    @Test
    public void isValidYear_shouldReturnTrue_forDay365InAnyYear() {
        DayOfYear day365 = DayOfYear.of(365);
        assertTrue(day365.isValidYear(2011), "Day 365 should be valid in standard year");
        assertTrue(day365.isValidYear(2012), "Day 365 should be valid in leap year");
        assertTrue(day365.isValidYear(2013), "Day 365 should be valid in standard year");
    }

    //-----------------------------------------------------------------------
    // Query tests
    //-----------------------------------------------------------------------
    
    @Test
    public void query_shouldReturnExpectedValues() {
        assertEquals(IsoChronology.INSTANCE, SAMPLE_DAY.query(TemporalQueries.chronology()));
        assertNull(SAMPLE_DAY.query(TemporalQueries.localDate()));
        assertNull(SAMPLE_DAY.query(TemporalQueries.localTime()));
        assertNull(SAMPLE_DAY.query(TemporalQueries.offset()));
        assertNull(SAMPLE_DAY.query(TemporalQueries.precision()));
        assertNull(SAMPLE_DAY.query(TemporalQueries.zone()));
        assertNull(SAMPLE_DAY.query(TemporalQueries.zoneId()));
    }

    //-----------------------------------------------------------------------
    // Temporal adjustment tests
    //-----------------------------------------------------------------------
    
    @Test
    public void adjustInto_shouldSetDayOfYear_standardYear() {
        LocalDate baseDate = LocalDate.of(2007, 1, 1);
        
        for (int dayValue = 1; dayValue <= DAYS_IN_STANDARD_YEAR; dayValue++) {
            DayOfYear dayOfYear = DayOfYear.of(dayValue);
            LocalDate expectedDate = LocalDate.of(2007, 1, 1).plusDays(dayValue - 1);
            LocalDate actualDate = dayOfYear.adjustInto(baseDate);
            assertEquals(expectedDate, actualDate);
        }
    }

    @Test
    public void adjustInto_shouldSetDayOfYear_fromEndOfStandardYear() {
        LocalDate endOfYear = LocalDate.of(2007, 12, 31);
        LocalDate expectedStart = LocalDate.of(2007, 1, 1);
        
        for (int dayValue = 1; dayValue <= DAYS_IN_STANDARD_YEAR; dayValue++) {
            DayOfYear dayOfYear = DayOfYear.of(dayValue);
            LocalDate expectedDate = expectedStart.plusDays(dayValue - 1);
            LocalDate actualDate = dayOfYear.adjustInto(endOfYear);
            assertEquals(expectedDate, actualDate);
        }
    }

    @Test
    public void adjustInto_shouldThrowException_forDay366InStandardYear() {
        LocalDate standardYearDate = LocalDate.of(2007, 1, 1);
        DayOfYear day366 = DayOfYear.of(DAYS_IN_LEAP_YEAR);
        
        assertThrows(DateTimeException.class, () -> day366.adjustInto(standardYearDate));
        
        LocalDate endOfStandardYear = LocalDate.of(2007, 12, 31);
        assertThrows(DateTimeException.class, () -> day366.adjustInto(endOfStandardYear));
    }

    @Test
    public void adjustInto_shouldSetDayOfYear_leapYear() {
        LocalDate baseDate = LocalDate.of(2008, 1, 1);
        
        for (int dayValue = 1; dayValue <= DAYS_IN_LEAP_YEAR; dayValue++) {
            DayOfYear dayOfYear = DayOfYear.of(dayValue);
            LocalDate expectedDate = LocalDate.of(2008, 1, 1).plusDays(dayValue - 1);
            LocalDate actualDate = dayOfYear.adjustInto(baseDate);
            assertEquals(expectedDate, actualDate);
        }
    }

    @Test
    public void adjustInto_shouldSetDayOfYear_fromEndOfLeapYear() {
        LocalDate endOfLeapYear = LocalDate.of(2008, 12, 31);
        LocalDate expectedStart = LocalDate.of(2008, 1, 1);
        
        for (int dayValue = 1; dayValue <= DAYS_IN_LEAP_YEAR; dayValue++) {
            DayOfYear dayOfYear = DayOfYear.of(dayValue);
            LocalDate expectedDate = expectedStart.plusDays(dayValue - 1);
            LocalDate actualDate = dayOfYear.adjustInto(endOfLeapYear);
            assertEquals(expectedDate, actualDate);
        }
    }

    @Test
    public void adjustInto_shouldThrowException_forNonIsoChronology() {
        assertThrows(DateTimeException.class, () -> SAMPLE_DAY.adjustInto(JapaneseDate.now()));
    }

    @Test
    public void adjustInto_shouldThrowException_whenPassedNull() {
        assertThrows(NullPointerException.class, () -> SAMPLE_DAY.adjustInto((Temporal) null));
    }

    //-----------------------------------------------------------------------
    // Date creation tests - atYear()
    //-----------------------------------------------------------------------
    
    @Test
    public void atYear_withYearObject_shouldCreateCorrectDate_standardYear() {
        LocalDate expectedStart = LocalDate.of(2007, 1, 1);
        
        for (int dayValue = 1; dayValue <= DAYS_IN_STANDARD_YEAR; dayValue++) {
            DayOfYear dayOfYear = DayOfYear.of(dayValue);
            LocalDate expectedDate = expectedStart.plusDays(dayValue - 1);
            LocalDate actualDate = dayOfYear.atYear(STANDARD_YEAR);
            assertEquals(expectedDate, actualDate);
        }
    }

    @Test
    public void atYear_withYearObject_shouldThrowException_forDay366InStandardYear() {
        DayOfYear day366 = DayOfYear.of(DAYS_IN_LEAP_YEAR);
        assertThrows(DateTimeException.class, () -> day366.atYear(STANDARD_YEAR));
    }

    @Test
    public void atYear_withYearObject_shouldCreateCorrectDate_leapYear() {
        LocalDate expectedStart = LocalDate.of(2008, 1, 1);
        
        for (int dayValue = 1; dayValue <= DAYS_IN_LEAP_YEAR; dayValue++) {
            DayOfYear dayOfYear = DayOfYear.of(dayValue);
            LocalDate expectedDate = expectedStart.plusDays(dayValue - 1);
            LocalDate actualDate = dayOfYear.atYear(LEAP_YEAR);
            assertEquals(expectedDate, actualDate);
        }
    }

    @Test
    public void atYear_withYearObject_shouldThrowException_whenPassedNull() {
        assertThrows(NullPointerException.class, () -> SAMPLE_DAY.atYear((Year) null));
    }

    @Test
    public void atYear_withIntYear_shouldCreateCorrectDate_standardYear() {
        LocalDate expectedStart = LocalDate.of(2007, 1, 1);
        
        for (int dayValue = 1; dayValue <= DAYS_IN_STANDARD_YEAR; dayValue++) {
            DayOfYear dayOfYear = DayOfYear.of(dayValue);
            LocalDate expectedDate = expectedStart.plusDays(dayValue - 1);
            LocalDate actualDate = dayOfYear.atYear(2007);
            assertEquals(expectedDate, actualDate);
        }
    }

    @Test
    public void atYear_withIntYear_shouldThrowException_forDay366InStandardYear() {
        DayOfYear day366 = DayOfYear.of(DAYS_IN_LEAP_YEAR);
        assertThrows(DateTimeException.class, () -> day366.atYear(2007));
    }

    @Test
    public void atYear_withIntYear_shouldCreateCorrectDate_leapYear() {
        LocalDate expectedStart = LocalDate.of(2008, 1, 1);
        
        for (int dayValue = 1; dayValue <= DAYS_IN_LEAP_YEAR; dayValue++) {
            DayOfYear dayOfYear = DayOfYear.of(dayValue);
            LocalDate expectedDate = expectedStart.plusDays(dayValue - 1);
            LocalDate actualDate = dayOfYear.atYear(2008);
            assertEquals(expectedDate, actualDate);
        }
    }

    @Test
    public void atYear_withIntYear_shouldThrowException_forInvalidYear() {
        assertThrows(DateTimeException.class, () -> SAMPLE_DAY.atYear(Year.MIN_VALUE - 1));
    }

    //-----------------------------------------------------------------------
    // Comparison tests
    //-----------------------------------------------------------------------
    
    @Test
    public void compareTo_shouldCompareByDayValue() {
        for (int i = 1; i <= DAYS_IN_LEAP_YEAR; i++) {
            DayOfYear dayA = DayOfYear.of(i);
            for (int j = 1; j <= DAYS_IN_LEAP_YEAR; j++) {
                DayOfYear dayB = DayOfYear.of(j);
                
                if (i < j) {
                    assertTrue(dayA.compareTo(dayB) < 0, 
                              String.format("Day %d should be less than day %d", i, j));
                    assertTrue(dayB.compareTo(dayA) > 0, 
                              String.format("Day %d should be greater than day %d", j, i));
                } else if (i > j) {
                    assertTrue(dayA.compareTo(dayB) > 0, 
                              String.format("Day %d should be greater than day %d", i, j));
                    assertTrue(dayB.compareTo(dayA) < 0, 
                              String.format("Day %d should be less than day %d", j, i));
                } else {
                    assertEquals(0, dayA.compareTo(dayB), 
                                String.format("Day %d should equal day %d", i, j));
                    assertEquals(0, dayB.compareTo(dayA), 
                                String.format("Day %d should equal day %d", j, i));
                }
            }
        }
    }

    @Test
    public void compareTo_shouldThrowException_whenPassedNull() {
        DayOfYear testDay = DayOfYear.of(1);
        assertThrows(NullPointerException.class, () -> testDay.compareTo(null));
    }

    //-----------------------------------------------------------------------
    // Equality tests
    //-----------------------------------------------------------------------
    
    @Test
    public void equals_and_hashCode_shouldFollowContract() {
        EqualsTester equalsTester = new EqualsTester();
        for (int dayValue = 1; dayValue <= DAYS_IN_LEAP_YEAR; dayValue++) {
            equalsTester.addEqualityGroup(DayOfYear.of(dayValue), DayOfYear.of(dayValue));
        }
        equalsTester.testEquals();
    }

    //-----------------------------------------------------------------------
    // String representation tests
    //-----------------------------------------------------------------------
    
    @Test
    public void toString_shouldReturnExpectedFormat() {
        for (int dayValue = 1; dayValue <= DAYS_IN_LEAP_YEAR; dayValue++) {
            DayOfYear day = DayOfYear.of(dayValue);
            String expectedString = "DayOfYear:" + dayValue;
            assertEquals(expectedString, day.toString());
        }
    }
}