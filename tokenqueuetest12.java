package org.jsoup.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for CSS identifier parsing and escaping in {@link TokenQueue}.
 * Includes tests ported from the W3C web-platform-tests repository and Chromium source.
 *
 * @see TokenQueue#escapeCssIdentifier(String)
 * @see TokenQueue#consumeCssIdentifier()
 */
@DisplayName("TokenQueue CSS Identifier Handling")
public class TokenQueueCssIdentifierTest {

    //<editor-fold desc="Tests for TokenQueue.escapeCssIdentifier()">

    @ParameterizedTest(name = "[{index}] Input: \"{1}\", Expected: \"{0}\"")
    @MethodSource({
        "w3cCssEscape_nullAndReplacementChars",
        "w3cCssEscape_numericAndDashPrefixes",
        "w3cCssEscape_controlAndSpecialChars",
        "w3cCssEscape_uncommonAndSurrogateChars",
        "customCssEscape_specialChars"
    })
    void escapeCssIdentifier(String expected, String input) {
        assertEquals(expected, TokenQueue.escapeCssIdentifier(input));
    }

    /**
     * Provides test cases for escaping null bytes and replacement characters.
     * @see <a href="https://github.com/web-platform-tests/wpt/blob/master/css/cssom/escape.html">W3C CSSOM Escape Tests</a>
     */
    private static Stream<Arguments> w3cCssEscape_nullAndReplacementChars() {
        return Stream.of(
            // Empty string
            Arguments.of("", ""),
            // Null bytes are replaced with the replacement character
            Arguments.of("\uFFFD", "\0"),
            Arguments.of("a\uFFFD", "a\0"),
            Arguments.of("\uFFFDb", "\0b"),
            Arguments.of("a\uFFFDb", "a\0b"),
            // Replacement characters are passed through
            Arguments.of("\uFFFD", "\uFFFD"),
            Arguments.of("a\uFFFD", "a\uFFFD"),
            Arguments.of("\uFFFDb", "\uFFFDb"),
            Arguments.of("a\uFFFDb", "a\uFFFDb")
        );
    }

    /**
     * Provides test cases for escaping identifiers starting with numbers or dashes.
     * @see <a href="https://github.com/web-platform-tests/wpt/blob/master/css/cssom/escape.html">W3C CSSOM Escape Tests</a>
     */
    private static Stream<Arguments> w3cCssEscape_numericAndDashPrefixes() {
        return Stream.of(
            // A digit at the start of an identifier is escaped
            Arguments.of("\\30 a", "0a"), Arguments.of("\\31 a", "1a"), Arguments.of("\\32 a", "2a"),
            Arguments.of("\\33 a", "3a"), Arguments.of("\\34 a", "4a"), Arguments.of("\\35 a", "5a"),
            Arguments.of("\\36 a", "6a"), Arguments.of("\\37 a", "7a"), Arguments.of("\\38 a", "8a"),
            Arguments.of("\\39 a", "9a"),
            // Digits after a letter are not escaped
            Arguments.of("a0b", "a0b"), Arguments.of("a1b", "a1b"), Arguments.of("a9b", "a9b"),
            // A dash followed by a digit is escaped
            Arguments.of("-\\30 a", "-0a"), Arguments.of("-\\31 a", "-1a"), Arguments.of("-\\39 a", "-9a"),
            // Double-dash prefix is allowed for custom properties
            Arguments.of("--a", "--a")
        );
    }

    /**
     * Provides test cases for escaping control characters and other special ASCII characters.
     * @see <a href="https://github.com/web-platform-tests/wpt/blob/master/css/cssom/escape.html">W3C CSSOM Escape Tests</a>
     */
    private static Stream<Arguments> w3cCssEscape_controlAndSpecialChars() {
        return Stream.of(
            // Control characters (U+0001 to U+001F) are escaped
            Arguments.of("\\1 \\2 \\1e \\1f ", "\u0001\u0002\u001E\u001F"),
            // A single leading dash is escaped
            Arguments.of("\\-", "-"),
            // Space and exclamation mark are escaped
            Arguments.of("\\ \\!xy", "\u0020\u0021\u0078\u0079"),
            // Backslashes are themselves escaped
            Arguments.of("hello\\\\world", "hello\\world")
        );
    }

    /**
     * Provides test cases for escaping high code points and surrogate pairs.
     * @see <a href="https://github.com/web-platform-tests/wpt/blob/master/css/cssom/escape.html">W3C CSSOM Escape Tests</a>
     */
    private static Stream<Arguments> w3cCssEscape_uncommonAndSurrogateChars() {
        return Stream.of(
            // Code points from U+0080 upwards are preserved
            Arguments.of("\u0080\u002D\u005F\u00A9", "\u0080\u002D\u005F\u00A9"),
            Arguments.of("hello\u1234world", "hello\u1234world"),
            // Astral symbols (surrogate pairs) are preserved
            Arguments.of("\uD834\uDF06", "\uD834\uDF06"), // U+1D306
            // Lone surrogates are preserved
            Arguments.of("\uDF06", "\uDF06"),
            Arguments.of("\uD834", "\uD834")
        );
    }

    /**
     * Provides additional custom test cases for escaping.
     */
    private static Stream<Arguments> customCssEscape_specialChars() {
        return Stream.of(
            Arguments.of("one\\#two\\.three\\/four\\\\five", "one#two.three/four\\five"),
            Arguments.of("-a", "-a"),
            Arguments.of("--", "--")
        );
    }

    //</editor-fold>

    //<editor-fold desc="Tests for TokenQueue.consumeCssIdentifier()">

    @ParameterizedTest(name = "[{index}] Input: \"{1}\", Expected: \"{0}\"")
    @MethodSource({
        "w3cCssConsume_hexEscapes",
        "w3cCssConsume_specialHexReplacements",
        "w3cCssConsume_generalEscapes",
        "w3cCssConsume_nonEdgeCases",
        "chromiumCssConsume_various",
        "chromiumCssConsume_simpleAndEdgeCases",
        "customCssConsume_various"
    })
    void consumeCssIdentifier(String expected, String cssIdentifier) {
        assertParsedCssIdentifierEquals(expected, cssIdentifier);
    }

    @Test
    void consumeCssIdentifier_whenEmpty_throwsException() {
        TokenQueue emptyQueue = new TokenQueue("");
        Exception exception = assertThrows(IllegalArgumentException.class, emptyQueue::consumeCssIdentifier);
        assertEquals("CSS identifier expected, but end of input found", exception.getMessage());
    }

    /**
     * Provides test cases for consuming hexadecimal escape sequences.
     * @see <a href="https://github.com/web-platform-tests/wpt/blob/master/dom/nodes/ParentNode-querySelector-escapes.html">W3C DOM Query Selector Escape Tests</a>
     */
    private static Stream<Arguments> w3cCssConsume_hexEscapes() {
        return Stream.of(
            Arguments.of("0nextIsWhiteSpace", "\\30 nextIsWhiteSpace"),
            Arguments.of("0nextIsNotHexLetters", "\\30nextIsNotHexLetters"),
            Arguments.of("0connectHexMoreThan6Hex", "\\000030connectHexMoreThan6Hex"),
            Arguments.of("0spaceMoreThan6Hex", "\\000030 spaceMoreThan6Hex")
        );
    }

    /**
     * Provides test cases for consuming special hex escape replacements (null, surrogate, out-of-range).
     * @see <a href="https://github.com/web-platform-tests/wpt/blob/master/dom/nodes/ParentNode-querySelector-escapes.html">W3C DOM Query Selector Escape Tests</a>
     */
    private static Stream<Arguments> w3cCssConsume_specialHexReplacements() {
        return Stream.of(
            // 1. Zero points are replaced with U+FFFD
            Arguments.of("zero\uFFFD", "zero\\0"),
            Arguments.of("zero\uFFFD", "zero\\000000"),
            // 2. Surrogate code points are replaced with U+FFFD
            Arguments.of("\uFFFDsurrogateFirst", "\\d83d surrogateFirst"),
            Arguments.of("surrogateSecond\uFFFd", "surrogateSecond\\dd11"),
            Arguments.of("surrogatePair\uFFFD\uFFFD", "surrogatePair\\d83d\\dd11"),
            // 3. Out-of-range code points are replaced with U+FFFD
            Arguments.of("outOfRange\uFFFD", "outOfRange\\110000"),
            Arguments.of("outOfRange\uFFFD", "outOfRange\\ffffff")
        );
    }

    /**
     * Provides test cases for consuming general character escapes.
     * @see <a href="https://github.com/web-platform-tests/wpt/blob/master/dom/nodes/ParentNode-querySelector-escapes.html">W3C DOM Query Selector Escape Tests</a>
     */
    private static Stream<Arguments> w3cCssConsume_generalEscapes() {
        return Stream.of(
            Arguments.of(".comma", "\\.comma"),
            Arguments.of("-minus", "\\-minus"),
            Arguments.of("g", "\\g")
        );
    }

    /**
     * Provides test cases for consuming non-edge-case escape sequences.
     * @see <a href="https://github.com/web-platform-tests/wpt/blob/master/dom/nodes/ParentNode-querySelector-escapes.html">W3C DOM Query Selector Escape Tests</a>
     */
    private static Stream<Arguments> w3cCssConsume_nonEdgeCases() {
        return Stream.of(
            Arguments.of("aBMPRegular", "\\61 BMPRegular"),
            Arguments.of("\uD83D\uDD11nonBMP", "\\1f511 nonBMP"),
            Arguments.of("00continueEscapes", "\\30\\30 continueEscapes"),
            Arguments.of("00continueEscapes", "\\30 \\30 continueEscapes"),
            Arguments.of("continueEscapes00", "continueEscapes\\30 \\30 "),
            Arguments.of("continueEscapes00", "continueEscapes\\30\\30")
        );
    }

    /**
     * Provides test cases from Chromium's CSS tests.
     * @see <a href="https://goo.gl/3Cxdov">Chromium CSS Tests</a>
     */
    private static Stream<Arguments> chromiumCssConsume_various() {
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
            Arguments.of(".,:!", "\\.\\,\\:\\!")
        );
    }

    /**
     * Provides test cases for simple identifiers and edge cases from Chromium's CSS tests.
     * @see <a href="https://goo.gl/3Cxdov">Chromium CSS Tests</a>
     */
    private static Stream<Arguments> chromiumCssConsume_simpleAndEdgeCases() {
        return Stream.of(
            // Simple identifiers
            Arguments.of("simple-ident", "simple-ident"),
            Arguments.of("testing123", "testing123"),
            Arguments.of("_underscore", "_underscore"),
            Arguments.of("-text", "-text"),
            Arguments.of("-m", "-\\6d"),
            Arguments.of("--abc", "--abc"),
            Arguments.of("--", "--"),
            Arguments.of("--11", "--11"),
            Arguments.of("---", "---"),
            // Unescaped characters
            Arguments.of("\u2003", "\u2003"),
            Arguments.of("\u00A0", "\u00A0"),
            Arguments.of("\u1234", "\u1234"),
            Arguments.of("\uD808\uDF45", "\uD808\uDF45"),
            // Edge cases with null, surrogates, and EOF
            Arguments.of("null\uFFFD", "null\\0"),
            Arguments.of("surrogate\uFFFD", "surrogate\\D800"),
            Arguments.of("\uDBFF\uDFFF", "\\10fFfF"),
            Arguments.of("eof\uFFFD", "eof\\"),
            Arguments.of("\uFFFD", "\u0000"),
            Arguments.of("ab\uFFFDc", "ab\u0000c")
        );
    }

    /**
     * Provides additional custom test cases for consuming identifiers.
     */
    private static Stream<Arguments> customCssConsume_various() {
        return Stream.of(
            Arguments.of("1st", "\\31\r\nst"),
            Arguments.of("1", "\\31\r"),
            Arguments.of("1a", "\\31\ra"),
            Arguments.of("1", "\\031"),
            Arguments.of("1", "\\00031"),
            Arguments.of("1", "\\0000031"),
            Arguments.of("a", "a\\\nb") // escaped newline is ignored
        );
    }

    private void assertParsedCssIdentifierEquals(String expected, String cssIdentifier) {
        TokenQueue tq = new TokenQueue(cssIdentifier);
        String consumed = tq.consumeCssIdentifier();
        assertEquals(expected, consumed);
    }

    //</editor-fold>
}