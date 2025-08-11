package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.language.Soundex;

/**
 * Test suite for the Soundex class functionality.
 * Tests cover encoding, error handling, configuration, and edge cases.
 */
public class SoundexTest {

    // ========== Constructor Tests ==========

    @Test
    public void testDefaultConstructor() {
        Soundex soundex = new Soundex();
        assertEquals("Default max length should be 4", 4, soundex.getMaxLength());
    }

    @Test
    public void testConstructorWithValidCharArray() {
        char[] customMapping = new char[1];
        customMapping[0] = '-';
        Soundex soundex = new Soundex(customMapping);
        assertEquals("Max length should be 4", 4, soundex.getMaxLength());
    }

    @Test
    public void testConstructorWithValidString() {
        Soundex soundex = new Soundex("/HU+\"}{6.):e:-&!R");
        assertEquals("Max length should be 4", 4, soundex.getMaxLength());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullCharArray() {
        new Soundex((char[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullString() {
        new Soundex((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullStringAndBoolean() {
        new Soundex((String) null, false);
    }

    // ========== Basic Encoding Tests ==========

    @Test
    public void testEncodeValidString() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("dAiwv)]=F=ceL=T[CcF");
        assertEquals("Should encode to D124", "D124", result);
    }

    @Test
    public void testEncodeEmptyString() {
        char[] emptyMapping = new char[8];
        Soundex soundex = new Soundex(emptyMapping);
        String result = soundex.encode("");
        assertEquals("Empty string should return empty result", "", result);
    }

    @Test
    public void testEncodeNullString() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex(null);
        // Test passes if no exception is thrown
        assertEquals("Max length should remain 4", 4, soundex.getMaxLength());
    }

    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        Object result = soundex.encode("atW+2N,x7`1kf@");
        assertEquals("Should encode object to A352", "A352", result);
    }

    // ========== Predefined Instance Tests ==========

    @Test
    public void testUsEnglishInstance() {
        String result = Soundex.US_ENGLISH.soundex("}}'3N[nM+hnR ayR6");
        assertEquals("US_ENGLISH should encode to N660", "N660", result);
    }

    @Test
    public void testUsEnglishWithSpecialCharacters() {
        String result = Soundex.US_ENGLISH.soundex("[&L!ug<F4wFviM+`RV{");
        assertEquals("Should handle special characters and encode to L215", "L215", result);
    }

    @Test
    public void testUsEnglishWithSingleSpecialCharacter() {
        String result = Soundex.US_ENGLISH.soundex(")");
        assertEquals("Single special character should return empty string", "", result);
    }

    @Test
    public void testUsEnglishGenealogyInstance() {
        String result = Soundex.US_ENGLISH_GENEALOGY.soundex("org.apache.commons.codec.language.Soundex");
        assertEquals("Genealogy instance should encode to O621", "O621", result);
    }

    @Test
    public void testUsEnglishGenealogyWithNull() {
        char[] customMapping = new char[3];
        Soundex soundex = new Soundex(customMapping);
        String result = soundex.US_ENGLISH_GENEALOGY.encode(null);
        assertNull("Null input should return null", result);
    }

    // ========== Difference Method Tests ==========

    @Test
    public void testDifferenceWithIdenticalStrings() {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("Hr~hEi", "Hr~hEi");
        assertEquals("Identical strings should have difference of 4", 4, difference);
    }

    @Test
    public void testDifferenceWithDifferentStrings() {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("01230120022455012623010202", "6]5]'j=[IE=9");
        assertEquals("Different strings should have difference of 0", 0, difference);
    }

    // ========== Configuration Tests ==========

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int maxLength = soundex.getMaxLength();
        assertEquals("Default max length should be 4", 4, maxLength);
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(-2841);
        int maxLength = soundex.getMaxLength();
        assertEquals("Max length should be set to -2841", -2841, maxLength);
    }

    // ========== Error Handling Tests ==========

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeWithUnmappedCharacter_F() {
        char[] limitedMapping = new char[5]; // Doesn't include mapping for 'F'
        Soundex soundex = new Soundex(limitedMapping);
        soundex.difference("EF\"kniaAVspLJDz", "$I<`&-Dq*{");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoundexWithUnmappedCharacter_Z() {
        Soundex soundex = new Soundex("IW=es%O[9p.u.:", true);
        soundex.soundex("?9+dzZ o|D}!;?at7Q@");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeWithUnmappedCharacter_G() {
        Soundex soundex = new Soundex("@'0g");
        soundex.encode("@'0g");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeObjectWithUnmappedCharacter_J() {
        Soundex soundex = new Soundex("]jH");
        soundex.encode((Object) "]jH");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferenceWithUnmappedCharacter_K() {
        char[] limitedMapping = new char[8]; // Doesn't include mapping for 'K'
        Soundex soundex = new Soundex(limitedMapping);
        soundex.difference("AW1D!AKs", "AW1D!AKs");
    }

    @Test(expected = Exception.class)
    public void testEncodeNonStringObject() throws Exception {
        Soundex soundex = new Soundex("org.apache.commons.codec.EncoderException", false);
        Object nonStringObject = new Object();
        soundex.US_ENGLISH_SIMPLIFIED.encode(nonStringObject);
    }
}