package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeMultiplyToInt correctly calculates the product of two long values
     * when the result fits within the range of an integer.
     */
    @Test
    public void safeMultiplyToInt_shouldReturnProduct_whenResultFitsInInt() {
        // Arrange
        long multiplicand = 1L;
        long multiplier = 2765L;
        int expectedProduct = 2765;

        // Act
        int actualProduct = FieldUtils.safeMultiplyToInt(multiplicand, multiplier);

        // Assert
        assertEquals(expectedProduct, actualProduct);
    }
}