package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    @Test
    public void min_shouldReturnSmallerValue_whenComparingNegativeAndZero() {
        // Arrange
        final double negativeNumber = -855.02919;
        final double zero = 0.0;
        final double expectedMinimum = -855.02919;

        // Act
        final double actualMinimum = IEEE754rUtils.min(negativeNumber, zero);

        // Assert
        assertEquals("The minimum of a negative number and zero should be the negative number",
                expectedMinimum, actualMinimum, 0.0);
    }
}