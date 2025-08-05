package org.jsoup.internal;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.jsoup.internal.StringUtil.*;
import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTest {

    @Test
    public void testJoin() {
        // Test joining a single empty string
        assertEquals("", join(Collections.singletonList(""), " "));

        // Test joining a single non-empty string
        assertEquals("one", join(Collections.singletonList("one"), " "));

        // Test joining multiple strings with a space separator
        assertEquals("one two three", join(Arrays.asList("one", "two", "three"), " "));
    }

    @Test
    public void testPadding() {
        // Test padding with default max width
        assertEquals("", padding(0));
        assertEquals(" ", padding(1));
        assertEquals("  ", padding(2));
        assertEquals("               ", padding(15));
        assertEquals("                              ", padding(45)); // Max default padding is 30

        // Test padding with unlimited length (-1)
        assertEquals("", padding(0, -1));
        assertEquals("                    ", padding(20, -1));
        assertEquals("                     ", padding(21, -1));
        assertEquals("                              ", padding(30, -1));
        assertEquals("                                             ", padding(45, -1));

        // Test padding with zero max width
        assertEquals("", padding(0, 0));
        assertEquals("", padding(21, 0));

        // Test padding with max width of 30
        assertEquals("", padding(0, 30));
        assertEquals(" ", padding(1, 30));
        assertEquals("  ", padding(2, 30));
        assertEquals("               ", padding(15, 30));
        assertEquals("                              ", padding(45, 30));

        // Test padding with max width less than requested width
        assertEquals(5, padding(20, 5).length());
    }

    @Test
    public void testPaddingInACan() {
        // Verify memoized padding array
        String[] paddingArray = padding;
        assertEquals(21, paddingArray.length);
        for (int i = 0; i < paddingArray.length; i++) {
            assertEquals(i, paddingArray[i].length());
        }
    }

    @Test
    public void testIsBlank() {
        // Test blank strings
        assertTrue(isBlank(null));
        assertTrue(isBlank(""));
        assertTrue(isBlank("      "));
        assertTrue(isBlank("   \r\n  "));

        // Test non-blank strings
        assertFalse(isBlank("hello"));
        assertFalse(isBlank("   hello   "));
    }

    @Test
    public void testIsNumeric() {
        // Test non-numeric strings
        assertFalse(isNumeric(null));
        assertFalse(isNumeric(" "));
        assertFalse(isNumeric("123 546"));
        assertFalse(isNumeric("hello"));
        assertFalse(isNumeric("123.334"));

        // Test numeric strings
        assertTrue(isNumeric("1"));
        assertTrue(isNumeric("1234"));
    }

    @Test
    public void testIsWhitespace() {
        // Test whitespace characters
        assertTrue(isWhitespace('\t'));
        assertTrue(isWhitespace('\n'));
        assertTrue(isWhitespace('\r'));
        assertTrue(isWhitespace('\f'));
        assertTrue(isWhitespace(' '));

        // Test non-whitespace characters
        assertFalse(isWhitespace('\u00a0'));
        assertFalse(isWhitespace('\u2000'));
        assertFalse(isWhitespace('\u3000'));
    }

    @Test
    public void testNormaliseWhiteSpace() {
        // Test normalizing whitespace in strings
        assertEquals(" ", normaliseWhitespace("    \r \n \r\n"));
        assertEquals(" hello there ", normaliseWhitespace("   hello   \r \n  there    \n"));
        assertEquals("hello", normaliseWhitespace("hello"));
        assertEquals("hello there", normaliseWhitespace("hello\nthere"));
    }

    @Test
    public void testNormaliseWhiteSpaceHandlesHighSurrogates() {
        // Test normalizing whitespace with high surrogate characters
        String input = "\ud869\udeb2\u304b\u309a  1";
        String expected = "\ud869\udeb2\u304b\u309a 1";

        assertEquals(expected, normaliseWhitespace(input));
        String extractedText = Jsoup.parse(input).text();
        assertEquals(expected, extractedText);
    }

    @Test
    public void testResolvesRelativeUrls() {
        // Test resolving relative URLs
        assertEquals("http://example.com/one/two?three", resolve("http://example.com", "./one/two?three"));
        assertEquals("http://example.com/one/two?three", resolve("http://example.com?one", "./one/two?three"));
        assertEquals("http://example.com/one/two?three#four", resolve("http://example.com", "./one/two?three#four"));
        assertEquals("https://example.com/one", resolve("http://example.com/", "https://example.com/one"));
        assertEquals("http://example.com/one/two.html", resolve("http://example.com/two/", "../one/two.html"));
        assertEquals("https://example2.com/one", resolve("https://example.com/", "//example2.com/one"));
        assertEquals("https://example.com:8080/one", resolve("https://example.com:8080", "./one"));
        assertEquals("https://example2.com/one", resolve("http://example.com/", "https://example2.com/one"));
        assertEquals("https://example.com/one", resolve("wrong", "https://example.com/one"));
        assertEquals("https://example.com/one", resolve("https://example.com/one", ""));
        assertEquals("", resolve("wrong", "also wrong"));
        assertEquals("ftp://example.com/one", resolve("ftp://example.com/two/", "../one"));
        assertEquals("ftp://example.com/one/two.c", resolve("ftp://example.com/one/", "./two.c"));
        assertEquals("ftp://example.com/one/two.c", resolve("ftp://example.com/one/", "two.c"));

        // Examples from RFC 3986 section 5.4.2
        assertEquals("http://example.com/g", resolve("http://example.com/b/c/d;p?q", "../../../g"));
        assertEquals("http://example.com/g", resolve("http://example.com/b/c/d;p?q", "../../../../g"));
        assertEquals("http://example.com/g", resolve("http://example.com/b/c/d;p?q", "/./g"));
        assertEquals("http://example.com/g", resolve("http://example.com/b/c/d;p?q", "/../g"));
        assertEquals("http://example.com/b/c/g.", resolve("http://example.com/b/c/d;p?q", "g."));
        assertEquals("http://example.com/b/c/.g", resolve("http://example.com/b/c/d;p?q", ".g"));
        assertEquals("http://example.com/b/c/g..", resolve("http://example.com/b/c/d;p?q", "g.."));
        assertEquals("http://example.com/b/c/..g", resolve("http://example.com/b/c/d;p?q", "..g"));
        assertEquals("http://example.com/b/g", resolve("http://example.com/b/c/d;p?q", "./../g"));
        assertEquals("http://example.com/b/c/g/", resolve("http://example.com/b/c/d;p?q", "./g/."));
        assertEquals("http://example.com/b/c/g/h", resolve("http://example.com/b/c/d;p?q", "g/./h"));
        assertEquals("http://example.com/b/c/h", resolve("http://example.com/b/c/d;p?q", "g/../h"));
        assertEquals("http://example.com/b/c/g;x=1/y", resolve("http://example.com/b/c/d;p?q", "g;x=1/./y"));
        assertEquals("http://example.com/b/c/y", resolve("http://example.com/b/c/d;p?q", "g;x=1/../y"));
        assertEquals("http://example.com/b/c/g?y/./x", resolve("http://example.com/b/c/d;p?q", "g?y/./x"));
        assertEquals("http://example.com/b/c/g?y/../x", resolve("http://example.com/b/c/d;p?q", "g?y/../x"));
        assertEquals("http://example.com/b/c/g#s/./x", resolve("http://example.com/b/c/d;p?q", "g#s/./x"));
        assertEquals("http://example.com/b/c/g#s/../x", resolve("http://example.com/b/c/d;p?q", "g#s/../x"));
    }

    @Test
    public void testStripsControlCharsFromUrls() {
        // Test resolving URLs with control characters
        assertEquals("foo:bar", resolve("\nhttps://\texample.com/", "\r\nfo\to:ba\br"));
    }

    @Test
    public void testAllowsSpaceInUrl() {
        // Test resolving URLs with spaces
        assertEquals("https://example.com/foo bar/", resolve("HTTPS://example.com/example/", "../foo bar/"));
    }

    @Test
    public void testIsAscii() {
        // Test ASCII strings
        assertTrue(isAscii(""));
        assertTrue(isAscii("example.com"));
        assertTrue(isAscii("One Two"));

        // Test non-ASCII strings
        assertFalse(isAscii("🧔"));
        assertFalse(isAscii("测试"));
        assertFalse(isAscii("测试.com"));
    }

    @Test
    public void testIsAsciiLetter() {
        // Test ASCII letters
        assertTrue(isAsciiLetter('a'));
        assertTrue(isAsciiLetter('n'));
        assertTrue(isAsciiLetter('z'));
        assertTrue(isAsciiLetter('A'));
        assertTrue(isAsciiLetter('N'));
        assertTrue(isAsciiLetter('Z'));

        // Test non-ASCII letters
        assertFalse(isAsciiLetter(' '));
        assertFalse(isAsciiLetter('-'));
        assertFalse(isAsciiLetter('0'));
        assertFalse(isAsciiLetter('ß'));
        assertFalse(isAsciiLetter('Ě'));
    }

    @Test
    public void testIsDigit() {
        // Test digit characters
        assertTrue(isDigit('0'));
        assertTrue(isDigit('1'));
        assertTrue(isDigit('2'));
        assertTrue(isDigit('3'));
        assertTrue(isDigit('4'));
        assertTrue(isDigit('5'));
        assertTrue(isDigit('6'));
        assertTrue(isDigit('7'));
        assertTrue(isDigit('8'));
        assertTrue(isDigit('9'));

        // Test non-digit characters
        assertFalse(isDigit('a'));
        assertFalse(isDigit('A'));
        assertFalse(isDigit('ä'));
        assertFalse(isDigit('Ä'));
        assertFalse(isDigit('١'));
        assertFalse(isDigit('୳'));
    }

    @Test
    public void testIsHexDigit() {
        // Test hex digit characters
        assertTrue(isHexDigit('0'));
        assertTrue(isHexDigit('1'));
        assertTrue(isHexDigit('2'));
        assertTrue(isHexDigit('3'));
        assertTrue(isHexDigit('4'));
        assertTrue(isHexDigit('5'));
        assertTrue(isHexDigit('6'));
        assertTrue(isHexDigit('7'));
        assertTrue(isHexDigit('8'));
        assertTrue(isHexDigit('9'));
        assertTrue(isHexDigit('a'));
        assertTrue(isHexDigit('b'));
        assertTrue(isHexDigit('c'));
        assertTrue(isHexDigit('d'));
        assertTrue(isHexDigit('e'));
        assertTrue(isHexDigit('f'));
        assertTrue(isHexDigit('A'));
        assertTrue(isHexDigit('B'));
        assertTrue(isHexDigit('C'));
        assertTrue(isHexDigit('D'));
        assertTrue(isHexDigit('E'));
        assertTrue(isHexDigit('F'));

        // Test non-hex digit characters
        assertFalse(isHexDigit('g'));
        assertFalse(isHexDigit('G'));
        assertFalse(isHexDigit('ä'));
        assertFalse(isHexDigit('Ä'));
        assertFalse(isHexDigit('١'));
        assertFalse(isHexDigit('୳'));
    }
}