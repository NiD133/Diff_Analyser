package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.charset.Charset;
import java.util.SortedMap;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Charsets} utility class.  Specifically focuses on the
 * {@link Charsets#requiredCharsets()} method.
 */
public class CharsetsTest {

    /**
     *  Tests that the {@link Charsets#requiredCharsets()} method returns a map
     *  containing the expected standard charsets.
     */
    @Test
    public void testRequiredCharsets() {
        // Retrieve the map of required charsets
        final SortedMap<String, Charset> requiredCharsets = Charsets.requiredCharsets();

        // Assert that the map is not null and contains some elements.  This provides
        // a basic sanity check that the method returns something usable.
        assertNotNull(requiredCharsets, "The requiredCharsets map should not be null.");

        // Verify that the map contains the expected standard charsets. We check
        // each one individually for clarity and better error messages if a test fails.

        // Verify US-ASCII charset
        Charset usAscii = requiredCharsets.get("US-ASCII");
        assertNotNull(usAscii, "US-ASCII charset should be present in the requiredCharsets map.");
        assertEquals("US-ASCII", usAscii.name(), "US-ASCII charset name mismatch.");

        // Verify ISO-8859-1 charset
        Charset iso88591 = requiredCharsets.get("ISO-8859-1");
        assertNotNull(iso88591, "ISO-8859-1 charset should be present in the requiredCharsets map.");
        assertEquals("ISO-8859-1", iso88591.name(), "ISO-8859-1 charset name mismatch.");

        // Verify UTF-8 charset
        Charset utf8 = requiredCharsets.get("UTF-8");
        assertNotNull(utf8, "UTF-8 charset should be present in the requiredCharsets map.");
        assertEquals("UTF-8", utf8.name(), "UTF-8 charset name mismatch.");

        // Verify UTF-16 charset
        Charset utf16 = requiredCharsets.get("UTF-16");
        assertNotNull(utf16, "UTF-16 charset should be present in the requiredCharsets map.");
        assertEquals("UTF-16", utf16.name(), "UTF-16 charset name mismatch.");

        // Verify UTF-16BE charset
        Charset utf16be = requiredCharsets.get("UTF-16BE");
        assertNotNull(utf16be, "UTF-16BE charset should be present in the requiredCharsets map.");
        assertEquals("UTF-16BE", utf16be.name(), "UTF-16BE charset name mismatch.");

        // Verify UTF-16LE charset
        Charset utf16le = requiredCharsets.get("UTF-16LE");
        assertNotNull(utf16le, "UTF-16LE charset should be present in the requiredCharsets map.");
        assertEquals("UTF-16LE", utf16le.name(), "UTF-16LE charset name mismatch.");
    }
}