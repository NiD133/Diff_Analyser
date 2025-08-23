package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the utility methods in {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeMultiply(long, long) correctly returns zero
     * when multiplying zero by zero. This serves as a basic sanity check.
     */
    @Test
    public void safeMultiply_shouldReturnZero_whenMultiplyingZeroByZero() {
        // Arrange
        final long multiplicand = 0L;
        final long multiplier = 0L;
        final long expectedProduct = 0L;

        // Act
        long actualProduct = FieldUtils.safeMultiply(multiplicand, multiplier);

        // Assert
        assertEquals(expectedProduct, actualProduct);
    }
}