package org.jsoup.internal;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class StringUtilTest {

    // ----- Character classification -----

    @Test
    public void isHexDigit_recognizesHexDigits() {
        assertTrue(StringUtil.isHexDigit('0'));
        assertTrue(StringUtil.isHexDigit('9'));
        assertTrue(StringUtil.isHexDigit('a'));
        assertTrue(StringUtil.isHexDigit('f'));
        assertTrue(StringUtil.isHexDigit('A'));
        assertTrue(StringUtil.isHexDigit('F'));
    }

    @Test
    public void isHexDigit_rejectsNonHex() {
        assertFalse(StringUtil.isHexDigit('g'));
        assertFalse(StringUtil.isHexDigit('G'));
        assertFalse(StringUtil.isHexDigit('%'));
        assertFalse(StringUtil.isHexDigit('\u008F'));
    }

    @Test
    public void isAsciiLetter_recognizesLetters() {
        assertTrue(StringUtil.isAsciiLetter('a'));
        assertTrue(StringUtil.isAsciiLetter('z'));
        assertTrue(StringUtil.isAsciiLetter('A'));
        assertTrue(StringUtil.isAsciiLetter('Z'));
    }

    @Test
    public void isAsciiLetter_rejectsNonLetters() {
        assertFalse(StringUtil.isAsciiLetter('0'));
        assertFalse(StringUtil.isAsciiLetter('!'));
        assertFalse(StringUtil.isAsciiLetter('}'));
    }

    @Test
    public void isDigit_recognizesDigits() {
        assertTrue(StringUtil.isDigit('0'));
        assertTrue(StringUtil.isDigit('9'));
    }

    @Test
    public void isDigit_rejectsNonDigits() {
        assertFalse(StringUtil.isDigit('a'));
        assertFalse(StringUtil.isDigit('%'));
        assertFalse(StringUtil.isDigit('Y'));
    }

    // ----- ASCII -----

    @Test
    public void isAscii_acceptsAsciiOnly() {
        assertTrue(StringUtil.isAscii("Hello 123!"));
        assertTrue(StringUtil.isAscii("")); // empty is ASCII
    }

    @Test
    public void isAscii_rejectsNonAscii() {
        assertFalse(StringUtil.isAscii("über"));    // contains non-ASCII
        assertFalse(StringUtil.isAscii("a\u0080b")); // extended char
    }

    @Test
    public void isAscii_nullThrows() {
        assertThrows(IllegalArgumentException.class, () -> StringUtil.isAscii(null));
    }

    // ----- Whitespace classification -----

    @Test
    public void isWhitespace_matchesHtmlSpecSet() {
        assertTrue(StringUtil.isWhitespace(9));   // \t
        assertTrue(StringUtil.isWhitespace(10));  // \n
        assertTrue(StringUtil.isWhitespace(12));  // \f
        assertTrue(StringUtil.isWhitespace(13));  // \r
        assertTrue(StringUtil.isWhitespace(32));  // space

        assertFalse(StringUtil.isWhitespace(0));
        assertFalse(StringUtil.isWhitespace('A'));
        assertFalse(StringUtil.isWhitespace(21));
    }

    @Test
    public void isActuallyWhitespace_matchesVisualWhitespace() {
        assertTrue(StringUtil.isActuallyWhitespace(9));
        assertTrue(StringUtil.isActuallyWhitespace(10));
        assertTrue(StringUtil.isActuallyWhitespace(12));
        assertTrue(StringUtil.isActuallyWhitespace(13));
        assertTrue(StringUtil.isActuallyWhitespace(32));
        assertTrue(StringUtil.isActuallyWhitespace(160)); // non-breaking space

        assertFalse(StringUtil.isActuallyWhitespace(-1));
        assertFalse(StringUtil.isActuallyWhitespace(193));
        assertFalse(StringUtil.isActuallyWhitespace(-5106));
    }

    @Test
    public void isInvisibleChar_commonInvisibleChars() {
        assertTrue(StringUtil.isInvisibleChar(173));  // soft hyphen
        assertTrue(StringUtil.isInvisibleChar(8203)); // zero width space
        assertFalse(StringUtil.isInvisibleChar(8222)); // not invisible
    }

    // ----- Blank / numeric / newline helpers -----

    @Test
    public void isBlank_variousInputs() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("      "));
        assertFalse(StringUtil.isBlank("x"));
        assertFalse(StringUtil.isBlank(" x "));
    }

    @Test
    public void isNumeric_variousInputs() {
        assertTrue(StringUtil.isNumeric("0"));
        assertTrue(StringUtil.isNumeric("1234567890"));
        assertFalse(StringUtil.isNumeric(""));
        assertFalse(StringUtil.isNumeric("12a3"));
        assertFalse(StringUtil.isNumeric(null));
    }

    @Test
    public void startsWithNewline_behavior() {
        assertFalse(StringUtil.startsWithNewline(null));
        assertFalse(StringUtil.startsWithNewline(""));
        assertFalse(StringUtil.startsWithNewline("abc"));
        assertTrue(StringUtil.startsWithNewline("\nabc"));
        assertFalse(StringUtil.startsWithNewline("\rabc")); // method checks '\n' only
    }

    // ----- Whitespace normalization -----

    @Test
    public void normaliseWhitespace_collapsesRunsAndConvertsControlsToSpace() {
        String in = "  a\t\tb\nc  ";
        String out = StringUtil.normaliseWhitespace(in);
        // Leading/trailing whitespace remains as single spaces, controls become spaces
        assertEquals(" a b c ", out);
    }

    @Test
    public void appendNormalisedWhitespace_respectsStripLeadingFlag() {
        StringBuilder sb1 = StringUtil.borrowBuilder();
        StringUtil.appendNormalisedWhitespace(sb1, "   a\tb\nc", true);
        assertEquals("a b c", StringUtil.releaseBuilder(sb1));

        StringBuilder sb2 = StringUtil.borrowBuilder();
        StringUtil.appendNormalisedWhitespace(sb2, "   a\tb\nc", false);
        assertEquals(" a b c", StringUtil.releaseBuilder(sb2));
    }

    // ----- Joining -----

    @Test
    public void join_collection_basic() {
        Collection<?> items = Arrays.asList("a", "b", "", "c");
        assertEquals("a,b,,c", StringUtil.join(items, ","));
    }

    @Test
    public void join_collection_empty() {
        assertEquals("", StringUtil.join(Collections.emptyList(), ","));
    }

    @Test
    public void join_iterator_basic() {
        Iterator<?> it = Arrays.asList(1, 2, 3).iterator();
        assertEquals("1|2|3", StringUtil.join(it, "|"));
    }

    @Test
    public void join_array_basic() {
        String[] arr = {"a", "b", "c"};
        assertEquals("a b c", StringUtil.join(arr, " "));
    }

    @Test
    public void join_array_empty() {
        assertEquals("", StringUtil.join(new String[0], ","));
    }

    @Test
    public void stringJoiner_addAndAppend() {
        StringUtil.StringJoiner j = new StringUtil.StringJoiner(", ");
        j.add("a").append("1"); // not separated
        j.add("b").append("2");
        assertEquals("a1, b2", j.complete());
    }

    @Test
    public void joiningCollector_worksWithStreams() {
        Collector<CharSequence, ?, String> collector = StringUtil.joining("|");
        String result = Stream.of("x", "y", "z").collect(collector);
        assertEquals("x|y|z", result);
    }

    // ----- Membership helpers -----

    @Test
    public void in_checksMembership() {
        assertTrue(StringUtil.in("b", "a", "b", "c"));
        assertFalse(StringUtil.in("x", "a", "b", "c"));
    }

    @Test
    public void inSorted_binarySearchOnSortedArray() {
        String[] sorted = {"a", "b", "c", "d"};
        assertTrue(StringUtil.inSorted("b", sorted));
        assertFalse(StringUtil.inSorted("x", sorted));
    }

    // ----- Padding -----

    @Test
    public void padding_basicAndMemoizedRange() {
        assertEquals("", StringUtil.padding(0));
        assertEquals("     ", StringUtil.padding(5));
        assertEquals(21, StringUtil.padding(21).length());
    }

    @Test
    public void padding_withMaxWidth() {
        // maxPaddingWidth -1 means unlimited
        assertEquals(30, StringUtil.padding(30, -1).length());

        // width must be >= 0
        assertThrows(IllegalArgumentException.class, () -> StringUtil.padding(-1));
        assertThrows(IllegalArgumentException.class, () -> StringUtil.padding(-1, -1));

        // if maxPaddingWidth >= 0, width must be <= maxPaddingWidth
        assertThrows(IllegalArgumentException.class, () -> StringUtil.padding(10, 5));
    }

    // ----- URL resolution -----

    @Test
    public void resolveUrl_relativeAgainstBase() throws MalformedURLException {
        URL base = new URL("https://example.com/a/b");
        URL resolved = StringUtil.resolve(base, "../c");
        assertEquals("https://example.com/c", resolved.toString());
    }

    @Test
    public void resolveUrl_absoluteRelReturnsRel() throws MalformedURLException {
        URL base = new URL("https://example.com/a/");
        URL resolved = StringUtil.resolve(base, "http://other.test/x");
        assertEquals("http://other.test/x", resolved.toString());
    }

    @Test
    public void resolveString_validInputs() {
        String result = StringUtil.resolve("https://example.com/a/b", "../c");
        assertEquals("https://example.com/c", result);
    }

    @Test
    public void resolveString_invalidInputsReturnEmpty() {
        // invalid base URL
        assertEquals("", StringUtil.resolve("::://bad", "/x"));
        // invalid rel URL for a valid base
        assertEquals("", StringUtil.resolve("https://example.com", "http://%zz")); // malformed rel
    }

    // ----- Builder pool -----

    @Test
    public void borrowAndReleaseBuilder_roundTrip() {
        StringBuilder sb = StringUtil.borrowBuilder();
        sb.append("hello").append(' ').append(123);
        String out = StringUtil.releaseBuilder(sb);
        assertEquals("hello 123", out);
        // Do not use sb after release; contract states it may be reused and mutated.
    }

    @Test
    public void releaseBuilderVoid_doesNotReturnString() {
        StringBuilder sb = StringUtil.borrowBuilder();
        sb.append("temp");
        // Just ensure it doesn't throw
        StringUtil.releaseBuilderVoid(sb);
    }
}