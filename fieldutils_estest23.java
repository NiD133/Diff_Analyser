package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    @Test
    public void safeMultiply_withBoundaryValuesResultingInMaxValue_doesNotOverflow() {
        // This test case verifies a boundary condition for safe multiplication.
        // The product of -1 and -Integer.MAX_VALUE should result in Integer.MAX_VALUE
        // without throwing an ArithmeticException for overflow.

        // Arrange
        final int multiplicand = -1;
        final int multiplier = -Integer.MAX_VALUE; // -2147483647

        // Act
        final int product = FieldUtils.safeMultiply(multiplicand, multiplier);

        // Assert
        assertEquals(Integer.MAX_VALUE, product);
    }
}