/*
 *  Copyright 2001-2011 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimePrinter;
import org.junit.Test;

import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AbstractPartial}.
 * This class tests the behavior of the abstract class using its concrete implementations
 * like {@link YearMonth}, {@link LocalDate}, and {@link LocalDateTime}.
 */
public class AbstractPartialTest {

    private static final YearMonth YEAR_MONTH_2024_06 = new YearMonth(2024, 6);
    private static final YearMonth YEAR_MONTH_2025_07 = new YearMonth(2025, 7);
    private static final LocalDateTime DATE_TIME_A = new LocalDateTime(2024, 6, 15, 10, 30, 0);
    private static final LocalDateTime DATE_TIME_B = new LocalDateTime(2024, 6, 15, 11, 0, 0);


    //-----------------------------------------------------------------------
    // Test comparison methods: compareTo, isAfter, isBefore, isEqual
    //-----------------------------------------------------------------------

    @Test
    public void compareTo_shouldReturnNegative_whenThisPartialIsSmaller() {
        // Arrange
        YearMonth smaller = new YearMonth(2024, 6);
        YearMonth larger = new YearMonth(2024, 7);

        // Act & Assert
        assertTrue("A smaller partial should return a negative value from compareTo", smaller.compareTo(larger) < 0);
    }

    @Test
    public void compareTo_shouldReturnPositive_whenThisPartialIsLarger() {
        // Arrange
        YearMonth smaller = new YearMonth(2024, 6);
        YearMonth larger = new YearMonth(2024, 7);

        // Act & Assert
        assertTrue("A larger partial should return a positive value from compareTo", larger.compareTo(smaller) > 0);
    }

    @Test
    public void compareTo_shouldReturnZero_whenPartialsAreIdentical() {
        // Arrange
        YearMonth ym1 = new YearMonth(2024, 6);
        YearMonth ym2 = new YearMonth(2024, 6);

        // Act & Assert
        assertEquals("Identical partials should return 0 from compareTo", 0, ym1.compareTo(ym2));
        assertEquals("A partial compared to itself should return 0", 0, ym1.compareTo(ym1));
    }

    @Test
    public void isBefore_shouldReturnTrue_whenThisPartialIsSmaller() {
        assertTrue("isBefore should be true for a smaller partial", YEAR_MONTH_2024_06.isBefore(YEAR_MONTH_2025_07));
    }

    @Test
    public void isBefore_shouldReturnFalse_whenThisPartialIsLarger() {
        assertFalse("isBefore should be false for a larger partial", YEAR_MONTH_2025_07.isBefore(YEAR_MONTH_2024_06));
    }

    @Test
    public void isBefore_shouldReturnFalse_whenPartialsAreIdentical() {
        assertFalse("isBefore should be false for identical partials", YEAR_MONTH_2024_06.isBefore(YEAR_MONTH_2024_06));
    }

    @Test
    public void isAfter_shouldReturnTrue_whenThisPartialIsLarger() {
        assertTrue("isAfter should be true for a larger partial", YEAR_MONTH_2025_07.isAfter(YEAR_MONTH_2024_06));
    }

    @Test
    public void isAfter_shouldReturnFalse_whenThisPartialIsSmaller() {
        assertFalse("isAfter should be false for a smaller partial", YEAR_MONTH_2024_06.isAfter(YEAR_MONTH_2025_07));
    }

    @Test
    public void isAfter_shouldReturnFalse_whenPartialsAreIdentical() {
        assertFalse("isAfter should be false for identical partials", YEAR_MONTH_2024_06.isAfter(YEAR_MONTH_2024_06));
    }

    @Test
    public void isEqual_shouldReturnTrue_whenPartialsAreIdentical() {
        // Arrange
        LocalDateTime dt1 = new LocalDateTime(2024, 6, 15, 10, 30);
        LocalDateTime dt2 = new LocalDateTime(2024, 6, 15, 10, 30);

        // Act & Assert
        assertTrue("isEqual should be true for identical partials", dt1.isEqual(dt2));
    }

    @Test
    public void isEqual_shouldReturnFalse_whenPartialsAreDifferent() {
        // Arrange
        LocalDateTime dt1 = new LocalDateTime(2024, 6, 15, 10, 30);
        LocalDateTime dt2 = new LocalDateTime(2024, 6, 15, 10, 31);

        // Act & Assert
        assertFalse("isEqual should be false for different partials", dt1.isEqual(dt2));
    }

    //-----------------------------------------------------------------------
    // Test equals() and hashCode()
    //-----------------------------------------------------------------------

    @Test
    public void equals_shouldReturnTrue_forEqualPartials() {
        // Arrange
        YearMonth ym1 = new YearMonth(2024, 6);
        YearMonth ym2 = new YearMonth(2024, 6);

        // Act & Assert
        assertTrue("equals() should return true for two identical YearMonth objects", ym1.equals(ym2));
        assertTrue("A partial must be equal to itself", ym1.equals(ym1));
    }

    @Test
    public void equals_shouldReturnFalse_forUnequalPartialsOfSameType() {
        // Arrange
        YearMonth ym1 = new YearMonth(2024, 6);
        YearMonth ym2 = new YearMonth(2025, 6);

        // Act & Assert
        assertFalse("equals() should return false for two different YearMonth objects", ym1.equals(ym2));
    }

    @Test
    public void equals_shouldReturnFalse_forDifferentPartialTypes() {
        // Arrange
        ReadablePartial ym = new YearMonth(2024, 2);
        ReadablePartial md = new MonthDay(2, 14);
        ReadablePartial ld = new LocalDate(2024, 2, 14);

        // Act & Assert
        assertFalse("YearMonth should not be equal to MonthDay", ym.equals(md));
        assertFalse("LocalDate should not be equal to YearMonth", ld.equals(ym));
    }

    @Test
    public void equals_shouldReturnFalse_forNonPartialObject() {
        // Arrange
        YearMonth ym = new YearMonth(2024, 6);

        // Act & Assert
        assertFalse("equals() should return false when comparing to a non-partial object", ym.equals("some string"));
        assertFalse("equals() should return false when comparing to null", ym.equals(null));
    }

    @Test
    public void hashCode_shouldBeConsistentWithEquals() {
        // Arrange
        YearMonth ym1 = new YearMonth(2024, 6);
        YearMonth ym2 = new YearMonth(2024, 6);
        YearMonth ym3 = new YearMonth(2025, 6);

        // Act & Assert
        assertEquals("Equal objects must have equal hashcodes", ym1.hashCode(), ym2.hashCode());
        assertNotEquals("Unequal objects should ideally have different hashcodes", ym1.hashCode(), ym3.hashCode());
    }

    //-----------------------------------------------------------------------
    // Test getters
    //-----------------------------------------------------------------------

    @Test
    public void get_byFieldType_shouldReturnValueForSupportedField() {
        assertEquals("get(DateTimeFieldType.year) should return the correct year", 2024, YEAR_MONTH_2024_06.get(DateTimeFieldType.year()));
        assertEquals("get(DateTimeFieldType.monthOfYear) should return the correct month", 6, YEAR_MONTH_2024_06.get(DateTimeFieldType.monthOfYear()));
    }

    @Test
    public void isSupported_shouldReturnTrueForSupportedField() {
        assertTrue("YearMonth should support 'year'", YEAR_MONTH_2024_06.isSupported(DateTimeFieldType.year()));
    }

    @Test
    public void isSupported_shouldReturnFalseForUnsupportedField() {
        assertFalse("YearMonth should not support 'dayOfMonth'", YEAR_MONTH_2024_06.isSupported(DateTimeFieldType.dayOfMonth()));
        assertFalse("isSupported(null) should return false", YEAR_MONTH_2024_06.isSupported(null));
    }

    @Test
    public void indexOf_shouldReturnCorrectIndexForSupportedField() {
        assertEquals("Index of 'year' in YearMonth should be 0", 0, YEAR_MONTH_2024_06.indexOf(DateTimeFieldType.year()));
        assertEquals("Index of 'monthOfYear' in YearMonth should be 1", 1, YEAR_MONTH_2024_06.indexOf(DateTimeFieldType.monthOfYear()));
    }

    @Test
    public void indexOf_shouldReturnNegativeOneForUnsupportedField() {
        assertEquals("Index of unsupported 'era' field should be -1", -1, YEAR_MONTH_2024_06.indexOf(DateTimeFieldType.era()));
    }

    @Test
    public void getField_shouldReturnCorrectFieldForIndex() {
        assertEquals("Field at index 0 should be 'year'", "year", YEAR_MONTH_2024_06.getField(0).getName());
    }

    @Test
    public void getFieldType_shouldReturnCorrectTypeForIndex() {
        assertEquals("Field type at index 1 should be 'monthOfYear'", DateTimeFieldType.monthOfYear(), YEAR_MONTH_2024_06.getFieldType(1));
    }

    @Test
    public void getFields_shouldReturnArrayOfFields() {
        // Arrange
        YearMonth ym = new YearMonth(2024, 6, ISOChronology.getInstanceUTC());

        // Act
        DateTimeField[] fields = ym.getFields();

        // Assert
        assertEquals("YearMonth should have 2 fields", 2, fields.length);
        assertEquals("First field should be 'year'", "year", fields[0].getName());
        assertEquals("Second field should be 'monthOfYear'", "monthOfYear", fields[1].getName());
    }

    @Test
    public void getFieldTypes_shouldReturnArrayOfFieldTypes() {
        // Arrange
        LocalDate localDate = new LocalDate(2024, 6, 15);

        // Act
        DateTimeFieldType[] types = localDate.getFieldTypes();

        // Assert
        DateTimeFieldType[] expected = {DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth()};
        assertArrayEquals("LocalDate should return year, month, day types", expected, types);
    }

    @Test
    public void getValues_shouldReturnArrayOfValues() {
        // Arrange
        LocalDate localDate = new LocalDate(2024, 6, 15);

        // Act
        int[] values = localDate.getValues();

        // Assert
        int[] expected = {2024, 6, 15};
        assertArrayEquals("getValues() should return the correct values for each field", expected, values);
    }

    //-----------------------------------------------------------------------
    // Test toDateTime()
    //-----------------------------------------------------------------------

    @Test
    public void toDateTime_withBaseInstant_shouldCombinePartialWithInstant() {
        // Arrange
        MonthDay monthDay = new MonthDay(6, 15);
        Instant baseInstant = new Instant("2024-01-01T10:20:30Z");

        // Act
        DateTime result = monthDay.toDateTime(baseInstant);

        // Assert
        DateTime expected = new DateTime("2024-06-15T10:20:30Z");
        assertEquals("Resulting DateTime should have the month and day from the partial", expected, result);
    }

    @Test
    public void toDateTime_withBaseInstantAndDifferentChronology_shouldUseBaseInstantChronology() {
        // Arrange
        MonthDay monthDay = new MonthDay(6, 15, GregorianChronology.getInstanceUTC());
        DateTime baseInstant = new DateTime(2024, 1, 1, 0, 0, CopticChronology.getInstanceUTC());

        // Act
        DateTime result = monthDay.toDateTime(baseInstant);

        // Assert
        assertEquals("Resulting DateTime should use the chronology of the base instant",
                CopticChronology.getInstanceUTC(), result.getChronology());
    }

    //-----------------------------------------------------------------------
    // Test toString()
    //-----------------------------------------------------------------------

    @Test
    public void toString_shouldReturnIso8601Format() {
        assertEquals("2024-06", new YearMonth(2024, 6).toString());
        assertEquals("--06-15", new MonthDay(6, 15).toString());
    }

    @Test
    public void toString_withNullFormatter_shouldUseDefaultToString() {
        assertEquals("toString(null) should behave like toString()",
                YEAR_MONTH_2024_06.toString(), YEAR_MONTH_2024_06.toString((DateTimeFormatter) null));
    }

    @Test
    public void toString_withCustomFormatter_shouldUseFormatter() {
        // Arrange
        LocalDate localDate = new LocalDate(2024, 6, 15);
        DateTimeFormatter formatter = new DateTimeFormatter(mock(DateTimePrinter.class), null);

        // This test doesn't check the output, just that the formatter is called.
        // A more complex mock could verify the output string.
        assertNotNull(localDate.toString(formatter));
    }

    //-----------------------------------------------------------------------
    // Test exception handling
    //-----------------------------------------------------------------------

    @Test(expected = ClassCastException.class)
    public void compareTo_withIncompatiblePartial_shouldThrowException() {
        new YearMonth().compareTo(new LocalTime());
    }

    @Test(expected = ClassCastException.class)
    public void isAfter_withIncompatiblePartial_shouldThrowException() {
        new LocalDateTime().isAfter(new YearMonth());
    }

    @Test(expected = ClassCastException.class)
    public void isBefore_withIncompatiblePartial_shouldThrowException() {
        new MonthDay().isBefore(new YearMonth());
    }

    @Test(expected = ClassCastException.class)
    public void isEqual_withIncompatiblePartial_shouldThrowException() {
        new YearMonth().isEqual(new LocalTime());
    }



    @Test(expected = IllegalArgumentException.class)
    public void get_byUnsupportedFieldType_shouldThrowException() {
        YEAR_MONTH_2024_06.get(DateTimeFieldType.dayOfMonth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void isEqual_withNull_shouldThrowException() {
        DATE_TIME_A.isEqual(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isBefore_withNull_shouldThrowException() {
        DATE_TIME_A.isBefore(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isAfter_withNull_shouldThrowException() {
        DATE_TIME_A.isAfter(null);
    }

    @Test(expected = NullPointerException.class)
    public void compareTo_withNull_shouldThrowException() {
        DATE_TIME_A.compareTo(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getField_withInvalidIndex_shouldThrowException() {
        YEAR_MONTH_2024_06.getField(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getFieldType_withInvalidIndex_shouldThrowException() {
        YEAR_MONTH_2024_06.getFieldType(-1);
    }
}