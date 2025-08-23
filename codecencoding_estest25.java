package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Tests for {@link CodecEncoding}.
 */
public class CodecEncodingTest {

    /**
     * Tests that getCodec() for a BHSD codec specifier (like 141) correctly consumes
     * exactly three bytes from the input stream to read the H, S, and D parameters.
     */
    @Test
    public void testGetCodecForBHSDConsumesThreeBytesFromStream() throws IOException, Pack200Exception {
        // Arrange
        // According to the Pack200 specification, a codec specifier value of 141
        // indicates a BHSD codec where B=1. The H, S, and D parameters are then
        // read as three subsequent bytes from the input stream.
        final int bhsdCodecSpecifier = 141;
        final Codec defaultCodec = Codec.UNSIGNED5; // A default codec is required, but not used in this case.

        // An input stream with more than 3 bytes.
        final byte[] headerData = { 10, 20, 30, 40, 50 };
        final ByteArrayInputStream headerStream = new ByteArrayInputStream(headerData);

        final int initialAvailableBytes = headerStream.available();
        final int expectedBytesRead = 3; // For H, S, and D parameters.

        // Act
        CodecEncoding.getCodec(bhsdCodecSpecifier, headerStream, defaultCodec);

        // Assert
        final int finalAvailableBytes = headerStream.available();
        final int expectedRemainingBytes = initialAvailableBytes - expectedBytesRead;

        assertEquals("The method should consume exactly 3 bytes from the stream.",
                expectedRemainingBytes, finalAvailableBytes);
    }
}