package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class TokenQueue_ESTest extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testConsumeCssIdentifier_EmptyString() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("%>-8wJ/e4'{T9H");
        String result = tokenQueue.consumeCssIdentifier();
        assertEquals("Expected empty string when consuming CSS identifier from '%>-8wJ/e4'{T9H'", "", result);
    }

    @Test(timeout = 4000)
    public void testUnescapeString() throws Throwable {
        String result = TokenQueue.unescape("H<b:oK|jCxQ\\P4C3U.");
        assertEquals("Expected unescaped string to be 'H<b:oK|jCxQP4C3U.'", "H<b:oK|jCxQP4C3U.", result);
    }

    @Test(timeout = 4000)
    public void testChompBalanced_IllegalArgumentException() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("(2IQL&'x,2.f\"fX");
        try {
            tokenQueue.chompBalanced('(', 'u');
            fail("Expected IllegalArgumentException due to unbalanced markers");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testConsumeCssIdentifier_ValidIdentifier() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("oUY}ddkFQ:tJ.q");
        String result = tokenQueue.consumeCssIdentifier();
        assertEquals("Expected CSS identifier 'oUY' from 'oUY}ddkFQ:tJ.q'", "oUY", result);
    }

    @Test(timeout = 4000)
    public void testUnescape_EmptyString() throws Throwable {
        String result = TokenQueue.unescape("");
        assertEquals("Expected unescaped empty string", "", result);
    }

    @Test(timeout = 4000)
    public void testToString_EmptyQueue() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("");
        String result = tokenQueue.toString();
        assertEquals("Expected empty string from empty TokenQueue", "", result);
    }

    @Test(timeout = 4000)
    public void testRemainder_DifferentResults() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("d:,&:wx");
        String firstRemainder = tokenQueue.remainder();
        String secondRemainder = tokenQueue.remainder();
        assertFalse("Expected different results from consecutive remainder calls", secondRemainder.equals(firstRemainder));
    }

    @Test(timeout = 4000)
    public void testConsumeElementSelector() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("k\"YT-6Ih:G3zAw");
        String result = tokenQueue.consumeElementSelector();
        assertEquals("Expected element selector 'k' from 'k\"YT-6Ih:G3zAw'", "k", result);

        boolean matchesWord = tokenQueue.matchesWord();
        assertFalse("Expected no word match after consuming element selector", matchesWord);
    }

    @Test(timeout = 4000)
    public void testMatchesWhitespace() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("Did not find balanced marker at '");
        tokenQueue.consumeCssIdentifier();
        boolean result = tokenQueue.matchesWhitespace();
        assertTrue("Expected whitespace match after consuming CSS identifier", result);
    }

    @Test(timeout = 4000)
    public void testMatchesAny_CharArray() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("Did not find balanced marker at '");
        char[] charArray = new char[7];
        charArray[3] = 'D';
        boolean result = tokenQueue.matchesAny(charArray);
        assertTrue("Expected match for character 'D' in TokenQueue", result);
    }

    @Test(timeout = 4000)
    public void testMatchesString() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("Jkl):ip4E/0M");
        boolean result = tokenQueue.matches("rD@)L`r>t");
        assertFalse("Expected no match for string 'rD@)L`r>t' in TokenQueue", result);
    }

    @Test(timeout = 4000)
    public void testMatchesChar() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("k\"YT-6Ih:G~3zAw");
        String elementSelector = tokenQueue.consumeElementSelector();
        assertEquals("Expected element selector 'k'", "k", elementSelector);

        boolean matchesChar = tokenQueue.matches('\"');
        assertTrue("Expected match for character '\"' in TokenQueue", matchesChar);
    }

    @Test(timeout = 4000)
    public void testMatchChomp() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("k\"YT-6Ih:G~3zAw");
        String elementSelector = tokenQueue.consumeElementSelector();
        assertEquals("Expected element selector 'k'", "k", elementSelector);

        boolean result = tokenQueue.matchChomp("k");
        assertFalse("Expected no match chomp for string 'k'", result);
    }

    @Test(timeout = 4000)
    public void testIsEmpty() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("");
        boolean result = tokenQueue.isEmpty();
        assertTrue("Expected empty TokenQueue", result);
    }

    @Test(timeout = 4000)
    public void testCurrentChar() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("$dXr'&T^\"0qAav");
        char currentChar = tokenQueue.current();
        assertEquals("Expected current character '$'", '$', currentChar);
    }

    @Test(timeout = 4000)
    public void testConsumeWhitespace() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("             ");
        boolean result = tokenQueue.consumeWhitespace();
        assertTrue("Expected whitespace consumption", result);
    }

    @Test(timeout = 4000)
    public void testConsumeTo() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("Jkl):ip4E/0M");
        tokenQueue.consume();
        String result = tokenQueue.consumeTo("Jkl):ip4E/0M");
        assertEquals("Expected consumption to 'Jkl):ip4E/0M'", "kl):ip4E/0M", result);
    }

    @Test(timeout = 4000)
    public void testChompBalanced_InvalidMarkers() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("f7(L)+X");
        String result = tokenQueue.chompBalanced('P', 'u');
        assertEquals("Expected chomp balanced result 'f'", "f", result);

        char consumedChar = tokenQueue.consume();
        assertEquals("Expected consumed character '7'", '7', consumedChar);
    }

    @Test(timeout = 4000)
    public void testUnescape_NullPointerException() throws Throwable {
        try {
            TokenQueue.unescape(null);
            fail("Expected NullPointerException for null input");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.TokenQueue", e);
        }
    }

    @Test(timeout = 4000)
    public void testClose_NullPointerException() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue(">'9sOEbGPT9N3{HK5");
        tokenQueue.close();
        try {
            tokenQueue.toString();
            fail("Expected NullPointerException after closing TokenQueue");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testMatchesAny_NullCharArray() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("dMk");
        try {
            tokenQueue.matchesAny((char[]) null);
            fail("Expected NullPointerException for null char array");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.CharacterReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testMatches_NullString() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("<b:oK|j\"CQ\\PCjGU.");
        try {
            tokenQueue.matches((String) null);
            fail("Expected NullPointerException for null string");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.CharacterReader", e);
        }
    }

    @Test(timeout = 4000)
    public void testEscapeCssIdentifier() throws Throwable {
        String result = TokenQueue.escapeCssIdentifier("%WApE_");
        assertEquals("Expected escaped CSS identifier '\\%WApE_'", "\\%WApE_", result);
    }

    @Test(timeout = 4000)
    public void testConsumeCssIdentifier_InvalidEscapeSequence() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("\u0005");
        tokenQueue.chompBalanced('J', 'x');
        try {
            tokenQueue.consumeCssIdentifier();
            fail("Expected IllegalArgumentException for invalid escape sequence");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.parser.TokenQueue", e);
        }
    }

    @Test(timeout = 4000)
    public void testConsumeToAny() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("|;tj;PF%cdg`,");
        tokenQueue.consume();
        tokenQueue.consume();
        tokenQueue.advance();
        tokenQueue.advance();
        tokenQueue.advance();
        char consumedChar = tokenQueue.consume();
        assertEquals("Expected consumed character 'P'", 'P', consumedChar);

        String[] terminators = new String[4];
        terminators[0] = "F";
        String result = tokenQueue.consumeToAny(terminators);
        assertEquals("Expected consumption to any terminator", "", result);
    }

    @Test(timeout = 4000)
    public void testMatchesWord() throws Throwable {
        TokenQueue tokenQueue = new TokenQueue("Z<}0z:#; O.[w{T3D");
        boolean result = tokenQueue.matchesWord();
        assertTrue("Expected match for word in TokenQueue", result);
    }
}