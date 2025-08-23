package org.joda.time;

import org.junit.Test;

/**
 * Test suite for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_whenResultOverflows_throwsArithmeticException() {
        // Arrange: Create a Weeks instance with the maximum integer value.
        Weeks maxWeeks = Weeks.MAX_VALUE;
        int multiplier = 7;

        // Act: Attempt to multiply by a value that will cause an integer overflow.
        // The @Test(expected) annotation asserts that this line throws an ArithmeticException.
        maxWeeks.multipliedBy(multiplier);
    }
}