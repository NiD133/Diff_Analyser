package org.apache.commons.lang3;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for CharSetUtils.
 *
 * The tests are organized by method and cover:
 * - Null and empty input handling.
 * - Typical, documented examples.
 * - A few additional, illustrative edge cases.
 */
public class CharSetUtilsTest {

    // containsAny

    @Test
    public void containsAny_nullAndEmptyInputs_returnFalse() {
        assertFalse(CharSetUtils.containsAny(null, "a-z"));
        assertFalse(CharSetUtils.containsAny("", "a-z"));
        assertFalse(CharSetUtils.containsAny("hello", (String[]) null));
        assertFalse(CharSetUtils.containsAny("hello")); // no sets provided
        assertFalse(CharSetUtils.containsAny("hello", new String[0])); // empty sets array
    }

    @Test
    public void containsAny_withRanges_matchesAndNonMatches() {
        assertTrue(CharSetUtils.containsAny("hello", "k-p")); // 'l' and 'o' are in k..p
        assertFalse(CharSetUtils.containsAny("hello", "a-d")); // none of h,e,l,o are in a..d
    }

    @Test
    public void containsAny_withLiteralCharacterSetString() {
        String allChars = "h|Bv_9mUP7'&Y";
        assertTrue(CharSetUtils.containsAny(allChars, allChars)); // shares characters with itself
        assertFalse(CharSetUtils.containsAny("A-Za-z", "Xii8KJ")); // no overlap
    }

    // count

    @Test
    public void count_nullAndEmptyInputs_returnZero() {
        assertEquals(0, CharSetUtils.count(null, "k-p"));
        assertEquals(0, CharSetUtils.count("", "k-p"));
        assertEquals(0, CharSetUtils.count("&", new String[0]));
    }

    @Test
    public void count_withRanges_examplesFromJavadoc() {
        assertEquals(3, CharSetUtils.count("hello", "k-p")); // l,l,o
        assertEquals(1, CharSetUtils.count("hello", "a-e")); // e
    }

    // delete

    @Test
    public void delete_nullAndEmptyInputs() {
        assertNull(CharSetUtils.delete(null, "hl"));
        assertEquals("", CharSetUtils.delete("", "hl"));
        assertEquals("A-Z", CharSetUtils.delete("A-Z", (String[]) null)); // null set -> unchanged
        assertEquals("A-Z", CharSetUtils.delete("A-Z")); // no sets -> unchanged
    }

    @Test
    public void delete_basicCharacterRemoval() {
        assertEquals("eo", CharSetUtils.delete("hello", "hl"));
        assertEquals("ho", CharSetUtils.delete("hello", "le"));
    }

    @Test
    public void delete_withRange_removesLettersOnly() {
        assertEquals("-", CharSetUtils.delete("A-Z", "A-Z")); // remove A..Z, keep '-'
    }

    // keep

    @Test
    public void keep_nullAndEmptyInputs() {
        assertNull(CharSetUtils.keep(null, "hl"));
        assertEquals("", CharSetUtils.keep("", "hl"));
        assertEquals("", CharSetUtils.keep("A-Z", (String[]) null)); // null set -> empty result
        assertEquals("", CharSetUtils.keep("A-Z")); // no sets -> empty result
    }

    @Test
    public void keep_basicCharacterRetention() {
        assertEquals("hll", CharSetUtils.keep("hello", "hl"));
        assertEquals("ell", CharSetUtils.keep("hello", "le"));
    }

    @Test
    public void keep_withRange_keepsOnlyMatching() {
        assertEquals("AZ", CharSetUtils.keep("A-Z", "A-Z")); // hyphen is not part of range
    }

    // squeeze

    @Test
    public void squeeze_nullAndEmptyInputs() {
        assertNull(CharSetUtils.squeeze(null, "k-p"));
        assertEquals("", CharSetUtils.squeeze("", "k-p"));
        assertEquals("...", CharSetUtils.squeeze("...", (String[]) null)); // null set -> unchanged
        assertEquals("...", CharSetUtils.squeeze("...")); // no sets -> unchanged
    }

    @Test
    public void squeeze_withRanges_examplesFromJavadoc() {
        assertEquals("helo", CharSetUtils.squeeze("hello", "k-p")); // collapse 'll'
        assertEquals("hello", CharSetUtils.squeeze("hello", "a-e")); // no collapsing
    }

    @Test
    public void squeeze_singleCharacterSet_collapsesRuns() {
        assertEquals(".", CharSetUtils.squeeze("...", ".")); // collapse '.' run
    }

    @Test
    public void squeeze_lowercaseAlphabet_example() {
        // Collapses only repeated lowercase letters
        assertEquals("ofset canot be negative",
                CharSetUtils.squeeze("offset cannot be negative", "a-z"));
    }

    // constructor

    @Test
    public void constructor_isPublicButNotIntendedForUse() {
        assertNotNull(new CharSetUtils());
    }
}