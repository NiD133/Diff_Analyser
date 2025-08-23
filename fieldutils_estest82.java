package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    @Test
    public void safeAdd_shouldReturnCorrectSum_whenAddingNearMaxValueWithoutOverflow() {
        // Arrange
        // Use a value very close to Long.MAX_VALUE to test boundary conditions.
        final long largePositiveValue = Long.MAX_VALUE - 7L;
        final long negativeValue = -275L;
        final long expectedSum = Long.MAX_VALUE - 282L; // -275L + (Long.MAX_VALUE - 7L)

        // Act
        long actualSum = FieldUtils.safeAdd(negativeValue, largePositiveValue);

        // Assert
        assertEquals(expectedSum, actualSum);
    }
}