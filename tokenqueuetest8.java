package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for CSS identifier parsing and escaping in TokenQueue.
 * Includes comprehensive test cases from the Web Platform Tests (WPT) to ensure spec compliance.
 */
public class TokenQueueCssTest {

    @DisplayName("Should correctly escape CSS identifiers per CSSOM spec")
    @ParameterizedTest(name = "[{index}] input=''{1}'', expected=''{0}''")
    @MethodSource("cssIdentifierEscapeTestCases")
    void escapeCssIdentifier(String expected, String input) {
        assertEquals(expected, TokenQueue.escapeCssIdentifier(input));
    }

    /**
     * Provides test cases for CSS identifier escaping.
     * @see <a href="https://github.com/web-platform-tests/wpt/blob/328fa1c67bf5dfa6f24571d4c41dd10224b6d247/css/cssom/escape.html">WPT cssom/escape.html</a>
     */
    private static Stream<Arguments> cssIdentifierEscapeTestCases() {
        return Stream.of(
            // Null bytes are replaced with the replacement character
            Arguments.of("", ""),
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

            // Digits that are not at the start are not escaped
            Arguments.of("a0b", "a0b"), Arguments.of("a1b", "a1b"), Arguments.of("a2b", "a2b"),
            Arguments.of("a3b", "a3b"), Arguments.of("a4b", "a4b"), Arguments.of("a5b", "a5b"),
            Arguments.of("a6b", "a6b"), Arguments.of("a7b", "a7b"), Arguments.of("a8b", "a8b"),
            Arguments.of("a9b", "a9b"),

            // A hyphen followed by a digit at the start is escaped
            Arguments.of("-\\30 a", "-0a"), Arguments.of("-\\31 a", "-1a"), Arguments.of("-\\32 a", "-2a"),
            Arguments.of("-\\33 a", "-3a"), Arguments.of("-\\34 a", "-4a"), Arguments.of("-\\35 a", "-5a"),
            Arguments.of("-\\36 a", "-6a"), Arguments.of("-\\37 a", "-7a"), Arguments.of("-\\38 a", "-8a"),
            Arguments.of("-\\39 a", "-9a"),

            // Double dash prefix is not escaped
            Arguments.of("--a", "--a"),
            Arguments.of("--", "--"),

            // Control characters
            Arguments.of("\\1 \\2 \\1e \\1f ", "\u0001\u0002\u001E\u001F"),
            Arguments.of("\u0080\u002D\u005F\u00A9", "\u0080\u002D\u005F\u00A9"),
            Arguments.of("\\7f \u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F", "\u007F\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F"),
            Arguments.of("\u00A0\u00A1\u00A2", "\u00A0\u00A1\u00A2"),

            // Alphanumeric characters are not escaped
            Arguments.of("a0123456789b", "a0123456789b"),
            Arguments.of("abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz"),
            Arguments.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),

            // Backslashes are escaped
            Arguments.of("hello\\\\world", "hello\\world"),

            // Code points greater than U+0080 are preserved
            Arguments.of("hello\u1234world", "hello\u1234world"),

            // A single dash at the start is escaped
            Arguments.of("\\-", "-"),

            // Other characters are escaped
            Arguments.of("\\ \\!xy", "\u0020\u0021\u0078\u0079"),
            Arguments.of("one\\#two\\.three\\/four\\\\five", "one#two.three/four\\five"),

            // Astral symbols (surrogate pairs) are preserved
            Arguments.of("\uD834\uDF06", "\uD834\uDF06"), // U+1D306 TETRAGRAM FOR CENTRE

            // Lone surrogates are preserved
            Arguments.of("\uDF06", "\uDF06"),
            Arguments.of("\uD834", "\uD834"),

            // Hyphen not at the start is not escaped
            Arguments.of("-a", "-a")
        );
    }

    @DisplayName("Should correctly parse (consume) CSS identifiers")
    @ParameterizedTest(name = "[{index}] input=''{1}'', expected=''{0}''")
    @MethodSource("cssIdentifierConsumeTestCases")
    void consumeCssIdentifier(String expected, String cssToParse) {
        TokenQueue tq = new TokenQueue(cssToParse);
        String actual = tq.consumeCssIdentifier();
        assertEquals(expected, actual);
    }

    /**
     * Provides test cases for consuming CSS identifiers.
     * @see <a href="https://github.com/web-platform-tests/wpt/blob/36036fb5212a3fc15fc5750cecb1923ba4071668/dom/nodes/ParentNode-querySelector-escapes.html">WPT querySelector-escapes.html</a>
     * @see <a href="https://goo.gl/3Cxdov">Chromium CSS ident tests</a>
     */
    private static Stream<Arguments> cssIdentifierConsumeTestCases() {
        return Stream.of(
            // Hex digit escapes
            Arguments.of("0nextIsWhiteSpace", "\\30 nextIsWhiteSpace"),
            Arguments.of("0nextIsNotHexLetters", "\\30nextIsNotHexLetters"),
            Arguments.of("0connectHexMoreThan6Hex", "\\000030connectHexMoreThan6Hex"),
            Arguments.of("0spaceMoreThan6Hex", "\\000030 spaceMoreThan6Hex"),
            Arguments.of("aBMPRegular", "\\61 BMPRegular"),
            Arguments.of("\uD83D\uDD11nonBMP", "\\1f511 nonBMP"),
            Arguments.of("00continueEscapes", "\\30\\30 continueEscapes"),
            Arguments.of("00continueEscapes", "\\30 \\30 continueEscapes"),
            Arguments.of("continueEscapes00", "continueEscapes\\30 \\30 "),
            Arguments.of("continueEscapes00", "continueEscapes\\30\\30"),
            Arguments.of("1st", "\\31\r\nst"),
            Arguments.of("1", "\\31\r"),
            Arguments.of("1a", "\\31\ra"),
            Arguments.of("1", "\\031"), Arguments.of("1", "\\00031"), Arguments.of("1", "\\000031"),

            // Zero, surrogate, and out-of-range code points become replacement characters
            Arguments.of("zero\uFFFD", "zero\\0"),
            Arguments.of("zero\uFFFD", "zero\\000000"),
            Arguments.of("\uFFFDsurrogateFirst", "\\d83d surrogateFirst"),
            Arguments.of("surrogateSecond\uFFFd", "surrogateSecond\\dd11"),
            Arguments.of("surrogatePair\uFFFD\uFFFD", "surrogatePair\\d83d\\dd11"),
            Arguments.of("outOfRange\uFFFD", "outOfRange\\110000"),
            Arguments.of("outOfRange\uFFFD", "outOfRange\\ffffff"),

            // Other escapes
            Arguments.of(".comma", "\\.comma"),
            Arguments.of("-minus", "\\-minus"),
            Arguments.of("g", "\\g"),
            Arguments.of("a", "a\\\nb"), // escaped newline is consumed

            // Chromium tests
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

            // Unescaped identifiers
            Arguments.of("simple-ident", "simple-ident"),
            Arguments.of("testing123", "testing123"),
            Arguments.of("_underscore", "_underscore"),
            Arguments.of("-text", "-text"),
            Arguments.of("-m", "-\\6d"),
            Arguments.of("--abc", "--abc"),
            Arguments.of("--", "--"),
            Arguments.of("--11", "--11"),
            Arguments.of("---", "---"),
            Arguments.of("\u2003", "\u2003"), // em space
            Arguments.of("\u00A0", "\u00A0"), // non-breaking space
            Arguments.of("\u1234", "\u1234"),
            Arguments.of("\uD808\uDF45", "\uD808\uDF45"),

            // Null characters
            Arguments.of("\uFFFD", "\u0000"),
            Arguments.of("ab\uFFFDc", "ab\u0000c")
        );
    }

    /**
     * This test validates Jsoup's selector correctly handles regex metacharacters when they are quoted
     * using {@link Pattern#quote(String)}. It is not a direct test of {@link TokenQueue} but relies on
     * correct parsing. It would be better placed in a Selector test suite.
     */
    @DisplayName("Selector should handle quoted regex metacharacters in :matches")
    @ParameterizedTest(name = "[{index}] pattern=`{1}`")
    @MethodSource("quotedPatternTestCases")
    void selectWithQuotedRegexMetacharacters(String expectedText, String patternToQuote) {
        Document doc = Jsoup.parse("<div>\\) foo1</div><div>( foo2</div><div>1) foo3</div>");
        String selector = "div:matches(" + Pattern.quote(patternToQuote) + ")";
        String actualText = doc.select(selector).first().text();
        assertEquals(expectedText, actualText);
    }

    private static Stream<Arguments> quotedPatternTestCases() {
        return Stream.of(
            Arguments.of("\\) foo1", "\\)"),
            Arguments.of("( foo2", "("),
            Arguments.of("1) foo3", "1)")
        );
    }
}