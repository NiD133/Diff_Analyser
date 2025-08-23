package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that multiplying a Years instance by a negative scalar
     * produces the correct negative result.
     */
    @Test
    public void multipliedBy_withNegativeScalar_returnsCorrectlyMultipliedYears() {
        // Arrange
        Years threeYears = Years.THREE;
        int scalar = -1129;
        int expectedYears = -3387; // 3 * -1129

        // Act
        Years result = threeYears.multipliedBy(scalar);

        // Assert
        assertEquals(expectedYears, result.getYears());
    }
}