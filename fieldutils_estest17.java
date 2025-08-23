package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link FieldUtils} class.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeNegate correctly returns 0 for an input of 0.
     * The negation of 0 is 0, and the method should handle this edge case without error.
     */
    @Test
    public void safeNegate_shouldReturnZero_whenInputIsZero() {
        // Arrange: Define the input and the expected outcome.
        int value = 0;
        int expected = 0;

        // Act: Call the method under test.
        int actual = FieldUtils.safeNegate(value);

        // Assert: Verify that the actual result matches the expected result.
        assertEquals(expected, actual);
    }
}