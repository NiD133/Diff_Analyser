package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that {@link ArrayFill#fill(long[], long)} correctly handles an empty array.
     * The method should not throw an exception and should return the same array instance.
     */
    @Test
    public void testFillLongArrayWithEmptyArray() {
        // Arrange: Create an empty long array.
        final long[] emptyArray = new long[0];

        // Act: Call the fill method on the empty array.
        final long[] resultArray = ArrayFill.fill(emptyArray, 123L); // The value doesn't matter for an empty array.

        // Assert: The returned array should be the same instance as the input.
        assertSame("The method should return the same instance for an empty array.", emptyArray, resultArray);
    }
}