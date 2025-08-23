package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    @Test
    public void safeMultiply_withTwoPositiveLongs_shouldReturnCorrectProduct() {
        // Arrange
        final long multiplicand = 24L;
        final long multiplier = 24L;
        final long expectedProduct = 576L;

        // Act
        final long actualProduct = FieldUtils.safeMultiply(multiplicand, multiplier);

        // Assert
        assertEquals(expectedProduct, actualProduct);
    }
}