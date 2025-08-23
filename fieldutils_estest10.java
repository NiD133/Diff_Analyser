package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeSubtract correctly returns the original value when zero is subtracted.
     */
    @Test
    public void safeSubtract_shouldReturnOriginalValue_whenSubtractingZero() {
        // Arrange
        final long minuend = 3600L;
        final long expectedResult = 3600L;

        // Act
        long actualResult = FieldUtils.safeSubtract(minuend, 0L);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}