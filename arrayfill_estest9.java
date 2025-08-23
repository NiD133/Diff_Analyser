package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link org.apache.commons.lang3.ArrayFill}.
 */
public class ArrayFillTest {

    @Test
    public void testFillCharArray() {
        // Arrange: Create an array and define the expected state after filling.
        final char[] inputArray = new char[3];
        final char fillValue = 'X';
        final char[] expectedArray = {'X', 'X', 'X'};

        // Act: Call the method under test.
        final char[] resultArray = ArrayFill.fill(inputArray, fillValue);

        // Assert: Verify the array is filled correctly and the same instance is returned.
        assertArrayEquals("The array should be filled with the specified value.", expectedArray, resultArray);
        assertSame("The method should return the same array instance that was passed in.", inputArray, resultArray);
    }
}