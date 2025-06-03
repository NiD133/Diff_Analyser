package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.Charset;

public class CharsetsTest {

    @Test
    public void testToCharset_NullInput_ReturnsUsAscii() {
        // Arrange: No explicit arrangement needed as we're testing with null input.

        // Act: Convert a null String to a Charset, using US-ASCII as the default.
        Charset charset = Charsets.toCharset((String) null, Charset.forName("US-ASCII"));

        // Assert: The resulting Charset should be US-ASCII.
        assertEquals("US-ASCII", charset.name());
    }
}