package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This test class contains tests for the ArrayFill class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class ArrayFill_ESTestTest17 extends ArrayFill_ESTest_scaffolding {

    /**
     * Tests that ArrayFill.fill() returns the same instance when called with an empty short array.
     * This verifies the fluent API behavior for an edge case where no elements are modified.
     */
    @Test
    public void testFillEmptyShortArrayReturnsSameInstance() {
        // Arrange: Create an empty short array.
        final short[] emptyArray = new short[0];

        // Act: Call the fill method on the empty array.
        final short[] resultArray = ArrayFill.fill(emptyArray, (short) 42);

        // Assert: The returned array should be the exact same instance as the input array.
        assertSame("The fill method should return the same array instance for fluent chaining.",
                   emptyArray, resultArray);
    }
}