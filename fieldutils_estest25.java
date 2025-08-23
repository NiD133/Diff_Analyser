package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    @Test
    public void safeDivide_withMinValueDividend_returnsCorrectQuotient() {
        // The safeDivide method is designed to prevent overflow, which is a risk
        // when dealing with Long.MIN_VALUE (e.g., Long.MIN_VALUE / -1).
        // This test verifies that for a non-overflowing case involving Long.MIN_VALUE,
        // the division result is calculated correctly.

        // Arrange
        final long dividend = Long.MIN_VALUE; // -9223372036854775808L
        final long divisor = 33L;
        final long expectedQuotient = -279496122328932600L; // Pre-calculated result of Long.MIN_VALUE / 33L

        // Act
        long actualQuotient = FieldUtils.safeDivide(dividend, divisor);

        // Assert
        assertEquals(expectedQuotient, actualQuotient);
    }
}