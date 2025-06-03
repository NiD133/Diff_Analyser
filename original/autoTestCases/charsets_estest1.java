package org.apache.commons.io;

import org.junit.jupiter.api.Test; // Use JUnit 5
import static org.junit.jupiter.api.Assertions.*; // More concise assertions
import java.nio.charset.Charset;

class CharsetsTest { // Renamed class for clarity (reflecting what's being tested)

    @Test
    void testToCharsetWithNullCharsetUsesDefault() {
        // Arrange: Define a default charset
        Charset defaultCharset = Charsets.UTF_8;

        // Act: Call the method being tested with a null charset and the default
        Charset resultCharset = Charsets.toCharset(null, defaultCharset);

        // Assert: Verify that the result is the default charset
        assertNotNull(resultCharset, "Result should not be null."); // Clear message
        assertEquals(defaultCharset, resultCharset, "Should return the default charset when null is provided."); // More descriptive message
    }
}