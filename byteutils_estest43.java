package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the {@link ByteUtils} class.
 * This specific test focuses on the exception handling of the fromLittleEndian method.
 */
// The original class name and inheritance are preserved to show a direct improvement.
public class ByteUtils_ESTestTest43 extends ByteUtils_ESTest_scaffolding {

    /**
     * Tests that fromLittleEndian(byte[], int, int) throws an IllegalArgumentException
     * when the requested length is greater than 8, as a long can hold at most 8 bytes.
     */
    @Test
    public void fromLittleEndianThrowsIfLengthIsGreaterThanEight() {
        // Arrange
        byte[] buffer = new byte[1]; // A non-null buffer is required, its size and content are irrelevant here.
        int offset = 0;
        int invalidLength = 9; // Any length > 8 is invalid for a long.
        String expectedMessage = "Can't read more than eight bytes into a long value";

        // Act & Assert
        try {
            ByteUtils.fromLittleEndian(buffer, offset, invalidLength);
            fail("Expected an IllegalArgumentException because the length is greater than 8.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}