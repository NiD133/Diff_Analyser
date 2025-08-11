package org.jsoup.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Readable, focused tests for TokenQueue.
 * These tests cover common, documented behaviors with clear input/output examples and avoid brittle internal-state checks.
 */
class TokenQueueTest {

    // --------------- unescape ----------------

    @Test
    void unescape_removesBackslashEscapes() {
        assertEquals("H<b:oK|jCxQP4C3U.", TokenQueue.unescape("H<b:oK|jCxQ\\P4C3U."));
        assertEquals("b", TokenQueue.unescape("b\\"));
        assertEquals("", TokenQueue.unescape(""));
    }

    @Test
    void unescape_null_throwsNpe() {
        assertThrows(NullPointerException.class, () -> TokenQueue.unescape(null));
    }

    // --------------- escapeCssIdentifier ----------------

    @Test
    void escapeCssIdentifier_examplesFromSpecAndEdgeCases() {
        // Simple percent escapes
        assertEquals("\\%WApE_", TokenQueue.escapeCssIdentifier("%WApE_"));

        // Already safe characters are not modified
        assertEquals("v\uFFFD", TokenQueue.escapeCssIdentifier("v\uFFFD"));
        assertEquals("", TokenQueue.escapeCssIdentifier(""));

        // Mixed specials
        assertEquals("k\\\"YT\\7f -6Ih\\:G\\~3zAw", TokenQueue.escapeCssIdentifier("k\"YT\u007f-6Ih:G~3zAw"));
        assertEquals("\\34 O\\*\\!\\*d1\\^m3R\\|W", TokenQueue.escapeCssIdentifier("4O*!*d1^m3R|W"));

        // Leading hyphen + digit gets escaped
        assertEquals("-\\36 Ih", TokenQueue.escapeCssIdentifier("-6Ih"));

        // Single hyphen
        assertEquals("\\-", TokenQueue.escapeCssIdentifier("-"));

        // Space and percent
        assertEquals("-\\ \\%", TokenQueue.escapeCssIdentifier("- %"));

        // Control char -> escaped codepoint with trailing space
        assertEquals("\\5 ", TokenQueue.escapeCssIdentifier("\u0005"));
    }

    @Test
    void escapeCssIdentifier_null_throwsNpe() {
        assertThrows(NullPointerException.class, () -> TokenQueue.escapeCssIdentifier(null));
    }

    // --------------- consumeCssIdentifier ----------------

    @Test
    void consumeCssIdentifier_whenStartsWithIdentifier_returnsIt() {
        TokenQueue q = new TokenQueue("oUY}ddkFQ:tJ.q");
        assertEquals("oUY", q.consumeCssIdentifier());
    }

    @Test
    void consumeCssIdentifier_whenStartsWithInvalid_returnsEmpty() {
        TokenQueue q = new TokenQueue("%>-8wJ/e4'{T9H");
        assertEquals("", q.consumeCssIdentifier());
    }

    @Test
    void consumeCssIdentifier_replacesNullCharWithReplacement() {
        TokenQueue q = new TokenQueue("v\u0000{Z|N\u007fV");
        assertEquals("v\uFFFD", q.consumeCssIdentifier());
    }

    @Test
    void consumeCssIdentifier_onEmptyQueue_throwsIllegalArgument() {
        TokenQueue q = new TokenQueue("");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, q::consumeCssIdentifier);
        assertTrue(ex.getMessage().toLowerCase().contains("css identifier expected"));
    }

    // --------------- consumeElementSelector ----------------

    @Test
    void consumeElementSelector_basic() {
        assertEquals("b", new TokenQueue("b").consumeElementSelector());
        assertEquals("WC**", new TokenQueue("WC**~WF-]9Y$]").consumeElementSelector());
    }

    // --------------- whitespace, words, and simple matches ----------------

    @Test
    void whitespace_consumptionAndChecks() {
        TokenQueue withSpaces = new TokenQueue("             abc");
        assertTrue(withSpaces.consumeWhitespace()); // consumed
        assertFalse(new TokenQueue("abc").matchesWhitespace());
        assertTrue(new TokenQueue("Did not find balanced marker at '").matchesWhitespace()); // leading 'D' is not whitespace; following is, after consuming ident from empty -> demonstrate check
    }

    @Test
    void matches_and_matchChomp_examples() {
        TokenQueue exact = new TokenQueue("|;tj;PF%cdg`,");
        assertTrue(exact.matches("|;tj;PF%cdg`,"));
        assertFalse(new TokenQueue("Jkl):ip4E/0M").matches("rD@)L`r>t"));
        assertFalse(new TokenQueue("%>-8wJ/e4'{T9H").matches('2'));

        TokenQueue word = new TokenQueue("Z<}0z:#; O.[w{T3D");
        assertTrue(word.matchesWord());

        TokenQueue startsWithO = new TokenQueue("org.jsoup.internal.StringUtil");
        assertTrue(startsWithO.matchChomp('o')); // consumes 'o'
        assertTrue(startsWithO.remainder().startsWith("rg."));
    }

    // --------------- consume / advance / current / remainder ----------------

    @Test
    void basicCharacterOps_andRemainder() {
        TokenQueue q = new TokenQueue("8Nw@t]UFUu54QXB.");
        assertEquals('8', q.current());
        assertEquals('8', q.consume());
        q.advance(); // drop next char ('N')
        assertFalse(q.isEmpty());
        assertEquals("@t]UFUu54QXB.", q.remainder());
        assertTrue(new TokenQueue("").isEmpty());
    }

    @Test
    void toString_returnsOriginalInput() {
        TokenQueue q = new TokenQueue(">'9sOEbGPT9N3{HK5");
        assertEquals(">'9sOEbGPT9N3{HK5", q.toString());
    }

    // --------------- consume(String) ----------------

    @Test
    void consume_wrongPrefix_throwsIllegalState() {
        TokenQueue q = new TokenQueue("k\"YT\u007f-6Ih:G~3zAw");
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> q.consume("Must Lbe false"));
        assertTrue(ex.getMessage().toLowerCase().contains("did not match expected sequence"));
    }

    @Test
    void consume_emptyString_isNoop() {
        new TokenQueue("").consume("");
    }

    // --------------- consumeTo / consumeToAny ----------------

    @Test
    void consumeTo_whenTerminatorNotFound_returnsRemainder() {
        TokenQueue q = new TokenQueue("Jkl):ip4E/0M");
        q.consume(); // drop 'J'
        assertEquals("kl):ip4E/0M", q.consumeTo("Jkl):ip4E/0M")); // terminator not found, so returns remainder
    }

    @Test
    void consumeTo_whenTerminatorIsAtStart_returnsEmpty() {
        TokenQueue q = new TokenQueue("%>-8wJ/e4'{T9H");
        assertEquals("", q.consumeTo("%>-8wJ/e4'{T9H"));
    }

    @Test
    void consumeToAny_stopsBeforeAnyProvidedCaseInsensitive() {
        TokenQueue q = new TokenQueue("abc<Def rest");
        assertEquals("abc", q.consumeToAny("<", "|", "DEF")); // "DEF" matches case-insensitively
        // Next char should be '<' or start of "Def"
        assertTrue(q.matches("<") || q.matches("Def"));
    }

    @Test
    void matchesAny_withCharArray() {
        TokenQueue q = new TokenQueue("Did not find balanced marker at '");
        assertTrue(q.matchesAny(new char[] { 0, 0, 0, 'D', 0, 0, 0 }));
        assertFalse(new TokenQueue("Z<}0z:#; O.[w{T3D").matchesAny(new char[8]));
    }

    // --------------- chompBalanced ----------------

    @Test
    void chompBalanced_balancedExample() {
        TokenQueue q = new TokenQueue("Z<}0z:#; O.[w{T3D");
        assertEquals("<}0z:#; O.[w{", q.chompBalanced('Z', 'T'));
    }

    @Test
    void chompBalanced_whenUnbalanced_throwsIllegalArgument() {
        // A few representative unbalanced cases, messages can vary, so only assert the type
        assertThrows(IllegalArgumentException.class, () -> new TokenQueue("(2IQL&'x,2.f\"fX").chompBalanced('(', 'u'));
        assertThrows(IllegalArgumentException.class, () -> new TokenQueue("{#7l{C/gwF;D^Xtn&").chompBalanced('{', ';'));
        assertThrows(IllegalArgumentException.class, () -> new TokenQueue("'O*!.d1^'3oR|&").chompBalanced('\'', '\''));
        assertThrows(IllegalArgumentException.class, () -> new TokenQueue("The '%s' parameter must not be empty.").chompBalanced('T', 'T'));
    }

    @Test
    void chompBalanced_thenConsumeCssIdentifier_flow() {
        TokenQueue q = new TokenQueue("k\"YT\u007f-6Ih:G~3zAw");
        assertEquals("k", q.consumeElementSelector());
        assertEquals("\"", q.chompBalanced('!', '!')); // quotes are returned verbatim
        assertEquals("YT", q.consumeCssIdentifier());
    }

    // --------------- matchesWhitespace / matchesWord quick checks ----------------

    @Test
    void matchesWhitespace_falseWhenNoWhitespace() {
        assertFalse(new TokenQueue("f7(L)+X").matchesWhitespace());
    }

    @Test
    void matchesWord_trueOnLetterOrDigit() {
        assertTrue(new TokenQueue("Z<}0z:#; O.[w{T3D").matchesWord());
    }
}