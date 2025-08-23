package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.CharEncoding;
import org.junit.Test;

/**
 * Tests for {@link URLCodec}.
 */
public class URLCodecTest {

    /**
     * Tests that the default constructor initializes the codec with UTF-8 encoding.
     */
    @Test
    public void defaultConstructorShouldSetUtf8Encoding() {
        // Arrange: Create a URLCodec instance using its default constructor.
        final URLCodec urlCodec = new URLCodec();
        final String expectedEncoding = CharEncoding.UTF_8;

        // Act: Retrieve the encoding from the newly created instance.
        final String actualEncoding = urlCodec.getEncoding();

        // Assert: Verify that the encoding is the expected default, UTF-8.
        assertEquals(expectedEncoding, actualEncoding);
    }
}