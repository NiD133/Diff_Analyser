package org.joda.time.field;

import org.junit.Test;
import java.math.RoundingMode;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the utility methods in {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeDivide correctly returns 1 when dividing Integer.MIN_VALUE by itself.
     * This is an edge case where the dividend and divisor are the same negative boundary value.
     */
    @Test
    public void safeDivide_whenDividingIntegerMinValueByItself_shouldReturnOne() {
        // Arrange
        final long dividend = Integer.MIN_VALUE;
        final long divisor = Integer.MIN_VALUE;
        // The rounding mode doesn't affect the result here, as there is no remainder.
        final RoundingMode roundingMode = RoundingMode.FLOOR;
        final long expectedQuotient = 1L;

        // Act
        long actualQuotient = FieldUtils.safeDivide(dividend, divisor, roundingMode);

        // Assert
        assertEquals("Dividing a number by itself should result in 1", expectedQuotient, actualQuotient);
    }
}