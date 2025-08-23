package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeAdd correctly adds two negative long values
     * without causing an overflow.
     */
    @Test
    public void safeAdd_shouldCorrectlyAddTwoNegativeLongs() {
        // Arrange
        long value1 = -1185L;
        long value2 = -1185L;
        long expectedSum = -2370L;

        // Act
        long actualSum = FieldUtils.safeAdd(value1, value2);

        // Assert
        assertEquals(expectedSum, actualSum);
    }
}