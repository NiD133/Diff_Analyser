package org.threeten.extra;

import org.junit.Test;

/**
 * This test class has been improved for better understandability.
 * It focuses on testing the behavior of the DayOfMonth.compareTo method.
 */
public class DayOfMonthTest {

    /**
     * Tests that calling compareTo with a null argument throws a NullPointerException.
     * This is the expected behavior as the method contract requires a non-null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testCompareToThrowsExceptionOnNullInput() {
        // Given a DayOfMonth instance
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // When comparing it to null
        dayOfMonth.compareTo(null);

        // Then a NullPointerException is expected, as declared by the @Test annotation.
    }
}