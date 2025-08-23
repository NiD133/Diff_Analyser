package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import java.time.ArithmeticException;

/**
 * This test verifies the behavior of the UtcInstant class when calculations result in an overflow.
 */
public class UtcInstantOverflowTest {

    /**
     * Tests that adding a large duration to a UtcInstant far in the future
     * correctly throws an ArithmeticException when the result exceeds the
     * maximum representable value.
     */
    @Test(expected = ArithmeticException.class)
    public void plus_whenResultingInstantExceedsTimeLineRange_throwsArithmeticException() {
        // Arrange: Create an instant far in the future and a large duration.
        // The values are chosen such that their sum will cause a long overflow
        // during the addition calculation inside the plus() method.
        long largeDayCount = 86_399_999_999_628L;
        long nanoOfDay = 86_399_999_999_628L; // A valid nano-of-day value.

        UtcInstant farFutureInstant = UtcInstant.ofModifiedJulianDay(largeDayCount, nanoOfDay);
        Duration largeDuration = Duration.ofDays(largeDayCount);

        // Act: Adding the large duration to the far-future instant should cause an overflow.
        // The @Test(expected) annotation handles the assertion.
        farFutureInstant.plus(largeDuration);
    }
}