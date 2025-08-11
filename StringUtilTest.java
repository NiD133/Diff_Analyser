package org.jsoup.internal;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;

import static org.jsoup.internal.StringUtil.normaliseWhitespace;
import static org.jsoup.internal.StringUtil.resolve;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StringUtil tests")
public class StringUtilTest {

    // Simple helper to make expected-space strings easy to read at a glance
    private static String spaces(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(' ');
        return sb.toString();
    }

    @Nested
    @DisplayName("join(...)")
    class JoinTests {
        @Test
        @DisplayName("joins collection with separator")
        void joinCollection() {
            assertEquals("", StringUtil.join(Collections.singletonList(""), " "));
            assertEquals("one", StringUtil.join(Collections.singletonList("one"), " "));
            assertEquals("one two three", StringUtil.join(Arrays.asList("one", "two", "three"), " "));
        }
    }

    @Nested
    @DisplayName("padding(...)")
    class PaddingTests {

        @Test
        @DisplayName("default padding (max 30) returns correct lengths")
        void defaultPadding() {
            assertEquals(spaces(0), StringUtil.padding(0));
            assertEquals(spaces(1), StringUtil.padding(1));
            assertEquals(spaces(2), StringUtil.padding(2));
            assertEquals(spaces(15), StringUtil.padding(15));
            assertEquals(spaces(30), StringUtil.padding(45)); // default taps out at 30
        }

        @Test
        @DisplayName("memoized padding (0..20) and unlimited (-1) behavior")
        void memoizedAndUnlimited() {
            // memoization is 0..20; maxPaddingWidth is ignored when memoized path returns early
            assertEquals(spaces(0), StringUtil.padding(0, -1));
            assertEquals(spaces(20), StringUtil.padding(20, -1));

            // escape memoization at width 21
            assertEquals(spaces(21), StringUtil.padding(21, -1));

            // unlimited (-1) allows any requested width
            assertEquals(spaces(30), StringUtil.padding(30, -1));
            assertEquals(spaces(45), StringUtil.padding(45, -1));
        }

        @Test
        @DisplayName("max padding width limits are respected")
        void respectsMaxPaddingWidth() {
            // max = 0
            assertEquals(spaces(0), StringUtil.padding(0, 0));
            assertEquals(spaces(0), StringUtil.padding(21, 0)); // escapes memoization but clamped to 0

            // max = 30
            assertEquals(spaces(0), StringUtil.padding(0, 30));
            assertEquals(spaces(1), StringUtil.padding(1, 30));
            assertEquals(spaces(2), StringUtil.padding(2, 30));
            assertEquals(spaces(15), StringUtil.padding(15, 30));
            assertEquals(spaces(30), StringUtil.padding(45, 30));

            // max applies even when width is memoized
            assertEquals(5, StringUtil.padding(20, 5).length());
        }

        @Test
        @DisplayName("precomputed padding cache is consistent (0..20)")
        void precomputedPaddingCache() {
            String[] padding = StringUtil.padding;
            assertEquals(21, padding.length, "Cache should provide entries for widths 0..20 inclusive");
            for (int i = 0; i < padding.length; i++) {
                assertEquals(i, padding[i].length(), "Cache entry " + i + " should be " + i + " spaces");
            }
        }
    }

    @Nested
    @DisplayName("blank / numeric / whitespace / normalization")
    class WhitespaceAndFormatTests {

        @Test
        @DisplayName("isBlank")
        void isBlank() {
            assertTrue(StringUtil.isBlank(null));
            assertTrue(StringUtil.isBlank(""));
            assertTrue(StringUtil.isBlank("      "));
            assertTrue(StringUtil.isBlank("   \r\n  "));

            assertFalse(StringUtil.isBlank("hello"));
            assertFalse(StringUtil.isBlank("   hello   "));
        }

        @Test
        @DisplayName("isNumeric (ASCII digits only)")
        void isNumeric() {
            assertFalse(StringUtil.isNumeric(null));
            assertFalse(StringUtil.isNumeric(" "));
            assertFalse(StringUtil.isNumeric("123 546"));
            assertFalse(StringUtil.isNumeric("hello"));
            assertFalse(StringUtil.isNumeric("123.334"));

            assertTrue(StringUtil.isNumeric("1"));
            assertTrue(StringUtil.isNumeric("1234"));
        }

        @ParameterizedTest(name = "isWhitespace returns true for ''{0}''")
        @ValueSource(chars = {'\t', '\n', '\r', '\f', ' '})
        @DisplayName("isWhitespace true cases")
        void isWhitespaceTrue(char c) {
            assertTrue(StringUtil.isWhitespace(c));
        }

        @ParameterizedTest(name = "isWhitespace returns false for ''{0}''")
        @ValueSource(chars = {'\u00A0', '\u2000', '\u3000'})
        @DisplayName("isWhitespace false cases (non-breaking / unicode spaces)")
        void isWhitespaceFalse(char c) {
            assertFalse(StringUtil.isWhitespace(c));
        }

        @Test
        @DisplayName("normaliseWhitespace collapses runs and converts control whitespace to single spaces")
        void normaliseWhiteSpace() {
            assertEquals(" ", normaliseWhitespace("    \r \n \r\n"));
            assertEquals(" hello there ", normaliseWhitespace("   hello   \r \n  there    \n"));
            assertEquals("hello", normaliseWhitespace("hello"));
            assertEquals("hello there", normaliseWhitespace("hello\nthere"));
        }

        @Test
        @DisplayName("normaliseWhitespace handles high-surrogate/low-surrogate pairs")
        void normaliseWhiteSpaceHandlesHighSurrogates() {
            String input = "\ud869\udeb2\u304b\u309a  1";
            String expected = "\ud869\udeb2\u304b\u309a 1";

            assertEquals(expected, normaliseWhitespace(input));
            String extractedText = Jsoup.parse(input).text();
            assertEquals(expected, extractedText);
        }
    }

    @Nested
    @DisplayName("URL resolution and sanitization")
    class UrlResolutionTests {

        private void assertResolves(String base, String rel, String expected) {
            assertEquals(expected, resolve(base, rel),
                "resolve(" + base + ", " + rel + ")");
        }

        @Test
        @DisplayName("resolves relative URLs against base")
        void resolvesRelativeUrls() {
            assertResolves("http://example.com", "./one/two?three", "http://example.com/one/two?three");
            assertResolves("http://example.com?one", "./one/two?three", "http://example.com/one/two?three");
            assertResolves("http://example.com", "./one/two?three#four", "http://example.com/one/two?three#four");
            assertResolves("http://example.com/", "https://example.com/one", "https://example.com/one");
            assertResolves("http://example.com/two/", "../one/two.html", "http://example.com/one/two.html");
            assertResolves("https://example.com/", "//example2.com/one", "https://example2.com/one");
            assertResolves("https://example.com:8080", "./one", "https://example.com:8080/one");
            assertResolves("http://example.com/", "https://example2.com/one", "https://example2.com/one");
            assertResolves("wrong", "https://example.com/one", "https://example.com/one");
            assertResolves("https://example.com/one", "", "https://example.com/one");
            assertResolves("wrong", "also wrong", "");
            assertResolves("ftp://example.com/two/", "../one", "ftp://example.com/one");
            assertResolves("ftp://example.com/one/", "./two.c", "ftp://example.com/one/two.c");
            assertResolves("ftp://example.com/one/", "two.c", "ftp://example.com/one/two.c");

            // RFC 3986 section 5.4.2 examples
            assertResolves("http://example.com/b/c/d;p?q", "../../../g", "http://example.com/g");
            assertResolves("http://example.com/b/c/d;p?q", "../../../../g", "http://example.com/g");
            assertResolves("http://example.com/b/c/d;p?q", "/./g", "http://example.com/g");
            assertResolves("http://example.com/b/c/d;p?q", "/../g", "http://example.com/g");
            assertResolves("http://example.com/b/c/d;p?q", "g.", "http://example.com/b/c/g.");
            assertResolves("http://example.com/b/c/d;p?q", ".g", "http://example.com/b/c/.g");
            assertResolves("http://example.com/b/c/d;p?q", "g..", "http://example.com/b/c/g..");
            assertResolves("http://example.com/b/c/d;p?q", "..g", "http://example.com/b/c/..g");
            assertResolves("http://example.com/b/c/d;p?q", "./../g", "http://example.com/b/g");
            assertResolves("http://example.com/b/c/d;p?q", "./g/.", "http://example.com/b/c/g/");
            assertResolves("http://example.com/b/c/d;p?q", "g/./h", "http://example.com/b/c/g/h");
            assertResolves("http://example.com/b/c/d;p?q", "g/../h", "http://example.com/b/c/h");
            assertResolves("http://example.com/b/c/d;p?q", "g;x=1/./y", "http://example.com/b/c/g;x=1/y");
            assertResolves("http://example.com/b/c/d;p?q", "g;x=1/../y", "http://example.com/b/c/y");
            assertResolves("http://example.com/b/c/d;p?q", "g?y/./x", "http://example.com/b/c/g?y/./x");
            assertResolves("http://example.com/b/c/d;p?q", "g?y/../x", "http://example.com/b/c/g?y/../x");
            assertResolves("http://example.com/b/c/d;p?q", "g#s/./x", "http://example.com/b/c/g#s/./x");
            assertResolves("http://example.com/b/c/d;p?q", "g#s/../x", "http://example.com/b/c/g#s/../x");
        }

        @Test
        @DisplayName("strips ASCII control chars before resolving")
        void stripsControlCharsFromUrls() {
            // should resolve to an absolute URL after control-char sanitization
            assertEquals("foo:bar", resolve("\nhttps://\texample.com/", "\r\nfo\to:ba\br"));
        }

        @Test
        @DisplayName("allows spaces in URL path")
        void allowsSpaceInUrl() {
            assertEquals("https://example.com/foo bar/", resolve("HTTPS://example.com/example/", "../foo bar/"));
        }
    }

    @Nested
    @DisplayName("ASCII checks and character classes")
    class AsciiAndCharClassTests {

        @Test
        @DisplayName("isAscii")
        void isAscii() {
            assertTrue(StringUtil.isAscii(""));
            assertTrue(StringUtil.isAscii("example.com"));
            assertTrue(StringUtil.isAscii("One Two"));
            assertFalse(StringUtil.isAscii("🧔"));
            assertFalse(StringUtil.isAscii("测试"));
            assertFalse(StringUtil.isAscii("测试.com"));
        }

        @ParameterizedTest(name = "isAsciiLetter returns true for ''{0}''")
        @ValueSource(chars = {'a', 'n', 'z', 'A', 'N', 'Z'})
        @DisplayName("isAsciiLetter true cases")
        void isAsciiLetterTrue(char c) {
            assertTrue(StringUtil.isAsciiLetter(c));
        }

        @ParameterizedTest(name = "isAsciiLetter returns false for ''{0}''")
        @ValueSource(chars = {' ', '-', '0', 'ß', 'Ě'})
        @DisplayName("isAsciiLetter false cases")
        void isAsciiLetterFalse(char c) {
            assertFalse(StringUtil.isAsciiLetter(c));
        }

        @ParameterizedTest(name = "isDigit returns true for ''{0}''")
        @ValueSource(chars = {'0','1','2','3','4','5','6','7','8','9'})
        @DisplayName("isDigit true cases")
        void isDigitTrue(char c) {
            assertTrue(StringUtil.isDigit(c));
        }

        @ParameterizedTest(name = "isDigit returns false for ''{0}''")
        @ValueSource(chars = {'a','A','ä','Ä','\u0661','\u0B73'})
        @DisplayName("isDigit false cases (letters, diacritics, non-ASCII digits)")
        void isDigitFalse(char c) {
            assertFalse(StringUtil.isDigit(c));
        }

        @ParameterizedTest(name = "isHexDigit returns true for ''{0}''")
        @ValueSource(chars = {
            '0','1','2','3','4','5','6','7','8','9',
            'a','b','c','d','e','f',
            'A','B','C','D','E','F'
        })
        @DisplayName("isHexDigit true cases")
        void isHexDigitTrue(char c) {
            assertTrue(StringUtil.isHexDigit(c));
        }

        @ParameterizedTest(name = "isHexDigit returns false for ''{0}''")
        @ValueSource(chars = {'g','G','ä','Ä','\u0661','\u0B73'})
        @DisplayName("isHexDigit false cases")
        void isHexDigitFalse(char c) {
            assertFalse(StringUtil.isHexDigit(c));
        }
    }
}