package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that adding zero to zero using safeAdd returns zero.
     * This verifies the most basic addition case.
     */
    @Test
    public void safeAdd_shouldReturnZero_whenAddingZeroToZero() {
        // Arrange
        long value1 = 0L;
        long value2 = 0L;
        long expectedResult = 0L;

        // Act
        long actualResult = FieldUtils.safeAdd(value1, value2);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}