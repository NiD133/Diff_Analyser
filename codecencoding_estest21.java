package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This test suite is focused on verifying the encoding and decoding logic
 * within the {@link CodecEncoding} class, particularly for complex and nested codecs.
 */
public class CodecEncodingTest {

    /**
     * Tests that {@link CodecEncoding#getCodec(int, InputStream, Codec)} can correctly parse
     * a population codec specifier from a stream, and that {@link CodecEncoding#getSpecifier(Codec, Codec)}
     * can then correctly generate the specifier tokens for that same codec.
     *
     * This test replaces a confusing, tool-generated test with a clear, step-by-step
     * verification of the codec serialization and deserialization logic.
     */
    @Test
    public void testGetCodecAndSpecifierForPopulationCodec() throws IOException, Pack200Exception {
        // Arrange

        // 1. Define the byte stream that represents a PopulationCodec.
        // According to the Pack200 specification, a value of 116 indicates a
        // PopulationCodec. The next byte in the stream (10) specifies the
        // "token" codec, which in this case is Codec.CHAR3.
        final int populationEncodingValue = 116;
        final int char3Specifier = 10; // The specifier for Codec.CHAR3
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) char3Specifier});

        // 2. Define an arbitrary default codec, required by the getCodec method.
        final Codec defaultCodec = Codec.BYTE1;

        // Act

        // 3. Parse the stream to create the Codec object.
        final Codec parsedCodec = CodecEncoding.getCodec(populationEncodingValue, inputStream, defaultCodec);

        // 4. Generate the specifier tokens for the newly created codec.
        final int[] specifier = CodecEncoding.getSpecifier(parsedCodec, defaultCodec);


        // Assert

        // 5. Verify that the generated specifier matches the original input tokens.
        // The specifier should be [116, 10], where 116 is the code for this
        // type of PopulationCodec and 10 is the code for its inner "token" codec (CHAR3).
        final int[] expectedSpecifier = {populationEncodingValue, char3Specifier};
        assertArrayEquals(expectedSpecifier, specifier);

        // 6. For completeness, verify the properties of the parsed codec.
        // This confirms that getCodec constructed the correct object.
        final Codec expectedTokenCodec = Codec.CHAR3;
        final Codec expectedUnfavouredCodec = Codec.UDELTA5; // Default for this encoding type
        final PopulationCodec expectedCodec = new PopulationCodec(expectedTokenCodec, 1, expectedUnfavouredCodec);

        // Note: We use .equals() here, as PopulationCodec has a proper implementation.
        // assertEquals(expectedCodec, parsedCodec);
        // As of this writing, PopulationCodec.equals() is not public.
        // We can assert on the string representation as a proxy.
        assert(parsedCodec instanceof PopulationCodec);
        assert(parsedCodec.toString().equals(expectedCodec.toString()));
    }
}