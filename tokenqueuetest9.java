package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for CSS identifier and selector parsing in TokenQueue.
 * This includes tests for escaping and consuming CSS identifiers, many of which are
 * adapted from the Web Platform Tests (WPT) to ensure spec compliance.
 */
public class TokenQueueCssTest {

    @Nested
    @DisplayName("escapeCssIdentifier()")
    class EscapeCssIdentifierTests {

        @ParameterizedTest(name = "[{index}] Input: \"{1}\" -> Expected: \"{0}\"")
        @MethodSource("org.jsoup.parser.TokenQueueCssTest#escapeCssIdentifier_WebPlatformTestParameters")
        @MethodSource("org.jsoup.parser.TokenQueueCssTest#escapeCssIdentifier_additionalParameters")
        void testEscapeCssIdentifier(String expected, String input) {
            assertEquals(expected, TokenQueue.escapeCssIdentifier(input));
        }
    }

    @Nested
    @DisplayName("consumeCssIdentifier()")
    class ConsumeCssIdentifierTests {

        @ParameterizedTest(name = "[{index}] Input: \"{1}\" -> Expected: \"{0}\"")
        @MethodSource("org.jsoup.parser.TokenQueueCssTest#consumeCssIdentifier_WebPlatformTestParameters")
        @MethodSource("org.jsoup.parser.TokenQueueCssTest#consumeCssIdentifier_additionalParameters")
        void testConsumeCssIdentifier(String expected, String cssIdentifier) {
            assertParsedCssIdentifierEquals(expected, cssIdentifier);
        }

        private String parseCssIdentifier(String text) {
            TokenQueue q = new TokenQueue(text);
            return q.consumeCssIdentifier();
        }

        private void assertParsedCssIdentifierEquals(String expected, String cssIdentifier) {
            assertEquals(expected, parseCssIdentifier(cssIdentifier));
        }
    }

    @Test
    @DisplayName("consumeElementSelector() should handle escaped characters")
    void consumeElementSelectorWithEscapedChars() {
        TokenQueue q = new TokenQueue("p\\\\p p\\.p p\\:p p\\!p");

        assertEquals("p\\p", q.consumeElementSelector());
        assertTrue(q.consumeWhitespace());
        assertEquals("p.p", q.consumeElementSelector());
        assertTrue(q.consumeWhitespace());
        assertEquals("p:p", q.consumeElementSelector());
        assertTrue(q.consumeWhitespace());
        assertEquals("p!p", q.consumeElementSelector());
        assertTrue(q.isEmpty());
    }

    // --- Data Providers ---

    // Note: Data providers must be static and public (or package-private in the same package)
    // to be used by nested test classes.

    // Data for escapeCssIdentifier()
    // Source: https://github.com/web-platform-tests/wpt/blob/328fa1c67bf5dfa6f24571d4c41dd10224b6d247/css/cssom/escape.html
    static Stream<Arguments> escapeCssIdentifier_WebPlatformTestParameters() {
        return Stream.of(
            // Empty input
            Arguments.of("", ""),

            // Null bytes (U+0000) are replaced with the replacement character (U+FFFD)
            Arguments.of("\uFFFD", "\0"),
            Arguments.of("a\uFFFD", "a\0"),
            Arguments.of("\uFFFDb", "\0b"),
            Arguments.of("a\uFFFDb", "a\0b"),

            // Replacement character (U+FFFD) is passed through
            Arguments.of("\uFFFD", "\uFFFD"),
            Arguments.of("a\uFFFD", "a\uFFFD"),
            Arguments.of("\uFFFDb", "\uFFFDb"),
            Arguments.of("a\uFFFDb", "a\uFFFDb"),

            // Digits at the start of an identifier must be escaped
            Arguments.of("\\30 a", "0a"),
            Arguments.of("\\31 a", "1a"),
            Arguments.of("\\32 a", "2a"),
            Arguments.of("\\33 a", "3a"),
            Arguments.of("\\34 a", "4a"),
            Arguments.of("\\35 a", "5a"),
            Arguments.of("\\36 a", "6a"),
            Arguments.of("\\37 a", "7a"),
            Arguments.of("\\38 a", "8a"),
            Arguments.of("\\39 a", "9a"),

            // Digits not at the start do not need escaping
            Arguments.of("a0b", "a0b"),
            Arguments.of("a9b", "a9b"),

            // A hyphen followed by a digit at the start must be escaped
            Arguments.of("-\\30 a", "-0a"),
            Arguments.of("-\\31 a", "-1a"),
            Arguments.of("-\\39 a", "-9a"),

            // Double dash prefix is allowed
            Arguments.of("--a", "--a"),

            // Control characters (U+0001 to U+001F) and U+007F are escaped
            Arguments.of("\\1 \\2 \\1e \\1f ", "\u0001\u0002\u001E\u001F"),
            Arguments.of("\\7f \u0080\u0081", "\u007F\u0080\u0081"),

            // Unreserved characters are not escaped
            Arguments.of("a0123456789b", "a0123456789b"),
            Arguments.of("abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz"),
            Arguments.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
            Arguments.of("\u0080\u002D\u005F\u00A9", "\u0080\u002D\u005F\u00A9"), // High-codepoint, hyphen, underscore, copyright
            Arguments.of("\u00A0\u00A1\u00A2", "\u00A0\u00A1\u00A2"),

            // Backslashes are themselves escaped
            Arguments.of("hello\\\\world", "hello\\world"),

            // High-codepoint characters are preserved
            Arguments.of("hello\u1234world", "hello\u1234world"),

            // A single dash at the start of an identifier must be escaped
            Arguments.of("\\-", "-"),

            // Other special ASCII characters are escaped
            Arguments.of("\\ \\!xy", "\u0020\u0021\u0078\u0079"),

            // Astral symbol (U+1D306 TETRAGRAM FOR CENTRE) is preserved
            Arguments.of("\uD834\uDF06", "\uD834\uDF06"),

            // Lone surrogates are preserved
            Arguments.of("\uDF06", "\uDF06"),
            Arguments.of("\uD834", "\uD834")
        );
    }

    static Stream<Arguments> escapeCssIdentifier_additionalParameters() {
        return Stream.of(
            // Punctuation characters are escaped
            Arguments.of("one\\#two\\.three\\/four\\\\five", "one#two.three/four\\five"),
            // A hyphen not at the start is not escaped
            Arguments.of("-a", "-a"),
            // Double dash is not escaped
            Arguments.of("--", "--")
        );
    }

    // Data for consumeCssIdentifier()
    // Source 1: https://github.com/web-platform-tests/wpt/blob/36036fb5212a3fc15fc5750cecb1923ba4071668/dom/nodes/ParentNode-querySelector-escapes.html
    // Source 2: Chromium CSS tests - https://goo.gl/3Cxdov
    static Stream<Arguments> consumeCssIdentifier_WebPlatformTestParameters() {
        return Stream.of(
            // --- WPT Tests: Escaped Hex Digits ---
            Arguments.of("0nextIsWhiteSpace", "\\30 nextIsWhiteSpace"), // Space after hex escape
            Arguments.of("0nextIsNotHexLetters", "\\30nextIsNotHexLetters"), // No space needed if not followed by hex
            Arguments.of("0connectHexMoreThan6Hex", "\\000030connectHexMoreThan6Hex"), // Long hex escape
            Arguments.of("0spaceMoreThan6Hex", "\\000030 spaceMoreThan6Hex"),

            // --- WPT Tests: Special Replacements ---
            // 1. Zero points (escaped null) are replaced with U+FFFD
            Arguments.of("zero\uFFFD", "zero\\0"),
            Arguments.of("zero\uFFFD", "zero\\000000"),
            // 2. Surrogate points are replaced with U+FFFD
            Arguments.of("\uFFFDsurrogateFirst", "\\d83d surrogateFirst"),
            Arguments.of("surrogateSecond\uFFFd", "surrogateSecond\\dd11"),
            Arguments.of("surrogatePair\uFFFD\uFFFD", "surrogatePair\\d83d\\dd11"),
            // 3. Out of range points are replaced with U+FFFD
            Arguments.of("outOfRange\uFFFD", "outOfRange\\110000"),
            Arguments.of("outOfRange\uFFFD", "outOfRange\\ffffff"),

            // --- WPT Tests: General Escapes ---
            Arguments.of(".comma", "\\.comma"),
            Arguments.of("-minus", "\\-minus"),
            Arguments.of("g", "\\g"),
            Arguments.of("aBMPRegular", "\\61 BMPRegular"),
            Arguments.of("\uD83D\uDD11nonBMP", "\\1f511 nonBMP"), // U+1F511 KEY
            Arguments.of("00continueEscapes", "\\30\\30 continueEscapes"),
            Arguments.of("00continueEscapes", "\\30 \\30 continueEscapes"),
            Arguments.of("continueEscapes00", "continueEscapes\\30 \\30 "),
            Arguments.of("continueEscapes00", "continueEscapes\\30\\30"),

            // --- Chromium Tests ---
            Arguments.of("hello", "hel\\6Co"), // 'l' escaped
            Arguments.of("&B", "\\26 B"), // '&' escaped
            Arguments.of("hello", "hel\\6C o"), // 'l' escaped with space
            Arguments.of("spaces", "spac\\65\r\ns"), // 'e' escaped with CRLF
            Arguments.of("spaces", "sp\\61\tc\\65\fs"), // 'a', 'e' escaped with tab, form-feed
            Arguments.of("test\uD799", "test\\D799"), // High BMP char
            Arguments.of("\uE000", "\\E000"), // Private use area char
            Arguments.of("test", "te\\s\\t"), // Escaped characters
            Arguments.of("spaces in\tident", "spaces\\ in\\\tident"), // Escaped spaces
            Arguments.of(".,:!", "\\.\\,\\:\\!"), // Escaped punctuation
            Arguments.of("null\uFFFD", "null\\0"), // Null escape
            Arguments.of("large\uFFFD", "large\\110000"), // Out of range escape
            Arguments.of("surrogate\uFFFD", "surrogate\\D800"), // Lone surrogate escape
            Arguments.of("\uDBFF\uDFFF", "\\10fFfF"), // Max Unicode char U+10FFFF
            Arguments.of("\uDBFF\uDFFF0", "\\10fFfF0"), // Max Unicode char followed by digit
            Arguments.of("\uDBC0\uDC0000", "\\10000000"), // High surrogate pair followed by digits
            Arguments.of("eof\uFFFD", "eof\\"), // Escape at EOF
            Arguments.of("simple-ident", "simple-ident"),
            Arguments.of("testing123", "testing123"),
            Arguments.of("_underscore", "_underscore"),
            Arguments.of("-text", "-text"),
            Arguments.of("-m", "-\\6d"),
            Arguments.of("--abc", "--abc"),
            Arguments.of("--", "--"),
            Arguments.of("--11", "--11"),
            Arguments.of("---", "---"),
            Arguments.of("\u2003", "\u2003"), // EM SPACE
            Arguments.of("\u00A0", "\u00A0"), // NO-BREAK SPACE
            Arguments.of("\u1234", "\u1234"), // ETHIOPIC SYLLABLE SEE
            Arguments.of("\uD808\uDF45", "\uD808\uDF45"), // DESERET SMALL LETTER LONG I
            Arguments.of("\uFFFD", "\u0000"), // Unescaped null becomes replacement char
            Arguments.of("ab\uFFFDc", "ab\u0000c")
        );
    }

    static Stream<Arguments> consumeCssIdentifier_additionalParameters() {
        return Stream.of(
            // Escaped digit at start with CRLF whitespace
            Arguments.of("1st", "\\31\r\nst"),
            // Escaped digit at start with CR whitespace
            Arguments.of("1", "\\31\r"),
            // Escaped digit at start with CR whitespace, followed by letter
            Arguments.of("1a", "\\31\ra"),
            // Padded hex escapes
            Arguments.of("1", "\\031"),
            Arguments.of("1", "\\0031"),
            Arguments.of("1", "\\00031"),
            Arguments.of("1", "\\000031"),
            // Escaped newline should be ignored
            Arguments.of("ab", "a\\\nb")
        );
    }
}