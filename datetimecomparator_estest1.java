package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link DateTimeComparator}.
 */
public class DateTimeComparatorTest {

    /**
     * Tests that the compare method throws an IllegalArgumentException when one of the
     * arguments is a String that cannot be parsed into a date-time object.
     */
    @Test
    public void compare_withUnparseableString_shouldThrowIllegalArgumentException() {
        // Arrange: Create a comparator and define an unparseable string argument.
        // The 'null' argument is treated by the comparator as the current time ("now").
        DateTimeComparator dateOnlyComparator = DateTimeComparator.getDateOnlyInstance();
        String unparseableDateString = "DateTimeComparator[dayOfYear-]";
        Object now = null;

        // Act & Assert: Attempt the comparison and verify the expected exception.
        try {
            dateOnlyComparator.compare(now, unparseableDateString);
            fail("Expected an IllegalArgumentException for the unparseable string.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message clearly indicates the cause of the failure.
            String expectedMessage = "Invalid format: \"" + unparseableDateString + "\"";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}