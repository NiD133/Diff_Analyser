package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class to verify the correctness of predefined Charset constants in the {@link Charsets} class.
 * This class focuses on ensuring that the Charsets class correctly provides the expected Charset instances.
 */
public class CharsetsTest { // Renamed to CharsetsTest for clarity and convention

    /**
     * Tests that the {@link Charsets#ISO_8859_1} constant returns the correct Charset instance
     * corresponding to the ISO-8859-1 character encoding.
     */
    @Test
    @DisplayName("Test ISO-8859-1 Charset Name") // More descriptive test name
    public void testIso8859_1() {
        // Verify that the name of the Charset retrieved from Charsets.ISO_8859_1 matches "ISO-8859-1".
        assertEquals("ISO-8859-1", Charsets.ISO_8859_1.name(), "The Charset name should be ISO-8859-1"); // Added assertion message
    }
}