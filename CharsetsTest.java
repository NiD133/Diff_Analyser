package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.SortedMap;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Charsets} class.
 */
@SuppressWarnings("deprecation") // testing deprecated code
public class CharsetsTest {

    /**
     * Constant for parameterized tests to get available charsets.
     */
    public static final String AVAIL_CHARSETS = "org.apache.commons.io.CharsetsTest#availableCharsetsKeySet";

    /**
     * Constant for parameterized tests to get required charsets.
     */
    public static final String REQUIRED_CHARSETS = "org.apache.commons.io.CharsetsTest#getRequiredCharsetNames";

    /**
     * Returns the set of available charset names.
     *
     * @return Set of available charset names.
     */
    public static Set<String> availableCharsetsKeySet() {
        return Charset.availableCharsets().keySet();
    }

    /**
     * Returns the set of required charset names.
     *
     * @return Set of required charset names.
     */
    public static Set<String> getRequiredCharsetNames() {
        return Charsets.requiredCharsets().keySet();
    }

    @Test
    public void testIso8859_1() {
        // Verify that the ISO-8859-1 charset is correctly defined
        assertEquals("ISO-8859-1", Charsets.ISO_8859_1.name());
    }

    @Test
    public void testRequiredCharsets() {
        // Retrieve the map of required charsets
        final SortedMap<String, Charset> requiredCharsets = Charsets.requiredCharsets();

        // Verify that the required charsets are present and correctly mapped
        assertEquals("US-ASCII", requiredCharsets.get("US-ASCII").name());
        assertEquals("ISO-8859-1", requiredCharsets.get("ISO-8859-1").name());
        assertEquals("UTF-8", requiredCharsets.get("UTF-8").name());
        assertEquals("UTF-16", requiredCharsets.get("UTF-16").name());
        assertEquals("UTF-16BE", requiredCharsets.get("UTF-16BE").name());
        assertEquals("UTF-16LE", requiredCharsets.get("UTF-16LE").name());
    }

    @Test
    public void testToCharset_String() {
        // Test conversion of null and default charsets
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((String) null));
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((Charset) null));
        assertEquals(Charset.defaultCharset(), Charsets.toCharset(Charset.defaultCharset()));
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8));
    }

    @Test
    public void testToCharset_String_Charset() {
        // Test conversion with default charset fallback
        assertNull(Charsets.toCharset((String) null, null));
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((String) null, Charset.defaultCharset()));
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((Charset) null, Charset.defaultCharset()));
        assertNull(Charsets.toCharset((Charset) null, null));
        assertEquals(Charset.defaultCharset(), Charsets.toCharset(Charset.defaultCharset(), Charset.defaultCharset()));
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8, Charset.defaultCharset()));
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8, null));
    }

    @Test
    public void testUsAscii() {
        // Verify that the US-ASCII charset is correctly defined
        assertEquals(StandardCharsets.US_ASCII.name(), Charsets.US_ASCII.name());
    }

    @Test
    public void testUtf16() {
        // Verify that the UTF-16 charset is correctly defined
        assertEquals(StandardCharsets.UTF_16.name(), Charsets.UTF_16.name());
    }

    @Test
    public void testUtf16Be() {
        // Verify that the UTF-16BE charset is correctly defined
        assertEquals(StandardCharsets.UTF_16BE.name(), Charsets.UTF_16BE.name());
    }

    @Test
    public void testUtf16Le() {
        // Verify that the UTF-16LE charset is correctly defined
        assertEquals(StandardCharsets.UTF_16LE.name(), Charsets.UTF_16LE.name());
    }

    @Test
    public void testUtf8() {
        // Verify that the UTF-8 charset is correctly defined
        assertEquals(StandardCharsets.UTF_8.name(), Charsets.UTF_8.name());
    }
}