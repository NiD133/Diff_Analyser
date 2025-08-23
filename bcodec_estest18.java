package org.apache.commons.codec.net;

import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Unit tests for the {@link BCodec} class.
 */
public class BCodecTest {

    /**
     * Tests that the BCodec constructor throws a NullPointerException
     * when the provided Charset is null. This is the expected behavior
     * as a Charset is required for encoding and decoding operations.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullCharsetShouldThrowNullPointerException() {
        // Attempt to create a BCodec instance with a null Charset.
        // This should trigger a NullPointerException.
        new BCodec((Charset) null);
    }
}