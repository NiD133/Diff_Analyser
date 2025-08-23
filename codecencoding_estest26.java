package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Unit tests for the {@link CodecEncoding} class.
 */
public class CodecEncodingTest {

    /**
     * Verifies that getCodec() consumes one byte from the input stream when provided
     * with an encoding specifier that requires an additional byte. According to the
     * Pack200 specification, specifiers >= 116 may require reading from the stream.
     * The value 144 corresponds to a POPULATION codec, which reads a "favored value" byte.
     */
    @Test
    public void getCodecWithValue144ShouldConsumeOneByteFromStream() throws Exception {
        // Arrange
        // 144 is a specifier for a POPULATION codec, which requires reading an extra byte.
        final int populationEncodingSpecifier = 144;
        final BHSDCodec defaultCodec = Codec.UNSIGNED5; // A default codec is required, but its specific type is not critical for this test.
        final byte[] inputData = new byte[16]; // The content doesn't matter, just that the stream is not empty.
        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final int initialAvailableBytes = inputStream.available();

        // Act
        CodecEncoding.getCodec(populationEncodingSpecifier, inputStream, defaultCodec);

        // Assert
        final int bytesConsumed = 1;
        final int expectedAvailableBytes = initialAvailableBytes - bytesConsumed;
        final int actualAvailableBytes = inputStream.available();

        assertEquals("Should have consumed one byte from the input stream",
                expectedAvailableBytes, actualAvailableBytes);
    }
}