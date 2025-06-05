package org.example;

import org.jfree.data.Range;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class DateRangeTest {

    @Test
    public void testGetUpperMillis_positiveRange() {
        // Arrange: Define a date range with positive bounds.
        DateRange dateRange = new DateRange(1000.0, 2000.0);

        // Act: Get the upper bound in milliseconds.
        long upperMillis = dateRange.getUpperMillis();

        // Assert: Verify that the upper bound is as expected.
        assertEquals(2000L, upperMillis);
    }

    @Test
    public void testGetUpperMillis_negativeRange() {
        // Arrange: Define a date range with negative bounds.
        DateRange dateRange = new DateRange(-100.0, -50.0);

        // Act: Get the upper bound in milliseconds.
        long upperMillis = dateRange.getUpperMillis();

        // Assert: Verify that the upper bound is as expected.
        assertEquals(-50L, upperMillis);
    }

    @Test
    public void testGetLowerMillis_positiveRange() {
        // Arrange: Define a date range with positive bounds.
        DateRange dateRange = new DateRange(1000.0, 2000.0);

        // Act: Get the lower bound in milliseconds.
        long lowerMillis = dateRange.getLowerMillis();

        // Assert: Verify that the lower bound is as expected.
        assertEquals(1000L, lowerMillis);
    }

    @Test
    public void testGetLowerMillis_negativeRange() {
        // Arrange: Define a date range with negative bounds.
        DateRange dateRange = new DateRange(-100.0, -50.0);

        // Act: Get the lower bound in milliseconds.
        long lowerMillis = dateRange.getLowerMillis();

        // Assert: Verify that the lower bound is as expected.
        assertEquals(-100L, lowerMillis);
    }

    @Test
    public void testConstructor_nullRange() {
        // Act & Assert: Verify that a NullPointerException is thrown when constructing a DateRange with a null Range.
        assertThrows(NullPointerException.class, () -> new DateRange((Range) null));
    }

    @Test
    public void testConstructor_nullDates() {
        // Act & Assert: Verify that a NullPointerException is thrown when constructing a DateRange with null Date objects.
        assertThrows(NullPointerException.class, () -> new DateRange((Date) null, (Date) null));
    }

    @Test
    public void testConstructor_invalidDateRange() {
        // Arrange: Create two Date objects where the lower date is actually later than the upper date.
        Date lowerDate = new Date(2000);
        Date upperDate = new Date(1000);

        // Act & Assert: Verify that an IllegalArgumentException is thrown because the lower bound is greater than the upper bound.
        assertThrows(IllegalArgumentException.class, () -> new DateRange(lowerDate, upperDate));
    }

    @Test
    public void testConstructor_invalidDoubleRange() {
        // Act & Assert: Verify that an IllegalArgumentException is thrown when constructing a DateRange with an invalid double range (lower > upper).
        assertThrows(IllegalArgumentException.class, () -> new DateRange(100.0, 50.0));
    }

    @Test
    public void testConstructor_validDates() {
        // Arrange: Create two Date objects representing the start and end of a range.
        Date startDate = new Date(1672531200000L); // January 1, 2023
        Date endDate = new Date(1675123200000L);   // January 31, 2023

        // Act: Create a DateRange object.
        DateRange dateRange = new DateRange(startDate, endDate);

        // Assert: No exception should be thrown.  Implicitly tests constructor.
        assertNotNull(dateRange);
    }

    @Test
    public void testGetUpperDate() {
        // Arrange: Define a date range.
        Date originalDate = new Date(1000L);
        DateRange dateRange = new DateRange(0.0, 1000.0);

        // Act: Get the upper date.
        Date upperDate = dateRange.getUpperDate();

        // Assert: Verify that the upper date is as expected.
        assertEquals(originalDate, upperDate);
    }

    @Test
    public void testGetLowerDate() {
        // Arrange: Define a date range.
        Date originalDate = new Date(0L);
        DateRange dateRange = new DateRange(0.0, 1000.0);

        // Act: Get the lower date.
        Date lowerDate = dateRange.getLowerDate();

        // Assert: Verify that the lower date is as expected.
        assertEquals(originalDate, lowerDate);
    }

    @Test
    public void testToString() {
        // Arrange: Define a date range.
        DateRange dateRange = new DateRange(0.0, 86400000.0); // 24 hours in milliseconds

        // Act: Get the string representation.
        String stringRepresentation = dateRange.toString();

        // Assert: Verify that the string representation contains the expected date range in a human-readable format.  The exact format depends on the locale.
        assertTrue(stringRepresentation.contains("Jan 1, 1970")); // Basic check to ensure it is a valid date.
    }

    @Test
    public void testDefaultConstructor() {
        // Arrange & Act: Use the default constructor.
        DateRange dateRange = new DateRange();

        // Assert: The lower bound should be 0 and the upper bound should be 1.
        assertEquals(0L, dateRange.getLowerMillis());
        assertEquals(1L, dateRange.getUpperMillis());
    }
}