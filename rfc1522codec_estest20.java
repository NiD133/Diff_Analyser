package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link RFC1522Codec} class, using its concrete implementation {@link BCodec}.
 */
public class BCodecTest {

    /**
     * Tests that an instance of BCodec created with the default constructor
     * correctly reports its default charset as "UTF-8".
     */
    @Test
    public void getDefaultCharsetShouldReturnUtf8ForDefaultConstructor() {
        // Arrange
        // The BCodec default constructor should initialize with the UTF-8 charset.
        BCodec bCodec = new BCodec();
        String expectedCharset = "UTF-8";

        // Act
        String actualCharset = bCodec.getDefaultCharset();

        // Assert
        assertEquals("The default charset should be UTF-8.", expectedCharset, actualCharset);
    }
}