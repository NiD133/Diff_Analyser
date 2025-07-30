package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TokenQueue class.
 */
public class TokenQueueTest {

    // Test chompBalanced method with balanced parentheses
    @Test
    public void testChompBalanced() {
        TokenQueue tokenQueue = new TokenQueue(":contains(one (two) three) four");
        String prefix = tokenQueue.consumeTo("(");
        String balancedContent = tokenQueue.chompBalanced('(', ')');
        String remainingContent = tokenQueue.remainder();

        assertEquals(":contains", prefix);
        assertEquals("one (two) three", balancedContent);
        assertEquals(" four", remainingContent);
    }

    // Test chompBalanced method with escaped balanced parentheses
    @Test
    public void testChompEscapedBalanced() {
        TokenQueue tokenQueue = new TokenQueue(":contains(one (two) \\( \\) \\) three) four");
        String prefix = tokenQueue.consumeTo("(");
        String balancedContent = tokenQueue.chompBalanced('(', ')');
        String remainingContent = tokenQueue.remainder();

        assertEquals(":contains", prefix);
        assertEquals("one (two) \\( \\) \\) three", balancedContent);
        assertEquals("one (two) ( ) ) three", TokenQueue.unescape(balancedContent));
        assertEquals(" four", remainingContent);
    }

    // Test chompBalanced method to match as much as possible
    @Test
    public void testChompBalancedMatchesAsMuchAsPossible() {
        TokenQueue tokenQueue = new TokenQueue("unbalanced(something(or another)) else");
        tokenQueue.consumeTo("(");
        String matchedContent = tokenQueue.chompBalanced('(', ')');
        assertEquals("something(or another)", matchedContent);
    }

    // Test unescape method
    @Test
    public void testUnescape() {
        assertEquals("one ( ) \\", TokenQueue.unescape("one \\( \\) \\\\"));
    }

    // Test unescape method with different input
    @Test
    public void testUnescapeWithDifferentInput() {
        assertEquals("\\&", TokenQueue.unescape("\\\\\\&"));
    }

    // Parameterized test for escapeCssIdentifier method
    @ParameterizedTest
    @MethodSource("provideEscapeCssIdentifierParameters")
    public void testEscapeCssIdentifier(String expected, String input) {
        assertEquals(expected, TokenQueue.escapeCssIdentifier(input));
    }

    // Provide parameters for escapeCssIdentifier tests
    private static Stream<Arguments> provideEscapeCssIdentifierParameters() {
        return Stream.concat(
            escapeCssIdentifier_WebPlatformTestParameters(),
            escapeCssIdentifier_additionalParameters()
        );
    }

    // Web Platform Test parameters for escapeCssIdentifier
    private static Stream<Arguments> escapeCssIdentifier_WebPlatformTestParameters() {
        return Stream.of(
            Arguments.of("", ""),
            Arguments.of("\uFFFD", "\0"),
            Arguments.of("a\uFFFD", "a\0"),
            Arguments.of("\uFFFDb", "\0b"),
            Arguments.of("a\uFFFDb", "a\0b"),
            Arguments.of("\uFFFD", "\uFFFD"),
            Arguments.of("a\uFFFD", "a\uFFFD"),
            Arguments.of("\uFFFDb", "\uFFFDb"),
            Arguments.of("a\uFFFDb", "a\uFFFDb"),
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
            Arguments.of("a0b", "a0b"),
            Arguments.of("a1b", "a1b"),
            Arguments.of("a2b", "a2b"),
            Arguments.of("a3b", "a3b"),
            Arguments.of("a4b", "a4b"),
            Arguments.of("a5b", "a5b"),
            Arguments.of("a6b", "a6b"),
            Arguments.of("a7b", "a7b"),
            Arguments.of("a8b", "a8b"),
            Arguments.of("a9b", "a9b"),
            Arguments.of("-\\30 a", "-0a"),
            Arguments.of("-\\31 a", "-1a"),
            Arguments.of("-\\32 a", "-2a"),
            Arguments.of("-\\33 a", "-3a"),
            Arguments.of("-\\34 a", "-4a"),
            Arguments.of("-\\35 a", "-5a"),
            Arguments.of("-\\36 a", "-6a"),
            Arguments.of("-\\37 a", "-7a"),
            Arguments.of("-\\38 a", "-8a"),
            Arguments.of("-\\39 a", "-9a"),
            Arguments.of("--a", "--a"),
            Arguments.of("\\1 \\2 \\1e \\1f ", "\u0001\u0002\u001E\u001F"),
            Arguments.of("\u0080\u002D\u005F\u00A9", "\u0080\u002D\u005F\u00A9"),
            Arguments.of("\\7f \u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F", "\u007F\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F"),
            Arguments.of("\u00A0\u00A1\u00A2", "\u00A0\u00A1\u00A2"),
            Arguments.of("a0123456789b", "a0123456789b"),
            Arguments.of("abcdefghijklmnopqrstuvwxyz", "abcdefghijklmnopqrstuvwxyz"),
            Arguments.of("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
            Arguments.of("hello\\\\world", "hello\\world"),
            Arguments.of("hello\u1234world", "hello\u1234world"),
            Arguments.of("\\-", "-"),
            Arguments.of("\\ \\!xy", "\u0020\u0021\u0078\u0079"),
            Arguments.of("\uD834\uDF06", "\uD834\uDF06"),
            Arguments.of("\uDF06", "\uDF06"),
            Arguments.of("\uD834", "\uD834")
        );
    }

    // Additional parameters for escapeCssIdentifier
    private static Stream<Arguments> escapeCssIdentifier_additionalParameters() {
        return Stream.of(
            Arguments.of("one\\#two\\.three\\/four\\\\five", "one#two.three/four\\five"),
            Arguments.of("-a", "-a"),
            Arguments.of("--", "--")
        );
    }

    // Test nested quotes in HTML and CSS selectors
    @Test
    public void testNestedQuotes() {
        validateNestedQuotes("<html><body><a id=\"identifier\" onclick=\"func('arg')\" /></body></html>", "a[onclick*=\"('arg\"]");
        validateNestedQuotes("<html><body><a id=\"identifier\" onclick=func('arg') /></body></html>", "a[onclick*=\"('arg\"]");
        validateNestedQuotes("<html><body><a id=\"identifier\" onclick='func(\"arg\")' /></body></html>", "a[onclick*='(\"arg']");
        validateNestedQuotes("<html><body><a id=\"identifier\" onclick=func(\"arg\") /></body></html>", "a[onclick*='(\"arg']");
    }

    // Helper method to validate nested quotes
    private static void validateNestedQuotes(String html, String selector) {
        assertEquals("#identifier", Jsoup.parse(html).select(selector).first().cssSelector());
    }

    // Test chompBalanced method throwing IllegalArgumentException
    @Test
    public void testChompBalancedThrowIllegalArgumentException() {
        try {
            TokenQueue tokenQueue = new TokenQueue("unbalanced(something(or another)) else");
            tokenQueue.consumeTo("(");
            tokenQueue.chompBalanced('(', '+');
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals("Did not find balanced marker at 'something(or another)) else'", expected.getMessage());
        }
    }

    // Test quoted pattern matching
    @Test
    public void testQuotedPattern() {
        final Document doc = Jsoup.parse("<div>\\) foo1</div><div>( foo2</div><div>1) foo3</div>");
        assertEquals("\\) foo1", doc.select("div:matches(" + Pattern.quote("\\)") + ")").get(0).childNode(0).toString());
        assertEquals("( foo2", doc.select("div:matches(" + Pattern.quote("(") + ")").get(0).childNode(0).toString());
        assertEquals("1) foo3", doc.select("div:matches(" + Pattern.quote("1)") + ")").get(0).childNode(0).toString());
    }

    // Test consuming escaped tags
    @Test
    public void testConsumeEscapedTag() {
        TokenQueue tokenQueue = new TokenQueue("p\\\\p p\\.p p\\:p p\\!p");

        assertEquals("p\\p", tokenQueue.consumeElementSelector());
        assertTrue(tokenQueue.consumeWhitespace());

        assertEquals("p.p", tokenQueue.consumeElementSelector());
        assertTrue(tokenQueue.consumeWhitespace());

        assertEquals("p:p", tokenQueue.consumeElementSelector());
        assertTrue(tokenQueue.consumeWhitespace());

        assertEquals("p!p", tokenQueue.consumeElementSelector());
        assertTrue(tokenQueue.isEmpty());
    }

    // Test consuming escaped IDs
    @Test
    public void testConsumeEscapedId() {
        TokenQueue tokenQueue = new TokenQueue("i\\.d i\\\\d");

        assertEquals("i.d", tokenQueue.consumeCssIdentifier());
        assertTrue(tokenQueue.consumeWhitespace());

        assertEquals("i\\d", tokenQueue.consumeCssIdentifier());
        assertTrue(tokenQueue.isEmpty());
    }

    // Test escape at end of file
    @Test
    void testEscapeAtEof() {
        TokenQueue tokenQueue = new TokenQueue("Foo\\");
        String result = tokenQueue.consumeElementSelector();
        assertEquals("Foo", result); // no escape, no eof. Just straight up Foo.
    }

    // Parameterized test for consumeCssIdentifier method
    @ParameterizedTest
    @MethodSource("provideCssIdentifierParameters")
    void testConsumeCssIdentifier_WebPlatformTests(String expected, String cssIdentifier) {
        assertParsedCssIdentifierEquals(expected, cssIdentifier);
    }

    // Provide parameters for consumeCssIdentifier tests
    private static Stream<Arguments> provideCssIdentifierParameters() {
        return Stream.concat(
            cssIdentifiers(),
            cssAdditionalIdentifiers()
        );
    }

    // Web Platform Test parameters for consumeCssIdentifier
    private static Stream<Arguments> cssIdentifiers() {
        return Stream.of(
            Arguments.of("0nextIsWhiteSpace", "\\30 nextIsWhiteSpace"),
            Arguments.of("0nextIsNotHexLetters", "\\30nextIsNotHexLetters"),
            Arguments.of("0connectHexMoreThan6Hex", "\\000030connectHexMoreThan6Hex"),
            Arguments.of("0spaceMoreThan6Hex", "\\000030 spaceMoreThan6Hex"),
            Arguments.of("zero\uFFFD", "zero\\0"),
            Arguments.of("zero\uFFFD", "zero\\000000"),
            Arguments.of("\uFFFDsurrogateFirst", "\\d83d surrogateFirst"),
            Arguments.of("surrogateSecond\uFFFd", "surrogateSecond\\dd11"),
            Arguments.of("surrogatePair\uFFFD\uFFFD", "surrogatePair\\d83d\\dd11"),
            Arguments.of("outOfRange\uFFFD", "outOfRange\\110000"),
            Arguments.of("outOfRange\uFFFD", "outOfRange\\110030"),
            Arguments.of("outOfRange\uFFFD", "outOfRange\\555555"),
            Arguments.of("outOfRange\uFFFD", "outOfRange\\ffffff"),
            Arguments.of(".comma", "\\.comma"),
            Arguments.of("-minus", "\\-minus"),
            Arguments.of("g", "\\g"),
            Arguments.of("aBMPRegular", "\\61 BMPRegular"),
            Arguments.of("\uD83D\uDD11nonBMP", "\\1f511 nonBMP"),
            Arguments.of("00continueEscapes", "\\30\\30 continueEscapes"),
            Arguments.of("00continueEscapes", "\\30 \\30 continueEscapes"),
            Arguments.of("continueEscapes00", "continueEscapes\\30 \\30 "),
            Arguments.of("continueEscapes00", "continueEscapes\\30 \\30"),
            Arguments.of("continueEscapes00", "continueEscapes\\30\\30 "),
            Arguments.of("continueEscapes00", "continueEscapes\\30\\30"),
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

    // Additional parameters for consumeCssIdentifier
    private static Stream<Arguments> cssAdditionalIdentifiers() {
        return Stream.of(
            Arguments.of("1st", "\\31\r\nst"),
            Arguments.of("1", "\\31\r"),
            Arguments.of("1a", "\\31\ra"),
            Arguments.of("1", "\\031"),
            Arguments.of("1", "\\0031"),
            Arguments.of("1", "\\00031"),
            Arguments.of("1", "\\000031"),
            Arguments.of("1", "\\000031"),
            Arguments.of("a", "a\\\nb")
        );
    }

    // Test consuming CSS identifier with empty input
    @Test
    void testConsumeCssIdentifierWithEmptyInput() {
        TokenQueue emptyQueue = new TokenQueue("");
        Exception exception = assertThrows(IllegalArgumentException.class, emptyQueue::consumeCssIdentifier);
        assertEquals("CSS identifier expected, but end of input found", exception.getMessage());
    }

    // Test consuming CSS identifier with invalid but supported input for backwards compatibility
    @Test
    public void testConsumeCssIdentifier_invalidButSupportedForBackwardsCompatibility() {
        assertParsedCssIdentifierEquals("1", "1");
        assertParsedCssIdentifierEquals("-", "-");
        assertParsedCssIdentifierEquals("-1", "-1");
    }

    // Helper method to parse CSS identifier
    private static String parseCssIdentifier(String text) {
        TokenQueue tokenQueue = new TokenQueue(text);
        return tokenQueue.consumeCssIdentifier();
    }

    // Helper method to assert parsed CSS identifier equals expected value
    private void assertParsedCssIdentifierEquals(String expected, String cssIdentifier) {
        assertEquals(expected, parseCssIdentifier(cssIdentifier));
    }
}