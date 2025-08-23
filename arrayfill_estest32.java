package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Contains tests for the {@link ArrayFill} class.
 * This test was improved for clarity from an auto-generated version.
 */
public class ArrayFill_ESTestTest32 extends ArrayFill_ESTest_scaffolding {

    /**
     * Tests that {@link ArrayFill#fill(boolean[], boolean)} correctly handles a null input array
     * by returning null, as specified in its contract.
     */
    @Test
    public void fillBooleanArray_shouldReturnNull_whenInputArrayIsNull() {
        // Arrange: Define a null boolean array to pass to the method.
        final boolean[] inputArray = null;

        // Act: Call the fill method with the null array.
        final boolean[] resultArray = ArrayFill.fill(inputArray, true);

        // Assert: Verify that the method returns null for a null input.
        assertNull("The fill method should return null when the input array is null.", resultArray);
    }
}