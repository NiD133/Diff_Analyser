package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.SortedMap;

import org.apache.commons.io.Charsets;

public class CharsetsTest {

    @Test
    public void testToCharset_CharsetNull_DefaultToUTF8() {
        // Arrange: Pass null Charset as input
        // Act: Convert null charset to charset.
        Charset result = Charsets.toCharset((Charset) null);

        // Assert: Result should default to UTF-8
        assertEquals("UTF-8", result.name());
    }

    @Test
    public void testToCharset_CharsetNull_FallbackToGivenCharset() {
        // Arrange: Pass null Charset as input, and UTF-8 as default
        Charset defaultCharset = Charsets.UTF_8;

        // Act: Convert null charset to charset, defaulting to UTF-8.
        Charset result = Charsets.toCharset((Charset) null, defaultCharset);

        // Assert: Result should be UTF-8
        assertEquals("UTF-8", result.name());
    }

    @Test
    public void testToCharset_CharsetNotNull_ReturnSameCharset() {
        // Arrange:  Use US-ASCII charset.
        Charset usAscii = Charsets.US_ASCII;

        // Act: Convert it to charset
        Charset result = Charsets.toCharset(usAscii);

        // Assert: Result should be the same US-ASCII instance.
        assertSame(usAscii, result);
    }

    @Test
    public void testToCharset_StringNull_DefaultToNull() {
        // Arrange: String charsetName is null
        // Act: Convert null string to charset, defaulting to null.
        Charset result = Charsets.toCharset((String) null);

        // Assert: Result should be null
        assertNull(result);
    }

     @Test
    public void testToCharset_StringNull_FallbackToGivenCharset() {
        // Arrange: String charsetName is null, fallback to US_ASCII
        Charset defaultCharset = Charsets.US_ASCII;

        // Act: Convert null string to charset, defaulting to US_ASCII.
        Charset result = Charsets.toCharset((String) null, defaultCharset);

        // Assert: Result should be US-ASCII
        assertEquals("US-ASCII", result.name());
    }


    @Test
    public void testToCharset_ValidCharsetName_ReturnsCharset() {
        // Arrange: Valid charset name "ISO-8859-2"
        // Act: Convert "ISO-8859-2" string to charset.
        Charset result = Charsets.toCharset("ISO-8859-2");

        // Assert: Result should be the corresponding Charset.
        assertEquals("ISO-8859-2", result.name());
    }

    @Test(expected = IllegalCharsetNameException.class)
    public void testToCharset_InvalidCharsetName_ThrowsIllegalCharsetNameException() {
        // Arrange: Invalid charset name "Tv&)_"
        // Act: Try to convert it to charset
        // Assert: Should throw IllegalCharsetNameException
        Charsets.toCharset("Tv&)_");
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void testToCharset_UnsupportedCharsetName_ThrowsUnsupportedCharsetException() {
        // Arrange: Unsupported charset name "unsupported-charset"
        // Act: Try to convert it to charset with UTF-8 as default.
        // Assert: Should throw UnsupportedCharsetException
        Charsets.toCharset("unsupported-charset", Charsets.UTF_8);
    }

    @Test
    public void testToCharset_CharsetAndStringNull_ReturnsNull() {
        // Arrange: Both charset and string are null.
        // Act: Convert them to Charset.
        Charset result = Charsets.toCharset((String) null, (Charset) null);
        // Assert: Returns null.
        assertNull(result);
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void testToCharset_UnsupportedCharset_ThrowsException() {
        // Arrange: Pass a string of unsupported charset name
        // Act & Assert: Should throw UnsupportedCharsetException
        Charsets.toCharset("org.apache.commons.io.serialization.ObjectStreamClassPredicate");
    }


    @Test
    public void testRequiredCharsets_ReturnsMap() {
        // Act: Get all required charsets
        SortedMap<String, Charset> result = Charsets.requiredCharsets();

        // Assert:  Map should not be empty
        assertFalse(result.isEmpty());
    }
}