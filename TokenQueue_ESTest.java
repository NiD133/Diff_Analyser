package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.parser.TokenQueue;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class TokenQueue_ESTest extends TokenQueue_ESTest_scaffolding {

    // ================= Constructor Tests =================
    @Test(timeout = 4000)
    public void constructor_NullInput_ThrowsNullPointerException() {
        try {
            new TokenQueue(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected when input is null
        }
    }

    // ================= Basic Operation Tests =================
    @Test(timeout = 4000)
    public void isEmpty_EmptyQueue_ReturnsTrue() {
        TokenQueue tokenQueue0 = new TokenQueue("");
        assertTrue(tokenQueue0.isEmpty());
    }

    @Test(timeout = 4000)
    public void isEmpty_NonEmptyQueue_ReturnsFalse() {
        TokenQueue tokenQueue0 = new TokenQueue("%>-8wJ/e4'{T9H");
        assertFalse(tokenQueue0.isEmpty());
    }

    @Test(timeout = 4000)
    public void current_NonEmptyQueue_ReturnsFirstCharacter() {
        TokenQueue tokenQueue0 = new TokenQueue("$dXr'&T^\"0qAav");
        assertEquals('$', tokenQueue0.current());
    }

    @Test(timeout = 4000)
    public void toString_EmptyQueue_ReturnsEmptyString() {
        TokenQueue tokenQueue0 = new TokenQueue("");
        assertEquals("", tokenQueue0.toString());
    }

    @Test(timeout = 4000)
    public void remainder_AfterConstruction_ReturnsFullString() {
        TokenQueue tokenQueue0 = new TokenQueue("TestString");
        assertEquals("TestString", tokenQueue0.remainder());
    }

    // ================= Matching Tests =================
    @Test(timeout = 4000)
    public void matches_ExactMatch_ReturnsTrue() {
        TokenQueue tokenQueue0 = new TokenQueue("|;tj;PF%cdg`,");
        assertTrue(tokenQueue0.matches("|;tj;PF%cdg`,"));
    }

    @Test(timeout = 4000)
    public void matches_NoMatch_ReturnsFalse() {
        TokenQueue tokenQueue0 = new TokenQueue("Jkl):ip4E/0M");
        assertFalse(tokenQueue0.matches("rD@)L`r>t"));
    }

    @Test(timeout = 4000)
    public void matchesChar_MatchesFirstCharacter_ReturnsTrue() {
        TokenQueue tokenQueue0 = new TokenQueue("k\"YT-6Ih:G~3zAw");
        tokenQueue0.consumeElementSelector(); // Consumes 'k'
        assertTrue(tokenQueue0.matches('\"'));
    }

    @Test(timeout = 4000)
    public void matchesChar_NoMatch_ReturnsFalse() {
        TokenQueue tokenQueue0 = new TokenQueue("%>-8wJ/e4'{T9H");
        assertFalse(tokenQueue0.matches('2'));
    }

    @Test(timeout = 4000)
    public void matchesAny_MatchesFirstCharacter_ReturnsTrue() {
        TokenQueue tokenQueue0 = new TokenQueue("Did not find balanced marker at '");
        char[] testChars = new char[7];
        testChars[3] = 'D';
        assertTrue(tokenQueue0.matchesAny(testChars));
    }

    @Test(timeout = 4000)
    public void matchesAny_NoMatch_ReturnsFalse() {
        TokenQueue tokenQueue0 = new TokenQueue("Z<}0z:#; O.[w{T3D");
        char[] testChars = new char[8];
        assertFalse(tokenQueue0.matchesAny(testChars));
    }

    @Test(timeout = 4000)
    public void matchesWhitespace_AfterIdentifier_ReturnsTrue() {
        TokenQueue tokenQueue0 = new TokenQueue("Did not find balanced marker at '");
        tokenQueue0.consumeCssIdentifier(); // Consumes "Did"
        assertTrue(tokenQueue0.matchesWhitespace());
    }

    @Test(timeout = 4000)
    public void matchesWhitespace_NoWhitespace_ReturnsFalse() {
        TokenQueue tokenQueue0 = new TokenQueue("f7(L)+X");
        assertFalse(tokenQueue0.matchesWhitespace());
    }

    @Test(timeout = 4000)
    public void matchesWord_ValidWord_ReturnsTrue() {
        TokenQueue tokenQueue0 = new TokenQueue("Z<}0z:#; O.[w{T3D");
        assertTrue(tokenQueue0.matchesWord());
    }

    @Test(timeout = 4000)
    public void matchesWord_AfterElementSelector_ReturnsFalse() {
        TokenQueue tokenQueue0 = new TokenQueue("k\"YT-6Ih:G3zAw");
        tokenQueue0.consumeElementSelector(); // Consumes 'k'
        assertFalse(tokenQueue0.matchesWord());
    }

    // ================= Consumption Tests =================
    @Test(timeout = 4000)
    public void consume_ValidInput_ReturnsFirstCharacter() {
        TokenQueue tokenQueue0 = new TokenQueue("Test");
        assertEquals('T', tokenQueue0.consume());
    }

    @Test(timeout = 4000)
    public void consumeTo_ExistingSequence_ReturnsSubstring() {
        TokenQueue tokenQueue0 = new TokenQueue("Jkl):ip4E/0M");
        tokenQueue0.consume(); // Consumes 'J'
        assertEquals("kl):ip4E/0M", tokenQueue0.consumeTo("Jkl):ip4E/0M"));
    }

    @Test(timeout = 4000)
    public void consumeTo_SameAsStart_ReturnsEmptyString() {
        TokenQueue tokenQueue0 = new TokenQueue("%>-8wJ/e4'{T9H");
        assertEquals("", tokenQueue0.consumeTo("%>-8wJ/e4'{T9H"));
    }

    @Test(timeout = 4000)
    public void consumeWhitespace_OnlyWhitespace_ReturnsTrue() {
        TokenQueue tokenQueue0 = new TokenQueue("             ");
        assertTrue(tokenQueue0.consumeWhitespace());
    }

    @Test(timeout = 4000)
    public void consumeWhitespace_NoWhitespace_ReturnsFalse() {
        TokenQueue tokenQueue0 = new TokenQueue("bIV>C5`");
        assertFalse(tokenQueue0.consumeWhitespace());
    }

    @Test(timeout = 4000)
    public void matchChomp_MatchingChar_ConsumesChar() {
        TokenQueue tokenQueue0 = new TokenQueue("org.jsoup.internal.StringUtil");
        assertTrue(tokenQueue0.matchChomp('o'));
    }

    @Test(timeout = 4000)
    public void matchChomp_AfterFullConsumption_ReturnsFalse() {
        TokenQueue tokenQueue0 = new TokenQueue("Z<}0z:#; O.[w{T3D");
        tokenQueue0.matchChomp("Z<}0z:#; O.[w{T3D");
        assertFalse(tokenQueue0.matchChomp('Z'));
    }

    @Test(timeout = 4000)
    public void matchChompString_AfterElementSelector_ReturnsFalse() {
        TokenQueue tokenQueue0 = new TokenQueue("k\"YT-6Ih:G~3zAw");
        tokenQueue0.consumeElementSelector(); // Consumes 'k'
        assertFalse(tokenQueue0.matchChomp("k"));
    }

    // ================= CSS Handling Tests =================
    @Test(timeout = 4000)
    public void consumeCssIdentifier_InvalidStart_ReturnsEmptyString() {
        TokenQueue tokenQueue0 = new TokenQueue("%>-8wJ/e4'{T9H");
        assertEquals("", tokenQueue0.consumeCssIdentifier());
    }

    @Test(timeout = 4000)
    public void consumeCssIdentifier_ValidIdentifier_ReturnsIdentifier() {
        TokenQueue tokenQueue0 = new TokenQueue("oUY}ddkFQ:tJ.q");
        assertEquals("oUY", tokenQueue0.consumeCssIdentifier());
    }

    @Test(timeout = 4000)
    public void consumeCssIdentifier_ContainsNullChar_ReplacesNull() {
        TokenQueue tokenQueue0 = new TokenQueue("v\u0000{Z|NV");
        assertEquals("v\uFFFD", tokenQueue0.consumeCssIdentifier());
    }

    @Test(timeout = 4000)
    public void consumeCssIdentifier_EmptyAfterProcessing_ThrowsException() {
        TokenQueue tokenQueue0 = new TokenQueue("\u0005");
        tokenQueue0.chompBalanced('J', 'x');
        try {
            tokenQueue0.consumeCssIdentifier();
            fail("Expecting exception: IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("CSS identifier expected, but end of input found", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void consumeElementSelector_SingleCharacter_ReturnsCharacter() {
        TokenQueue tokenQueue0 = new TokenQueue("b");
        assertEquals("b", tokenQueue0.consumeElementSelector());
    }

    @Test(timeout = 4000)
    public void consumeElementSelector_WithSpecialChars_ReturnsValidSelector() {
        TokenQueue tokenQueue0 = new TokenQueue("WC**~WF-]9Y$]");
        assertEquals("WC**", tokenQueue0.consumeElementSelector());
    }

    @Test(timeout = 4000)
    public void escapeCssIdentifier_NullInput_ThrowsException() {
        try {
            TokenQueue.escapeCssIdentifier(null);
            fail("Expecting exception: NullPointerException");
        } catch(NullPointerException e) {
            // Expected for null input
        }
    }

    @Test(timeout = 4000)
    public void escapeCssIdentifier_StartsWithPercent_EscapesPercent() {
        assertEquals("\\%WApE_", TokenQueue.escapeCssIdentifier("%WApE_"));
    }

    @Test(timeout = 4000)
    public void escapeCssIdentifier_StartsWithDigit_EscapesDigit() {
        assertEquals("\\34 O\\*\\!\\*d1\\^m3R\\|W", 
                     TokenQueue.escapeCssIdentifier("4O*!*d1^m3R|W"));
    }

    @Test(timeout = 4000)
    public void escapeCssIdentifier_StartsWithHyphen_EscapesHyphen() {
        assertEquals("\\-", TokenQueue.escapeCssIdentifier("-"));
    }

    @Test(timeout = 4000)
    public void escapeCssIdentifier_HyphenWithDigit_EscapesDigit() {
        assertEquals("-\\36 Ih", TokenQueue.escapeCssIdentifier("-6Ih"));
    }

    @Test(timeout = 4000)
    public void escapeCssIdentifier_SpecialCharacters_EscapesAppropriately() {
        assertEquals("k\\\"YT\\7f -6Ih\\:G\\~3zAw", 
                     TokenQueue.escapeCssIdentifier("k\"YT-6Ih:G~3zAw"));
    }

    @Test(timeout = 4000)
    public void escapeCssIdentifier_ControlCharacter_EscapesAsCodePoint() {
        assertEquals("\\5 ", TokenQueue.escapeCssIdentifier("\u0005"));
    }

    @Test(timeout = 4000)
    public void escapeCssIdentifier_EmptyString_ReturnsEmpty() {
        assertEquals("", TokenQueue.escapeCssIdentifier(""));
    }

    // ================= Unescape Tests =================
    @Test(timeout = 4000)
    public void unescape_NullInput_ThrowsException() {
        try {
            TokenQueue.unescape(null);
            fail("Expecting exception: NullPointerException");
        } catch(NullPointerException e) {
            // Expected for null input
        }
    }

    @Test(timeout = 4000)
    public void unescape_EmptyString_ReturnsEmpty() {
        assertEquals("", TokenQueue.unescape(""));
    }

    @Test(timeout = 4000)
    public void unescape_WithBackslash_RemovesEscape() {
        assertEquals("H<b:oK|jCxQP4C3U.", 
                     TokenQueue.unescape("H<b:oK|jCxQ\\P4C3U."));
    }

    @Test(timeout = 4000)
    public void unescape_TrailingBackslash_RemovesBackslash() {
        assertEquals("b", TokenQueue.unescape("b\\"));
    }

    // ================= Balanced Chomping Tests =================
    @Test(timeout = 4000)
    public void chompBalanced_ValidMarkers_ReturnsContent() {
        TokenQueue tokenQueue0 = new TokenQueue("Z<}0z:#; O.[w{T3D");
        assertEquals("<}0z:#; O.[w{", tokenQueue0.chompBalanced('Z', 'T'));
    }

    @Test(timeout = 4000)
    public void chompBalanced_UnbalancedParentheses_ThrowsException() {
        TokenQueue tokenQueue0 = new TokenQueue("(2IQL&'x,2.f\"fX");
        try {
            tokenQueue0.chompBalanced('(', 'u');
            fail("Expecting exception: IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("Did not find balanced marker at '2IQL&'x,2.f\"fX'", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void chompBalanced_SameOpenClose_ThrowsException() {
        TokenQueue tokenQueue0 = new TokenQueue(" 5mL\"2HD\"v");
        try {
            tokenQueue0.chompBalanced(' ', ' ');
            fail("Expecting exception: IllegalArgumentException");
        } catch(IllegalArgumentException e) {
            assertEquals("Did not find balanced marker at '5mL\"2HD\"v'", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void chompBalanced_AfterFullMatch_ReturnsEmpty() {
        TokenQueue tokenQueue0 = new TokenQueue("Z<}0z:#; O.[w{T3D");
        tokenQueue0.matchChomp("Z<}0z:#; O.[w{T3D");
        assertEquals("", tokenQueue0.chompBalanced('Z', 'T'));
    }

    // ================= Complex Workflow Tests =================
    @Test(timeout = 4000)
    public void workflow_ElementSelectorAndChompAndCssIdentifier() {
        TokenQueue tokenQueue0 = new TokenQueue("k\"YT-6Ih:G~3zAw");
        assertEquals("k", tokenQueue0.consumeElementSelector());
        assertEquals("\"", tokenQueue0.chompBalanced('!', '!'));
        assertEquals("YT", tokenQueue0.consumeCssIdentifier());
    }

    @Test(timeout = 4000)
    public void workflow_ConsumeToAnyAfterProcessing() {
        TokenQueue tokenQueue0 = new TokenQueue("|;tj;PF%cdg`,");
        tokenQueue0.consume(); // '|'
        tokenQueue0.consume(); // ';'
        tokenQueue0.advance(); // 't'
        tokenQueue0.advance(); // 'j'
        tokenQueue0.advance(); // ';'
        assertEquals('P', tokenQueue0.consume());
        
        String[] terminators = new String[]{"F"};
        assertEquals("", tokenQueue0.consumeToAny(terminators));
    }

    // ================= Closed Queue Tests =================
    @Test(timeout = 4000)
    public void methodsAfterClose_ThrowNullPointerException() {
        TokenQueue tokenQueue0 = new TokenQueue("Test");
        tokenQueue0.close();
        
        verifyClosedQueueBehavior(() -> tokenQueue0.toString());
        verifyClosedQueueBehavior(() -> tokenQueue0.remainder());
        verifyClosedQueueBehavior(() -> tokenQueue0.matchesWord());
        verifyClosedQueueBehavior(() -> tokenQueue0.matchesWhitespace());
        verifyClosedQueueBehavior(() -> tokenQueue0.matches('T'));
        verifyClosedQueueBehavior(() -> tokenQueue0.matchChomp("Test"));
        verifyClosedQueueBehavior(() -> tokenQueue0.matchChomp('T'));
        verifyClosedQueueBehavior(() -> tokenQueue0.current());
        verifyClosedQueueBehavior(() -> tokenQueue0.consumeWhitespace());
        verifyClosedQueueBehavior(() -> tokenQueue0.consumeElementSelector());
        verifyClosedQueueBehavior(() -> tokenQueue0.consumeCssIdentifier());
        verifyClosedQueueBehavior(() -> tokenQueue0.consume("Test"));
        verifyClosedQueueBehavior(() -> tokenQueue0.consume());
        verifyClosedQueueBehavior(() -> tokenQueue0.chompBalanced('(', ')'));
    }

    private void verifyClosedQueueBehavior(Runnable operation) {
        try {
            operation.run();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior for closed queue
        }
    }

    // ================= Error Handling Tests =================
    @Test(timeout = 4000)
    public void consumeTo_EmptyString_ThrowsException() {
        TokenQueue tokenQueue0 = new TokenQueue("");
        try {
            tokenQueue0.consumeTo("");
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected for empty target
        }
    }

    @Test(timeout = 4000)
    public void consumeTo_NullInput_ThrowsException() {
        TokenQueue tokenQueue0 = new TokenQueue("Test");
        try {
            tokenQueue0.consumeTo(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected for null input
        }
    }

    @Test(timeout = 4000)
    public void consumeToAny_NullInput_ThrowsException() {
        TokenQueue tokenQueue0 = new TokenQueue("Grh3(>~p#auroy");
        try {
            tokenQueue0.consumeToAny((String[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected for null input
        }
    }

    @Test(timeout = 4000)
    public void consume_NonMatchingSequence_ThrowsException() {
        TokenQueue tokenQueue0 = new TokenQueue("k\"YT-6Ih:G~3zAw");
        try {
            tokenQueue0.consume("Must Lbe false");
            fail("Expecting exception: IllegalStateException");
        } catch(IllegalStateException e) {
            assertEquals("Queue did not match expected sequence", e.getMessage());
        }
    }
}