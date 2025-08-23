package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link IEEE754rUtils}.
 */
public class IEEE754rUtilsTest {

    @Test
    public void minWithThreeDoublesShouldReturnSmallestValue() {
        // Arrange
        final double a = 1660.66;
        final double b = -567.84087; // The expected minimum
        final double c = 4173.8887585;
        final double expected = -567.84087;

        // Act
        final double actual = IEEE754rUtils.min(a, b, c);

        // Assert
        assertEquals(expected, actual, 0.01);
    }
}