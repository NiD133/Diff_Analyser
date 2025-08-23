package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeAdd(int, int) correctly adds two positive integers
     * when the result does not cause an overflow.
     */
    @Test
    public void safeAdd_shouldReturnCorrectSum_forPositiveIntegers() {
        // Arrange: Define two integers to add and their expected sum.
        int value1 = 4504;
        int value2 = 4504;
        int expectedSum = 9008;

        // Act: Call the method under test.
        int actualSum = FieldUtils.safeAdd(value1, value2);

        // Assert: Verify that the actual sum matches the expected sum.
        assertEquals(expectedSum, actualSum);
    }
}