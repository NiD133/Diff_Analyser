package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.CharBuffer;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;

/**
 * Readable tests for NumericEntityUnescaper.
 *
 * Focus:
 * - Correctly unescapes decimal and hex numeric entities.
 * - Behavior with/without a required semicolon depending on options.
 * - Indexed translate contract (return value and exceptions).
 * - Constructor behavior with null options.
 */
public class NumericEntityUnescaperTest {

    // -------- Helpers

    private static String unescape(final String s, final OPTION... opts) {
        return new NumericEntityUnescaper(opts).translate(s);
    }

    // -------- Happy-path unescaping

    @Test
    public void unescapesDecimalEntity_whenSemicolonPresent_defaultRequiresSemicolon() {
        // 65 (decimal) => 'A'
        assertEquals("pre A post", unescape("pre &#65; post"));
    }

    @Test
    public void unescapesHexEntity_lowerAndUpperCaseX() {
        // 0x41 => 'A'
        assertEquals("A", unescape("&#x41;"));
        // 0x7A => 'z'
        assertEquals("z", unescape("&#X7a;"));
    }

    @Test
    public void doesNotUnescape_whenSemicolonMissing_byDefault() {
        // Default option requires a semicolon, so the input is unchanged.
        assertEquals("&#65", unescape("&#65"));
        assertEquals("&#x41", unescape("&#x41"));
    }

    @Test
    public void unescapes_whenSemicolonMissing_withOptionalOption() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);

        // Without semicolon, should still unescape and stop at first non-digit.
        assertEquals("A next", unescaper.translate("&#65 next"));
        assertEquals("A next", unescaper.translate("&#x41 next"));
    }

    @Test
    public void throwsWhenSemicolonMissing_withErrorOption() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);

        assertThrows(IllegalArgumentException.class, () -> unescaper.translate("&#65"));
        assertThrows(IllegalArgumentException.class, () -> unescaper.translate("&#x41"));
    }

    @Test
    public void leavesNonEntitiesUnchanged() {
        final String s1 = "D+RTgb,eb:&],ms";
        final String s2 = "D+RTgb,eb:&s";
        assertEquals(s1, unescape(s1));
        assertEquals(s2, unescape(s2));
    }

    // -------- Indexed translate(...) contract

    @Test
    public void indexedTranslate_returnsZeroWhenNoEntityAtIndex() throws IOException {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final int consumed = unescaper.translate("0", 0, new StringWriter());
        assertEquals(0, consumed);
    }

    @Test
    public void indexedTranslate_consumesEntireEntityAndWritesDecodedChar() throws IOException {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final StringWriter out = new StringWriter();

        // "&#3;" length is 4, should be fully consumed and decoded to U+0003.
        final int consumed = unescaper.translate("&#3;", 0, out);

        assertEquals(4, consumed);
        assertEquals("\u0003", out.toString());
    }

    @Test
    public void indexedTranslate_throwsNPE_whenInputIsNull() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertThrows(NullPointerException.class, () -> unescaper.translate(null, 0, new StringWriter()));
    }

    @Test
    public void indexedTranslate_throwsStringIndexOutOfBounds_whenIndexBeyondStringEnd() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        // Index equals length triggers StringIndexOutOfBoundsException when accessing charAt(index).
        assertThrows(StringIndexOutOfBoundsException.class, () -> unescaper.translate("abc", 3, new StringWriter()));
    }

    @Test
    public void indexedTranslate_throwsIndexOutOfBounds_whenIndexBeyondCharBufferLimit() {
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final CharBuffer buf = CharBuffer.wrap("abc");
        // Index beyond limit triggers Buffer's IndexOutOfBoundsException.
        assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(buf, 5, new StringWriter()));
    }

    // -------- Constructor behavior

    @Test
    public void constructor_throwsNPE_whenNullOptionProvided() {
        // Passing a vararg array containing null is not allowed by EnumSet.copyOf(...)
        assertThrows(NullPointerException.class, () -> new NumericEntityUnescaper((OPTION) null));
    }
}