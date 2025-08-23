package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that filling an empty char array returns the same empty array instance.
     */
    @Test
    public void testFillWithEmptyCharArray() {
        // Arrange
        final char[] emptyArray = new char[0];

        // Act
        final char[] resultArray = ArrayFill.fill(emptyArray, 's');

        // Assert
        // The method should return the exact same array instance.
        assertSame("The returned array should be the same instance as the input", emptyArray, resultArray);
        // The length should, of course, remain 0.
        assertEquals("The array length should be 0", 0, resultArray.length);
    }
}