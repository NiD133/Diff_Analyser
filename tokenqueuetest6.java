package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for CSS-related token consumption in TokenQueue.
 * Includes tests for consuming and escaping CSS identifiers, based on W3C and browser implementation test suites.
 */
public class TokenQueueCssTest {
    private static final String REPLACEMENT_CHAR = "\uFFFD";
    private static final String NULL_CHAR = "\0";

    @DisplayName("escapeCssIdentifier should correctly escape CSS identifiers")
    @ParameterizedTest(name = "[{index}] Input: \"{1}\", Expected: \"{0}\"")
    @MethodSource("cssIdentifierEscapeTestCases")
    void escapeCssIdentifier(String expected, String input) {
        assertEquals(expected, TokenQueue.escapeCssIdentifier(input));
    }

    private static Stream<Arguments> cssIdentifierEscapeTestCases() {
        // Combines WPT tests with additional edge cases for comprehensive coverage.
        return Stream.concat(
            webPlatformTestCasesForEscape(),
            additionalTestCasesForEscape()
        );
    }

    /**
     * Test cases for escaping CSS identifiers, sourced from the Web Platform Tests.
     * @see <a href="https://github.com/web-platform-tests/wpt/blob/328fa1c67bf5dfa6f24571d4c41dd10224b6d247/css/cssom/escape.html">WPT escape.html</a>
     */
    private static Stream<Arguments> webPlatformTestCasesForEscape() {
        return Stream.of(
            // Empty string
            Arguments.of("", ""),

            // Null bytes are replaced with the replacement character
            Arguments.of(REPLACEMENT_CHAR, NULL_CHAR),
            Arguments.of("a" + REPLACEMENT_CHAR, "a" + NULL_CHAR),
            Arguments.of(REPLACEMENT_CHAR + "b", NULL_CHAR + "b"),
            Arguments.of("a" + REPLACEMENT_CHAR + "b", "a" + NULL_CHAR + "b"),

            // Replacement characters are passed through
            Arguments.of(REPLACEMENT_CHAR, REPLACEMENT_CHAR),
            Arguments.of("a" + REPLACEMENT_CHAR, "a" + REPLACEMENT_CHAR),

            // A digit at the start of an identifier is escaped
            Arguments.of("\\30 a", "0a"), Arguments.of("\\31 a", "1a"), Arguments.of("\\39 a", "9a"),

            // Digits not at the start are not escaped
            Arguments.of("a0b", "a0b"), Arguments.of("a9b", "a9b"),

            // A hyphen followed by a digit at the start is escaped
            Arguments.of("-\\30 a", "-0a"), Arguments.of("-\\31 a", "-1a"), Arguments.of("-\\39 a", "-9a"),

            // Double dash prefix is not escaped
            Arguments.of("--a", "--a"),

            // Control characters are escaped
            Arguments.of("\\1 \\2 \\1e \\1f ", "\u0001\u0002\u001E\u001F"),
            Arguments.of("\\7f \u0080", "\u007F\u0080"), // non-ascii and others are not escaped

            // Valid identifier characters are not escaped
            Arguments.of("\u0080\u002D\u005F\u00A9", "\u0080\u002D\u005F\u00A9"), // includes hyphen and underscore
            Arguments.of("a0123456789b", "a0123456789b"),
            Arguments.of("abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz"),
            Arguments.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),

            // Backslashes are themselves escaped
            Arguments.of("hello\\\\world", "hello\\world"),

            // High code points are preserved
            Arguments.of("hello\u1234world", "hello\u1234world"),

            // A single dash at the start is escaped
            Arguments.of("\\-", "-"),

            // Other special ASCII characters are escaped
            Arguments.of("\\ \\!xy", "\u0020\u0021\u0078\u0079"),

            // Astral symbols (surrogate pairs) are preserved
            Arguments.of("\uD834\uDF06", "\uD834\uDF06"), // U+1D306 TETRAGRAM FOR CENTRE

            // Lone surrogates are preserved
            Arguments.of("\uDF06", "\uDF06"),
            Arguments.of("\uD834", "\uD834")
        );
    }

    private static Stream<Arguments> additionalTestCasesForEscape() {
        return Stream.of(
            // Characters that need escaping in selectors
            Arguments.of("one\\#two\\.three\\/four\\\\five", "one#two.three/four\\five"),
            // A hyphen not at the start is not escaped
            Arguments.of("-a", "-a"),
            // A double dash is not escaped
            Arguments.of("--", "--")
        );
    }

    @DisplayName("consumeCssIdentifier should correctly parse CSS identifiers")
    @ParameterizedTest(name = "[{index}] Input: \"{1}\", Expected: \"{0}\"")
    @MethodSource("cssIdentifierParseTestCases")
    void consumeCssIdentifier(String expected, String cssIdentifier) {
        TokenQueue tq = new TokenQueue(cssIdentifier);
        String consumed = tq.consumeCssIdentifier();
        assertEquals(expected, consumed);
    }

    private static Stream<Arguments> cssIdentifierParseTestCases() {
        return Stream.concat(
            webPlatformTestCasesForParse(),
            additionalTestCasesForParse()
        );
    }

    /**
     * Test cases for consuming CSS identifiers, sourced from WPT and Chromium tests.
     * @see <a href="https://github.com/web-platform-tests/wpt/blob/36036fb5212a3fc15fc5750cecb1923ba4071668/dom/nodes/ParentNode-querySelector-escapes.html">WPT escapes.html</a>
     * @see <a href="https://goo.gl/3Cxdov">Chromium CSS ident tests</a>
     */
    private static Stream<Arguments> webPlatformTestCasesForParse() {
        return Stream.of(
            // WPT: Hex digit escapes
            Arguments.of("0nextIsWhiteSpace", "\\30 nextIsWhiteSpace"),
            Arguments.of("0nextIsNotHexLetters", "\\30nextIsNotHexLetters"),
            Arguments.of("0connectHexMoreThan6Hex", "\\000030connectHexMoreThan6Hex"),

            // WPT: Special hex replacements (null, surrogates, out of range)
            Arguments.of("zero" + REPLACEMENT_CHAR, "zero\\0"),
            Arguments.of("zero" + REPLACEMENT_CHAR, "zero\\000000"),
            Arguments.of(REPLACEMENT_CHAR + "surrogateFirst", "\\d83d surrogateFirst"),
            Arguments.of("surrogateSecond" + REPLACEMENT_CHAR, "surrogateSecond\\dd11"),
            Arguments.of("outOfRange" + REPLACEMENT_CHAR, "outOfRange\\110000"),

            // WPT: Simple escapes
            Arguments.of(".comma", "\\.comma"),
            Arguments.of("-minus", "\\-minus"),
            Arguments.of("g", "\\g"),

            // WPT: Non-edge cases
            Arguments.of("aBMPRegular", "\\61 BMPRegular"),
            Arguments.of("\uD83D\uDD11nonBMP", "\\1f511 nonBMP"),
            Arguments.of("00continueEscapes", "\\30\\30 continueEscapes"),

            // Chromium: Various escape sequences
            Arguments.of("hello", "hel\\6Co"),
            Arguments.of("&B", "\\26 B"),
            Arguments.of("spaces", "spac\\65\r\ns"),
            Arguments.of("test\uD799", "test\\D799"),
            Arguments.of("test", "te\\s\\t"),
            Arguments.of(".,:!", "\\.\\,\\:\\!"),

            // Chromium: Null, out of range, and surrogate escapes
            Arguments.of("null" + REPLACEMENT_CHAR, "null\\0"),
            Arguments.of("large" + REPLACEMENT_CHAR, "large\\110000"),
            Arguments.of("surrogate" + REPLACEMENT_CHAR, "surrogate\\D800"),

            // Chromium: High code points
            Arguments.of("\uDBFF\uDFFF", "\\10fFfF"), // valid surrogate pair for U+10FFFF
            Arguments.of("\uDBFF\uDFFF" + "0", "\\10fFfF0"),

            // Chromium: Edge cases
            Arguments.of("eof" + REPLACEMENT_CHAR, "eof\\"),
            Arguments.of("simple-ident", "simple-ident"),
            Arguments.of("-text", "-text"),
            Arguments.of("-m", "-\\6d"),
            Arguments.of("--abc", "--abc"),
            Arguments.of("--", "--"),

            // Chromium: Unescaped characters
            Arguments.of("\u2003", "\u2003"), // em space
            Arguments.of("\uD808\uDF45", "\uD808\uDF45"), // surrogate pair

            // Chromium: Null character input
            Arguments.of(REPLACEMENT_CHAR, NULL_CHAR),
            Arguments.of("ab" + REPLACEMENT_CHAR + "c", "ab" + NULL_CHAR + "c")
        );
    }

    private static Stream<Arguments> additionalTestCasesForParse() {
        return Stream.of(
            // Escaped digit followed by newline
            Arguments.of("1st", "\\31\r\nst"),
            // Escaped digit at end of input with trailing whitespace
            Arguments.of("1", "\\31\r"),
            Arguments.of("1a", "\\31\ra"),
            // Padded hex escapes
            Arguments.of("1", "\\031"),
            Arguments.of("1", "\\00031"),
            Arguments.of("1", "\\000031"),
            // An escaped newline should be ignored
            Arguments.of("ab", "a\\\nb")
        );
    }

    @Nested
    @DisplayName("Selector parsing with nested quotes")
    class SelectorWithNestedQuotesTest {
        private void validate(String html, String selector) {
            Document doc = Jsoup.parse(html);
            // The selector should find the element with id="identifier"
            assertEquals("#identifier", doc.select(selector).first().cssSelector());
        }

        @Test
        @DisplayName("should handle single quotes inside double-quoted attribute selector")
        void handlesSingleQuotesInDoubleQuotedAttribute() {
            String html = "<html><body><a id=\"identifier\" onclick=\"func('arg')\" /></body></html>";
            // Note the unclosed quote in the selector value, which is valid.
            String selector = "a[onclick*=\"('arg\"]";
            validate(html, selector);
        }

        @Test
        @DisplayName("should handle single quotes in unquoted attribute value")
        void handlesSingleQuotesInUnquotedAttribute() {
            String html = "<html><body><a id=\"identifier\" onclick=func('arg') /></body></html>";
            String selector = "a[onclick*=\"('arg\"]";
            validate(html, selector);
        }

        @Test
        @DisplayName("should handle double quotes inside single-quoted attribute selector")
        void handlesDoubleQuotesInSingleQuotedAttribute() {
            String html = "<html><body><a id=\"identifier\" onclick='func(\"arg\")' /></body></html>";
            String selector = "a[onclick*='(\"arg']";
            validate(html, selector);
        }

        @Test
        @DisplayName("should handle double quotes in unquoted attribute value")
        void handlesDoubleQuotesInUnquotedAttribute() {
            String html = "<html><body><a id=\"identifier\" onclick=func(\"arg\") /></body></html>";
            String selector = "a[onclick*='(\"arg']";
            validate(html, selector);
        }
    }
}