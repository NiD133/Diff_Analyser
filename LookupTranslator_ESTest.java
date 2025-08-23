package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.CharBuffer;

import org.junit.Test;

/**
 * Unit tests for LookupTranslator focused on clarity and intent.
 *
 * Key ideas tested:
 * - No-op behavior when there are no mappings.
 * - Basic replacement and the number of consumed characters.
 * - Longest-match behavior when multiple keys share a prefix.
 * - Low-level translate index bounds behavior (different exceptions by input type).
 * - Constructor input validation (pair size, empty key, null entries).
 */
public class LookupTranslatorTest {

    // No mappings: translating any string should return it unchanged.
    @Test
    public void translate_returnsOriginal_whenNoMappings() {
        LookupTranslator translator = new LookupTranslator((CharSequence[][]) null);

        String result = translator.translate("abc");

        assertEquals("abc", result);
    }

    // Low-level translate: verify it writes the mapped value and returns the number of consumed chars.
    @Test
    public void lowLevelTranslate_writesReplacement_andReturnsConsumedLength() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[] {"ab", "X"}
        );

        StringWriter out = new StringWriter();

        int consumed = translator.translate("abacus", 0, out);

        assertEquals("X", out.toString());
        assertEquals(2, consumed);
    }

    // If nothing matches at a given index, low-level translate returns 0 and writes nothing.
    @Test
    public void lowLevelTranslate_noMatch_returnsZero_andWritesNothing() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[] {"ab", "X"}
        );

        StringWriter out = new StringWriter();

        int consumed = translator.translate("zzz", 0, out);

        assertEquals(0, consumed);
        assertEquals("", out.toString());
    }

    // Longest-match behavior: when keys share a prefix, the longest key is chosen.
    @Test
    public void translate_choosesLongestMatch_whenMultipleKeysSharePrefix() {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[] {"ca", "Y"},
            new CharSequence[] {"cat", "X"}
        );

        String result = translator.translate("cats");

        assertEquals("Xs", result);
    }

    // Index out of range for String input: StringIndexOutOfBoundsException.
    @Test
    public void lowLevelTranslate_indexOutOfRange_onString_throwsStringIndexOutOfBoundsException() {
        LookupTranslator translator = new LookupTranslator((CharSequence[][]) null);
        StringWriter out = new StringWriter();

        assertThrows(StringIndexOutOfBoundsException.class, () -> translator.translate("abc", 3, out));
    }

    // Index out of range for CharBuffer input: IndexOutOfBoundsException (from NIO buffer).
    @Test
    public void lowLevelTranslate_indexOutOfRange_onCharBuffer_throwsIndexOutOfBoundsException() {
        LookupTranslator translator = new LookupTranslator((CharSequence[][]) null);
        StringWriter out = new StringWriter();
        CharBuffer input = CharBuffer.wrap("a");

        assertThrows(IndexOutOfBoundsException.class, () -> translator.translate(input, 2, out));
    }

    // Constructor validation: each pair must have at least two elements [key, value].
    @Test
    public void constructor_rejectsPairWithLessThanTwoElements() {
        CharSequence[][] bad = new CharSequence[][] {
            new CharSequence[0] // length-0 pair -> accessing seq[0] fails
        };

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> new LookupTranslator(bad));
    }

    // Constructor validation: key must not be empty (charAt(0) used to build prefix set).
    @Test
    public void constructor_rejectsEmptyKey() {
        assertThrows(StringIndexOutOfBoundsException.class,
            () -> new LookupTranslator(new CharSequence[] {"", "X"}));
    }

    // Constructor validation: null key is not allowed.
    @Test
    public void constructor_rejectsNullKey() {
        assertThrows(NullPointerException.class,
            () -> new LookupTranslator(new CharSequence[] {null, "X"}));
    }

    // Constructor validation: null value is not allowed.
    @Test
    public void constructor_rejectsNullValue() {
        assertThrows(NullPointerException.class,
            () -> new LookupTranslator(new CharSequence[] {"a", null}));
    }
}