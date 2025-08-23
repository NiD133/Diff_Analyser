package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    @Test
    public void safeMultiplyShouldReturnZeroWhenFirstOperandIsZero() {
        // Arrange
        final int nonZeroOperand = 3306;
        final int expectedResult = 0;

        // Act
        final int actualResult = FieldUtils.safeMultiply(0, nonZeroOperand);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}