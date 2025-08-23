package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class contains tests for the {@link CodecEncoding} class, focusing on specifier generation.
 */
public class CodecEncodingTest {

    /**
     * Tests that {@link CodecEncoding#getSpecifier(Codec, Codec)} generates the correct
     * integer array representation for a {@link RunCodec} with a large K-value.
     */
    @Test(timeout = 4000)
    public void getSpecifierForRunCodecWithLargeKValueShouldReturnCorrectEncoding() {
        // ARRANGE
        // A specifier is an integer array that represents a codec's encoding format.
        // This test verifies the specifier for a RunCodec, which encodes a sequence of values.

        // The RunCodec is defined by a count (K) and two sub-codecs (A and B).
        // We use the maximum integer value for K and the UDELTA5 codec for A and B.
        final BHSDCodec udelta5Codec = Codec.UDELTA5;
        final RunCodec runCodec = new RunCodec(Integer.MAX_VALUE, udelta5Codec, udelta5Codec);

        // The default codec for the band can affect the final specifier.
        // This test uses a complex PopulationCodec as the default.
        final PopulationCodec defaultCodec = new PopulationCodec(runCodec, runCodec, runCodec);

        // The expected specifier consists of four parts:
        // 1. A token identifying it as a RunCodec (124).
        // 2. The encoded representation of the K-value (524286 for Integer.MAX_VALUE).
        // 3. The specifier for the 'A' codec (41 for UDELTA5).
        // 4. The specifier for the 'B' codec (41 for UDELTA5).
        final int[] expectedSpecifier = {124, 524286, 41, 41};

        // ACT
        final int[] actualSpecifier = CodecEncoding.getSpecifier(runCodec, defaultCodec);

        // ASSERT
        assertNotNull(actualSpecifier);
        assertArrayEquals(expectedSpecifier, actualSpecifier);
    }
}