package org.joda.time.field;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    @Test
    public void safeMultiply_zeroTimesZero_shouldReturnZero() {
        // Arrange
        long value1 = 0L;
        long value2 = 0L;
        long expectedResult = 0L;

        // Act
        long actualResult = FieldUtils.safeMultiply(value1, value2);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}