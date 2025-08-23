package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that isLessThan() treats a null argument as a zero-value Weeks instance.
     * According to the method's contract, comparing any negative Weeks value to null
     * should return true.
     */
    @Test
    public void isLessThan_withNullArgument_shouldBeTreatedAsComparingToZero() {
        // Arrange: A Weeks instance with the minimum possible (negative) value.
        Weeks minValueWeeks = Weeks.MIN_VALUE;

        // Act: Compare the negative Weeks value to null.
        boolean result = minValueWeeks.isLessThan(null);

        // Assert: The result must be true, because MIN_VALUE is less than zero.
        assertTrue("A negative Weeks value should be less than null (which is treated as zero).", result);
    }
}