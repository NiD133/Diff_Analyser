package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void isLessThan_shouldReturnTrue_whenNegativeMinutesIsComparedToNull() {
        // Arrange: Create a Minutes instance with a negative value.
        // The isLessThan() method's contract states that a null argument is treated as zero.
        Minutes negativeMinutes = Minutes.MIN_VALUE;

        // Act: Compare the negative Minutes instance to null.
        boolean result = negativeMinutes.isLessThan(null);

        // Assert: The result should be true because any negative value is less than zero.
        assertTrue("A negative Minutes value should be considered less than null (which represents zero).", result);
    }
}