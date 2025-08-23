package org.apache.commons.io.input.buffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Test suite for the {@link CircularByteBuffer} class, focusing on exception-throwing scenarios.
 */
// The original class name and inheritance are preserved, assuming they are part of a larger test suite structure.
public class CircularByteBuffer_ESTestTest23 extends CircularByteBuffer_ESTest_scaffolding {

    /**
     * Verifies that the read(byte[], int, int) method throws an IllegalArgumentException
     * when called with a negative length argument.
     */
    @Test
    public void readShouldThrowIllegalArgumentExceptionForNegativeLength() {
        // Arrange: Set up the test objects and parameters.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] destination = new byte[16];
        final int offset = 2;
        final int negativeLength = -65;

        // Act & Assert: Verify that the expected exception is thrown and check its message.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> buffer.read(destination, offset, negativeLength)
        );

        assertEquals("Illegal length: " + negativeLength, thrown.getMessage());
    }
}