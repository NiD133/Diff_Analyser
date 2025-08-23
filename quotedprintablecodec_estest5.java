package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.nio.charset.StandardCharsets;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    /**
     * Tests that a QuotedPrintableCodec instance created with the default
     * constructor uses UTF-8 as its default charset.
     */
    @Test
    public void testDefaultConstructorUsesUTF8Charset() {
        // Arrange
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String expectedCharset = StandardCharsets.UTF_8.name();

        // Act
        String actualCharset = codec.getDefaultCharset();

        // Assert
        assertEquals("The default charset should be UTF-8.", expectedCharset, actualCharset);
    }
}