package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that ArrayFill.fill() correctly handles an empty double array
     * by returning the same instance without modification.
     */
    @Test
    public void testFillWithEmptyDoubleArray() {
        // Arrange
        final double[] emptyArray = new double[0];
        final double fillValue = -259.0;

        // Act
        final double[] resultArray = ArrayFill.fill(emptyArray, fillValue);

        // Assert
        // The method should return the same array instance.
        assertSame("The returned array should be the same instance as the input array", emptyArray, resultArray);
        // The length of the array should, of course, remain 0.
        assertEquals("The length of the empty array should not change", 0, resultArray.length);
    }
}