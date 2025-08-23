package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for {@link CodecEncoding}.
 */
public class CodecEncodingTest {

    /**
     * Tests that {@link CodecEncoding#getCodec(int, InputStream, Codec)} returns the correct
     * pre-defined canonical codec when the encoding value is less than 116.
     * <p>
     * According to the Pack200 specification, values from 1 to 115 represent
     * canonical codecs that are fixed and do not require reading from the input stream.
     * This test verifies that the method correctly retrieves the codec from the internal
     * canonical table for such values.
     * </p>
     */
    @Test
    public void getCodecShouldReturnCanonicalCodecForValueBelow116() throws IOException, Pack200Exception {
        // Arrange
        final int canonicalValue = 115; // A value representing a pre-defined canonical codec.
        final InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        final Codec defaultCodec = Codec.BYTE1; // A default codec that should be ignored for this value.

        // Act
        final Codec actualCodec = CodecEncoding.getCodec(canonicalValue, emptyInputStream, defaultCodec);

        // Assert
        // The method should return the specific canonical codec instance associated with the value.
        final BHSDCodec expectedCodec = CodecEncoding.getCanonicalCodec(canonicalValue);
        assertSame("Expected the canonical codec for value 115", expectedCodec, actualCodec);

        // The original test only verified that the codec was signed.
        // The assertSame check is more precise, but we can keep this for clarity.
        assertTrue("The canonical codec for value 115 should be signed", actualCodec.isSigned());
    }
}