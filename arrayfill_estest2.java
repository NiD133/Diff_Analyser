package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that {@code ArrayFill.fill(short[], short)} correctly fills all
     * elements of the array with the specified value.
     */
    @Test
    public void testFillShortArray() {
        // Arrange: Define the input array and the value to fill it with.
        final short[] array = new short[5];
        final short fillValue = -100;

        // Act: Call the method under test.
        final short[] result = ArrayFill.fill(array, fillValue);

        // Assert: Verify that the array is filled correctly.
        final short[] expected = {-100, -100, -100, -100, -100};
        assertArrayEquals(expected, array);
    }

    /**
     * Tests that {@code ArrayFill.fill(short[], short)} returns the same
     * array instance that was passed as input, enabling a fluent style.
     */
    @Test
    public void testFillShortArrayReturnsSameInstance() {
        // Arrange
        final short[] array = new short[3];

        // Act
        final short[] result = ArrayFill.fill(array, (short) 42);

        // Assert
        assertSame("The method should return the same array instance.", array, result);
    }
}