package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    @Test
    public void safeDivide_withZeroDividend_returnsZero() {
        // Arrange
        final long dividend = 0L;
        final long divisor = 3961L;
        final long expectedResult = 0L;

        // Act
        final long actualResult = FieldUtils.safeDivide(dividend, divisor);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}