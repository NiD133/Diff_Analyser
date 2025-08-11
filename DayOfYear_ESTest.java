package org.threeten.extra;

import org.junit.Test;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.HijrahDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * A more understandable and maintainable test suite for {@link DayOfYear}.
 */
public class DayOfYearTest {

    private static final DayOfYear DAY_1 = DayOfYear.of(1);
    private static final DayOfYear DAY_45 = DayOfYear.of(45);
    private static final DayOfYear DAY_366 = DayOfYear.of(366);

    //region Factory methods: of(), now(), from()

    @Test
    public void testOf_validValues_succeeds() {
        assertEquals(1, DayOfYear.of(1).getValue());
        assertEquals(366, DayOfYear.of(366).getValue());
    }

    @Test(expected = DateTimeException.class)
    public void testOf_invalidDay_zero_throwsException() {
        DayOfYear.of(0);
    }

    @Test(expected = DateTimeException.class)
    public void testOf_invalidDay_367_throwsException() {
        DayOfYear.of(367);
    }

    @Test
    public void testNow_withFixedClock() {
        // Arrange: A fixed clock at Feb 14, 2023 (day 45)
        Clock fixedClock = Clock.fixed(Instant.parse("2023-02-14T10:00:00Z"), ZoneOffset.UTC);

        // Act & Assert
        assertEquals(DAY_45, DayOfYear.now(fixedClock));
    }

    @Test(expected = NullPointerException.class)
    public void testNow_withNullZone_throwsException() {
        DayOfYear.now((ZoneId) null);
    }

    @Test(expected = NullPointerException.class)
    public void testNow_withNullClock_throwsException() {
        DayOfYear.now((Clock) null);
    }

    @Test
    public void testFrom_withLocalDate() {
        // Arrange: Feb 14 is the 45th day of the year
        LocalDate date = LocalDate.of(2023, 2, 14);

        // Act & Assert
        assertEquals(DAY_45, DayOfYear.from(date));
    }

    @Test
    public void testFrom_withDayOfYear() {
        assertEquals(DAY_45, DayOfYear.from(DAY_45));
    }

    @Test(expected = DateTimeException.class)
    public void testFrom_temporalWithoutDayOfYear_throwsException() {
        // Month does not have a DAY_OF_YEAR field
        DayOfYear.from(Month.NOVEMBER);
    }

    @Test(expected = NullPointerException.class)
    public void testFrom_withNullTemporal_throwsException() {
        DayOfYear.from(null);
    }

    //endregion

    //region Accessors: getValue(), isSupported(), get(), getLong(), range()

    @Test
    public void testGetValue() {
        assertEquals(45, DAY_45.getValue());
    }

    @Test
    public void testIsSupported() {
        assertTrue(DAY_45.isSupported(ChronoField.DAY_OF_YEAR));
        assertFalse(DAY_45.isSupported(ChronoField.DAY_OF_MONTH));
        assertFalse(DAY_45.isSupported(null));
    }

    @Test
    public void testGet_forDayOfYearField_returnsValue() {
        assertEquals(45, DAY_45.get(ChronoField.DAY_OF_YEAR));
    }

    @Test
    public void testGetLong_forDayOfYearField_returnsValue() {
        assertEquals(45L, DAY_45.getLong(ChronoField.DAY_OF_YEAR));
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void testGet_forUnsupportedField_throwsException() {
        DAY_1.get(ChronoField.ERA);
    }

    @Test(expected = NullPointerException.class)
    public void testGet_withNullField_throwsException() {
        DAY_1.get(null);
    }

    @Test
    public void testRange_forDayOfYearField() {
        ValueRange expectedRange = ValueRange.of(1, 366);
        assertEquals(expectedRange, DAY_45.range(ChronoField.DAY_OF_YEAR));
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void testRange_forUnsupportedField_throwsException() {
        DAY_1.range(ChronoField.SECOND_OF_MINUTE);
    }

    //endregion

    //region Querying: query()

    @Test
    public void testQuery_withSupportedAndUnsupportedTypes() {
        // A query for a supported type returns the value
        assertEquals(IsoChronology.INSTANCE, DAY_45.query(TemporalQueries.chronology()));
        
        // A query for an unsupported type returns null
        assertNull(DAY_45.query(TemporalQueries.zone()));
    }

    @Test(expected = DateTimeException.class)
    public void testQuery_forYear_throwsException() {
        // DayOfYear does not contain year information
        DAY_45.query(TemporalQueries.year());
    }

    @Test(expected = NullPointerException.class)
    public void testQuery_withNull_throwsException() {
        DAY_1.query(null);
    }

    //endregion

    //region Adjusting: adjustInto()

    @Test
    public void testAdjustInto_setsDayOfYearOnLocalDate() {
        LocalDate initialDate = LocalDate.of(2024, 1, 1); // Day 1 of a leap year
        LocalDate expectedDate = LocalDate.of(2024, 2, 14); // Day 45 of 2024

        LocalDate adjustedDate = (LocalDate) DAY_45.adjustInto(initialDate);

        assertEquals(expectedDate, adjustedDate);
    }

    @Test(expected = DateTimeException.class)
    public void testAdjustInto_day366InNonLeapYear_throwsException() {
        LocalDate nonLeapYearDate = LocalDate.of(2023, 1, 1);
        DAY_366.adjustInto(nonLeapYearDate);
    }

    @Test(expected = DateTimeException.class)
    public void testAdjustInto_withNonIsoChronology_throwsException() {
        HijrahDate hijrahDate = HijrahDate.now();
        DAY_45.adjustInto(hijrahDate);
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void testAdjustInto_withUnsupportedTemporal_throwsException() {
        YearMonth yearMonth = YearMonth.of(2023, 2);
        DAY_45.adjustInto(yearMonth);
    }

    //endregion

    //region Combination: atYear(), isValidYear()

    @Test
    public void testAtYear() {
        LocalDate expected = LocalDate.of(2024, 2, 14);
        assertEquals(expected, DAY_45.atYear(2024));
        assertEquals(expected, DAY_45.atYear(Year.of(2024)));
    }

    @Test
    public void testAtYear_day366_inLeapYear() {
        LocalDate expected = LocalDate.of(2024, 12, 31);
        assertEquals(expected, DAY_366.atYear(2024));
        assertEquals(expected, DAY_366.atYear(Year.of(2024)));
    }

    @Test(expected = DateTimeException.class)
    public void testAtYear_day366_inNonLeapYear_usingInt_throwsException() {
        DAY_366.atYear(2023);
    }

    @Test(expected = DateTimeException.class)
    public void testAtYear_day366_inNonLeapYear_usingYear_throwsException() {
        DAY_366.atYear(Year.of(2023));
    }

    @Test
    public void testIsValidYear() {
        // A regular day is valid in any year
        assertTrue(DayOfYear.of(50).isValidYear(2023)); // non-leap
        assertTrue(DayOfYear.of(50).isValidYear(2024)); // leap

        // Day 366 is only valid in leap years
        assertTrue(DAY_366.isValidYear(2024)); // leap year
        assertFalse(DAY_366.isValidYear(2023)); // non-leap year
    }

    //endregion

    //region Comparison: compareTo(), equals(), hashCode()

    @Test
    public void testCompareTo() {
        assertTrue(DAY_1.compareTo(DAY_45) < 0);
        assertTrue(DAY_45.compareTo(DAY_1) > 0);
        assertEquals(0, DAY_45.compareTo(DayOfYear.of(45)));
    }

    @Test(expected = NullPointerException.class)
    public void testCompareTo_withNull_throwsException() {
        DAY_1.compareTo(null);
    }

    @Test
    public void testEqualsAndHashCode() {
        DayOfYear day45_1 = DayOfYear.of(45);
        DayOfYear day45_2 = DayOfYear.of(45);
        DayOfYear day46 = DayOfYear.of(46);

        // equals
        assertEquals(day45_1, day45_1);      // self
        assertEquals(day45_1, day45_2);      // equal values
        assertNotEquals(day45_1, day46);     // different values
        assertNotEquals(day45_1, null);      // null
        assertNotEquals(day45_1, "45"); // different type

        // hashCode
        assertEquals(day45_1.hashCode(), day45_2.hashCode());
        assertNotEquals(day45_1.hashCode(), day46.hashCode());
    }

    //endregion

    //region String representation: toString()

    @Test
    public void testToString() {
        assertEquals("DayOfYear:45", DAY_45.toString());
        assertEquals("DayOfYear:366", DAY_366.toString());
    }

    //endregion
}