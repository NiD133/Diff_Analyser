package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.math.RoundingMode;

/**
 * Unit tests for {@link FieldUtils}.
 * This class focuses on the safeDivide method with rounding.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeDivide correctly handles Long.MIN_VALUE with RoundingMode.UP.
     * RoundingMode.UP rounds away from zero. For a negative result, this means rounding
     * down to the next more negative integer.
     */
    @Test
    public void safeDivide_withLongMinValueAndRoundingUp_roundsAwayFromZero() {
        // Arrange
        final long dividend = Long.MIN_VALUE; // -9,223,372,036,854,775,808
        final long divisor = 319L;

        // The exact division result is ~ -28,913,391,965,061,993.75
        // With RoundingMode.UP (away from zero), this rounds to the next integer
        // further from zero, which is -28,913,391,965,061,994.
        final long expectedResult = -28913391965061994L;

        // Act
        long actualResult = FieldUtils.safeDivide(dividend, divisor, RoundingMode.UP);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}