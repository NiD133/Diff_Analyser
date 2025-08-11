package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.language.Metaphone;

/**
 * Test suite for the Metaphone phonetic encoding algorithm.
 * Tests cover basic functionality, edge cases, and specific phonetic rules.
 */
public class MetaphoneTest {

    private static final int DEFAULT_MAX_CODE_LENGTH = 4;

    // Basic functionality tests
    
    @Test(timeout = 4000)
    public void testDefaultMaxCodeLength() {
        Metaphone metaphone = new Metaphone();
        assertEquals("Default max code length should be 4", DEFAULT_MAX_CODE_LENGTH, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testSetMaxCodeLength() {
        Metaphone metaphone = new Metaphone();
        
        metaphone.setMaxCodeLen(0);
        assertEquals("Max code length should be settable to 0", 0, metaphone.getMaxCodeLen());
        
        metaphone.setMaxCodeLen(-3486);
        assertEquals("Max code length should accept negative values", -3486, metaphone.getMaxCodeLen());
    }

    // Null and empty string handling

    @Test(timeout = 4000)
    public void testMetaphoneWithNullInput() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone(null);
        // Test passes if no exception is thrown
        assertEquals("Max code length should remain unchanged", DEFAULT_MAX_CODE_LENGTH, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testMetaphoneWithEmptyString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone("");
        // Test passes if no exception is thrown
        assertEquals("Max code length should remain unchanged", DEFAULT_MAX_CODE_LENGTH, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testEncodeWithNullString() {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.encode((String) null);
        // Test passes if no exception is thrown
        assertEquals("Max code length should remain unchanged", DEFAULT_MAX_CODE_LENGTH, metaphone.getMaxCodeLen());
    }

    // Encode method tests

    @Test(timeout = 4000)
    public void testEncodeWithStringObject() {
        Metaphone metaphone = new Metaphone();
        Object result = metaphone.encode((Object) "CH");
        assertEquals("CH should encode to X", "X", result);
        assertEquals("Max code length should remain unchanged", DEFAULT_MAX_CODE_LENGTH, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testEncodeWithInvalidObjectType() {
        Metaphone metaphone = new Metaphone();
        try {
            metaphone.encode((Object) metaphone);
            fail("Should throw exception for non-String object");
        } catch(Exception e) {
            assertTrue("Should mention parameter type in error message", 
                      e.getMessage().contains("Parameter supplied to Metaphone encode is not of type java.lang.String"));
        }
    }

    // Phonetic rule tests - consonant combinations

    @Test(timeout = 4000)
    public void testConsonantCombination_CH() {
        Metaphone metaphone = new Metaphone();
        assertEquals("CH should encode to X", "X", metaphone.metaphone("CH"));
        assertEquals("TCH should encode to X", "X", metaphone.metaphone("TCH"));
    }

    @Test(timeout = 4000)
    public void testConsonantCombination_TH() {
        Metaphone metaphone = new Metaphone();
        assertEquals("TH should encode to 0", "0", metaphone.metaphone("TH"));
    }

    @Test(timeout = 4000)
    public void testConsonantCombination_SCH() {
        Metaphone metaphone = new Metaphone();
        assertEquals("SCH should encode to SK", "SK", metaphone.metaphone("SCH"));
        assertEquals("SC should encode to SK", "SK", metaphone.metaphone("SC"));
    }

    @Test(timeout = 4000)
    public void testConsonantCombination_GN() {
        Metaphone metaphone = new Metaphone();
        assertEquals("GN should encode to N", "N", metaphone.metaphone("GN"));
    }

    @Test(timeout = 4000)
    public void testConsonantCombination_MB() {
        Metaphone metaphone = new Metaphone();
        assertEquals("MB should encode to M", "M", metaphone.metaphone("MB"));
    }

    // Single character tests

    @Test(timeout = 4000)
    public void testSingleCharacter_V() {
        Metaphone metaphone = new Metaphone();
        assertEquals("V should encode to V", "V", metaphone.metaphone("v"));
    }

    // Vowel handling tests

    @Test(timeout = 4000)
    public void testVowelSequence() {
        Metaphone metaphone = new Metaphone();
        assertEquals("AEIOU should encode to E", "E", metaphone.metaphone("AEIOU"));
    }

    // Complex word tests

    @Test(timeout = 4000)
    public void testComplexWord_WithNumbers() {
        Metaphone metaphone = new Metaphone();
        assertEquals("Word with numbers should be processed", "JLNL", metaphone.metaphone("jL:A; nlq0j6l"));
        assertEquals("Complex string should be processed", "JMSN", metaphone.metaphone("dgioM<zN0.Q`{R ["));
    }

    @Test(timeout = 4000)
    public void testComplexWord_WithSpecialChars() {
        Metaphone metaphone = new Metaphone();
        assertEquals("Word with special chars should extract H", "H", metaphone.metaphone("!1-HOe,>9Y[:a%E"));
        assertEquals("pH should encode to F", "F", metaphone.metaphone("&pH"));
    }

    // G-related phonetic rules

    @Test(timeout = 4000)
    public void testG_Rules() {
        Metaphone metaphone = new Metaphone();
        assertEquals("GA should encode to K", "K", metaphone.metaphone("GA"));
        assertEquals("CGH should encode to K", "K", metaphone.metaphone("CGH"));
        assertEquals("!gIj should encode to JJ", "JJ", metaphone.metaphone("!gIj"));
    }

    // Comparison tests

    @Test(timeout = 4000)
    public void testIsMetaphoneEqual_EmptyStrings() {
        Metaphone metaphone = new Metaphone();
        boolean result = metaphone.isMetaphoneEqual("", "");
        // Test passes regardless of result - testing that method executes without error
        assertEquals("Max code length should remain unchanged", DEFAULT_MAX_CODE_LENGTH, metaphone.getMaxCodeLen());
    }

    @Test(timeout = 4000)
    public void testIsMetaphoneEqual_DifferentCodes() {
        Metaphone metaphone = new Metaphone();
        boolean result = metaphone.isMetaphoneEqual("I", "X");
        assertFalse("I and X should have different metaphone codes", result);
        assertEquals("Max code length should remain unchanged", DEFAULT_MAX_CODE_LENGTH, metaphone.getMaxCodeLen());
    }

    // Additional phonetic pattern tests

    @Test(timeout = 4000)
    public void testPhoneticPattern_ACW() {
        Metaphone metaphone = new Metaphone();
        assertEquals("acw should encode to AK", "AK", metaphone.metaphone("acw"));
    }

    @Test(timeout = 4000)
    public void testPhoneticPattern_KBMF() {
        Metaphone metaphone = new Metaphone();
        assertEquals("KBMF should encode to itself", "KBMF", metaphone.metaphone("KBMF"));
    }

    @Test(timeout = 4000)
    public void testPhoneticPattern_WithW() {
        Metaphone metaphone = new Metaphone();
        assertEquals("WHeee`OhCYD should encode to WST", "WST", metaphone.metaphone("WHeee`OhCYD"));
        assertEquals("wr`hH should encode to R", "R", metaphone.metaphone("wr`hH"));
    }

    @Test(timeout = 4000)
    public void testPhoneticPattern_WithComplexInput() {
        Metaphone metaphone = new Metaphone();
        assertEquals("uCghA:%<#k~K+2> should encode to UKKK", "UKKK", metaphone.metaphone("uCghA:%<#k~K+2>"));
        assertEquals("!gm!DG77KI} should encode to KMTK", "KMTK", metaphone.metaphone("!gm!DG77KI}"));
        assertEquals("chuQ)i92HWt should encode to KKT", "KKT", metaphone.metaphone("chuQ)i92HWt"));
    }

    @Test(timeout = 4000)
    public void testPhoneticPattern_WithZ() {
        Metaphone metaphone = new Metaphone();
        assertEquals("H]H0>$Z should encode to S", "S", metaphone.metaphone("H]H0>$Z"));
    }

    @Test(timeout = 4000)
    public void testPhoneticPattern_WithP() {
        Metaphone metaphone = new Metaphone();
        assertEquals("Am<p[7Pu444indX8o6 should encode to AMPP", "AMPP", metaphone.metaphone("Am<p[7Pu444indX8o6"));
    }
}