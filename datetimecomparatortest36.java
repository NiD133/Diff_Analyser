package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;

/**
 * Contains tests for the {@link DateTimeComparator} class, focusing on its sorting capabilities.
 */
public class DateTimeComparatorTest {

    /**
     * Tests that the comparator for the 'weekyear' field correctly sorts a list of DateTime objects.
     */
    @Test
    public void sortByWeekyearShouldOrderDatesChronologically() {
        // Arrange
        DateTime dateTime2010 = new DateTime("2010-04-01T10:00:00", DateTimeZone.UTC);
        DateTime dateTime2002 = new DateTime("2002-01-01T10:00:00", DateTimeZone.UTC);

        // Create a list that is initially in reverse chronological order.
        List<DateTime> dates = new ArrayList<>(Arrays.asList(dateTime2010, dateTime2002));
        
        // Define the expected order after sorting.
        List<DateTime> expectedOrder = Arrays.asList(dateTime2002, dateTime2010);

        // Sanity check: ensure the list is not already sorted.
        assertNotEquals("Test setup failure: list is already sorted.", expectedOrder, dates);

        // Get the comparator for sorting by the 'weekyear' field.
        Comparator<Object> weekyearComparator = DateTimeComparator.getInstance(DateTimeFieldType.weekyear());

        // Act
        // Sort the list using the weekyear comparator.
        dates.sort(weekyearComparator);

        // Assert
        // Verify that the list is now sorted as expected.
        assertEquals("The list should be sorted chronologically by weekyear", expectedOrder, dates);
    }
}