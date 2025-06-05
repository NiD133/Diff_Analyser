package org.apache.commons.io;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Charsets} utility class.
 */
class CharsetsTest {

    @Test
    void testToCharset_Charset_WithNonNullCharset_ReturnsSameCharset() {
        Charset utf8 = StandardCharsets.UTF_8;
        Charset result = Charsets.toCharset(utf8);
        assertSame(utf8, result, "Should return the same Charset instance when input is not null.");
    }

    @Test
    void testToCharset_Charset_WithNullCharset_ReturnsDefaultCharset() {
        Charset result = Charsets.toCharset((Charset) null);
        assertEquals(Charset.defaultCharset(), result, "Should return the default Charset when input is null.");
    }

    @Test
    void testToCharset_CharsetCharset_firstCharsetNull_returnsSecondCharset() {
        Charset utf8 = StandardCharsets.UTF_8;
        Charset result = Charsets.toCharset(null, utf8);
        assertSame(utf8, result, "Should return the default Charset when first input is null.");
    }

    @Test
    void testToCharset_CharsetCharset_secondCharsetNull_returnsFirstCharset() {
        Charset utf8 = StandardCharsets.UTF_8;
        Charset result = Charsets.toCharset(utf8, null);
        assertSame(utf8, result, "Should return the same Charset instance when input is not null.");
    }

    @Test
    void testToCharset_String_WithValidCharsetName_ReturnsCharset() {
        Charset result = Charsets.toCharset("UTF-8");
        assertEquals(StandardCharsets.UTF_8, result, "Should return the Charset for the given name.");
    }

    @Test
    void testToCharset_StringCharset_WithValidCharsetName_ReturnsCharset() {
        Charset utf8 = StandardCharsets.UTF_8;
        Charset result = Charsets.toCharset("UTF-8", utf8);
        assertEquals(StandardCharsets.UTF_8, result, "Should return the Charset for the given name.");
    }

    @Test
    void testToCharset_String_WithNullCharsetName_ReturnsDefaultCharset() {
        Charset defaultCharset = Charset.defaultCharset();
        Charset result = Charsets.toCharset((String) null);
        assertEquals(defaultCharset, result, "Should return the default Charset when charset name is null.");
    }

    @Test
    void testToCharset_StringCharset_WithNullCharsetName_ReturnsDefaultCharset() {
        Charset utf8 = StandardCharsets.UTF_8;
        Charset result = Charsets.toCharset((String) null, utf8);
        assertSame(utf8, result, "Should return the default Charset when charset name is null.");
    }

    @Test
    void testToCharset_String_WithInvalidCharsetName_ThrowsUnsupportedCharsetException() {
        assertThrows(UnsupportedCharsetException.class, () -> Charsets.toCharset("INVALID_CHARSET"),
                "Should throw UnsupportedCharsetException for invalid charset name.");
    }

    @Test
    void testToCharset_StringCharset_WithInvalidCharsetName_ThrowsUnsupportedCharsetException() {
        Charset utf8 = StandardCharsets.UTF_8;
        assertThrows(UnsupportedCharsetException.class, () -> Charsets.toCharset("INVALID_CHARSET", utf8),
                "Should throw UnsupportedCharsetException for invalid charset name.");
    }

    @Test
    void testToCharset_String_WithIllegalCharsetName_ThrowsIllegalCharsetNameException() {
        assertThrows(IllegalCharsetNameException.class, () -> Charsets.toCharset("?"),
                "Should throw IllegalCharsetNameException for illegal charset name.");
    }

    @Test
    void testToCharset_StringCharset_WithIllegalCharsetName_ThrowsIllegalCharsetNameException() {
        Charset utf8 = StandardCharsets.UTF_8;
        assertThrows(IllegalCharsetNameException.class, () -> Charsets.toCharset("?", utf8),
                "Should throw IllegalCharsetNameException for illegal charset name.");
    }

    @Test
    void testRequiredCharsets_ReturnsNonEmptyMap() {
        SortedMap<String, Charset> requiredCharsets = Charsets.requiredCharsets();
        assertFalse(requiredCharsets.isEmpty(), "Should return a non-empty map of required charsets.");
        assertEquals(6, requiredCharsets.size());
        assertTrue(requiredCharsets.containsKey("US-ASCII"));
        assertTrue(requiredCharsets.containsKey("ISO-8859-1"));
        assertTrue(requiredCharsets.containsKey("UTF-8"));
        assertTrue(requiredCharsets.containsKey("UTF-16BE"));
        assertTrue(requiredCharsets.containsKey("UTF-16LE"));
        assertTrue(requiredCharsets.containsKey("UTF-16"));
    }
}