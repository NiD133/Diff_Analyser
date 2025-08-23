package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable unit tests for Metaphone covering:
 * - Default and configurable max code length
 * - Null and empty input handling
 * - Core phonetic transformation rules (TH, CH, TCH, SC, SCH, GN, MB, CGH, GA, vowels)
 * - encode(Object) behavior and exception message
 * - isMetaphoneEqual positive and negative cases
 * - Truncation behavior driven by max code length
 */
public class MetaphoneTest {

    private final Metaphone metaphone = new Metaphone();

    private void assertCode(final String word, final String expected) {
        assertEquals("metaphone(\"" + word + "\")", expected, metaphone.metaphone(word));
    }

    // Max code length

    @Test
    public void defaultMaxCodeLenIsFour() {
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void canIncreaseAndResetMaxCodeLen() {
        metaphone.setMaxCodeLen(8);
        assertEquals(8, metaphone.getMaxCodeLen());

        metaphone.setMaxCodeLen(0);
        assertEquals(0, metaphone.getMaxCodeLen());
    }

    // Null and empty handling

    @Test
    public void encodeString_nullReturnsNull() {
        assertNull(metaphone.encode((String) null));
    }

    @Test
    public void metaphone_nullReturnsNull() {
        assertNull(metaphone.metaphone(null));
    }

    @Test
    public void emptyStringReturnsEmptyString() {
        assertEquals("", metaphone.metaphone(""));
        assertEquals("", metaphone.encode(""));
    }

    // Core algorithm rules

    @Test
    public void rule_TH_encodedAsZero() {
        assertCode("TH", "0");
    }

    @Test
    public void rule_CH_encodedAsX() {
        assertCode("CH", "X");
        assertCode("ch", "X"); // lowercase input is acceptable
    }

    @Test
    public void rule_TCH_treatedAsCH() {
        assertCode("TCH", "X");
    }

    @Test
    public void rule_SC_encodedAsSK() {
        assertCode("SC", "SK");
    }

    @Test
    public void rule_SCH_encodedAsSK() {
        assertCode("SCH", "SK");
    }

    @Test
    public void rule_GN_initialGIsSilent() {
        assertCode("GN", "N");
    }

    @Test
    public void rule_MB_finalBIsSilent() {
        assertCode("MB", "M");
    }

    @Test
    public void rule_CGH_reducesToK() {
        assertCode("CGH", "K");
    }

    @Test
    public void rule_GA_reducesToK() {
        assertCode("GA", "K");
    }

    @Test
    public void vowels_onlyInitialVowelIsKept() {
        // Only the initial vowel is kept; subsequent vowels are dropped.
        assertCode("AEIOU", "A");
        assertCode("BABE", "BB");
    }

    @Test
    public void lettersThatMapToThemselves_exampleKBMF() {
        assertCode("KBMF", "KBMF");
    }

    // encode(Object) contract

    @Test
    public void encodeObject_acceptsString() throws Exception {
        Object result = metaphone.encode((Object) "CH");
        assertEquals("X", result);
    }

    @Test
    public void encodeObject_withNonStringThrowsEncoderException() {
        try {
            metaphone.encode(new Object());
            fail("Expected EncoderException for non-String input");
        } catch (EncoderException e) {
            assertTrue(e.getMessage().contains("not of type java.lang.String"));
        }
    }

    // Equality behavior

    @Test
    public void isMetaphoneEqual_returnsTrueForTypicalEquivalents() {
        // Classic metaphone example: "Smith" and "Smyth" have the same code
        assertTrue(metaphone.isMetaphoneEqual("Smith", "Smyth"));
    }

    @Test
    public void isMetaphoneEqual_returnsFalseForDifferentSounds() {
        assertFalse(metaphone.isMetaphoneEqual("I", "X"));
    }

    // Truncation behavior

    @Test
    public void defaultMaxCodeLenTruncatesToFourCharacters() {
        // "Pfister" -> "PFSTR" (canonical Metaphone); default max (4) truncates to "PFST"
        assertCode("Pfister", "PFST");
    }

    @Test
    public void increasingMaxCodeLenAllowsLongerCodes() {
        metaphone.setMaxCodeLen(10);
        assertCode("Pfister", "PFSTR");
    }
}