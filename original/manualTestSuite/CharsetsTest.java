package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.SortedMap;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Charsets} utility class.
 * This class provides constants for commonly used character encodings
 * and utility methods for working with Charsets.
 */
@SuppressWarnings("deprecation") // suppress warnings for testing deprecated code
public class CharsetsTest {

    @Test
    public void testIso8859_1() {
        // Verify that the Charsets.ISO_8859_1 constant is correctly initialized.
        // It should represent the ISO-8859-1 character encoding.
        assertEquals("ISO-8859-1", Charsets.ISO_8859_1.name());
    }

    @Test
    public void testRequiredCharsets() {
        // Verify that the requiredCharsets method returns a map containing expected character sets.
        // This test checks for the presence and correctness of certain standard charsets (as of Java 6).
        final SortedMap<String, Charset> requiredCharsets = Charsets.requiredCharsets();

        // Assert that the expected charsets are present and have the correct names.
        assertEquals("US-ASCII", requiredCharsets.get("US-ASCII").name());
        assertEquals("ISO-8859-1", requiredCharsets.get("ISO-8859-1").name());
        assertEquals("UTF-8", requiredCharsets.get("UTF-8").name());
        assertEquals("UTF-16", requiredCharsets.get("UTF-16").name());
        assertEquals("UTF-16BE", requiredCharsets.get("UTF-16BE").name());
        assertEquals("UTF-16LE", requiredCharsets.get("UTF-16LE").name());
    }

    @Test
    public void testToCharset_String() {
        // Tests the toCharset(String) method to ensure it handles null and default values correctly.
        // When a null or invalid charset name is passed, it should return the default charset.

        assertEquals(Charset.defaultCharset(), Charsets.toCharset((String) null));
    }

    @Test
    public void testToCharset_Charset() {
        // Tests the toCharset(Charset) method to ensure it returns the provided Charset or the default if null.
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((Charset) null));
        assertEquals(Charset.defaultCharset(), Charsets.toCharset(Charset.defaultCharset()));
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8));
    }

    @Test
    public void testToCharset_String_Charset() {
        // Tests the toCharset(String, Charset) method with both String and Charset input,
        // to ensure it returns the provided Charset if the String is null or invalid,
        // and handles cases where the backup Charset is null.

        assertNull(Charsets.toCharset((String) null, null));
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((String) null, Charset.defaultCharset()));
    }

    @Test
    public void testToCharset_Charset_Charset() {
        // Tests the toCharset(Charset, Charset) method, particularly handling null primary and fallback Charset.
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((Charset) null, Charset.defaultCharset()));
        assertNull(Charsets.toCharset((Charset) null, null));
        assertEquals(Charset.defaultCharset(), Charsets.toCharset(Charset.defaultCharset(), Charset.defaultCharset()));
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8, Charset.defaultCharset()));
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8, null));
    }

    @Test
    public void testUsAscii() {
        // Verify that the Charsets.US_ASCII constant is correctly initialized.
        // It should represent the US-ASCII character encoding.
        assertEquals(StandardCharsets.US_ASCII.name(), Charsets.US_ASCII.name());
    }

    @Test
    public void testUtf16() {
        // Verify that the Charsets.UTF_16 constant is correctly initialized.
        // It should represent the UTF-16 character encoding.
        assertEquals(StandardCharsets.UTF_16.name(), Charsets.UTF_16.name());
    }

    @Test
    public void testUtf16Be() {
        // Verify that the Charsets.UTF_16BE constant is correctly initialized.
        // It should represent the UTF-16BE character encoding.
        assertEquals(StandardCharsets.UTF_16BE.name(), Charsets.UTF_16BE.name());
    }

    @Test
    public void testUtf16Le() {
        // Verify that the Charsets.UTF_16LE constant is correctly initialized.
        // It should represent the UTF-16LE character encoding.
        assertEquals(StandardCharsets.UTF_16LE.name(), Charsets.UTF_16LE.name());
    }

    @Test
    public void testUtf8() {
        // Verify that the Charsets.UTF_8 constant is correctly initialized.
        // It should represent the UTF-8 character encoding.
        assertEquals(StandardCharsets.UTF_8.name(), Charsets.UTF_8.name());
    }

}