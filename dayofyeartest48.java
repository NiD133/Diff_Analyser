package org.threeten.extra;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

/**
 * Tests the implementation of equals() and hashCode() in the {@link DayOfYear} class.
 */
public class DayOfYearEqualsHashCodeTest {

    private static final int MAX_DAY_OF_YEAR = 366;

    /**
     * Verifies that the equals() and hashCode() methods adhere to their contract
     * for all possible DayOfYear values.
     * <p>
     * This test uses Guava's {@link EqualsTester} to confirm that:
     * 1. Two {@code DayOfYear} instances with the same value are equal and have the same hash code.
     * 2. Two {@code DayOfYear} instances with different values are not equal.
     */
    @Test
    public void equalsAndHashCode_shouldFollowContract() {
        EqualsTester equalsTester = new EqualsTester();

        // Create a separate equality group for each possible day of the year (1 to 366).
        // This exhaustively verifies that DayOfYear.of(i) is only equal to another DayOfYear.of(i).
        for (int i = 1; i <= MAX_DAY_OF_YEAR; i++) {
            equalsTester.addEqualityGroup(DayOfYear.of(i), DayOfYear.of(i));
        }

        equalsTester.testEquals();
    }
}