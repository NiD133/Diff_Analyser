package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeMultiply correctly returns zero when any long value is multiplied by zero.
     * This is a fundamental property of multiplication and should hold true.
     */
    @Test
    public void safeMultiplyByZeroShouldReturnZero() {
        // Arrange: Define the inputs and the expected outcome.
        // Using Long.MAX_VALUE to demonstrate the test works with large numbers.
        long anyValue = Long.MAX_VALUE;
        long expectedResult = 0L;

        // Act: Call the method under test.
        long actualResult = FieldUtils.safeMultiply(anyValue, 0L);

        // Assert: Verify that the actual result matches the expected result.
        assertEquals(expectedResult, actualResult);
    }
}