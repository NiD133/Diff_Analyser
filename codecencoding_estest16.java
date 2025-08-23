package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * This test class contains an improved version of a previously auto-generated test
 * for the {@link CodecEncoding#getSpecifier(Codec, Codec)} method.
 */
public class CodecEncoding_ESTestTest16 {

    /**
     * Tests that getSpecifier() for a PopulationCodec returns the correct specifier
     * when all its sub-codecs are identical and also match the default codec.
     *
     * <p>
     * According to the Pack200 specification, the specifier for a PopulationCodec
     * with three identical sub-codecs (C) and a default codec (C_default) is
     * defined as {@code (143, S(C, C_default))}. When C is the same as C_default,
     * the specifier for C, {@code S(C, C_default)}, is empty. Therefore, the
     * final specifier should be a single-element array containing just {@code 143}.
     * </p>
     */
    @Test
    public void getSpecifierForPopulationCodecWithIdenticalSubCodecsMatchingDefault() {
        // Arrange
        // A standard BHSDCodec to be used for the sub-codecs and as the default codec.
        final BHSDCodec sharedCodec = Codec.MDELTA5;

        // A PopulationCodec where the token, favoured, and unfavoured codecs are all the same.
        final PopulationCodec populationCodec = new PopulationCodec(sharedCodec, sharedCodec, sharedCodec);

        // Act
        // Get the specifier for the PopulationCodec, using the shared codec as the default.
        final int[] actualSpecifier = CodecEncoding.getSpecifier(populationCodec, sharedCodec);

        // Assert
        // The specifier should be [143]. The original auto-generated test asserted a
        // length of 2, which appears incorrect based on the specification and the
        // source code implementation. This assertion is more precise and correct.
        final int[] expectedSpecifier = {143};
        assertArrayEquals(expectedSpecifier, actualSpecifier);
    }
}