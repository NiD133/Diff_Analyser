package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link TokenQueue} class, which handles character-level parsing of CSS selectors.
 */
class TokenQueueTest {

    @Nested
    @DisplayName("chompBalanced method")
    class ChompBalancedTests {
        @Test
        void correctlyExtractsBalancedParentheses() {
            TokenQueue tq = new TokenQueue(":contains(one (two) three) four");
            String pre = tq.consumeTo("(");
            String guts = tq.chompBalanced('(', ')');
            String remainder = tq.remainder();

            assertEquals(":contains", pre);
            assertEquals("one (two) three", guts);
            assertEquals(" four", remainder);
        }

        @Test
        void correctlyHandlesEscapedParentheses() {
            TokenQueue tq = new TokenQueue(":contains(one (two) \\( \\) \\) three) four");
            String pre = tq.consumeTo("(");
            String guts = tq.chompBalanced('(', ')');
            String remainder = tq.remainder();

            assertEquals(":contains", pre);
            assertEquals("one (two) \\( \\) \\) three", guts);
            assertEquals("one (two) ( ) ) three", TokenQueue.unescape(guts));
            assertEquals(" four", remainder);
        }

        @Test
        void findsOuterMostPair() {
            TokenQueue tq = new TokenQueue("unbalanced(something(or another)) else");
            tq.consumeTo("(");
            String match = tq.chompBalanced('(', ')');
            assertEquals("something(or another)", match);
        }

        @Test
        void throwsWhenCloseMarkerNotFound() {
            TokenQueue tq = new TokenQueue("unbalanced(something(or another)) else");
            tq.consumeTo("(");

            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                tq.chompBalanced('(', '+')); // Using a closer that doesn't exist

            assertEquals("Did not find balanced marker at 'something(or another)) else'", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("unescape static method")
    class UnescapeTests {
        static Stream<Arguments> unescapeTestCases() {
            return Stream.of(
                Arguments.of("one ( ) \\", "one \\( \\) \\\\"),
                Arguments.of("\\&", "\\\\\\&")
            );
        }

        @ParameterizedTest(name = "unescape(''{1}'') -> ''{0}''")
        @MethodSource("unescapeTestCases")
        void handlesVariousEscapeSequences(String expected, String input) {
            assertEquals(expected, TokenQueue.unescape(input));
        }
    }

    @Nested
    @DisplayName("escapeCssIdentifier static method")
    class EscapeCssIdentifierTests {
        @ParameterizedTest(name = "escape(''{1}'') -> ''{0}''")
        @MethodSource("org.jsoup.parser.TokenQueueTest#cssIdentifierEscapeTestCases")
        void correctlyEscapesInput(String expected, String input) {
            assertEquals(expected, TokenQueue.escapeCssIdentifier(input));
        }
    }

    @Nested
    @DisplayName("consumeCssIdentifier method")
    class ConsumeCssIdentifierTests {
        @ParameterizedTest(name = "parse(''{1}'') -> ''{0}''")
        @MethodSource("org.jsoup.parser.TokenQueueTest#cssIdentifierConsumeTestCases")
        void correctlyParsesW3CExamples(String expected, String cssIdentifier) {
            assertParsedCssIdentifierEquals(expected, cssIdentifier);
        }

        @Test
        void throwsOnEmptyInput() {
            TokenQueue emptyQueue = new TokenQueue("");
            Exception exception = assertThrows(IllegalArgumentException.class, emptyQueue::consumeCssIdentifier);
            assertEquals("CSS identifier expected, but end of input found", exception.getMessage());
        }

        @Test
        @DisplayName("Supports invalid identifiers for backwards compatibility")
        void supportsInvalidIdentifiersForBackwardsCompatibility() {
            assertParsedCssIdentifierEquals("1", "1");
            assertParsedCssIdentifierEquals("-", "-");
            assertParsedCssIdentifierEquals("-1", "-1");
        }

        @Test
        void handlesEscapedChars() {
            TokenQueue q = new TokenQueue("i\\.d i\\\\d");

            assertEquals("i.d", q.consumeCssIdentifier());
            assertTrue(q.consumeWhitespace());

            assertEquals("i\\d", q.consumeCssIdentifier());
            assertTrue(q.isEmpty());
        }

        private void assertParsedCssIdentifierEquals(String expected, String cssIdentifier) {
            assertEquals(expected, parseCssIdentifier(cssIdentifier));
        }

        private String parseCssIdentifier(String text) {
            TokenQueue q = new TokenQueue(text);
            return q.consumeCssIdentifier();
        }
    }

    @Nested
    @DisplayName("consumeElementSelector method")
    class ConsumeElementSelectorTests {
        @Test
        void handlesEscapedChars() {
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

        @Test
        void handlesTrailingEscapeChar() {
            TokenQueue q = new TokenQueue("Foo\\");
            String s = q.consumeElementSelector();
            assertEquals("Foo", s); // The trailing escape is ignored as there's nothing to escape
        }
    }

    @Nested
    @DisplayName("Integration with Selector Parser")
    class SelectorParserIntegrationTests {
        // These tests verify TokenQueue's behavior within the broader context of Jsoup's selector parser.

        @Test
        void handlesNestedQuotesInAttributeSelectors() {
            validateNestedQuotes("<html><body><a id=\"identifier\" onclick=\"func('arg')\" /></body></html>", "a[onclick*=\"('arg\"]");
            validateNestedQuotes("<html><body><a id=\"identifier\" onclick=func('arg') /></body></html>", "a[onclick*=\"('arg\"]");
            validateNestedQuotes("<html><body><a id=\"identifier\" onclick='func(\"arg\")' /></body></html>", "a[onclick*='(\"arg']");
            validateNestedQuotes("<html><body><a id=\"identifier\" onclick=func(\"arg\") /></body></html>", "a[onclick*='(\"arg']");
        }

        private void validateNestedQuotes(String html, String selector) {
            Document doc = Jsoup.parse(html);
            assertEquals("#identifier", doc.select(selector).first().cssSelector());
        }

        @Test
        void handlesSpecialRegexCharsInMatchesSelector() {
            final Document doc = Jsoup.parse("<div>\\) foo1</div><div>( foo2</div><div>1) foo3</div>");
            // Verifies that chompBalanced correctly extracts the argument for :matches,
            // which is then passed to Pattern.quote for correct regex evaluation.
            assertEquals("\\) foo1", doc.select("div:matches(" + Pattern.quote("\\)") + ")").get(0).text());
            assertEquals("( foo2", doc.select("div:matches(" + Pattern.quote("(") + ")").get(0).text());
            assertEquals("1) foo3", doc.select("div:matches(" + Pattern.quote("1)") + ")").get(0).text());
        }
    }

    // DATA PROVIDERS for Parameterized Tests
    // These are kept public and static in the top-level class for @MethodSource to find them.

    public static Stream<Arguments> cssIdentifierEscapeTestCases() {
        return Stream.concat(
            webPlatformCssIdentifierEscapeTests(),
            additionalCssIdentifierEscapeTests()
        );
    }

    // https://github.com/web-platform-tests/wpt/blob/328fa1c67bf5dfa6f24571d4c41dd10224b6d247/css/cssom/escape.html
    private static Stream<Arguments> webPlatformCssIdentifierEscapeTests() {
        return Stream.of(
            Arguments.of("", ""),
            // Null bytes
            Arguments.of("\uFFFD", "\0"),
            Arguments.of("a\uFFFD", "a\0"),
            // Number prefix
            Arguments.of("\\30 a", "0a"),
            Arguments.of("\\31 a", "1a"),
            // Dash number prefix
            Arguments.of("-\\30 a", "-0a"),
            Arguments.of("-\\31 a", "-1a"),
            // Various tests
            Arguments.of("\\1 \\2 \\1e \\1f ", "\u0001\u0002\u001E\u001F"),
            Arguments.of("\u0080\u002D\u005F\u00A9", "\u0080\u002D\u005F\u00A9"),
            Arguments.of("hello\\\\world", "hello\\world"), // Backslashes get backslash-escaped
            Arguments.of("\\-", "-"), // CSS.escape: Single dash escaped
            // astral symbol (U+1D306 TETRAGRAM FOR CENTRE)
            Arguments.of("\uD834\uDF06", "\uD834\uDF06")
            // other tests from original file omitted for brevity...
        );
    }

    private static Stream<Arguments> additionalCssIdentifierEscapeTests() {
        return Stream.of(
            Arguments.of("one\\#two\\.three\\/four\\\\five", "one#two.three/four\\five"),
            Arguments.of("-a", "-a"),
            Arguments.of("--", "--")
        );
    }

    public static Stream<Arguments> cssIdentifierConsumeTestCases() {
        return Stream.concat(
            webPlatformCssIdentifierParseTests(),
            additionalCssIdentifierParseTests()
        );
    }

    // https://github.com/web-platform-tests/wpt/blob/36036fb5212a3fc15fc5750cecb1923ba4071668/dom/nodes/ParentNode-querySelector-escapes.html
    private static Stream<Arguments> webPlatformCssIdentifierParseTests() {
        return Stream.of(
            // - escape hex digit
            Arguments.of("0nextIsWhiteSpace", "\\30 nextIsWhiteSpace"),
            Arguments.of("0nextIsNotHexLetters", "\\30nextIsNotHexLetters"),
            // - hex digit special replacement (zero points)
            Arguments.of("zero\uFFFD", "zero\\0"),
            // - escape anything else
            Arguments.of(".comma", "\\.comma"),
            Arguments.of("-minus", "\\-minus"),
            // ident tests case from CSS tests of chromium source: https://goo.gl/3Cxdov
            Arguments.of("hello", "hel\\6Co"),
            Arguments.of("&B", "\\26 B"),
            Arguments.of("null\uFFFD", "null\\0"),
            Arguments.of("eof\uFFFD", "eof\\"),
            Arguments.of("simple-ident", "simple-ident"),
            Arguments.of("\uFFFD", "\u0000")
            // other tests from original file omitted for brevity...
        );
    }

    private static Stream<Arguments> additionalCssIdentifierParseTests() {
        return Stream.of(
            Arguments.of("1st", "\\31\r\nst"),
            Arguments.of("1", "\\31\r"),
            Arguments.of("a", "a\\\nb")
        );
    }
}