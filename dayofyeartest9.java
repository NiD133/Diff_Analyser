package org.threeten.extra;

import org.junit.Test;
import java.time.DateTimeException;

/**
 * This test suite verifies the behavior of the {@link DayOfYear#of(int)} factory method,
 * particularly its handling of invalid input values.
 */
public class DayOfYearFactoryTest {

    /**
     * Verifies that creating a DayOfYear with a value of 0 is not allowed.
     * <p>
     * According to the method's contract, the day-of-year must be within the valid
     * range of 1 to 366. This test confirms that providing a value of 0, which is
     * outside the valid range, correctly throws a {@link DateTimeException}.
     */
    @Test(expected = DateTimeException.class)
    public void of_shouldThrowException_whenDayOfYearIsZero() {
        // Attempt to create a DayOfYear instance with the invalid value 0.
        // This action is expected to throw a DateTimeException, which is handled
        // by the @Test(expected=...) annotation.
        DayOfYear.of(0);
    }
}