package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that multiplying two negative long values results in a positive product.
     */
    @Test
    public void safeMultiply_twoNegativeLongs_returnsPositiveProduct() {
        // Arrange
        final long multiplicand = -1L;
        final long multiplier = -1L;
        final long expectedProduct = 1L;

        // Act
        final long actualProduct = FieldUtils.safeMultiply(multiplicand, multiplier);

        // Assert
        assertEquals(expectedProduct, actualProduct);
    }
}