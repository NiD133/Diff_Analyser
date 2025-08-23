package org.apache.commons.compress.harmony.pack200;

import org.junit.Test;

/**
 * Unit tests for the {@link CodecEncoding} class.
 */
public class CodecEncodingTest {

    /**
     * Verifies that calling getCanonicalCodec() with an index greater than the
     * number of defined canonical codecs throws an ArrayIndexOutOfBoundsException.
     * The source class defines 116 canonical codecs, so valid indices are 0-115.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getCanonicalCodecShouldThrowExceptionForIndexOutOfBounds() {
        // The canonicalCodec array has a fixed size of 116.
        // Any index >= 116 is invalid and should cause an exception.
        final int invalidIndex = 260;
        CodecEncoding.getCanonicalCodec(invalidIndex);
    }
}