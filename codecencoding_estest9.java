package org.apache.commons.compress.harmony.pack200;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 * Tests for {@link CodecEncoding#getSpecifier(Codec, Codec)}.
 */
public class CodecEncodingTest {

    /**
     * Tests that getSpecifier correctly encodes a RunCodec where its 'B' sub-codec
     * is the same as the default codec for the band. In this scenario, the specifier
     * for the 'B' codec should be omitted from the output.
     */
    @Test(timeout = 4000)
    public void testGetSpecifierForRunCodecWithDefaultBCodec() throws Exception {
        // Arrange
        // The codec to be encoded is a RunCodec(K, A, B).
        // Here, K=4, and both sub-codecs A and B are SIGNED5.
        final int runLength = 4;
        final BHSDCodec signed5Codec = Codec.SIGNED5;
        final RunCodec runCodec = new RunCodec(runLength, signed5Codec, signed5Codec);

        // The default codec for the band is also SIGNED5. This means the 'B' codec
        // of the RunCodec is considered the default.
        final Codec defaultCodecForBand = Codec.SIGNED5;

        // The expected specifier is [125, 27].
        // - 125: The header byte for this specific RunCodec configuration.
        // - 27: The canonical specifier for the 'A' codec (SIGNED5).
        // The specifier for the 'B' codec is omitted as it matches the default.
        final int[] expectedSpecifier = {125, 27};

        // Act
        final int[] actualSpecifier = CodecEncoding.getSpecifier(runCodec, defaultCodecForBand);

        // Assert
        assertArrayEquals(
            "The generated specifier for the RunCodec is incorrect.",
            expectedSpecifier,
            actualSpecifier);
    }
}