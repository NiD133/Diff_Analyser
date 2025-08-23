package org.apache.commons.codec.net;

import org.apache.commons.codec.CharEncoding;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that a URLCodec instance created with the default constructor
     * correctly reports UTF-8 as its default charset.
     */
    @Test
    public void testDefaultConstructorSetsCharsetToUTF8() {
        // Arrange: Create a URLCodec instance using the default constructor.
        final URLCodec urlCodec = new URLCodec();
        final String expectedCharset = CharEncoding.UTF_8;

        // Act: Get the default charset from the instance.
        final String actualCharset = urlCodec.getDefaultCharset();

        // Assert: Verify that the default charset is UTF-8.
        assertEquals("The default charset should be UTF-8", expectedCharset, actualCharset);
    }
}