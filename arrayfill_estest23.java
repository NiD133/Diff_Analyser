package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link org.apache.commons.lang3.ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that calling ArrayFill.fill() on an empty array
     * returns the same array instance without modification.
     * This is an important edge case to ensure the method handles
     * empty arrays gracefully.
     */
    @Test
    public void testFillWithEmptyArrayReturnsSameInstance() {
        // Arrange: Create an empty float array.
        final float[] emptyArray = new float[0];

        // Act: Call the fill method on the empty array.
        final float[] resultArray = ArrayFill.fill(emptyArray, 1.0F);

        // Assert: The method should return the exact same instance it was given.
        assertSame("The returned array should be the same instance as the input for an empty array.",
            emptyArray, resultArray);
    }
}