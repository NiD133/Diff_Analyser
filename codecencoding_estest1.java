package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for {@link CodecEncoding}.
 */
public class CodecEncodingTest {

    /**
     * Tests that getSpecifier() for a RunCodec correctly omits the specifiers
     * for its inner codecs if they are the same as the default codec for the band.
     * This results in a more compact encoding.
     */
    @Test
    public void getSpecifierForRunCodecWithDefaultInnerCodecsShouldBeCompact() {
        // Arrange
        // The default codec for the band, which is also used as the inner codec.
        final BHSDCodec defaultAndInnerCodec = Codec.CHAR3;

        // A RunCodec with K=256, where both its inner codecs (A and B) are the default codec.
        final int kValue = 256;
        final RunCodec runCodec = new RunCodec(kValue, defaultAndInnerCodec, defaultAndInnerCodec);

        // The specifier for a RunCodec is defined as (129, K-1, specifier(A), specifier(B)).
        // Since both inner codecs A and B are the same as the default, their specifiers
        // should be empty. Thus, the expected result is just (129, 256-1).
        final int[] expectedSpecifier = {129, 255};

        // Act
        final int[] actualSpecifier = CodecEncoding.getSpecifier(runCodec, defaultAndInnerCodec);

        // Assert
        assertArrayEquals("The specifier should be compact when inner codecs match the default.",
            expectedSpecifier, actualSpecifier);
    }
}