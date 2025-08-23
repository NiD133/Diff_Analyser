package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeDivide correctly calculates the quotient of two negative long values.
     * This serves as a standard case for division involving large negative numbers.
     */
    @Test
    public void safeDivide_withTwoNegativeLongs_returnsCorrectQuotient() {
        // Arrange
        // A dividend close to Long.MIN_VALUE to test with large numbers.
        final long dividend = -9_223_372_036_854_775_765L;
        final long divisor = -2_123L;
        final long expectedQuotient = 4_344_499_310_812_423L;

        // Act
        final long actualQuotient = FieldUtils.safeDivide(dividend, divisor);

        // Assert
        assertEquals(expectedQuotient, actualQuotient);
    }
}