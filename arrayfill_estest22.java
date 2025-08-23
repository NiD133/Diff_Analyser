package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link ArrayFill}.
 * This class focuses on edge cases like null inputs.
 */
public class ArrayFill_ESTestTest22 extends ArrayFill_ESTest_scaffolding {

    /**
     * Tests that {@link ArrayFill#fill(int[], int)} returns null
     * when the input array is null.
     */
    @Test
    public void testFillIntArrayShouldReturnNullForNullInput() {
        // The Javadoc for ArrayFill.fill states that a null input array is permissible
        // and should result in a null return value. This test verifies that behavior.
        // The fill value (0) is arbitrary as it's not used when the array is null.
        final int[] result = ArrayFill.fill((int[]) null, 0);

        assertNull("The method should return null when the input array is null.", result);
    }
}