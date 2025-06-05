package org.jfree.data.time;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link DateRange} class.  The goal is to ensure the class
 * behaves as expected regarding equality, serialization, immutability and cloneability.
 */
public class DateRangeTest {

    @Test
    @DisplayName("Test equality of DateRange objects")
    public void testEquals() {
        // Arrange: Create two DateRange objects with the same start and end dates
        Date startDate = new Date(1000L);
        Date endDate = new Date(2000L);
        DateRange dateRange1 = new DateRange(startDate, endDate);
        DateRange dateRange2 = new DateRange(startDate, endDate);

        // Assert: The two DateRange objects should be equal
        assertEquals(dateRange1, dateRange2, "DateRange objects with same start and end dates should be equal");
        assertEquals(dateRange2, dateRange1, "Equality should be symmetric");

        // Act: Modify the start date of the first DateRange
        Date modifiedStartDate = new Date(1111L);
        dateRange1 = new DateRange(modifiedStartDate, endDate);

        // Assert: The DateRange objects should no longer be equal
        assertNotEquals(dateRange1, dateRange2, "DateRange objects with different start dates should not be equal");

        // Act: Update the second DateRange to have the same start date as the first
        dateRange2 = new DateRange(modifiedStartDate, endDate);

        // Assert: The DateRange objects should now be equal
        assertEquals(dateRange1, dateRange2, "DateRange objects with same start and end dates should be equal");

        // Act: Modify the end date of the first DateRange
        Date modifiedEndDate = new Date(2222L);
        dateRange1 = new DateRange(modifiedStartDate, modifiedEndDate);

        // Assert: The DateRange objects should no longer be equal
        assertNotEquals(dateRange1, dateRange2, "DateRange objects with different end dates should not be equal");

        // Act: Update the second DateRange to have the same end date as the first
        dateRange2 = new DateRange(modifiedStartDate, modifiedEndDate);

        // Assert: The DateRange objects should now be equal
        assertEquals(dateRange1, dateRange2, "DateRange objects with same start and end dates should be equal");
    }

    @Test
    @DisplayName("Test serialization and deserialization of a DateRange object")
    public void testSerialization() {
        // Arrange: Create a DateRange object
        Date startDate = new Date(1000L);
        Date endDate = new Date(2000L);
        DateRange originalDateRange = new DateRange(startDate, endDate);

        // Act: Serialize and deserialize the DateRange object
        DateRange deserializedDateRange = TestUtils.serialised(originalDateRange);

        // Assert: The original and deserialized DateRange objects should be equal
        assertEquals(originalDateRange, deserializedDateRange, "The deserialized DateRange object should be equal to the original");
    }

    @Test
    @DisplayName("DateRange should not be cloneable")
    public void testClone() {
        // Arrange: Create a DateRange object
        Date startDate = new Date(1000L);
        Date endDate = new Date(2000L);
        DateRange dateRange = new DateRange(startDate, endDate);

        // Assert: DateRange should NOT implement the Cloneable interface
        assertFalse(dateRange instanceof Cloneable, "DateRange should not implement the Cloneable interface");
    }

    @Test
    @DisplayName("Test immutability of DateRange")
    public void testImmutable() {
        // Arrange: Create Date objects and a DateRange object
        Date initialStartDate = new Date(10L);
        Date initialEndDate = new Date(20L);
        DateRange dateRange = new DateRange(initialStartDate, initialEndDate);

        // Act: Modify the original Date objects after creating the DateRange
        initialStartDate.setTime(11L); // Mutate the original Date object
        dateRange.getUpperDate().setTime(22L); // Attempt to mutate the Date object obtained from the DateRange

        // Assert: The DateRange object should remain unchanged (immutable)
        assertEquals(new Date(10L), dateRange.getLowerDate(), "Lower date should not be affected by external modification");
        assertEquals(new Date(20L), dateRange.getUpperDate(), "Upper date should not be affected by external modification");
    }
}