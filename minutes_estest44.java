package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that isLessThan() returns false when comparing a positive Minutes object to null.
     * The Javadoc for isLessThan() specifies that a null input is treated as zero.
     * This test verifies that ONE minute is not less than ZERO minutes.
     */
    @Test
    public void isLessThan_shouldReturnFalse_whenComparingPositiveMinutesToNull() {
        // Arrange
        Minutes oneMinute = Minutes.ONE;

        // Act
        // The isLessThan() method treats a null argument as a Minutes object with a value of zero.
        boolean isLessThan = oneMinute.isLessThan(null);

        // Assert
        // Since 1 is not less than 0, the result must be false.
        assertFalse("One minute should not be less than null (zero)", isLessThan);
    }
}