package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class CodecEncoding_ESTestTest19 extends CodecEncoding_ESTest_scaffolding {

    /**
     * Tests that the getSpecifier method correctly encodes a RunCodec.
     * A RunCodec is a composite codec that represents runs of identical values.
     * Its specifier consists of an opcode, an encoded run length, and specifiers
     * for its sub-codecs.
     */
    @Test
    public void getSpecifierForRunCodecReturnsCorrectEncoding() {
        // Arrange
        // A RunCodec is defined by a run length (K) and two codecs (A and B).
        // In this test, both codecs A and B are UNSIGNED5.
        final int runLength = 631;
        final BHSDCodec elementCodec = Codec.UNSIGNED5; // Codec for both A and B
        final Codec codecToEncode = new RunCodec(runLength, elementCodec, elementCodec);

        // The default codec for the band, which can affect the encoding of sub-codecs.
        final Codec defaultCodec = Codec.CHAR3;

        // The expected specifier for a RunCodec(K, A, B) follows the format:
        // [opcode, encoded(K-1), specifier(A), specifier(B)]
        //
        // 122: The opcode for a RunCodec.
        //  38: The value of (runLength - 1) = 630, encoded using the UNSIGNED5 codec.
        //  26: The specifier for the element codec (UNSIGNED5).
        //  26: The specifier for the element codec (UNSIGNED5).
        final int[] expectedSpecifier = { 122, 38, 26, 26 };

        // Act
        final int[] actualSpecifier = CodecEncoding.getSpecifier(codecToEncode, defaultCodec);

        // Assert
        assertArrayEquals("The specifier for the RunCodec is incorrect.", expectedSpecifier, actualSpecifier);
    }
}