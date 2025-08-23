package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link NumberOutput} class.
 */
public class NumberOutputTest {

    /**
     * Verifies that `outputLong` correctly writes the long value 0
     * into a character buffer at a specified offset.
     */
    @Test
    public void outputLongShouldCorrectlyWriteZero() {
        // Arrange: Set up the buffer, the value to write, and the starting offset.
        final int offset = 1;
        char[] buffer = new char[7];
        long value = 0L;

        // Expected buffer state after writing '0' at the given offset.
        // The rest of the array remains initialized with the default char value ('\u0000').
        char[] expectedBuffer = new char[7];
        expectedBuffer[offset] = '0';

        // Act: Call the method under test.
        int nextOffset = NumberOutput.outputLong(value, buffer, offset);

        // Assert: Check that the returned offset is correct and the buffer was modified as expected.
        // The new offset should be advanced by one character ('0').
        assertEquals("The next offset should be incremented by 1", offset + 1, nextOffset);
        assertArrayEquals("The buffer should contain '0' at the specified offset", expectedBuffer, buffer);
    }
}