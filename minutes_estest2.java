package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void isLessThan_shouldReturnFalseWhenComparingZeroToNull() {
        // The Javadoc for isLessThan() specifies that a null argument is treated as zero.
        // Therefore, this test verifies that comparing Minutes.ZERO to null is equivalent
        // to (0 < 0), which should be false.

        // Arrange
        Minutes zeroMinutes = Minutes.ZERO;

        // Act
        boolean result = zeroMinutes.isLessThan(null);

        // Assert
        assertFalse("Minutes.ZERO should not be less than null (which is treated as zero)", result);
    }
}