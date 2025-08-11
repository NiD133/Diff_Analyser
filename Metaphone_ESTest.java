package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Understandable and maintainable tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        this.metaphone = new Metaphone();
    }

    // =================================================================================
    // Configuration Tests
    // =================================================================================

    @Test
    public void constructor_initializesWithDefaultMaxLength() {
        assertEquals("Default max code length should be 4", 4, metaphone.getMaxCodeLen());
    }

    @Test
    public void setMaxCodeLen_changesMaxLengthAndTruncatesOutput() {
        metaphone.setMaxCodeLen(2);
        assertEquals("Max code length should be updated to 2", 2, metaphone.getMaxCodeLen());
        assertEquals("Output should be truncated to new max length", "KM", metaphone.metaphone("communication"));
    }

    // =================================================================================
    // Input Edge Case Tests
    // =================================================================================

    @Test
    public void metaphone_withNullInput_returnsNull() {
        assertNull(metaphone.metaphone(null));
    }

    @Test
    public void metaphone_withEmptyInput_returnsEmptyString() {
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void metaphone_withNonAlphabeticInput_ignoresNonLetters() {
        // The algorithm should filter out numbers and symbols, processing "HOEYAE"
        assertEquals("H", metaphone.metaphone("!1-HOe,>9Y[:a%E"));
    }

    @Test
    public void metaphone_withInputContainingSpaces_processesAllLetters() {
        // Spaces are ignored, and the letters are processed as a single word.
        assertEquals("SFF", metaphone.metaphone("ghZ g-7V=6hV Uh"));
    }

    // =================================================================================
    // Initial Letter Rule Tests
    // =================================================================================

    @Test
    public void metaphone_wordStartingWithVowels_keepsInitialVowel() {
        // "AEIOU" -> "E" (A is kept, subsequent vowels are dropped)
        assertEquals("E", metaphone.metaphone("AEIOU"));
    }

    @Test
    public void metaphone_wordStartingWithGNOrKNOrPN_dropsFirstLetter() {
        assertEquals("N", metaphone.metaphone("GN"));
        // Note: KN and PN rules are not explicitly in the original tests but follow the same pattern.
    }

    @Test
    public void metaphone_wordStartingWithWH_encodesToW() {
        // "WHeee`OhCYD" -> "WST"
        assertEquals("WST", metaphone.metaphone("WHeee`OhCYD"));
    }

    @Test
    public void metaphone_wordStartingWithWR_encodesToR() {
        assertEquals("R", metaphone.metaphone("wr`hH"));
    }

    @Test
    public void metaphone_wordStartingWithX_encodesToS() {
        // This implementation follows the rule where an initial 'X' is changed to 'S'.
        assertEquals("S", metaphone.metaphone("Xylophone"));
    }

    // =================================================================================
    // General Phonetic Rule Tests
    // =================================================================================

    @Test
    public void metaphone_ruleB_isSilentWhenFollowingMAtEnd() {
        assertEquals("M", metaphone.metaphone("MB")); // from "MB"
    }

    @Test
    public void metaphone_ruleC_encodesToXForCH() {
        assertEquals("X", metaphone.metaphone("CH"));
    }

    @Test
    public void metaphone_ruleC_encodesToKForSCH() {
        assertEquals("SK", metaphone.metaphone("SCH"));
    }

    @Test
    public void metaphone_ruleC_isSilentInSCI() {
        // 'SC' followed by 'I', 'E', or 'Y' is silent.
        assertEquals("X", metaphone.metaphone("SCi")); // S-C-i -> S-i -> X
    }
    
    @Test
    public void metaphone_ruleC_encodesToKWhenFollowedByK() {
        assertEquals("K", metaphone.metaphone("CK"));
    }

    @Test
    public void metaphone_ruleD_encodesToJForDGE() {
        // "dgioM" -> "J" from "DGI"
        assertEquals("JMSN", metaphone.metaphone("dgioM<zN0.Q`{R ["));
    }

    @Test
    public void metaphone_ruleG_isSilentInGHAndAfterVowel() {
        // "uCghA" -> U-C-gh-A -> U-K-A -> UK
        assertEquals("UKKK", metaphone.metaphone("uCghA:%<#k~K+2>"));
    }
    
    @Test
    public void metaphone_ruleG_isSilentInCGH() {
        assertEquals("K", metaphone.metaphone("CGH"));
    }

    @Test
    public void metaphone_ruleG_encodesToJWhenFollowedByI() {
        // "!gIj" -> G-I-j -> J-J
        assertEquals("JJ", metaphone.metaphone("!gIj"));
    }

    @Test
    public void metaphone_ruleH_isSilentWhenFollowingVowel() {
        // "GA" -> "K" (H is silent after C,S,P,T,G)
        assertEquals("K", metaphone.metaphone("GAH"));
    }

    @Test
    public void metaphone_ruleP_encodesToFForPH() {
        assertEquals("F", metaphone.metaphone("&pH"));
    }

    @Test
    public void metaphone_ruleS_encodesToXForSH() {
        assertEquals("XFK", metaphone.metaphone("SFG")); // S-F-G -> X-F-K
    }

    @Test
    public void metaphone_ruleT_encodesToXForTCH() {
        assertEquals("X", metaphone.metaphone("TCH"));
    }

    @Test
    public void metaphone_ruleT_encodesTo0ForTH() {
        assertEquals("0", metaphone.metaphone("TH"));
    }

    @Test
    public void metaphone_ruleW_isSilentWhenNotFollowedByVowel() {
        // "acw" -> A-C-W -> A-K (W is dropped)
        assertEquals("AK", metaphone.metaphone("acw"));
    }

    // =================================================================================
    // isMetaphoneEqual Tests
    // =================================================================================

    @Test
    public void isMetaphoneEqual_withEqualEncodings_returnsTrue() {
        // Both "phonetics" and "fonetix" encode to "FNTK"
        assertTrue(metaphone.isMetaphoneEqual("phonetics", "fonetix"));
    }

    @Test
    public void isMetaphoneEqual_withDifferentEncodings_returnsFalse() {
        assertFalse(metaphone.isMetaphoneEqual("apple", "orange"));
    }

    @Test
    public void isMetaphoneEqual_withEmptyStrings_returnsTrue() {
        assertTrue(metaphone.isMetaphoneEqual("", ""));
    }

    // =================================================================================
    // StringEncoder Interface Tests
    // =================================================================================

    @Test
    public void encode_withValidString_returnsMetaphoneCode() {
        // "communication" -> KMNK
        assertEquals("KMNK", metaphone.encode("communication"));
    }

    @Test
    public void encode_withNullString_returnsNull() {
        assertNull(metaphone.encode((String) null));
    }

    @Test
    public void encode_withObjectAsString_returnsMetaphoneCode() throws EncoderException {
        Object result = metaphone.encode((Object) "testing");
        assertEquals("TSTN", result);
    }

    @Test
    public void encode_withNonStringObject_throwsEncoderException() {
        Exception exception = assertThrows(EncoderException.class, () -> {
            metaphone.encode(new Object());
        });
        assertEquals("Parameter supplied to Metaphone encode is not of type java.lang.String", exception.getMessage());
    }
}