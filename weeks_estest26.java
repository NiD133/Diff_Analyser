package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void dividedBy_whenDividingMaxValueByPositiveInteger_returnsCorrectlyTruncatedResult() {
        // Arrange
        final Weeks maxWeeks = Weeks.MAX_VALUE; // Represents Integer.MAX_VALUE (2,147,483,647) weeks
        final int divisor = 888;

        // The expected result is based on integer division, where the fractional part is discarded.
        // 2,147,483,647 / 888 = 2,418,337.43... which truncates to 2,418,337
        final int expectedWeeks = 2418337;

        // Act
        final Weeks result = maxWeeks.dividedBy(divisor);

        // Assert
        assertEquals(
            "Division of MAX_VALUE weeks should be correctly truncated",
            expectedWeeks,
            result.getWeeks()
        );
    }
}