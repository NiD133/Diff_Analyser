package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link FieldUtils}.
 */
public class FieldUtilsTest {

    @Test
    public void safeMultiplyToInt_withZeroValues_shouldReturnZero() {
        // Arrange
        long value1 = 0L;
        long value2 = 0L;

        // Act
        int result = FieldUtils.safeMultiplyToInt(value1, value2);

        // Assert
        assertEquals(0, result);
    }
}