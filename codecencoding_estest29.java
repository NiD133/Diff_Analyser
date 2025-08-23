package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

// Note: The class name and its parent are from the original EvoSuite-generated test.
public class CodecEncoding_ESTestTest29 extends CodecEncoding_ESTest_scaffolding {

    /**
     * Tests that getCodec() returns the correct canonical codec for a given specifier value.
     * For specifier values less than 116, the input stream and default codec are ignored.
     * This test case focuses on the specifier value '1'.
     */
    @Test
    public void getCodecShouldReturnCanonicalCodecForValueOne() throws Exception {
        // Arrange
        final int canonicalSpecifier = 1;

        // For a canonical specifier < 116, the InputStream and default Codec are not used.
        // We provide non-null, valid-but-irrelevant values for these parameters.
        final InputStream unusedInputStream = new ByteArrayInputStream(new byte[0]);
        final Codec unusedDefaultCodec = Codec.UNSIGNED5;

        // The expected codec is the canonical one defined for the specifier 1.
        // This corresponds to a BHSDCodec(1, 256), representing an unsigned byte.
        final BHSDCodec expectedCodec = CodecEncoding.getCanonicalCodec(canonicalSpecifier);

        // Act
        final Codec actualCodec = CodecEncoding.getCodec(canonicalSpecifier, unusedInputStream, unusedDefaultCodec);

        // Assert
        assertNotNull("The returned codec should not be null.", actualCodec);
        assertEquals("The returned codec should be the canonical codec for the given specifier.",
                expectedCodec, actualCodec);

        // Further verify properties of the returned codec to make the test more concrete
        // and to document the behavior of the codec for specifier 1.
        assertTrue("The codec should be an instance of BHSDCodec.", actualCodec instanceof BHSDCodec);
        final BHSDCodec bhsdCodec = (BHSDCodec) actualCodec;
        assertEquals("Smallest value for an unsigned byte codec should be 0.", 0L, bhsdCodec.smallest());
        assertEquals("Largest value for an unsigned byte codec should be 255.", 255L, bhsdCodec.largest());
    }
}