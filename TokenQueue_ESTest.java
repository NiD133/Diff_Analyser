package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A comprehensive and understandable test suite for the TokenQueue class.
 * This suite focuses on clarity, maintainability, and thorough coverage of the TokenQueue's functionality.
 */
public class TokenQueueTest {

    // region Constructor and Basic State Tests

    @Test
    public void constructor_withNull_shouldThrowException() {
        try {
            new TokenQueue(null);
            fail("Constructor should not accept null input.");
        } catch (IllegalArgumentException e) {
            // This is the expected behavior.
        }
    }

    @Test
    public void isEmpty_shouldReturnTrueForEmptyQueue() {
        TokenQueue queue = new TokenQueue("");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void isEmpty_shouldReturnFalseForNonEmptyQueue() {
        TokenQueue queue = new TokenQueue("a");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void current_shouldReturnFirstCharWithoutConsuming() {
        TokenQueue queue = new TokenQueue("abc");
        assertEquals('a', queue.current());
        assertEquals("abc", queue.toString()); // Verify queue is unchanged
    }

    @Test
    public void advance_shouldMovePosition() {
        TokenQueue queue = new TokenQueue("abc");
        queue.advance();
        assertEquals("bc", queue.remainder());
    }

    @Test
    public void remainder_shouldConsumeAndReturnRemainingQueue() {
        TokenQueue queue = new TokenQueue("one two");
        String remainder = queue.remainder();

        assertEquals("one two", remainder);
        assertTrue("Queue should be empty after calling remainder()", queue.isEmpty());
        assertEquals("Calling remainder() on an empty queue should return empty string", "", queue.remainder());
    }

    // endregion

    // region Consumption Method Tests

    @Test
    public void consume_withMatchingSequence_shouldAdvanceQueue() {
        TokenQueue queue = new TokenQueue("start-end");
        queue.consume("start");
        assertEquals("-end", queue.remainder());
    }

    @Test(expected = IllegalStateException.class)
    public void consume_withMismatchedSequence_shouldThrowException() {
        TokenQueue queue = new TokenQueue("start-end");
        queue.consume("stop"); // This sequence does not match the start of the queue
    }

    @Test
    public void consumeTo_shouldConsumeUntilDelimiter() {
        TokenQueue queue = new TokenQueue("one two three");
        String pre = queue.consumeTo(" two");
        assertEquals("one", pre);
        assertEquals(" two three", queue.remainder());
    }

    @Test
    public void consumeTo_withNoDelimiter_shouldConsumeToEnd() {
        TokenQueue queue = new TokenQueue("one two three");
        String pre = queue.consumeTo("four");
        assertEquals("one two three", pre);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void consumeToAny_shouldConsumeUntilFirstMatch() {
        TokenQueue queue = new TokenQueue("one,two;three");
        String pre = queue.consumeToAny(",", ";");
        assertEquals("one", pre);
        assertEquals(",two;three", queue.remainder());
    }

    @Test
    public void consumeCssIdentifier_shouldParseValidIdentifier() {
        TokenQueue queue = new TokenQueue("ident-123#foo");
        String ident = queue.consumeCssIdentifier();
        assertEquals("ident-123", ident);
        assertEquals("#foo", queue.remainder());
    }

    @Test
    public void consumeCssIdentifier_withInvalidStart_shouldReturnEmpty() {
        TokenQueue queue = new TokenQueue("#id");
        String ident = queue.consumeCssIdentifier();
        assertEquals("", ident);
        assertEquals("#id", queue.remainder());
    }

    @Test
    public void consumeCssIdentifier_withEscapedChars_shouldUnescape() {
        TokenQueue queue = new TokenQueue("h\\ello-w\\orld other");
        String ident = queue.consumeCssIdentifier();
        assertEquals("hello-world", ident);
        assertEquals(" other", queue.remainder());
    }

    @Test
    public void consumeElementSelector_shouldParseValidSelector() {
        TokenQueue queue = new TokenQueue("ns|tag-name.class");
        String selector = queue.consumeElementSelector();
        assertEquals("ns|tag-name", selector);
        assertEquals(".class", queue.remainder());
    }

    @Test
    public void consumeElementSelector_shouldStopAtCombinator() {
        TokenQueue queue = new TokenQueue("tag>child");
        String selector = queue.consumeElementSelector();
        assertEquals("tag", selector);
        assertEquals(">child", queue.remainder());
    }

    @Test
    public void consumeWhitespace_shouldConsumeLeadingWhitespace() {
        TokenQueue queue = new TokenQueue("  \t\n  text");
        assertTrue(queue.consumeWhitespace());
        assertEquals("text", queue.remainder());
    }

    @Test
    public void consumeWhitespace_withNoWhitespace_shouldReturnFalse() {
        TokenQueue queue = new TokenQueue("text");
        assertFalse(queue.consumeWhitespace());
        assertEquals("text", queue.remainder());
    }

    // endregion

    // region chompBalanced Tests

    @Test
    public void chompBalanced_withSimplePair_shouldReturnContent() {
        TokenQueue queue = new TokenQueue("(content) after");
        String balanced = queue.chompBalanced('(', ')');
        assertEquals("content", balanced);
        assertEquals(" after", queue.remainder());
    }

    @Test
    public void chompBalanced_withNestedPair_shouldReturnOuterContent() {
        TokenQueue queue = new TokenQueue("(one (two) three) four");
        String balanced = queue.chompBalanced('(', ')');
        assertEquals("one (two) three", balanced);
        assertEquals(" four", queue.remainder());
    }

    @Test
    public void chompBalanced_withEscapedChars_shouldNotCountThemAsBalancing() {
        TokenQueue queue = new TokenQueue("(a \\( b \\) c) d");
        String balanced = queue.chompBalanced('(', ')');
        assertEquals("a \\( b \\) c", balanced);
        assertEquals(" d", queue.remainder());
    }
    
    @Test
    public void chompBalanced_withQuotedChars_shouldNotCountThemAsBalancing() {
        TokenQueue queue = new TokenQueue("('a)b') c");
        String balanced = queue.chompBalanced('(', ')');
        assertEquals("'a)b'", balanced);
        assertEquals(" c", queue.remainder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void chompBalanced_withUnbalancedPair_shouldThrowException() {
        TokenQueue queue = new TokenQueue("(unbalanced");
        queue.chompBalanced('(', ')');
    }

    @Test
    public void chompBalanced_whenOpenerNotFound_shouldReturnEmpty() {
        TokenQueue queue = new TokenQueue("no-opener) here");
        String balanced = queue.chompBalanced('(', ')');
        assertEquals("", balanced);
        assertEquals("no-opener) here", queue.remainder());
    }

    // endregion

    // region Matching Method Tests

    @Test
    public void matches_shouldPerformCaseInsensitiveCheck() {
        TokenQueue queue = new TokenQueue("Check");
        assertTrue(queue.matches("check"));
        assertTrue(queue.matches("ChEcK"));
        assertFalse(queue.matches("checks"));
    }

    @Test
    public void matchChomp_shouldConsumeOnCaseInsensitiveMatch() {
        TokenQueue queue = new TokenQueue("Check-rest");
        assertTrue(queue.matchChomp("check"));
        assertEquals("-rest", queue.remainder());
    }

    @Test
    public void matchChomp_shouldNotConsumeOnMismatch() {
        TokenQueue queue = new TokenQueue("Check-rest");
        assertFalse(queue.matchChomp("fail"));
        assertEquals("Check-rest", queue.remainder());
    }

    @Test
    public void matchesAny_shouldReturnTrueForAnyMatch() {
        TokenQueue queue = new TokenQueue("> selector");
        assertTrue(queue.matchesAny('>', '+', '~'));
    }

    @Test
    public void matchesAny_shouldReturnFalseForNoMatch() {
        TokenQueue queue = new TokenQueue(".selector");
        assertFalse(queue.matchesAny('>', '+', '~'));
    }

    @Test
    public void matchesWord_shouldDetectWord() {
        TokenQueue queue = new TokenQueue("word1 another");
        assertTrue(queue.matchesWord());
        queue.consume("word1");
        assertFalse("Should not match word if current char is space", queue.matchesWord());
    }

    // endregion

    // region Static Utility Method Tests

    @Test
    public void unescape_shouldHandleEscapedChars() {
        assertEquals("H<b:oK|jCxQP4C3U.", TokenQueue.unescape("H<b:oK|jCxQ\\P4C3U."));
    }

    @Test
    public void unescape_withTrailingEscape_shouldRemoveIt() {
        assertEquals("b", TokenQueue.unescape("b\\"));
    }

    @Test
    public void unescape_withEmptyString_shouldReturnEmpty() {
        assertEquals("", TokenQueue.unescape(""));
    }


    @Test
    public void escapeCssIdentifier_shouldEscapeSpecialChars() {
        assertEquals("k\\\"YT\\ -6Ih\\:G\\~3zAw", TokenQueue.escapeCssIdentifier("k\"YT -6Ih:G~3zAw"));
        assertEquals("\\31 23", TokenQueue.escapeCssIdentifier("123")); // Identifiers can't start with a digit unless escaped
        assertEquals("-\\31 23", TokenQueue.escapeCssIdentifier("-123"));
        assertEquals("\\-", TokenQueue.escapeCssIdentifier("-"));
        assertEquals("\\.class", TokenQueue.escapeCssIdentifier(".class"));
    }

    @Test
    public void escapeCssIdentifier_withValidChars_shouldNotChange() {
        assertEquals("valid-ident", TokenQueue.escapeCssIdentifier("valid-ident"));
        assertEquals("ValidIdent", TokenQueue.escapeCssIdentifier("ValidIdent"));
        assertEquals("_valid", TokenQueue.escapeCssIdentifier("_valid"));
    }

    @Test
    public void escapeCssIdentifier_withEmptyString_shouldReturnEmpty() {
        assertEquals("", TokenQueue.escapeCssIdentifier(""));
    }

    // endregion

    // region Scenario and Edge Case Tests

    @Test
    public void sequenceOfOperations_shouldBehaveAsExpected() {
        TokenQueue queue = new TokenQueue("tag#id.class[attr=val]");
        
        // Consume tag
        String tag = queue.consumeElementSelector();
        assertEquals("tag", tag);
        
        // Consume ID
        assertTrue(queue.matchChomp("#"));
        String id = queue.consumeCssIdentifier();
        assertEquals("id", id);

        // Consume class
        assertTrue(queue.matchChomp("."));
        String className = queue.consumeCssIdentifier();
        assertEquals("class", className);

        // Consume attribute
        String attr = queue.chompBalanced('[', ']');
        assertEquals("attr=val", attr);
        
        assertTrue(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void usingClosedQueue_shouldThrowException() {
        TokenQueue queue = new TokenQueue("test");
        queue.close();
        // Most operations on a closed queue will throw a NullPointerException
        // because the underlying reader is nulled. We test one representative method.
        queue.matches("t");
    }

    // endregion
}