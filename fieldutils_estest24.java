package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.math.RoundingMode;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeDivide returns 0 when the dividend is 0,
     * regardless of the divisor or rounding mode.
     */
    @Test
    public void safeDivide_shouldReturnZero_whenDividendIsZero() {
        // Arrange
        final long dividend = 0L;
        final long divisor = 218L;
        // The specific rounding mode should not affect the outcome when the dividend is zero.
        final RoundingMode roundingMode = RoundingMode.CEILING;

        // Act
        final long result = FieldUtils.safeDivide(dividend, divisor, roundingMode);

        // Assert
        assertEquals(0L, result);
    }
}