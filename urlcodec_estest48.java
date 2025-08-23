package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Contains tests for the static methods of the {@link URLCodec} class.
 */
public class URLCodecStaticTest {

    @Test
    public void decodeUrlShouldReturnNullForNullInput() {
        // The static decodeUrl method is expected to handle a null input
        // gracefully by returning null, without throwing an exception.

        // Act
        final byte[] result = URLCodec.decodeUrl(null);

        // Assert
        assertNull("Decoding a null byte array should result in null.", result);
    }
}