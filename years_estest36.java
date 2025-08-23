package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void minus_whenSubtractingYearsFromItself_shouldReturnZero() {
        // Arrange
        Years threeYears = Years.THREE;

        // Act
        Years result = threeYears.minus(threeYears);

        // Assert
        assertEquals(Years.ZERO, result);
    }
}