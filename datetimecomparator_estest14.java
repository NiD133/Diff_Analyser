package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link DateTimeComparator}.
 */
public class DateTimeComparatorTest {

    /**
     * Tests that comparing two null objects returns 0.
     * The {@link DateTimeComparator#compare(Object, Object)} method treats null inputs
     * as the current time ("now"). Therefore, comparing two nulls is equivalent to
     * comparing "now" with "now", which should result in equality (0).
     */
    @Test
    public void compare_whenBothInputsAreNull_shouldReturnZero() {
        // Arrange
        // The specific comparator instance (time-only, date-only, etc.) doesn't matter
        // for this test, as the null-handling logic is universal.
        DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();

        // Act
        int result = comparator.compare(null, null);

        // Assert
        assertEquals("Comparing two nulls should be equivalent to comparing 'now' with 'now', resulting in 0.", 0, result);
    }
}