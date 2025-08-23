package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    @Test
    public void testFillBooleanArray() {
        // Arrange
        final boolean[] actualArray = new boolean[4]; // Initial state: {false, false, false, false}
        final boolean[] expectedArray = {true, true, true, true};

        // Act
        final boolean[] resultArray = ArrayFill.fill(actualArray, true);

        // Assert
        // 1. Verify that the method returns the same array instance that was passed in.
        assertSame("The method should return the same array instance.", actualArray, resultArray);

        // 2. Verify that the array contents are correctly filled.
        assertArrayEquals("The array should be filled with 'true'.", expectedArray, resultArray);
    }
}