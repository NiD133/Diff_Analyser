package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;

/**
 * Test suite for TokenQueue class - a character reader with helpers for parsing CSS selectors.
 * Tests cover basic queue operations, CSS identifier parsing, balanced chomping, and error handling.
 */
public class TokenQueueTest {

    // ========== CSS Identifier Consumption Tests ==========
    
    @Test
    public void consumeCssIdentifier_WithInvalidStartingCharacter_ReturnsEmptyString() {
        TokenQueue queue = new TokenQueue("%invalid-start");
        
        String result = queue.consumeCssIdentifier();
        
        assertEquals("CSS identifier starting with % should return empty string", "", result);
    }

    @Test
    public void consumeCssIdentifier_WithValidIdentifier_ReturnsIdentifierPart() {
        TokenQueue queue = new TokenQueue("validId}remaining");
        
        String result = queue.consumeCssIdentifier();
        
        assertEquals("Should consume valid CSS identifier until delimiter", "validId", result);
    }

    @Test
    public void consumeCssIdentifier_WithNullCharacter_ReplacesWithReplacementChar() {
        TokenQueue queue = new TokenQueue("v\u0000invalid");
        
        String result = queue.consumeCssIdentifier();
        
        assertEquals("Null character should be replaced with Unicode replacement char", "v\uFFFD", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void consumeCssIdentifier_AtEndOfInput_ThrowsException() {
        TokenQueue queue = new TokenQueue("test");
        queue.consumeTo("test"); // consume all content
        
        queue.consumeCssIdentifier();
    }

    // ========== String Unescaping Tests ==========
    
    @Test
    public void unescape_WithBackslashEscapedString_RemovesEscapes() {
        String result = TokenQueue.unescape("escaped\\backslash");
        
        assertEquals("Should remove backslash escapes", "escapedbackslash", result);
    }

    @Test
    public void unescape_WithEmptyString_ReturnsEmpty() {
        String result = TokenQueue.unescape("");
        
        assertEquals("Empty string should remain empty", "", result);
    }

    @Test(expected = NullPointerException.class)
    public void unescape_WithNull_ThrowsNullPointerException() {
        TokenQueue.unescape(null);
    }

    // ========== CSS Identifier Escaping Tests ==========
    
    @Test
    public void escapeCssIdentifier_WithSpecialCharacters_EscapesCorrectly() {
        String result = TokenQueue.escapeCssIdentifier("special%chars");
        
        assertEquals("Should escape % character", "\\%special_chars", result);
    }

    @Test
    public void escapeCssIdentifier_WithDigitStart_EscapesFirstDigit() {
        String result = TokenQueue.escapeCssIdentifier("123abc");
        
        assertEquals("Should escape leading digit", "\\31 23abc", result);
    }

    @Test
    public void escapeCssIdentifier_WithHyphenOnly_EscapesHyphen() {
        String result = TokenQueue.escapeCssIdentifier("-");
        
        assertEquals("Single hyphen should be escaped", "\\-", result);
    }

    @Test
    public void escapeCssIdentifier_WithHyphenFollowedByDigit_EscapesDigit() {
        String result = TokenQueue.escapeCssIdentifier("-6test");
        
        assertEquals("Digit after hyphen should be escaped", "-\\36 test", result);
    }

    @Test
    public void escapeCssIdentifier_WithControlCharacter_EscapesWithHex() {
        String result = TokenQueue.escapeCssIdentifier("\u0005");
        
        assertEquals("Control character should be hex escaped", "\\5 ", result);
    }

    @Test(expected = NullPointerException.class)
    public void escapeCssIdentifier_WithNull_ThrowsNullPointerException() {
        TokenQueue.escapeCssIdentifier(null);
    }

    // ========== Balanced Chomping Tests ==========
    
    @Test(expected = IllegalArgumentException.class)
    public void chompBalanced_WithUnbalancedMarkers_ThrowsException() {
        TokenQueue queue = new TokenQueue("(unbalanced");
        
        queue.chompBalanced('(', ')');
    }

    @Test
    public void chompBalanced_WithBalancedMarkers_ReturnsContent() {
        TokenQueue queue = new TokenQueue("start<content>end");
        
        String result = queue.chompBalanced('<', '>');
        
        assertEquals("Should return content between balanced markers", "content", result);
    }

    @Test
    public void chompBalanced_WithNoMatchingStart_ReturnsUpToEnd() {
        TokenQueue queue = new TokenQueue("content");
        
        String result = queue.chompBalanced('(', ')');
        
        assertEquals("Should return single character when no markers found", "c", result);
    }

    // ========== Element Selector Tests ==========
    
    @Test
    public void consumeElementSelector_WithValidSelector_ReturnsSelector() {
        TokenQueue queue = new TokenQueue("div.class");
        
        String result = queue.consumeElementSelector();
        
        assertEquals("Should consume element selector", "div", result);
    }

    @Test
    public void consumeElementSelector_WithWildcardAndNamespace_ReturnsFullSelector() {
        TokenQueue queue = new TokenQueue("ns|*~remaining");
        
        String result = queue.consumeElementSelector();
        
        assertEquals("Should consume namespace and wildcard", "ns|*", result);
    }

    // ========== Queue State Tests ==========
    
    @Test
    public void isEmpty_WithEmptyQueue_ReturnsTrue() {
        TokenQueue queue = new TokenQueue("");
        
        assertTrue("Empty queue should return true", queue.isEmpty());
    }

    @Test
    public void isEmpty_WithContent_ReturnsFalse() {
        TokenQueue queue = new TokenQueue("content");
        
        assertFalse("Queue with content should return false", queue.isEmpty());
    }

    @Test
    public void current_ReturnsCurrentCharacter() {
        TokenQueue queue = new TokenQueue("test");
        
        char result = queue.current();
        
        assertEquals("Should return first character", 't', result);
    }

    // ========== Matching Tests ==========
    
    @Test
    public void matches_WithMatchingString_ReturnsTrue() {
        TokenQueue queue = new TokenQueue("test string");
        
        boolean result = queue.matches("test");
        
        assertTrue("Should match beginning of queue", result);
    }

    @Test
    public void matches_WithNonMatchingString_ReturnsFalse() {
        TokenQueue queue = new TokenQueue("test string");
        
        boolean result = queue.matches("other");
        
        assertFalse("Should not match different string", result);
    }

    @Test
    public void matches_WithMatchingChar_ReturnsTrue() {
        TokenQueue queue = new TokenQueue("test");
        
        boolean result = queue.matches('t');
        
        assertTrue("Should match first character", result);
    }

    @Test
    public void matchesWhitespace_WithWhitespace_ReturnsTrue() {
        TokenQueue queue = new TokenQueue(" test");
        
        boolean result = queue.matchesWhitespace();
        
        assertTrue("Should match whitespace", result);
    }

    @Test
    public void matchesWhitespace_WithoutWhitespace_ReturnsFalse() {
        TokenQueue queue = new TokenQueue("test");
        
        boolean result = queue.matchesWhitespace();
        
        assertFalse("Should not match non-whitespace", result);
    }

    @Test
    public void matchesWord_WithWordCharacter_ReturnsTrue() {
        TokenQueue queue = new TokenQueue("word");
        
        boolean result = queue.matchesWord();
        
        assertTrue("Should match word character", result);
    }

    // ========== Consumption Tests ==========
    
    @Test
    public void consume_ReturnsAndRemovesFirstChar() {
        TokenQueue queue = new TokenQueue("test");
        
        char result = queue.consume();
        
        assertEquals("Should return first character", 't', result);
        assertEquals("Should advance queue position", 'e', queue.current());
    }

    @Test
    public void consumeWhitespace_WithWhitespace_ReturnsTrueAndConsumes() {
        TokenQueue queue = new TokenQueue("   test");
        
        boolean result = queue.consumeWhitespace();
        
        assertTrue("Should return true when whitespace consumed", result);
        assertEquals("Should advance past whitespace", 't', queue.current());
    }

    @Test
    public void consumeWhitespace_WithoutWhitespace_ReturnsFalse() {
        TokenQueue queue = new TokenQueue("test");
        
        boolean result = queue.consumeWhitespace();
        
        assertFalse("Should return false when no whitespace", result);
    }

    @Test
    public void consumeTo_WithFoundSequence_ReturnsContentBeforeSequence() {
        TokenQueue queue = new TokenQueue("start-end-remaining");
        
        String result = queue.consumeTo("-end");
        
        assertEquals("Should return content before sequence", "start", result);
    }

    @Test
    public void consumeTo_WithSameSequence_ReturnsEmpty() {
        TokenQueue queue = new TokenQueue("test");
        
        String result = queue.consumeTo("test");
        
        assertEquals("Should return empty when sequence matches start", "", result);
    }

    @Test(expected = IllegalStateException.class)
    public void consume_WithNonMatchingSequence_ThrowsException() {
        TokenQueue queue = new TokenQueue("test");
        
        queue.consume("different");
    }

    @Test
    public void consume_WithMatchingSequence_ConsumesSuccessfully() {
        TokenQueue queue = new TokenQueue("test");
        
        queue.consume("test"); // Should not throw
        
        assertTrue("Queue should be empty after consuming all content", queue.isEmpty());
    }

    // ========== Match and Chomp Tests ==========
    
    @Test
    public void matchChomp_WithMatchingChar_ReturnsTrueAndConsumes() {
        TokenQueue queue = new TokenQueue("test");
        
        boolean result = queue.matchChomp('t');
        
        assertTrue("Should return true for matching character", result);
        assertEquals("Should advance past matched character", 'e', queue.current());
    }

    @Test
    public void matchChomp_WithNonMatchingChar_ReturnsFalse() {
        TokenQueue queue = new TokenQueue("test");
        
        boolean result = queue.matchChomp('x');
        
        assertFalse("Should return false for non-matching character", result);
        assertEquals("Should not advance queue", 't', queue.current());
    }

    // ========== Utility Tests ==========
    
    @Test
    public void toString_ReturnsRemainingContent() {
        TokenQueue queue = new TokenQueue("test content");
        
        String result = queue.toString();
        
        assertEquals("Should return all content", "test content", result);
    }

    @Test
    public void remainder_ReturnsRemainingContent() {
        TokenQueue queue = new TokenQueue("test content");
        queue.consume(); // consume 't'
        
        String result = queue.remainder();
        
        assertEquals("Should return remaining content", "est content", result);
    }

    // ========== Error Handling After Close ==========
    
    @Test(expected = NullPointerException.class)
    public void operationsAfterClose_ThrowNullPointerException() {
        TokenQueue queue = new TokenQueue("test");
        queue.close();
        
        queue.toString(); // Should throw NPE
    }

    // ========== Null Parameter Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void constructor_WithNull_ThrowsNullPointerException() {
        new TokenQueue(null);
    }

    @Test(expected = NullPointerException.class)
    public void matches_WithNullString_ThrowsNullPointerException() {
        TokenQueue queue = new TokenQueue("test");
        
        queue.matches((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void consumeTo_WithNull_ThrowsNullPointerException() {
        TokenQueue queue = new TokenQueue("test");
        
        queue.consumeTo(null);
    }

    @Test(expected = NullPointerException.class)
    public void consumeToAny_WithNull_ThrowsNullPointerException() {
        TokenQueue queue = new TokenQueue("test");
        
        queue.consumeToAny((String[]) null);
    }

    // ========== Edge Cases ==========
    
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void consumeTo_WithEmptySequenceOnEmptyQueue_ThrowsException() {
        TokenQueue queue = new TokenQueue("");
        
        queue.consumeTo("");
    }

    @Test
    public void advance_OnEmptyQueue_DoesNotThrow() {
        TokenQueue queue = new TokenQueue("");
        
        queue.advance(); // Should not throw
    }

    @Test
    public void consumeToAny_WithMultipleOptions_ReturnsContentBeforeFirstMatch() {
        TokenQueue queue = new TokenQueue("content;more:data");
        
        String result = queue.consumeToAny(";", ":");
        
        assertEquals("Should stop at first matching sequence", "content", result);
    }
}