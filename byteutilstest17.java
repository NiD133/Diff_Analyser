package org.apache.commons.compress.utils;

import static org.apache.commons.compress.utils.ByteUtils.fromLittleEndian;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.utils.ByteUtils.InputStreamByteSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ByteUtils}.
 */
class ByteUtilsTest {

    @Test
    @DisplayName("fromLittleEndian with a ByteSupplier should throw an IOException if the stream ends before providing the requested number of bytes.")
    void fromLittleEndianWithSupplierThrowsIOExceptionOnPrematureEnd() {
        // Arrange: An input stream with fewer bytes than we will request.
        final byte[] sourceBytes = { 2, 3 }; // 2 bytes available
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceBytes);
        final InputStreamByteSupplier supplier = new InputStreamByteSupplier(inputStream);
        final int requestedLength = 3; // 3 bytes requested

        // Act & Assert: Attempting to read more bytes than available should throw an IOException.
        assertThrows(IOException.class,
            () -> fromLittleEndian(supplier, requestedLength),
            "Should throw IOException when the supplier cannot provide enough bytes.");
    }
}