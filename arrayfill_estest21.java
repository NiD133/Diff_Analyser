package org.apache.commons.lang3;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Test suite for the {@link ArrayFill} class.
 */
public class ArrayFillTest {

    /**
     * Tests that ArrayFill.fill() on an empty array returns the same array instance.
     * This verifies the correct handling of an edge case where no filling operation is needed.
     */
    @Test
    public void testFillWithEmptyIntArrayReturnsSameInstance() {
        // Arrange: Create an empty integer array.
        final int[] emptyArray = new int[0];
        final int fillValue = 123; // The value is irrelevant for an empty array.

        // Act: Call the fill method.
        final int[] resultArray = ArrayFill.fill(emptyArray, fillValue);

        // Assert: The method should return the exact same instance, not a copy.
        assertSame("For an empty array, the returned instance should be the same as the input.", emptyArray, resultArray);
    }
}