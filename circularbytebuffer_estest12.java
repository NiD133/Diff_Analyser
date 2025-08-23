package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Contains tests for the {@link CircularByteBuffer} class.
 */
public class CircularByteBufferTest {

    /**
     * Tests that calling the add() method with a zero length and an offset of 0
     * on an empty byte array throws an IllegalArgumentException. This test
     * verifies a specific input validation edge case.
     */
    @Test
    public void addWithZeroLengthFromEmptyArrayShouldThrowIllegalArgumentException() {
        // Arrange: Create a buffer and define the parameters for the add operation.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] emptyArray = new byte[0];
        final int offset = 0;
        final int length = 0;

        // Act & Assert: Attempt the 'add' operation and verify the expected exception.
        try {
            buffer.add(emptyArray, offset, length);
            fail("Expected an IllegalArgumentException to be thrown, but no exception was caught.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            final String expectedMessage = "Illegal offset: 0";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}