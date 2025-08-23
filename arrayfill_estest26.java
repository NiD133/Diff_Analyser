package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Tests for {@link org.apache.commons.lang3.ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that ArrayFill.fill() returns null when the input double array is null,
     * which is the expected behavior according to the method's contract.
     */
    @Test
    public void testFillWithNullDoubleArrayShouldReturnNull() {
        // Arrange: The method under test is designed to handle null array inputs gracefully.
        // The specific fill value is irrelevant for this test case.
        final double fillValue = 1.0;

        // Act: Call the fill method with a null array.
        final double[] result = ArrayFill.fill(null, fillValue);

        // Assert: Verify that the method returns null as specified.
        assertNull("Passing a null array to fill() should return null.", result);
    }
}