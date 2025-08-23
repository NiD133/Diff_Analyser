package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeMultiply(long, int) returns the original value
     * when the multiplier is 1.
     */
    @Test
    public void safeMultiplyByOneReturnsSameValue() {
        // Arrange
        final long originalValue = -889L;
        final int multiplier = 1;

        // Act
        long result = FieldUtils.safeMultiply(originalValue, multiplier);

        // Assert
        assertEquals(originalValue, result);
    }
}