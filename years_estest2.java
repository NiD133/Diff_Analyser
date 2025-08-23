package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void isLessThan_shouldReturnFalse_whenComparingWithNull() {
        // The isLessThan() method's documentation states that a null argument
        // is treated as a period of zero years.
        // This test verifies that a period of two years is not less than zero.
        Years twoYears = Years.TWO;

        boolean result = twoYears.isLessThan(null);

        assertFalse("Two years should not be less than null (zero years)", result);
    }
}