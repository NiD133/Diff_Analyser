package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This class contains improved tests for the {@link ArrayFill} utility.
 * The original test was automatically generated and lacked clarity.
 */
public class ArrayFillImprovedTest {

    /**
     * Tests that calling ArrayFill.fill() on an empty array returns the same
     * array instance, as no filling operation is performed.
     */
    @Test
    public void testFill_withEmptyArray_shouldReturnSameInstance() {
        // Arrange: Create an empty array. The value to fill with is irrelevant
        // for an empty array, so we use a simple, clear example.
        final Object[] emptyArray = new Object[0];
        final String fillValue = "anyValue";

        // Act: Call the fill method.
        final Object[] resultArray = ArrayFill.fill(emptyArray, fillValue);

        // Assert: The returned array should be the exact same instance as the input,
        // confirming that no new array was allocated.
        assertSame("Filling an empty array should return the same instance", emptyArray, resultArray);
    }
}