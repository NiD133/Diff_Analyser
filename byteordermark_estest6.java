package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link ByteOrderMark} class.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that the get() method correctly retrieves the byte value
     * at a specified index from the Byte Order Mark.
     */
    @Test
    public void testGetShouldReturnByteAtSpecifiedIndex() {
        // Arrange: Create a custom ByteOrderMark with a specific byte pattern.
        final int[] bomBytes = {0, 0, -127, 0};
        final ByteOrderMark bom = new ByteOrderMark("CUSTOM-BOM", bomBytes);

        final int indexToTest = 2;
        final int expectedValue = -127;

        // Act: Get the byte at the specified index.
        final int actualValue = bom.get(indexToTest);

        // Assert: The returned value should match the value from the original array.
        assertEquals("The byte at the specified index should be returned.", expectedValue, actualValue);
    }
}