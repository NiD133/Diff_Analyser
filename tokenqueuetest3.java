package org.jsoup.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for CSS-related parsing in {@link TokenQueue}.
 * Includes tests for escaping and consuming CSS identifiers, largely based on Web Platform Tests (WPT).
 */
public class TokenQueueCssTest {

    @Nested
    @DisplayName("escapeCssIdentifier()")
    class EscapeCssIdentifier {
        @ParameterizedTest(name = "should escape ''{1}'' to ''{0}''")
        @MethodSource("org.jsoup.parser.TokenQueueCssTest#cssIdentifierEscapeTests")
        void testEscape(String expected, String input) {
            assertEquals(expected, TokenQueue.escapeCssIdentifier(input));
        }
    }

    @Nested
    @DisplayName("consumeCssIdentifier()")
    class ConsumeCssIdentifier {
        @ParameterizedTest(name = "should consume ''{1}'' as ''{0}''")
        @MethodSource("org.jsoup.parser.TokenQueueCssTest#cssIdentifierConsumeTests")
        void testConsume(String expected, String cssIdentifier) {
            assertEquals(expected, parseCssIdentifier(cssIdentifier));
        }

        private String parseCssIdentifier(String text) {
            TokenQueue q = new TokenQueue(text);
            return q.consumeCssIdentifier();
        }
    }

    @Nested
    @DisplayName("chompBalanced()")
    class ChompBalanced {
        @Test
        @DisplayName("should find the longest balanced match")
        void chompBalancedFindsLongestMatch() {
            TokenQueue tq = new TokenQueue("unbalanced(something(or another)) else");
            tq.consumeTo("(");
            String match = tq.chompBalanced('(', ')');
            assertEquals("something(or another)", match);
        }
    }

    // --- Data Providers for Parameterized Tests ---

    static Stream<Arguments> cssIdentifierEscapeTests() {
        // Combines WPT tests and additional jsoup-specific tests.
        // WPT Source: https://github.com/web-platform-tests/wpt/blob/328fa1c/css/cssom/escape.html
        return Stream.concat(
            webPlatformEscapeTests(),
            additionalEscapeTests()
        );
    }

    private static Stream<Arguments> webPlatformEscapeTests() {
        return Stream.of(
            // Empty string
            Arguments.of("", ""),
            // Null bytes are replaced with the replacement character
            Arguments.of("\uFFFD", "\0"),
            Arguments.of("a\uFFFD", "a\0"),
            Arguments.of("\uFFFDb", "\0b"),
            Arguments.of("a\uFFFDb", "a\0b"),
            // Replacement character is passed through
            Arguments.of("\uFFFD", "\uFFFD"),
            Arguments.of("a\uFFFD", "a\uFFFD"),
            Arguments.of("\uFFFDb", "\uFFFDb"),
            Arguments.of("a\uFFFDb", "a\uFFFDb"),
            // Identifiers starting with a digit are escaped
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
            // Identifiers starting with a letter then a digit are not escaped
            Arguments.of("a0b", "a0b"),
            Arguments.of("a1b", "a1b"),
            Arguments.of("a9b", "a9b"),
            // Identifiers starting with a dash then a digit are escaped
            Arguments.of("-\\30 a", "-0a"),
            Arguments.of("-\\31 a", "-1a"),
            Arguments.of("-\\39 a", "-9a"),
            // Double-dash prefix is not escaped
            Arguments.of("--a", "--a"),
            // Control characters are escaped
            Arguments.of("\\1 \\2 \\1e \\1f ", "\u0001\u0002\u001E\u001F"),
            // Non-ASCII characters are preserved
            Arguments.of("\u0080\u002D\u005F\u00A9", "\u0080\u002D\u005F\u00A9"),
            // Various characters
            Arguments.of("\\7f \u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F", "\u007F\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F"),
            Arguments.of("\u00A0\u00A1\u00A2", "\u00A0\u00A1\u00A2"),
            // Alphanumeric characters are preserved
            Arguments.of("a0123456789b", "a0123456789b"),
            Arguments.of("abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz"),
            Arguments.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
            // Backslashes are backslash-escaped
            Arguments.of("hello\\\\world", "hello\\world"),
            // Code points greater than U+0080 are preserved
            Arguments.of("hello\u1234world", "hello\u1234world"),
            // A single dash at the start is escaped
            Arguments.of("\\-", "-"),
            // Special characters are escaped
            Arguments.of("\\ \\!xy", "\u0020\u0021\u0078\u0079"),
            // Astral symbols (e.g., U+1D306) are preserved
            Arguments.of("\uD834\uDF06", "\uD834\uDF06"),
            // Lone surrogates are preserved
            Arguments.of("\uDF06", "\uDF06"),
            Arguments.of("\uD834", "\uD834")
        );
    }

    private static Stream<Arguments> additionalEscapeTests() {
        return Stream.of(
            Arguments.of("one\\#two\\.three\\/four\\\\five", "one#two.three/four\\five"),
            Arguments.of("-a", "-a"),
            Arguments.of("--", "--")
        );
    }

    static Stream<Arguments> cssIdentifierConsumeTests() {
        return Stream.of(
            webPlatformDomNodeTests(),
            chromiumSourceTests(),
            additionalConsumeTests()
        ).flatMap(s -> s);
    }

    // WPT Source: https://github.com/web-platform-tests/wpt/blob/36036fb/dom/nodes/ParentNode-querySelector-escapes.html
    private static Stream<Arguments> webPlatformDomNodeTests() {
        return Stream.of(
            // Hex digit escapes
            Arguments.of("0nextIsWhiteSpace", "\\30 nextIsWhiteSpace"),
            Arguments.of("0nextIsNotHexLetters", "\\30nextIsNotHexLetters"),
            Arguments.of("0connectHexMoreThan6Hex", "\\000030connectHexMoreThan6Hex"),
            Arguments.of("0spaceMoreThan6Hex", "\\000030 spaceMoreThan6Hex"),
            // Hex digit special replacements
            Arguments.of("zero\uFFFD", "zero\\0"),
            Arguments.of("zero\uFFFD", "zero\\000000"),
            // Surrogate code points
            Arguments.of("\uFFFDsurrogateFirst", "\\d83d surrogateFirst"),
            Arguments.of("surrogateSecond\uFFFd", "surrogateSecond\\dd11"),
            Arguments.of("surrogatePair\uFFFD\uFFFD", "surrogatePair\\d83d\\dd11"),
            // Out of range code points
            Arguments.of("outOfRange\uFFFD", "outOfRange\\110000"),
            Arguments.of("outOfRange\uFFFD", "outOfRange\\110030"),
            // Other escapes
            Arguments.of(".comma", "\\.comma"),
            Arguments.of("-minus", "\\-minus"),
            Arguments.of("g", "\\g"),
            // General cases
            Arguments.of("aBMPRegular", "\\61 BMPRegular"),
            Arguments.of("\uD83D\uDD11nonBMP", "\\1f511 nonBMP"),
            Arguments.of("00continueEscapes", "\\30\\30 continueEscapes"),
            Arguments.of("00continueEscapes", "\\30 \\30 continueEscapes"),
            Arguments.of("continueEscapes00", "continueEscapes\\30 \\30 "),
            Arguments.of("continueEscapes00", "continueEscapes\\30\\30")
        );
    }

    // Chromium Source: https://chromium.googlesource.com/chromium/src/+/refs/heads/main/third_party/blink/renderer/core/css/css-grammar-test.cc
    private static Stream<Arguments> chromiumSourceTests() {
        return Stream.of(
            Arguments.of("hello", "hel\\6Co"),
            Arguments.of("&B", "\\26 B"),
            Arguments.of("hello", "hel\\6C o"),
            Arguments.of("spaces", "spac\\65\r\ns"),
            Arguments.of("spaces", "sp\\61\tc\\65\fs"),
            Arguments.of("test\uD799", "test\\D799"),
            Arguments.of("\uE000", "\\E000"),
            Arguments.of("test", "te\\s\\t"),
            Arguments.of("spaces in\tident", "spaces\\ in\\\tident"),
            Arguments.of(".,:!", "\\.\\,\\:\\!"),
            Arguments.of("null\uFFFD", "null\\0"),
            Arguments.of("null\uFFFD", "null\\0000"),
            Arguments.of("large\uFFFD", "large\\110000"),
            Arguments.of("large\uFFFD", "large\\23456a"),
            Arguments.of("surrogate\uFFFD", "surrogate\\D800"),
            Arguments.of("surrogate\uFFFD", "surrogate\\0DBAC"),
            Arguments.of("\uFFFDsurrogate", "\\00DFFFsurrogate"),
            Arguments.of("\uDBFF\uDFFF", "\\10fFfF"),
            Arguments.of("\uDBFF\uDFFF0", "\\10fFfF0"),
            Arguments.of("\uDBC0\uDC0000", "\\10000000"),
            Arguments.of("eof\uFFFD", "eof\\"),
            Arguments.of("simple-ident", "simple-ident"),
            Arguments.of("testing123", "testing123"),
            Arguments.of("_underscore", "_underscore"),
            Arguments.of("-text", "-text"),
            Arguments.of("-m", "-\\6d"),
            Arguments.of("--abc", "--abc"),
            Arguments.of("--", "--"),
            Arguments.of("--11", "--11"),
            Arguments.of("---", "---"),
            Arguments.of("\u2003", "\u2003"),
            Arguments.of("\u00A0", "\u00A0"),
            Arguments.of("\u1234", "\u1234"),
            Arguments.of("\uD808\uDF45", "\uD808\uDF45"),
            Arguments.of("\uFFFD", "\u0000"),
            Arguments.of("ab\uFFFDc", "ab\u0000c")
        );
    }

    private static Stream<Arguments> additionalConsumeTests() {
        return Stream.of(
            Arguments.of("1st", "\\31\r\nst"),
            Arguments.of("1", "\\31\r"),
            Arguments.of("1a", "\\31\ra"),
            Arguments.of("1", "\\031"),
            Arguments.of("1", "\\0031"),
            Arguments.of("1", "\\00031"),
            Arguments.of("1", "\\000031"),
            Arguments.of("a", "a\\\nb")
        );
    }
}