package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.SortedMap;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link Charsets}.
 */
@SuppressWarnings("deprecation") // testing deprecated code
public class CharsetsTest {

    /**
     * For parameterized tests.
     */
    public static final String AVAIL_CHARSETS = "org.apache.commons.io.CharsetsTest#availableCharsetsKeySet";
    /**
     * For parameterized tests.
     */
    public static final String REQUIRED_CHARSETS = "org.apache.commons.io.CharsetsTest#getRequiredCharsetNames";

    /**
     * For parameterized tests.
     *
     * @return {@code Charset.availableCharsets().keySet()}.
     */
    public static Set<String> availableCharsetsKeySet() {
        return Charset.availableCharsets().keySet();
    }

    /**
     * For parameterized tests.
     *
     * @return {@code Charset.requiredCharsets().keySet()}.
     */
    public static Set<String> getRequiredCharsetNames() {
        return Charsets.requiredCharsets().keySet();
    }

    @Test
    public void testIso8859_1() {
        // Verify that the name of the ISO-8859-1 charset is correct
        assertEquals("ISO-8859-1", Charsets.ISO_8859_1.name());
    }

    @Test
    public void testRequiredCharsets() {
        // Get the required charsets
        final SortedMap<String, Charset> requiredCharsets = Charsets.requiredCharsets();

        // Verify that the required charsets are present
        verifyRequiredCharset(requiredCharsets, "US-ASCII", StandardCharsets.US_ASCII);
        verifyRequiredCharset(requiredCharsets, "ISO-8859-1", StandardCharsets.ISO_8859_1);
        verifyRequiredCharset(requiredCharsets, "UTF-8", StandardCharsets.UTF_8);
        verifyRequiredCharset(requiredCharsets, "UTF-16", StandardCharsets.UTF_16);
        verifyRequiredCharset(requiredCharsets, "UTF-16BE", StandardCharsets.UTF_16BE);
        verifyRequiredCharset(requiredCharsets, "UTF-16LE", StandardCharsets.UTF_16LE);
    }

    private void verifyRequiredCharset(final SortedMap<String, Charset> requiredCharsets, final String charsetName, final Charset expectedCharset) {
        // Verify that the charset is present and has the correct name
        assertEquals(expectedCharset.name(), requiredCharsets.get(charsetName).name());
    }

    @Test
    public void testToCharset_String() {
        // Verify that toCharset returns the default charset when given null
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((String) null));

        // Verify that toCharset returns the default charset when given a null Charset
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((Charset) null));

        // Verify that toCharset returns the default charset when given the default Charset
        assertEquals(Charset.defaultCharset(), Charsets.toCharset(Charset.defaultCharset()));

        // Verify that toCharset returns the given Charset when it is not null
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8));
    }

    @Test
    public void testToCharset_String_Charset() {
        // Verify that toCharset returns null when given null and a null default Charset
        assertNull(Charsets.toCharset((String) null, null));

        // Verify that toCharset returns the default charset when given null and a default Charset
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((String) null, Charset.defaultCharset()));

        // Verify that toCharset returns the default charset when given a null Charset and a default Charset
        assertEquals(Charset.defaultCharset(), Charsets.toCharset((Charset) null, Charset.defaultCharset()));

        // Verify that toCharset returns null when given a null Charset and a null default Charset
        assertNull(Charsets.toCharset((Charset) null, null));

        // Verify that toCharset returns the given Charset when it is not null and the default Charset is not null
        assertEquals(Charset.defaultCharset(), Charsets.toCharset(Charset.defaultCharset(), Charset.defaultCharset()));

        // Verify that toCharset returns the given Charset when it is not null and the default Charset is null
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8, null));

        // Verify that toCharset returns the given Charset when it is not null and the default Charset is not null
        assertEquals(StandardCharsets.UTF_8, Charsets.toCharset(StandardCharsets.UTF_8, Charset.defaultCharset()));
    }

    @Test
    public void testUsAscii() {
        // Verify that the name of the US-ASCII charset is correct
        assertEquals(StandardCharsets.US_ASCII.name(), Charsets.US_ASCII.name());
    }

    @Test
    public void testUtf16() {
        // Verify that the name of the UTF-16 charset is correct
        assertEquals(StandardCharsets.UTF_16.name(), Charsets.UTF_16.name());
    }

    @Test
    public void testUtf16Be() {
        // Verify that the name of the UTF-16BE charset is correct
        assertEquals(StandardCharsets.UTF_16BE.name(), Charsets.UTF_16BE.name());
    }

    @Test
    public void testUtf16Le() {
        // Verify that the name of the UTF-16LE charset is correct
        assertEquals(StandardCharsets.UTF_16LE.name(), Charsets.UTF_16LE.name());
    }

    @Test
    public void testUtf8() {
        // Verify that the name of the UTF-8 charset is correct
        assertEquals(StandardCharsets.UTF_8.name(), Charsets.UTF_8.name());
    }
}