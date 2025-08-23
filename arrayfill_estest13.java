package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * This class contains tests for the {@link ArrayFill} utility class.
 * The original test class name and inheritance from scaffolding are preserved.
 */
public class ArrayFill_ESTestTest13 extends ArrayFill_ESTest_scaffolding {

    /**
     * Tests that ArrayFill.fill() returns null when the input array is null.
     * The Javadoc for the method specifies that a null array is a valid input,
     * and in such cases, the method should simply return null.
     */
    @Test
    public void fill_withNullArray_shouldReturnNull() {
        // Arrange: The value to fill with is irrelevant when the array is null.
        // We use a simple Object to make this clear.
        final Object DUMMY_FILL_VALUE = new Object();

        // Act: Call the fill method with a null array.
        final Object[] result = ArrayFill.fill((Object[]) null, DUMMY_FILL_VALUE);

        // Assert: The method should return null as specified by its contract.
        assertNull("Passing a null array to fill() should return null.", result);
    }
}