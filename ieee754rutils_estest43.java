package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    @Test
    public void max_withThreeNegativeDoubles_shouldReturnLargestValue() {
        // Arrange
        final double value1 = -1.0;
        final double value2 = -1.0;
        final double value3 = -4660.4;
        final double expectedMax = -1.0;

        // Act
        final double actualMax = IEEE754rUtils.max(value1, value2, value3);

        // Assert
        // The largest value among the inputs is -1.0.
        assertEquals(expectedMax, actualMax, 0.0);
    }
}