package org.joda.time;

import org.junit.Test;

/**
 * Test suite for the {@link Weeks} class, focusing on edge cases and exception handling.
 */
public class WeeksTest {

    /**
     * Verifies that converting a Weeks instance to seconds throws an ArithmeticException
     * when the resulting value would cause an integer overflow.
     */
    @Test(expected = ArithmeticException.class)
    public void toStandardSeconds_whenResultOverflows_throwsArithmeticException() {
        // The toStandardSeconds() method calculates the total seconds by multiplying
        // the number of weeks by the number of seconds in one week (7 * 24 * 60 * 60 = 604,800).
        // Using Weeks.MIN_VALUE (which is Integer.MIN_VALUE) will cause this
        // multiplication to overflow the valid range for an integer.
        Weeks.MIN_VALUE.toStandardSeconds();
    }
}