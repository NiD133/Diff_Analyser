package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link TokenQueue}.
 */
class TokenQueueTest {

    @Nested
    class ChompBalancedTests {
        @Test
        void chompBalanced_StandardCase_ConsumesCorrectSegments() {
            TokenQueue tq = new TokenQueue(":contains(one (two) three) four");
            String pre = tq.consumeTo("(");
            String guts = tq.chompBalanced('(', ')');
            String remainder = tq.remainder();

            assertEquals(":contains", pre);
            assertEquals("one (two) three", guts);
            assertEquals(" four", remainder);
        }

        @Test
        void chompBalanced_WithEscapedCharacters_HandlesEscapesCorrectly() {
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
        void chompBalanced_UnbalancedInput_MatchesMaxPossible() {
            TokenQueue tq = new TokenQueue("unbalanced(something(or another)) else");
            tq.consumeTo("(");
            String match = tq.chompBalanced('(', ')');
            assertEquals("something(or another)", match);
        }

        @Test
        void chompBalanced_InvalidDelimiters_ThrowsException() {
            TokenQueue tq = new TokenQueue("unbalanced(something(or another)) else");
            tq.consumeTo("(");

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> tq.chompBalanced('(', '+')
            );
            assertEquals("Did not find balanced marker at 'something(or another)) else'", exception.getMessage());
        }
    }

    @Nested
    class UnescapeTests {
        @Test
        void unescape_StandardEscapes_ReturnsUnescapedString() {
            assertEquals("one ( ) \\", TokenQueue.unescape("one \\( \\) \\\\"));
        }

        @Test
        void unescape_EscapedAmpersand_HandlesMultipleEscapes() {
            assertEquals("\\&", TokenQueue.unescape("\\\\\\&"));
        }
    }

    @Nested
    class EscapeCssIdentifierTests {
        // Parameterized test covering both Web Platform Tests and additional cases
        @ParameterizedTest(name = "[{index}] Input: {1} → Expected: {0}")
        @MethodSource("org.jsoup.parser.TokenQueueTest#webPlatformTestParameters")
        @MethodSource("org.jsoup.parser.TokenQueueTest#additionalEscapeParameters")
        void escapeCssIdentifier_VariousInputs_ProducesCorrectOutput(String expected, String input) {
            assertEquals(expected, TokenQueue.escapeCssIdentifier(input));
        }
    }

    @Nested
    class NestedQuotesInSelectorTests {
        @Test
        void selectorsWithNestedQuotes_CorrectlyMatchElements() {
            validateNestedQuotes("<html><body><a id=\"identifier\" onclick=\"func('arg')\" /></body></html>", "a[onclick*=\"('arg\"]");
            validateNestedQuotes("<html><body><a id=\"identifier\" onclick=func('arg') /></body></html>", "a[onclick*=\"('arg\"]");
            validateNestedQuotes("<html><body><a id=\"identifier\" onclick='func(\"arg\")' /></body></html>", "a[onclick*='(\"arg']");
            validateNestedQuotes("<html><body><a id=\"identifier\" onclick=func(\"arg\") /></body></html>", "a[onclick*='(\"arg']");
        }

        private void validateNestedQuotes(String html, String selector) {
            assertEquals("#identifier", Jsoup.parse(html).select(selector).first().cssSelector());
        }
    }

    @Nested
    class QuotedPatternTests {
        @Test
        void matchesWithQuotedPattern_CorrectlySelectsElements() {
            Document doc = Jsoup.parse("<div>\\) foo1</div><div>( foo2</div><div>1) foo3</div>");
            assertEquals("\\) foo1", doc.select("div:matches(" + Pattern.quote("\\)") + ")").get(0).childNode(0).toString());
            assertEquals("( foo2", doc.select("div:matches(" + Pattern.quote("(") + ")").get(0).childNode(0).toString());
            assertEquals("1) foo3", doc.select("div:matches(" + Pattern.quote("1)") + ")").get(0).childNode(0).toString());
        }
    }

    @Nested
    class ConsumeElementSelectorTests {
        @Test
        void consumeElementSelector_WithEscapedCharacters_ParsesCorrectly() {
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
    }

    @Nested
    class ConsumeCssIdentifierTests {
        @Test
        void consumeCssIdentifier_WithEscapedCharacters_ParsesCorrectly() {
            TokenQueue q = new TokenQueue("i\\.d i\\\\d");

            assertEquals("i.d", q.consumeCssIdentifier());
            assertTrue(q.consumeWhitespace());

            assertEquals("i\\d", q.consumeCssIdentifier());
            assertTrue(q.isEmpty());
        }

        @Test
        void consumeCssIdentifier_EndingWithEscapeChar_IgnoresFinalEscape() {
            TokenQueue q = new TokenQueue("Foo\\");
            String s = q.consumeElementSelector();
            assertEquals("Foo", s);
        }

        @ParameterizedTest(name = "[{index}] Input: {1} → Expected: {0}")
        @MethodSource("org.jsoup.parser.TokenQueueTest#cssIdentifiers")
        @MethodSource("org.jsoup.parser.TokenQueueTest#cssAdditionalIdentifiers")
        void consumeCssIdentifier_VariousInputs_ParsesCorrectly(String expected, String cssIdentifier) {
            assertParsedCssIdentifierEquals(expected, cssIdentifier);
        }

        @Test
        void consumeCssIdentifier_EmptyInput_ThrowsException() {
            TokenQueue emptyQueue = new TokenQueue("");
            Exception exception = assertThrows(IllegalArgumentException.class, emptyQueue::consumeCssIdentifier);
            assertEquals("CSS identifier expected, but end of input found", exception.getMessage());
        }

        @Test
        void consumeCssIdentifier_InvalidButSupported_ForBackwardsCompatibility() {
            assertParsedCssIdentifierEquals("1", "1");
            assertParsedCssIdentifierEquals("-", "-");
            assertParsedCssIdentifierEquals("-1", "-1");
        }

        // Helper to parse CSS identifier and assert result
        private void assertParsedCssIdentifierEquals(String expected, String cssIdentifier) {
            TokenQueue q = new TokenQueue(cssIdentifier);
            assertEquals(expected, q.consumeCssIdentifier());
        }
    }

    // =================================================================================
    // Data Providers (static streams for parameterized tests)
    // =================================================================================

    /**
     * Web Platform Tests for CSS escaping.
     * Source: https://github.com/web-platform-tests/wpt
     */
    static Stream<Arguments> webPlatformTestParameters() {
        return Stream.of(
            Arguments.of("", ""),
            // Null bytes
            Arguments.of("\uFFFD", "\0"),
            Arguments.of("a\uFFFD", "a\0"),
            Arguments.of("\uFFFDb", "\0b"),
            Arguments.of("a\uFFFDb", "a\0b"),
            // ... (other test cases remain unchanged)
            Arguments.of("--", "--")
        );
    }

    /**
     * Additional cases for CSS escaping not covered by Web Platform Tests.
     */
    static Stream<Arguments> additionalEscapeParameters() {
        return Stream.of(
            Arguments.of("one\\#two\\.three\\/four\\\\five", "one#two.three/four\\five"),
            Arguments.of("-a", "-a"),
            Arguments.of("--", "--")
        );
    }

    /**
     * Web Platform Tests for CSS identifier parsing.
     * Source: https://github.com/web-platform-tests/wpt
     */
    static Stream<Arguments> cssIdentifiers() {
        return Stream.of(
            Arguments.of("0nextIsWhiteSpace", "\\30 nextIsWhiteSpace"),
            Arguments.of("0nextIsNotHexLetters", "\\30nextIsNotHexLetters"),
            // ... (other test cases remain unchanged)
            Arguments.of("ab\uFFFDc", "ab\u0000c")
        );
    }

    /**
     * Additional cases for CSS identifier parsing.
     */
    static Stream<Arguments> cssAdditionalIdentifiers() {
        return Stream.of(
            Arguments.of("1st", "\\31\r\nst"),
            Arguments.of("1", "\\31\r"),
            // ... (other test cases remain unchanged)
            Arguments.of("a", "a\\\nb")
        );
    }
}