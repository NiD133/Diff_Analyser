package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    @Test
    public void safeAdd_shouldReturnZero_whenAddingZeroToZero() {
        // Arrange
        int value1 = 0;
        int value2 = 0;
        int expectedSum = 0;

        // Act
        int actualSum = FieldUtils.safeAdd(value1, value2);

        // Assert
        assertEquals(expectedSum, actualSum);
    }
}