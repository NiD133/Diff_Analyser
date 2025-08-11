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
 * Tests for TokenQueue - a character reader with helpers for parsing CSS selectors.
 */
public class TokenQueueTest {

    // ========== Balanced Chomping Tests ==========
    
    @Test 
    public void chompBalanced_shouldExtractContentBetweenMatchingParentheses() {
        // Given: A token queue with nested parentheses
        TokenQueue tokenQueue = new TokenQueue(":contains(one (two) three) four");
        
        // When: We consume parts step by step
        String beforeParentheses = tokenQueue.consumeTo("(");
        String contentInsideParentheses = tokenQueue.chompBalanced('(', ')');
        String remainingContent = tokenQueue.remainder();

        // Then: Each part should be correctly extracted
        assertEquals(":contains", beforeParentheses);
        assertEquals("one (two) three", contentInsideParentheses);
        assertEquals(" four", remainingContent);
    }

    @Test 
    public void chompBalanced_shouldHandleEscapedCharacters() {
        // Given: A token queue with escaped parentheses
        TokenQueue tokenQueue = new TokenQueue(":contains(one (two) \\( \\) \\) three) four");
        
        // When: We consume and process the content
        String beforeParentheses = tokenQueue.consumeTo("(");
        String escapedContent = tokenQueue.chompBalanced('(', ')');
        String unescapedContent = TokenQueue.unescape(escapedContent);
        String remainingContent = tokenQueue.remainder();

        // Then: Escaped characters should be preserved and unescapable
        assertEquals(":contains", beforeParentheses);
        assertEquals("one (two) \\( \\) \\) three", escapedContent);
        assertEquals("one (two) ( ) ) three", unescapedContent);
        assertEquals(" four", remainingContent);
    }

    @Test 
    public void chompBalanced_shouldMatchAsMuchAsPossible() {
        // Given: An unbalanced string with nested parentheses
        TokenQueue tokenQueue = new TokenQueue("unbalanced(something(or another)) else");
        
        // When: We try to chomp balanced content
        tokenQueue.consumeTo("(");
        String matchedContent = tokenQueue.chompBalanced('(', ')');
        
        // Then: It should match the longest balanced sequence
        assertEquals("something(or another)", matchedContent);
    }

    @Test
    public void chompBalanced_shouldThrowExceptionForUnbalancedMarkers() {
        // Given: A token queue with mismatched brackets
        TokenQueue tokenQueue = new TokenQueue("unbalanced(something(or another)) else");
        tokenQueue.consumeTo("(");
        
        // When & Then: Attempting to balance with wrong closing character should throw exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> tokenQueue.chompBalanced('(', '+'));
        
        assertEquals("Did not find balanced marker at 'something(or another)) else'", 
            exception.getMessage());
    }

    // ========== Unescaping Tests ==========
    
    @Test 
    public void unescape_shouldConvertEscapedCharacters() {
        // Given: A string with escaped characters
        String escapedString = "one \\( \\) \\\\";
        
        // When: We unescape it
        String result = TokenQueue.unescape(escapedString);
        
        // Then: Escaped characters should be converted to their literal form
        assertEquals("one ( ) \\", result);
    }

    @Test 
    public void unescape_shouldHandleComplexEscapeSequences() {
        // Given: A string with multiple escape sequences
        String complexEscapedString = "\\\\\\&";
        
        // When: We unescape it
        String result = TokenQueue.unescape(complexEscapedString);
        
        // Then: The result should be properly unescaped
        assertEquals("\\&", result);
    }

    // ========== CSS Identifier Escaping Tests ==========
    
    @ParameterizedTest
    @MethodSource("getWebPlatformTestCases")
    @MethodSource("getAdditionalTestCases")
    public void escapeCssIdentifier_shouldEscapeAccordingToWebStandards(String expectedOutput, String input) {
        // When: We escape a CSS identifier
        String result = TokenQueue.escapeCssIdentifier(input);
        
        // Then: It should match the expected web platform standard output
        assertEquals(expectedOutput, result);
    }

    /**
     * Test cases from Web Platform Tests for CSS identifier escaping.
     * Source: https://github.com/web-platform-tests/wpt/blob/328fa1c67bf5dfa6f24571d4c41dd10224b6d247/css/cssom/escape.html
     */
    private static Stream<Arguments> getWebPlatformTestCases() {
        return Stream.of(
            // Empty string
            Arguments.of("", ""),

            // Null byte handling - should be replaced with replacement character
            Arguments.of("\uFFFD", "\0"),
            Arguments.of("a\uFFFD", "a\0"),
            Arguments.of("\uFFFDb", "\0b"),
            Arguments.of("a\uFFFDb", "a\0b"),

            // Replacement character handling
            Arguments.of("\uFFFD", "\uFFFD"),
            Arguments.of("a\uFFFD", "a\uFFFD"),

            // Numbers at start need escaping (CSS identifiers can't start with digits)
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

            // Numbers in middle don't need escaping
            Arguments.of("a0b", "a0b"),
            Arguments.of("a1b", "a1b"),
            Arguments.of("a2b", "a2b"),

            // Dash followed by number needs escaping
            Arguments.of("-\\30 a", "-0a"),
            Arguments.of("-\\31 a", "-1a"),
            Arguments.of("-\\32 a", "-2a"),

            // Double dash is valid
            Arguments.of("--a", "--a"),

            // Control characters need escaping
            Arguments.of("\\1 \\2 \\1e \\1f ", "\u0001\u0002\u001E\u001F"),
            
            // High Unicode characters are preserved
            Arguments.of("\u0080\u002D\u005F\u00A9", "\u0080\u002D\u005F\u00A9"),
            
            // Backslashes need escaping
            Arguments.of("hello\\\\world", "hello\\world"),
            
            // Special CSS characters need escaping
            Arguments.of("\\-", "-"),
            Arguments.of("\\ \\!xy", "\u0020\u0021\u0078\u0079"),

            // Astral symbols (surrogate pairs) are preserved
            Arguments.of("\uD834\uDF06", "\uD834\uDF06"),
            
            // Lone surrogates are preserved
            Arguments.of("\uDF06", "\uDF06"),
            Arguments.of("\uD834", "\uD834")
        );
    }

    private static Stream<Arguments> getAdditionalTestCases() {
        return Stream.of(
            // CSS special characters that need escaping
            Arguments.of("one\\#two\\.three\\/four\\\\five", "one#two.three/four\\five"),
            Arguments.of("-a", "-a"),
            Arguments.of("--", "--")
        );
    }

    // ========== Nested Quotes in HTML Tests ==========
    
    @Test 
    public void testNestedQuotes_shouldHandleQuotesInAttributes() {
        // Test various combinations of nested quotes in HTML attributes
        validateHtmlWithNestedQuotes(
            "<html><body><a id=\"identifier\" onclick=\"func('arg')\" /></body></html>", 
            "a[onclick*=\"('arg\"]"
        );
        
        validateHtmlWithNestedQuotes(
            "<html><body><a id=\"identifier\" onclick=func('arg') /></body></html>", 
            "a[onclick*=\"('arg\"]"
        );
        
        validateHtmlWithNestedQuotes(
            "<html><body><a id=\"identifier\" onclick='func(\"arg\")' /></body></html>", 
            "a[onclick*='(\"arg']"
        );
        
        validateHtmlWithNestedQuotes(
            "<html><body><a id=\"identifier\" onclick=func(\"arg\") /></body></html>", 
            "a[onclick*='(\"arg']"
        );
    }

    private static void validateHtmlWithNestedQuotes(String html, String selector) {
        // When: We parse HTML and select with the given selector
        String result = Jsoup.parse(html).select(selector).first().cssSelector();
        
        // Then: It should correctly identify the element
        assertEquals("#identifier", result);
    }

    // ========== Pattern Matching Tests ==========
    
    @Test
    public void testQuotedPattern_shouldHandleSpecialCharactersInRegex() {
        // Given: HTML with special regex characters in text content
        Document document = Jsoup.parse(
            "<div>\\) foo1</div><div>( foo2</div><div>1) foo3</div>"
        );
        
        // When & Then: We should be able to match these special characters using Pattern.quote
        assertEquals("\\) foo1", 
            document.select("div:matches(" + Pattern.quote("\\)") + ")")
                    .get(0).childNode(0).toString());
                    
        assertEquals("( foo2", 
            document.select("div:matches(" + Pattern.quote("(") + ")")
                    .get(0).childNode(0).toString());
                    
        assertEquals("1) foo3", 
            document.select("div:matches(" + Pattern.quote("1)") + ")")
                    .get(0).childNode(0).toString());
    }

    // ========== Element Selector Consumption Tests ==========
    
    @Test 
    public void consumeElementSelector_shouldHandleEscapedCharacters() {
        // Given: A token queue with escaped element selectors
        TokenQueue queue = new TokenQueue("p\\\\p p\\.p p\\:p p\\!p");

        // When & Then: Each escaped selector should be properly consumed
        assertEquals("p\\p", queue.consumeElementSelector());
        assertTrue(queue.consumeWhitespace());

        assertEquals("p.p", queue.consumeElementSelector());
        assertTrue(queue.consumeWhitespace());

        assertEquals("p:p", queue.consumeElementSelector());
        assertTrue(queue.consumeWhitespace());

        assertEquals("p!p", queue.consumeElementSelector());
        assertTrue(queue.isEmpty());
    }

    // ========== CSS Identifier Consumption Tests ==========
    
    @Test 
    public void consumeCssIdentifier_shouldHandleEscapedIdentifiers() {
        // Given: A token queue with escaped CSS identifiers
        TokenQueue queue = new TokenQueue("i\\.d i\\\\d");

        // When & Then: Escaped identifiers should be properly unescaped
        assertEquals("i.d", queue.consumeCssIdentifier());
        assertTrue(queue.consumeWhitespace());

        assertEquals("i\\d", queue.consumeCssIdentifier());
        assertTrue(queue.isEmpty());
    }

    @Test 
    public void consumeCssIdentifier_shouldHandleEscapeAtEndOfInput() {
        // Given: A token queue ending with an escape character
        TokenQueue queue = new TokenQueue("Foo\\");
        
        // When: We consume the element selector
        String result = queue.consumeElementSelector();
        
        // Then: The trailing escape should be ignored
        assertEquals("Foo", result);
    }

    @ParameterizedTest
    @MethodSource("getWebPlatformCssIdentifierTestCases")
    @MethodSource("getAdditionalCssIdentifierTestCases")
    public void consumeCssIdentifier_shouldParseAccordingToWebStandards(
            String expectedOutput, String cssIdentifier) {
        // When: We parse a CSS identifier
        String result = parseCssIdentifier(cssIdentifier);
        
        // Then: It should match the expected web platform standard output
        assertEquals(expectedOutput, result);
    }

    /**
     * Test cases from Web Platform Tests for CSS identifier parsing.
     * Source: https://github.com/web-platform-tests/wpt/blob/36036fb5212a3fc15fc5750cecb1923ba4071668/dom/nodes/ParentNode-querySelector-escapes.html
     */
    private static Stream<Arguments> getWebPlatformCssIdentifierTestCases() {
        return Stream.of(
            // Hex digit escaping with whitespace
            Arguments.of("0nextIsWhiteSpace", "\\30 nextIsWhiteSpace"),
            Arguments.of("0nextIsNotHexLetters", "\\30nextIsNotHexLetters"),
            Arguments.of("0connectHexMoreThan6Hex", "\\000030connectHexMoreThan6Hex"),
            Arguments.of("0spaceMoreThan6Hex", "\\000030 spaceMoreThan6Hex"),

            // Special character replacement for invalid code points
            Arguments.of("zero\uFFFD", "zero\\0"),
            Arguments.of("zero\uFFFD", "zero\\000000"),
            Arguments.of("\uFFFDsurrogateFirst", "\\d83d surrogateFirst"),
            Arguments.of("surrogateSecond\uFFFd", "surrogateSecond\\dd11"),
            Arguments.of("outOfRange\uFFFD", "outOfRange\\110000"),

            // Basic character escaping
            Arguments.of(".comma", "\\.comma"),
            Arguments.of("-minus", "\\-minus"),
            Arguments.of("g", "\\g"),

            // Unicode handling
            Arguments.of("aBMPRegular", "\\61 BMPRegular"),
            Arguments.of("\uD83D\uDD11nonBMP", "\\1f511 nonBMP"),
            
            // Complex escape sequences
            Arguments.of("hello", "hel\\6Co"),
            Arguments.of("&B", "\\26 B"),
            Arguments.of("hello", "hel\\6C o"),
            Arguments.of("spaces", "spac\\65\r\ns"),
            Arguments.of("spaces", "sp\\61\tc\\65\fs"),
            
            // Edge cases
            Arguments.of("simple-ident", "simple-ident"),
            Arguments.of("testing123", "testing123"),
            Arguments.of("_underscore", "_underscore"),
            Arguments.of("-text", "-text"),
            Arguments.of("--abc", "--abc"),
            Arguments.of("--", "--"),
            Arguments.of("---", "---")
        );
    }

    private static Stream<Arguments> getAdditionalCssIdentifierTestCases() {
        return Stream.of(
            Arguments.of("1st", "\\31\r\nst"),
            Arguments.of("1", "\\31\r"),
            Arguments.of("1a", "\\31\ra"),
            Arguments.of("1", "\\031"),
            Arguments.of("1", "\\0031"),
            Arguments.of("a", "a\\\nb")
        );
    }

    @Test 
    public void consumeCssIdentifier_shouldThrowExceptionForEmptyInput() {
        // Given: An empty token queue
        TokenQueue emptyQueue = new TokenQueue("");
        
        // When & Then: Attempting to consume CSS identifier should throw exception
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            emptyQueue::consumeCssIdentifier
        );
        
        assertEquals("CSS identifier expected, but end of input found", exception.getMessage());
    }

    @Test 
    public void consumeCssIdentifier_shouldSupportBackwardsCompatibility() {
        // Note: These are technically invalid CSS identifiers but supported for backwards compatibility
        assertParsedCssIdentifierEquals("1", "1");
        assertParsedCssIdentifierEquals("-", "-");
        assertParsedCssIdentifierEquals("-1", "-1");
    }

    // ========== Helper Methods ==========
    
    private static String parseCssIdentifier(String text) {
        TokenQueue queue = new TokenQueue(text);
        return queue.consumeCssIdentifier();
    }

    private void assertParsedCssIdentifierEquals(String expected, String cssIdentifier) {
        assertEquals(expected, parseCssIdentifier(cssIdentifier));
    }
}