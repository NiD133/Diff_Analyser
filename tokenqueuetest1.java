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
 * Tests for the {@link TokenQueue} class.
 * This suite is organized into nested classes, each focusing on a specific method of TokenQueue.
 */
class TokenQueueTest {

    @Nested
    @DisplayName("escapeCssIdentifier()")
    class EscapeCssIdentifierTests {

        @DisplayName("should correctly escape CSS identifiers")
        @ParameterizedTest(name = "[{index}] Input: \"{1}\" -> Expected: \"{0}\"")
        @MethodSource("cssIdentifierEscapeCases")
        void testEscapeCssIdentifier(String expected, String input) {
            assertEquals(expected, TokenQueue.escapeCssIdentifier(input));
        }

        private static Stream<Arguments> cssIdentifierEscapeCases() {
            // Test cases are sourced from W3C Web Platform Tests and jsoup's own additions.
            // See: https://github.com/web-platform-tests/wpt/blob/328fa1c67bf5dfa6f24571d4c41dd10224b6d247/css/cssom/escape.html
            Stream<Arguments> webPlatformTests = Stream.of(
                // Empty string
                Arguments.of("", ""),

                // Null bytes are replaced with replacement character U+FFFD
                Arguments.of("\uFFFD", "\0"),
                Arguments.of("a\uFFFD", "a\0"),
                Arguments.of("\uFFFDb", "\0b"),
                Arguments.of("a\uFFFDb", "a\0b"),

                // Replacement character is passed through
                Arguments.of("\uFFFD", "\uFFFD"),
                Arguments.of("a\uFFFD", "a\uFFFD"),
                Arguments.of("\uFFFDb", "\uFFFDb"),
                Arguments.of("a\uFFFDb", "a\uFFFDb"),

                // A digit at the start of an identifier is escaped
                Arguments.of("\\30 a", "0a"), Arguments.of("\\31 a", "1a"), Arguments.of("\\32 a", "2a"),
                Arguments.of("\\33 a", "3a"), Arguments.of("\\34 a", "4a"), Arguments.of("\\35 a", "5a"),
                Arguments.of("\\36 a", "6a"), Arguments.of("\\37 a", "7a"), Arguments.of("\\38 a", "8a"),
                Arguments.of("\\39 a", "9a"),

                // A digit that is not at the start is not escaped
                Arguments.of("a0b", "a0b"), Arguments.of("a1b", "a1b"), Arguments.of("a2b", "a2b"),
                Arguments.of("a3b", "a3b"), Arguments.of("a4b", "a4b"), Arguments.of("a5b", "a5b"),
                Arguments.of("a6b", "a6b"), Arguments.of("a7b", "a7b"), Arguments.of("a8b", "a8b"),
                Arguments.of("a9b", "a9b"),

                // A hyphen followed by a digit at the start is escaped
                Arguments.of("-\\30 a", "-0a"), Arguments.of("-\\31 a", "-1a"), Arguments.of("-\\32 a", "-2a"),
                Arguments.of("-\\33 a", "-3a"), Arguments.of("-\\34 a", "-4a"), Arguments.of("-\\35 a", "-5a"),
                Arguments.of("-\\36 a", "-6a"), Arguments.of("-\\37 a", "-7a"), Arguments.of("-\\38 a", "-8a"),
                Arguments.of("-\\39 a", "-9a"),

                // A double-dash prefix is not escaped if not followed by a digit
                Arguments.of("--a", "--a"),

                // Control characters are escaped
                Arguments.of("\\1 \\2 \\1e \\1f ", "\u0001\u0002\u001E\u001F"),
                Arguments.of("\\7f \u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F", "\u007F\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F"),

                // High code points are not escaped
                Arguments.of("\u0080\u002D\u005F\u00A9", "\u0080\u002D\u005F\u00A9"),
                Arguments.of("\u00A0\u00A1\u00A2", "\u00A0\u00A1\u00A2"),
                Arguments.of("hello\u1234world", "hello\u1234world"),

                // Alphanumeric characters are not escaped
                Arguments.of("a0123456789b", "a0123456789b"),
                Arguments.of("abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz"),
                Arguments.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),

                // A single dash at the start of an identifier is escaped
                Arguments.of("\\-", "-"),

                // Backslashes and other non-alphanumeric ASCII characters are escaped
                Arguments.of("hello\\\\world", "hello\\world"),
                Arguments.of("\\ \\!xy", "\u0020\u0021\u0078\u0079"),

                // Astral symbols (surrogate pairs) and lone surrogates are preserved
                Arguments.of("\uD834\uDF06", "\uD834\uDF06"),
                Arguments.of("\uDF06", "\uDF06"),
                Arguments.of("\uD834", "\uD834")
            );

            Stream<Arguments> additionalJsoupTests = Stream.of(
                Arguments.of("one\\#two\\.three\\/four\\\\five", "one#two.three/four\\five"),
                Arguments.of("-a", "-a"),
                Arguments.of("--", "--")
            );

            return Stream.concat(webPlatformTests, additionalJsoupTests);
        }
    }

    @Nested
    @DisplayName("consumeCssIdentifier()")
    class ConsumeCssIdentifierTests {

        @DisplayName("should correctly consume and unescape CSS identifiers")
        @ParameterizedTest(name = "[{index}] Input: \"{1}\" -> Expected: \"{0}\"")
        @MethodSource("cssIdentifierConsumeCases")
        void testConsumeCssIdentifier(String expected, String escapedIdentifier) {
            TokenQueue tq = new TokenQueue(escapedIdentifier);
            String actual = tq.consumeCssIdentifier();
            assertEquals(expected, actual);
        }

        private static Stream<Arguments> cssIdentifierConsumeCases() {
            // Test cases sourced from W3C Web Platform Tests and Chromium source.
            // See: https://github.com/web-platform-tests/wpt/blob/36036fb5212a3fc15fc5750cecb1923ba4071668/dom/nodes/ParentNode-querySelector-escapes.html
            // See: https://chromium.googlesource.com/chromium/src/+/refs/heads/main/third_party/blink/renderer/core/css/css-selector-parser-test.cc
            Stream<Arguments> webPlatformAndChromiumTests = Stream.of(
                // Hex digit escapes
                Arguments.of("0nextIsWhiteSpace", "\\30 nextIsWhiteSpace"),
                Arguments.of("0nextIsNotHexLetters", "\\30nextIsNotHexLetters"),
                Arguments.of("0connectHexMoreThan6Hex", "\\000030connectHexMoreThan6Hex"),
                Arguments.of("0spaceMoreThan6Hex", "\\000030 spaceMoreThan6Hex"),

                // Hex digit escapes resulting in special replacement characters
                Arguments.of("zero\uFFFD", "zero\\0"), // zero points
                Arguments.of("zero\uFFFD", "zero\\000000"),
                Arguments.of("\uFFFDsurrogateFirst", "\\d83d surrogateFirst"), // surrogate points
                Arguments.of("surrogateSecond\uFFFd", "surrogateSecond\\dd11"),
                Arguments.of("surrogatePair\uFFFD\uFFFD", "surrogatePair\\d83d\\dd11"),
                Arguments.of("outOfRange\uFFFD", "outOfRange\\110000"), // out of range points
                Arguments.of("outOfRange\uFFFD", "outOfRange\\ffffff"),

                // Other escapes
                Arguments.of(".comma", "\\.comma"),
                Arguments.of("-minus", "\\-minus"),
                Arguments.of("g", "\\g"),

                // Non-edge cases
                Arguments.of("aBMPRegular", "\\61 BMPRegular"),
                Arguments.of("\uD83D\uDD11nonBMP", "\\1f511 nonBMP"),
                Arguments.of("00continueEscapes", "\\30\\30 continueEscapes"),
                Arguments.of("00continueEscapes", "\\30 \\30 continueEscapes"),
                Arguments.of("continueEscapes00", "continueEscapes\\30 \\30 "),
                Arguments.of("continueEscapes00", "continueEscapes\\30\\30"),

                // Chromium test cases
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
                Arguments.of("large\uFFFD", "large\\110000"),
                Arguments.of("surrogate\uFFFD", "surrogate\\D800"),
                Arguments.of("\uDBFF\uDFFF", "\\10fFfF"),
                Arguments.of("\uDBFF\uDFFF0", "\\10fFfF0"),
                Arguments.of("\uDBC0\uDC0000", "\\10000000"),
                Arguments.of("eof\uFFFD", "eof\\"),

                // Regular identifiers (no escapes)
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

            Stream<Arguments> additionalJsoupTests = Stream.of(
                Arguments.of("1st", "\\31\r\nst"),
                Arguments.of("1", "\\31\r"),
                Arguments.of("1a", "\\31\ra"),
                Arguments.of("1", "\\031"),
                Arguments.of("1", "\\00031"),
                Arguments.of("1", "\\000031"),
                Arguments.of("a", "a\\\nb") // escaped newline
            );

            return Stream.concat(webPlatformAndChromiumTests, additionalJsoupTests);
        }
    }

    @Nested
    @DisplayName("chompBalanced()")
    class ChompBalancedTests {

        @Test
        @DisplayName("should correctly extract a balanced string")
        void chompBalanced() {
            // Arrange
            String input = ":contains(one (two) three) four";
            TokenQueue tq = new TokenQueue(input);

            // Act
            String pre = tq.consumeTo("(");
            String guts = tq.chompBalanced('(', ')');
            String remainder = tq.remainder();

            // Assert
            assertEquals(":contains", pre);
            assertEquals("one (two) three", guts);
            assertEquals(" four", remainder);
        }
    }
}