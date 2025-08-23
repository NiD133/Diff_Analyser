package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import org.junit.Test;

/**
 * Tests for {@link DateTimeComparator} specifically for millisecond-only comparisons.
 */
public class DateTimeComparatorMillisTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    /**
     * Tests a comparator that only considers the millisecond-of-second field.
     * This is achieved by creating a comparator with an upper limit of {@link DateTimeFieldType#secondOfMinute()}.
     */
    @Test
    public void testCompare_withMillisecondOnlyComparator() {
        // Arrange
        // This comparator only considers fields smaller than a second (i.e., milliseconds)
        // because the upper limit (secondOfMinute) is exclusive.
        Comparator<Object> millisOnlyComparator = DateTimeComparator.getInstance(null, DateTimeFieldType.secondOfMinute());

        // Test data with varying milliseconds but identical higher-order fields.
        DateTime baseTime = new DateTime(2023, 1, 1, 12, 0, 0, 500, UTC);
        DateTime laterTime = new DateTime(2023, 1, 1, 12, 0, 0, 501, UTC);
        DateTime sameTime = new DateTime(2023, 1, 1, 12, 0, 0, 500, UTC);

        // Test data with same milliseconds but different higher-order fields.
        DateTime differentSecond = new DateTime(2023, 1, 1, 12, 0, 1, 500, UTC);
        DateTime differentDay = new DateTime(2024, 5, 5, 10, 10, 10, 500, UTC);

        // Act & Assert

        // Test standard comparison logic: less than, greater than, and equals.
        assertTrue("Comparator should return negative when first instant is earlier in milliseconds",
                millisOnlyComparator.compare(baseTime, laterTime) < 0);

        assertTrue("Comparator should return positive when first instant is later in milliseconds",
                millisOnlyComparator.compare(laterTime, baseTime) > 0);

        assertEquals("Comparator should return zero for instants with the same milliseconds",
                0, millisOnlyComparator.compare(baseTime, sameTime));

        // Test that higher-order fields are ignored, as this is a millisecond-only comparison.
        assertEquals("Comparator should ignore the second field, returning zero for same milliseconds",
                0, millisOnlyComparator.compare(baseTime, differentSecond));
                
        assertEquals("Comparator should ignore all date and time fields, returning zero for same milliseconds",
                0, millisOnlyComparator.compare(baseTime, differentDay));
    }
}