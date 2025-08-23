package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void dividedBy_shouldPerformIntegerDivisionAndTruncateResult() {
        // Arrange
        final Minutes threeMinutes = Minutes.THREE;
        final int divisor = 2;
        final int expectedResult = 1; // 3 / 2 = 1 with integer division

        // Act
        Minutes result = threeMinutes.dividedBy(divisor);

        // Assert
        assertEquals(expectedResult, result.getMinutes());
    }
}