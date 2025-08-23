package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that sorting a single-element float array returns the same array instance
     * and its content is unchanged. This verifies the method's fluent API design
     * for a trivial case.
     */
    @Test
    public void testSortFloatArrayWithSingleElement() {
        // Arrange: Create a single-element array. It is already sorted by definition.
        final float[] inputArray = { 42.0f };
        final float[] expectedArray = { 42.0f };

        // Act: Call the sort method.
        final float[] resultArray = ArraySorter.sort(inputArray);

        // Assert: Verify the results.
        // 1. The method should return the exact same array instance for fluency.
        assertSame("The returned array should be the same instance as the input array.", inputArray, resultArray);

        // 2. The content of the array should be correct (although trivial for a single element).
        assertArrayEquals("The array content should be correctly sorted.", expectedArray, resultArray, 0.0f);
    }
}