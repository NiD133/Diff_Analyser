package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link Weeks} class.
 */
public class WeeksTest {

    @Test
    public void isGreaterThan_withNullOperand_shouldReturnFalseForMinValue() {
        // The Javadoc for isGreaterThan states that a null operand is treated as zero.
        // This test verifies that the minimum possible Weeks value is not greater than zero.

        // Arrange
        Weeks minWeeks = Weeks.MIN_VALUE;

        // Act
        boolean isGreater = minWeeks.isGreaterThan(null);

        // Assert
        assertFalse("Weeks.MIN_VALUE should not be greater than null (treated as zero)", isGreater);
    }
}