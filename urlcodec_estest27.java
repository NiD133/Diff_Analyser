package org.apache.commons.codec.net;

import org.junit.Test;

/**
 * Tests for the {@link URLCodec} class, focusing on exception handling for the decode method.
 */
public class URLCodecTest {

    /**
     * Tests that the decode(String, String) method throws a NullPointerException
     * when the provided charset name is null. This is the expected behavior because
     * the underlying implementation passes the charset directly to a standard
     * Java library method that does not accept null.
     */
    @Test(expected = NullPointerException.class)
    public void decodeWithNullCharsetShouldThrowNullPointerException() throws Exception {
        // Arrange
        URLCodec urlCodec = new URLCodec();
        String anyEncodedString = "test+data";

        // Act: This call is expected to throw a NullPointerException.
        // The test will pass if the exception is thrown, and fail otherwise.
        urlCodec.decode(anyEncodedString, null);
    }
}