package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link Seconds} class.
 * This version improves upon an auto-generated test for the isLessThan() method.
 */
public class Seconds_ESTestTest38 {

    @Test
    public void isLessThan_shouldReturnTrue_whenInstanceIsSmallerThanArgument() {
        // Arrange: Create two instances of Seconds for comparison.
        // Using constants like Seconds.ZERO and Seconds.THREE makes the intent clear
        // and avoids the complex setup seen in the original test.
        Seconds smaller = Seconds.ZERO;
        Seconds larger = Seconds.THREE;

        // Act: Call the method under test.
        boolean result = smaller.isLessThan(larger);

        // Assert: Verify that the result is correct.
        // The assertion message provides immediate context if the test fails.
        assertTrue("Zero seconds should be considered less than three seconds", result);
    }
}