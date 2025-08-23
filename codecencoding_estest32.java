package org.apache.commons.compress.harmony.pack200;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This test class focuses on verifying the functionality of the {@link CodecEncoding} class.
 * The original test was auto-generated, and this version has been refactored for clarity.
 */
public class CodecEncoding_ESTestTest32 extends CodecEncoding_ESTest_scaffolding {

    /**
     * Tests that {@link CodecEncoding#getSpecifierForDefaultCodec(BHSDCodec)} returns the correct,
     * hard-coded specifier for a known canonical codec. The Pack200 specification defines a
     * set of "canonical" codecs, each identified by a single-byte specifier. This test
     * verifies the mapping for the {@code Codec.CHAR3} codec.
     */
    @Test
    public void getSpecifierForDefaultCodecShouldReturnCorrectSpecifierForChar3() {
        // Arrange: Define the input codec and its expected specifier value.
        // According to the Pack200 specification, Codec.CHAR3 is a canonical codec.
        final BHSDCodec char3Codec = Codec.CHAR3;
        final int expectedSpecifier = 116;

        // Act: Call the method under test to get the specifier.
        final int actualSpecifier = CodecEncoding.getSpecifierForDefaultCodec(char3Codec);

        // Assert: Verify that the returned specifier matches the expected value.
        assertEquals("The specifier for the CHAR3 codec should be 116.", expectedSpecifier, actualSpecifier);
    }
}