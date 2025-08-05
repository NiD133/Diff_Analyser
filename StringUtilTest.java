package org.jsoup.internal;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.jsoup.internal.StringUtil.normaliseWhitespace;
import static org.jsoup.internal.StringUtil.resolve;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StringUtil")
public class StringUtilTest {

    @Nested
    @DisplayName("join()")
    class JoinTests {
        @Test
        void shouldJoinMultipleStrings() {
            assertEquals("one two three", StringUtil.join(Arrays.asList("one", "two", "three"), " "));
        }

        @Test
        void shouldHandleSingleItem() {
            assertEquals("one", StringUtil.join(Collections.singletonList("one"), " "));
        }

        @Test
        void shouldHandleEmptyItem() {
            assertEquals("", StringUtil.join(Collections.singletonList(""), " "));
        }
    }

    @Nested
    @DisplayName("padding()")
    class PaddingTests {
        @Test
        void shouldCreatePaddingOfCorrectLength() {
            assertEquals("", StringUtil.padding(0));
            assertEquals(" ", StringUtil.padding(1));
            assertEquals("  ", StringUtil.padding(2));
            assertEquals("               ", StringUtil.padding(15));
        }

        @Test
        void shouldCapPaddingAtDefaultMaxWidth() {
            // The default max width is 30.
            assertEquals(30, StringUtil.padding(45).length());
            assertEquals("                              ", StringUtil.padding(45));
        }

        @Test
        void shouldHandleCustomMaxWidth() {
            assertEquals(15, StringUtil.padding(45, 15).length());
            assertEquals("               ", StringUtil.padding(15, 15));
            assertEquals("", StringUtil.padding(1, 0));
        }

        @Test
        void shouldHandleUnlimitedMaxWidth() {
            // A max width of -1 means unlimited.
            assertEquals(45, StringUtil.padding(45, -1).length());
            assertEquals("                                             ", StringUtil.padding(45, -1));
        }

        @Test
        void shouldApplyMaxWidthEvenForMemoizedValues() {
            // Width 20 is normally memoized, but max width should still be applied.
            assertEquals(5, StringUtil.padding(20, 5).length());
        }

        @Test
        void memoizedPaddingArrayIsValid() {
            String[] padding = StringUtil.padding;
            // The internal cache should have 21 entries (for lengths 0 to 20).
            assertEquals(21, padding.length);
            for (int i = 0; i < padding.length; i++) {
                assertEquals(i, padding[i].length(), "Padding at index " + i + " has incorrect length.");
            }
        }
    }

    @Nested
    @DisplayName("isBlank()")
    class IsBlankTests {
        @Test
        void shouldReturnTrueForNullOrWhitespaceStrings() {
            assertTrue(StringUtil.isBlank(null));
            assertTrue(StringUtil.isBlank(""));
            assertTrue(StringUtil.isBlank("      "));
            assertTrue(StringUtil.isBlank("   \r\n  "));
        }

        @Test
        void shouldReturnFalseForNonBlankStrings() {
            assertFalse(StringUtil.isBlank("hello"));
            assertFalse(StringUtil.isBlank("   hello   "));
        }
    }

    @Nested
    @DisplayName("isNumeric()")
    class IsNumericTests {
        @Test
        void shouldReturnTrueForNumericStrings() {
            assertTrue(StringUtil.isNumeric("1"));
            assertTrue(StringUtil.isNumeric("1234"));
        }

        @Test
        void shouldReturnFalseForNonNumericStrings() {
            assertFalse(StringUtil.isNumeric(null));
            assertFalse(StringUtil.isNumeric(" "));
            assertFalse(StringUtil.isNumeric("123 546"));
            assertFalse(StringUtil.isNumeric("hello"));
            assertFalse(StringUtil.isNumeric("123.334"));
        }
    }

    @Nested
    @DisplayName("isWhitespace()")
    class IsWhitespaceTests {
        @Test
        void shouldReturnTrueForHtmlWhitespaceChars() {
            assertTrue(StringUtil.isWhitespace('\t'));
            assertTrue(StringUtil.isWhitespace('\n'));
            assertTrue(StringUtil.isWhitespace('\r'));
            assertTrue(StringUtil.isWhitespace('\f'));
            assertTrue(StringUtil.isWhitespace(' '));
        }

        @Test
        void shouldReturnFalseForNonHtmlWhitespaceChars() {
            // Unicode non-breaking spaces, etc., are not considered HTML whitespace.
            assertFalse(StringUtil.isWhitespace('\u00a0')); // non-breaking space
            assertFalse(StringUtil.isWhitespace('\u2000')); // en quad
            assertFalse(StringUtil.isWhitespace('\u3000')); // ideographic space
        }
    }

    @Nested
    @DisplayName("normaliseWhitespace()")
    class NormaliseWhitespaceTests {
        @Test
        void shouldCollapseWhitespaceAndTrim() {
            assertEquals(" ", normaliseWhitespace("    \r \n \r\n"));
            assertEquals(" hello there ", normaliseWhitespace("   hello   \r \n  there    \n"));
            assertEquals("hello", normaliseWhitespace("hello"));
            assertEquals("hello there", normaliseWhitespace("hello\nthere"));
        }

        @Test
        void shouldCorrectlyHandleHighSurrogates() {
            String stringWithSurrogate = "\ud869\udeb2\u304b\u309a  1"; // A CJK character, two spaces, and a '1'
            String expected = "\ud869\udeb2\u304b\u309a 1";

            assertEquals(expected, normaliseWhitespace(stringWithSurrogate));

            // Also verify via a full parse, which uses this method
            String extractedText = Jsoup.parse(stringWithSurrogate).text();
            assertEquals(expected, extractedText);
        }
    }

    @Nested
    @DisplayName("resolve()")
    class ResolveUrlTests {
        private static final String BASE_URL = "http://example.com/b/c/d;p?q";

        @Test
        void shouldResolveBasicRelativeUrls() {
            assertEquals("http://example.com/one/two?three", resolve("http://example.com", "./one/two?three"));
            assertEquals("http://example.com/one/two?three", resolve("http://example.com?one", "./one/two?three"));
            assertEquals("http://example.com/one/two?three#four", resolve("http://example.com", "./one/two?three#four"));
        }

        @Test
        void shouldHandleDifferentProtocolsAndHosts() {
            assertEquals("https://example.com/one", resolve("http://example.com/", "https://example.com/one"));
            assertEquals("https://example2.com/one", resolve("https://example.com/", "//example2.com/one"));
            assertEquals("ftp://example.com/one", resolve("ftp://example.com/two/", "../one"));
        }

        @Test
        void shouldHandlePathTraversal() {
            assertEquals("http://example.com/one/two.html", resolve("http://example.com/two/", "../one/two.html"));
            assertEquals("ftp://example.com/one/two.c", resolve("ftp://example.com/one/", "./two.c"));
            assertEquals("ftp://example.com/one/two.c", resolve("ftp://example.com/one/", "two.c"));
        }

        @Test
        void shouldHandleMalformedOrEmptyInputs() {
            assertEquals("https://example.com/one", resolve("wrong", "https://example.com/one"));
            assertEquals("https://example.com/one", resolve("https://example.com/one", ""));
            assertEquals("", resolve("wrong", "also wrong"));
        }

        @Test
        void shouldStripControlCharsBeforeResolving() {
            String baseUrlWithControlChars = "\nhttps://\texample.com/";
            String relUrlWithControlChars = "\r\nfo\to:ba\br";
            // Control chars are stripped, but 'foo:bar' is not a valid relative URL part,
            // so it's treated as the final resolved spec.
            assertEquals("foo:bar", resolve(baseUrlWithControlChars, relUrlWithControlChars));
        }

        @Test
        void shouldAllowSpacesInUrlPath() {
            assertEquals("https://example.com/foo bar/", resolve("HTTPS://example.com/example/", "../foo bar/"));
        }

        @Test
        @DisplayName("should resolve URLs according to RFC 3986 examples")
        void resolvesAccordingToRfc3986() {
            // Examples from rfc3986 section 5.4.2
            assertEquals("http://example.com/g", resolve(BASE_URL, "../../../g"));
            assertEquals("http://example.com/g", resolve(BASE_URL, "../../../../g"));
            assertEquals("http://example.com/g", resolve(BASE_URL, "/./g"));
            assertEquals("http://example.com/g", resolve(BASE_URL, "/../g"));
            assertEquals("http://example.com/b/c/g.", resolve(BASE_URL, "g."));
            assertEquals("http://example.com/b/c/.g", resolve(BASE_URL, ".g"));
            assertEquals("http://example.com/b/c/g..", resolve(BASE_URL, "g.."));
            assertEquals("http://example.com/b/c/..g", resolve(BASE_URL, "..g"));
            assertEquals("http://example.com/b/g", resolve(BASE_URL, "./../g"));
            assertEquals("http://example.com/b/c/g/", resolve(BASE_URL, "./g/."));
            assertEquals("http://example.com/b/c/g/h", resolve(BASE_URL, "g/./h"));
            assertEquals("http://example.com/b/c/h", resolve(BASE_URL, "g/../h"));
            assertEquals("http://example.com/b/c/g;x=1/y", resolve(BASE_URL, "g;x=1/./y"));
            assertEquals("http://example.com/b/c/y", resolve(BASE_URL, "g;x=1/../y"));
            assertEquals("http://example.com/b/c/g?y/./x", resolve(BASE_URL, "g?y/./x"));
            assertEquals("http://example.com/b/c/g?y/../x", resolve(BASE_URL, "g?y/../x"));
            assertEquals("http://example.com/b/c/g#s/./x", resolve(BASE_URL, "g#s/./x"));
            assertEquals("http://example.com/b/c/g#s/../x", resolve(BASE_URL, "g#s/../x"));
        }
    }

    @Nested
    @DisplayName("isAscii()")
    class IsAsciiTests {
        @Test
        void shouldReturnTrueForAsciiStrings() {
            assertTrue(StringUtil.isAscii(""));
            assertTrue(StringUtil.isAscii("example.com"));
            assertTrue(StringUtil.isAscii("One Two"));
        }

        @Test
        void shouldReturnFalseForNonAsciiStrings() {
            assertFalse(StringUtil.isAscii("🧔"));
            assertFalse(StringUtil.isAscii("测试"));
            assertFalse(StringUtil.isAscii("测试.com"));
        }
    }

    @Nested
    @DisplayName("isAsciiLetter()")
    class IsAsciiLetterTests {
        @Test
        void shouldReturnTrueForAsciiLetters() {
            for (char c = 'a'; c <= 'z'; c++) {
                assertTrue(StringUtil.isAsciiLetter(c));
            }
            for (char c = 'A'; c <= 'Z'; c++) {
                assertTrue(StringUtil.isAsciiLetter(c));
            }
        }

        @Test
        void shouldReturnFalseForNonAsciiLetters() {
            assertFalse(StringUtil.isAsciiLetter(' '));
            assertFalse(StringUtil.isAsciiLetter('-'));
            assertFalse(StringUtil.isAsciiLetter('0'));
            assertFalse(StringUtil.isAsciiLetter('ß'));
            assertFalse(StringUtil.isAsciiLetter('Ě'));
        }
    }

    @Nested
    @DisplayName("isDigit()")
    class IsDigitTests {
        @Test
        void shouldReturnTrueForDigits() {
            for (char c = '0'; c <= '9'; c++) {
                assertTrue(StringUtil.isDigit(c), "Should be a digit: " + c);
            }
        }

        @Test
        void shouldReturnFalseForNonDigits() {
            assertFalse(StringUtil.isDigit('a'));
            assertFalse(StringUtil.isDigit('A'));
            assertFalse(StringUtil.isDigit('ä'));
            assertFalse(StringUtil.isDigit('Ä'));
            assertFalse(StringUtil.isDigit('١')); // Arabic-Indic digit one
            assertFalse(StringUtil.isDigit('୳')); // Oriya digit three
        }
    }

    @Nested
    @DisplayName("isHexDigit()")
    class IsHexDigitTests {
        @Test
        void shouldReturnTrueForHexDigits() {
            "0123456789abcdefABCDEF".chars().forEach(c ->
                assertTrue(StringUtil.isHexDigit((char) c), "Should be a hex digit: " + (char) c)
            );
        }

        @Test
        void shouldReturnFalseForNonHexDigits() {
            assertFalse(StringUtil.isHexDigit('g'));
            assertFalse(StringUtil.isHexDigit('G'));
            assertFalse(StringUtil.isHexDigit('ä'));
            assertFalse(StringUtil.isHexDigit('Ä'));
            assertFalse(StringUtil.isHexDigit('١')); // Arabic-Indic digit one
            assertFalse(StringUtil.isHexDigit('୳')); // Oriya digit three
        }
    }
}