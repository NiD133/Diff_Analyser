package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link URLCodec}.
 */
public class URLCodecTest {

    /**
     * Tests that the getDefaultCharset() method correctly returns the
     * charset string that was provided in the constructor.
     */
    @Test
    public void getDefaultCharsetShouldReturnCharsetProvidedInConstructor() {
        // Arrange
        final String expectedCharset = "";
        final URLCodec urlCodec = new URLCodec(expectedCharset);

        // Act
        final String actualCharset = urlCodec.getDefaultCharset();

        // Assert
        assertEquals("The default charset should match the one provided in the constructor.",
                     expectedCharset, actualCharset);
    }
}