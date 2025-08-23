package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DateTimeComparator} class, focusing on its string representation.
 */
public class DateTimeComparatorTest {

    @Test
    public void toStringShouldReturnCorrectFormatWhenLowerAndUpperLimitsAreSame() {
        // Arrange: Create a comparator where the lower and upper field limits are identical.
        DateTimeFieldType limitType = DateTimeFieldType.weekyearOfCentury();
        DateTimeComparator comparator = DateTimeComparator.getInstance(limitType, limitType);

        String expectedToString = "DateTimeComparator[weekyearOfCentury]";

        // Act: Get the string representation of the comparator.
        String actualToString = comparator.toString();

        // Assert: Verify that the string format correctly represents the single limit.
        assertEquals(expectedToString, actualToString);
    }
}