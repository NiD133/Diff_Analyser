package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(DataInput, int)} throws an EOFException
     * if the stream ends before the requested number of bytes has been read.
     */
    @Test
    void fromLittleEndianWithDataInputShouldThrowEOFExceptionIfStreamEndsPrematurely() {
        // Arrange: Create an input stream with fewer bytes than we will request.
        final byte[] inputBytes = { 0x01, 0x02 }; // 2 bytes available
        final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(inputBytes));
        final int bytesToRead = 3; // But we request 3 bytes

        // Act & Assert: Expect an EOFException when trying to read past the end of the stream.
        assertThrows(EOFException.class, () -> fromLittleEndian(dataInput, bytesToRead));
    }
}