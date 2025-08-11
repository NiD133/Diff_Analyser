package org.apache.commons.lang3;

import org.junit.Test;

import java.nio.CharBuffer;

import static org.junit.Assert.*;

public class CharSequenceUtilsTest {

    // ---------------------------------------------------------------------
    // indexOf(CharSequence, int, int) - search for a single code point
    // ---------------------------------------------------------------------

    @Test
    public void indexOf_char_findsAsciiCharAtStart() {
        int idx = CharSequenceUtils.indexOf("abc", 'a', 0);
        assertEquals(0, idx);
    }

    @Test
    public void indexOf_char_returnsMinusOneWhenNotFound() {
        int idx = CharSequenceUtils.indexOf("abc", 'z', 0);
        assertEquals(-1, idx);
    }

    @Test
    public void indexOf_char_negativeStartBehavesLikeZero() {
        int idx = CharSequenceUtils.indexOf("abc", 'b', -100);
        assertEquals(1, idx);
    }

    @Test
    public void indexOf_char_startBeyondLengthReturnsMinusOne() {
        int idx = CharSequenceUtils.indexOf("abc", 'a', 100);
        assertEquals(-1, idx);
    }

    @Test
    public void indexOf_char_findsSupplementaryCodePoint() {
        // U+10000 is outside BMP; represent as a single code point.
        StringBuilder sb = new StringBuilder().appendCodePoint(0x10000).append("x");
        int idx = CharSequenceUtils.indexOf(sb, 0x10000, 0);
        assertEquals(0, idx);
    }

    @Test
    public void indexOf_char_doesNotFindMissingSupplementaryCodePoint() {
        String s = "abc";
        int idx = CharSequenceUtils.indexOf(s, 0x10000, 0);
        assertEquals(-1, idx);
    }

    // ---------------------------------------------------------------------
    // lastIndexOf(CharSequence, int, int) - search backwards for a code point
    // ---------------------------------------------------------------------

    @Test
    public void lastIndexOf_char_findsLastOccurrence() {
        int idx = CharSequenceUtils.lastIndexOf("abca", 'a', 100); // start beyond length -> search from end
        assertEquals(3, idx);
    }

    @Test
    public void lastIndexOf_char_negativeStartReturnsMinusOne() {
        int idx = CharSequenceUtils.lastIndexOf("abc", 'b', -1);
        assertEquals(-1, idx);
    }

    @Test
    public void lastIndexOf_char_startBeyondLengthStartsAtEnd() {
        int idx = CharSequenceUtils.lastIndexOf("abc", 'b', 999);
        assertEquals(1, idx);
    }

    @Test
    public void lastIndexOf_char_missingSupplementaryCodePoint() {
        int idx = CharSequenceUtils.lastIndexOf("abc", 0x10000, 2);
        assertEquals(-1, idx);
    }

    // ---------------------------------------------------------------------
    // indexOf(CharSequence, CharSequence, int)
    // ---------------------------------------------------------------------

    @Test
    public void indexOf_seq_findsSubstring() {
        int idx = CharSequenceUtils.indexOf("hello world", "world", 0);
        assertEquals(6, idx);
    }

    @Test
    public void indexOf_seq_returnsMinusOneWhenNotFound() {
        int idx = CharSequenceUtils.indexOf("hello world", "mars", 0);
        assertEquals(-1, idx);
    }

    @Test
    public void indexOf_seq_emptyNeedleWithLargeStartReturnsLength() {
        String s = "', is neither of type Map.Entry nor an Array"; // length 44
        int idx = CharSequenceUtils.indexOf(s, new StringBuffer(), Integer.MAX_VALUE);
        assertEquals(s.length(), idx);
    }

    @Test
    public void indexOf_seq_emptyHaystackAndEmptyNeedleReturnsZero() {
        int idx = CharSequenceUtils.indexOf("", new StringBuilder(), 8);
        assertEquals(0, idx);
    }

    @Test
    public void indexOf_seq_nullHaystackReturnsMinusOne() {
        int idx = CharSequenceUtils.indexOf(null, "x", 0);
        assertEquals(-1, idx);
    }

    @Test
    public void indexOf_seq_nullNeedleReturnsMinusOne() {
        int idx = CharSequenceUtils.indexOf("abc", null, 0);
        assertEquals(-1, idx);
    }

    @Test
    public void indexOf_seq_bothNullReturnsMinusOne() {
        int idx = CharSequenceUtils.indexOf(null, null, 0);
        assertEquals(-1, idx);
    }

    // ---------------------------------------------------------------------
    // lastIndexOf(CharSequence, CharSequence, int)
    // ---------------------------------------------------------------------

    @Test
    public void lastIndexOf_seq_findsLastSubstring() {
        int idx = CharSequenceUtils.lastIndexOf("ababa", "aba", 99);
        assertEquals(2, idx);
    }

    @Test
    public void lastIndexOf_seq_returnsMinusOneWhenNotFound() {
        int idx = CharSequenceUtils.lastIndexOf("ababa", "xyz", 4);
        assertEquals(-1, idx);
    }

    @Test
    public void lastIndexOf_seq_emptyNeedleReturnsMinOfStartAndLength() {
        // String's lastIndexOf("", fromIndex) returns min(fromIndex, length)
        int idx = CharSequenceUtils.lastIndexOf("ab", "", 4);
        assertEquals(2, idx);
    }

    @Test
    public void lastIndexOf_seq_nullHaystackReturnsMinusOne() {
        int idx = CharSequenceUtils.lastIndexOf(null, "x", 0);
        assertEquals(-1, idx);
    }

    @Test
    public void lastIndexOf_seq_nullNeedleReturnsMinusOne() {
        int idx = CharSequenceUtils.lastIndexOf("abc", null, 0);
        assertEquals(-1, idx);
    }

    @Test
    public void lastIndexOf_seq_bothNullReturnsMinusOne() {
        int idx = CharSequenceUtils.lastIndexOf(null, null, -10);
        assertEquals(-1, idx);
    }

    // ---------------------------------------------------------------------
    // regionMatches(CharSequence, boolean, int, CharSequence, int, int)
    // ---------------------------------------------------------------------

    @Test
    public void regionMatches_exactMatch() {
        boolean ok = CharSequenceUtils.regionMatches("abcdef", false, 2, "cdef", 0, 4);
        assertTrue(ok);
    }

    @Test
    public void regionMatches_ignoreCaseMatch() {
        boolean ok = CharSequenceUtils.regionMatches("AbCdEf", true, 2, "cDeF", 0, 4);
        assertTrue(ok);
    }

    @Test
    public void regionMatches_mismatchReturnsFalse() {
        boolean ok = CharSequenceUtils.regionMatches("abcdef", false, 2, "zzef", 0, 2);
        assertFalse(ok);
    }

    @Test
    public void regionMatches_zeroLengthAlwaysTrueWhenIndicesValid() {
        boolean ok = CharSequenceUtils.regionMatches(new StringBuilder(), false, 0, new StringBuffer(), 0, 0);
        assertTrue(ok);
    }

    @Test(expected = NullPointerException.class)
    public void regionMatches_throwsOnNullInputs() {
        CharSequenceUtils.regionMatches(null, false, 0, null, 0, 1);
    }

    @Test
    public void regionMatches_outOfBoundsReturnsFalse() {
        boolean ok = CharSequenceUtils.regionMatches("abc", false, 5, "x", 0, 1);
        assertFalse(ok);
    }

    // ---------------------------------------------------------------------
    // subSequence(CharSequence, int)
    // ---------------------------------------------------------------------

    @Test
    public void subSequence_returnsNullForNullInput() {
        assertNull(CharSequenceUtils.subSequence(null, 0));
    }

    @Test
    public void subSequence_returnsTailFromStart() {
        CharSequence out = CharSequenceUtils.subSequence("abcdef", 2);
        assertEquals("cdef", out);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void subSequence_throwsOnNegativeStart() {
        CharSequenceUtils.subSequence(CharBuffer.wrap("abc"), -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void subSequence_throwsWhenStartGreaterThanLength() {
        CharSequenceUtils.subSequence(new StringBuffer("abc"), 5);
    }

    // ---------------------------------------------------------------------
    // toCharArray(CharSequence)
    // ---------------------------------------------------------------------

    @Test
    public void toCharArray_fromString() {
        char[] chars = CharSequenceUtils.toCharArray("abc");
        assertArrayEquals(new char[] {'a', 'b', 'c'}, chars);
    }

    @Test
    public void toCharArray_fromEmptyStringBuilder() {
        char[] chars = CharSequenceUtils.toCharArray(new StringBuilder());
        assertArrayEquals(new char[0], chars);
    }

    @Test
    public void toCharArray_fromCharBuffer() {
        CharBuffer cb = CharBuffer.wrap("xyz");
        char[] chars = CharSequenceUtils.toCharArray(cb);
        assertArrayEquals(new char[] {'x', 'y', 'z'}, chars);
    }

    // ---------------------------------------------------------------------
    // Additional corner cases with code points
    // ---------------------------------------------------------------------

    @Test
    public void indexOf_char_supplementaryAtZeroThenShiftedContent() {
        StringBuilder sb = new StringBuilder().appendCodePoint(0x10000);
        sb.append("1234");
        // Insert some other content in between and ensure not found with wrong start
        int idxAtZero = CharSequenceUtils.indexOf(sb, 0x10000, 0);
        int idxNotFoundFromOne = CharSequenceUtils.indexOf(sb, 0x10000, 1);
        assertEquals(0, idxAtZero);
        assertEquals(-1, idxNotFoundFromOne);
    }

    @Test
    public void lastIndexOf_char_noMatchWhenSearchingDifferentSupplementary() {
        StringBuilder sb = new StringBuilder().appendCodePoint(0x10003);
        int idx = CharSequenceUtils.lastIndexOf(sb, 0x10002, 0);
        assertEquals(-1, idx);
    }
}