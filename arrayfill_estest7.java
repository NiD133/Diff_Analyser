package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Unit tests for the {@link ArrayFill} class.
 */
public class ArrayFillTest {

    /**
     * Tests that {@code ArrayFill.fill(float[], float)} correctly populates
     * every element of the array with the specified value.
     */
    @Test
    public void testFillFloatArrayFillsAllElements() {
        // Arrange: Create an initial array and define the expected state after the operation.
        final float[] inputArray = new float[5];
        final float fillValue = 95.0F;
        final float[] expectedArray = {95.0F, 95.0F, 95.0F, 95.0F, 95.0F};
        final float delta = 0.0f; // Using 0.0f since the values are exact.

        // Act: Call the method under test.
        final float[] resultArray = ArrayFill.fill(inputArray, fillValue);

        // Assert: Verify the results.
        // 1. Check that the array's contents are correct.
        assertArrayEquals("The array should be filled with the specified value.",
                expectedArray, resultArray, delta);

        // 2. Check that the method returns the same array instance, confirming its fluent API contract.
        assertSame("The method should return the same array instance that was passed in.",
                inputArray, resultArray);
    }
}