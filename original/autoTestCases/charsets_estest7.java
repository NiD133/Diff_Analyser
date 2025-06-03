package org.apache.commons.io;

import org.junit.jupiter.api.Test; // Use JUnit 5 for clarity
import static org.junit.jupiter.api.Assertions.*; // More readable assertions

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

public class CharsetsTest { // More descriptive class name

    @Test
    void testToCharset_ValidAlias_ReturnsCorrectCharset() { // Descriptive method name
        // Arrange:  The input alias for a Charset.
        String charsetAlias = "L2";

        // Act: Convert the alias to a Charset.
        Charset charset = Charsets.toCharset(charsetAlias);

        // Assert: The Charset returned is the expected one (ISO-8859-2).
        assertEquals(Charset.forName("ISO-8859-2"), charset, "The charset should be ISO-8859-2");
    }
}