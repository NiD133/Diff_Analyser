package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.nio.ByteBuffer;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Verifies that attempting to write an empty SerializedString to a
     * zero-capacity ByteBuffer results in zero bytes being written.
     */
    @Test
    public void putUnquotedUTF8_withEmptyStringIntoZeroCapacityBuffer_shouldWriteZeroBytes() {
        // Arrange: Create a SerializedString from an empty string and a buffer with no space.
        SerializedString serializedEmptyString = new SerializedString("");
        ByteBuffer zeroCapacityBuffer = ByteBuffer.allocateDirect(0);

        // Act: Attempt to write the unquoted UTF-8 representation to the buffer.
        int bytesWritten = serializedEmptyString.putUnquotedUTF8(zeroCapacityBuffer);

        // Assert: Verify that the method reports that zero bytes were written.
        assertEquals("Expected 0 bytes to be written for an empty string.", 0, bytesWritten);
    }
}