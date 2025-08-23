package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that ArrayFill.fill() for an object array correctly fills every element
     * and returns the same array instance, confirming its fluent API behavior.
     */
    @Test
    public void testFill_forObjectArray_shouldModifyAndReturnSameInstance() {
        // Arrange: Create an array to be filled and define the expected state after filling.
        final String[] actualArray = new String[3];
        final String fillValue = "filled";
        final String[] expectedArray = { "filled", "filled", "filled" };

        // Act: Call the method under test.
        final String[] resultArray = ArrayFill.fill(actualArray, fillValue);

        // Assert: Verify both the content and the identity of the array.
        // 1. The method should return the same array instance that was passed in.
        assertSame("The returned array should be the same instance as the input array.", actualArray, resultArray);

        // 2. The array's contents should be correctly filled with the specified value.
        assertArrayEquals("Each element of the array should be updated to the fill value.", expectedArray, actualArray);
    }
}