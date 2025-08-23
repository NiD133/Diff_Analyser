package org.joda.time;

import org.junit.Test;

/**
 * Unit test for the {@link Minutes#dividedBy(int)} method.
 */
public class MinutesTest {

    @Test(expected = ArithmeticException.class)
    public void dividedByZeroShouldThrowArithmeticException() {
        // Arrange: Create an arbitrary Minutes instance. The specific value doesn't matter for this test.
        Minutes tenMinutes = Minutes.minutes(10);

        // Act: Attempt to divide the Minutes instance by zero.
        // This action is expected to throw an ArithmeticException.
        tenMinutes.dividedBy(0);

        // Assert: The test passes if an ArithmeticException is thrown, as declared in the @Test annotation.
    }
}