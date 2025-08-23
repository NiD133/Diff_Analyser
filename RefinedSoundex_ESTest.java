package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.*;

public class RefinedSoundexTest {

    private static final String SAMPLE = "org.apache.commons.codec.language.RefinedSoundex";

    // -----------------------------
    // Construction
    // -----------------------------

    @Test
    public void constructsWithDefaultMapping() {
        RefinedSoundex rs = new RefinedSoundex();
        assertNotNull(rs);
    }

    @Test
    public void constructsWithEmptyCharArrayMapping() {
        // Should not throw; may not be useful, but allowed by constructor contract
        RefinedSoundex rs = new RefinedSoundex(new char[0]);
        assertNotNull(rs);
    }

    @Test
    public void constructWithNullStringMappingThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new RefinedSoundex((String) null));
    }

    @Test
    public void constructWithNullCharArrayMappingThrowsNPE() {
        assertThrows(NullPointerException.class, () -> new RefinedSoundex((char[]) null));
    }

    // -----------------------------
    // Mapping code lookups
    // -----------------------------

    @Test
    public void getMappingCode_usesUsEnglishMappingForLowercaseY() {
        // y/Y are vowels in the default mapping -> '0'
        assertEquals('0', RefinedSoundex.US_ENGLISH.getMappingCode('y'));
    }

    @Test
    public void getMappingCode_usesProvidedMappingForAlphabetChars() {
        // Using the provided mapping string as the internal alphabet mapping,
        // 'X' (index 23, 0-based) maps to the 24th character of the string, which is 'c'.
        RefinedSoundex custom = new RefinedSoundex(SAMPLE);
        assertEquals('c', custom.getMappingCode('X'));
    }

    @Test
    public void getMappingCode_nonAlphabeticCharsReturnNullChar() {
        RefinedSoundex custom = new RefinedSoundex(SAMPLE);
        assertEquals('\u0000', custom.getMappingCode('+'));
    }

    // -----------------------------
    // encode(String) and encode(Object)
    // -----------------------------

    @Test
    public void encode_nullStringReturnsNull() {
        assertNull(new RefinedSoundex().encode((String) null));
    }

    @Test
    public void encode_nonAlphabeticOnlyReturnsEmptyString() {
        assertEquals("", new RefinedSoundex().encode("\u007f"));
    }

    @Test
    public void encode_objectWithStringDelegatesToEncodeString() throws Exception {
        // For "O0", the refined soundex of the String under US English rules is "O0"
        Object encoded = RefinedSoundex.US_ENGLISH.encode((Object) "O0");
        assertEquals("O0", encoded);
    }

    @Test
    public void encode_objectWithNullThrowsEncoderException() {
        EncoderException ex = assertThrows(EncoderException.class,
                () -> new RefinedSoundex().encode((Object) null));
        // Optional: assert message to document behavior
        assertTrue(ex.getMessage().contains("Parameter supplied to RefinedSoundex encode is not of type java.lang.String"));
    }

    @Test
    public void encode_withCustomMappingEncodesAccordingToThatMapping() {
        RefinedSoundex custom = new RefinedSoundex(SAMPLE);
        String encoded = custom.encode(SAMPLE);
        assertEquals("Omsaogcagmom.gm.agcomaoasaphma.mom.ac", encoded);
    }

    // -----------------------------
    // soundex(String)
    // -----------------------------

    @Test
    public void soundex_nullReturnsNull() {
        assertNull(new RefinedSoundex().soundex(null));
    }

    @Test
    public void soundex_emptyStringReturnsEmptyString() {
        assertEquals("", new RefinedSoundex().soundex(""));
    }

    @Test
    public void soundex_usEnglishKnownValue() {
        // Known refined soundex for the sample string under US English mapping
        String code = RefinedSoundex.US_ENGLISH.soundex(SAMPLE);
        assertEquals("O09401030308083060370840409020806308605", code);
    }

    @Test
    public void soundex_withCustomMappingAndMixedCharsProducesExpectedCode() {
        RefinedSoundex custom = new RefinedSoundex("U>");
        String code = custom.soundex("<+Eq|qK!wg0f\u0006n_~");
        assertEquals("E", code);
    }

    // -----------------------------
    // difference(String, String)
    // -----------------------------

    @Test
    public void difference_withNullsReturnsZero() throws Exception {
        assertEquals(0, new RefinedSoundex().difference(null, null));
    }

    @Test
    public void difference_sameInputWithCustomMappingReturnsExpectedValue() throws Exception {
        // When using mapping "U>", the encoded forms for the identical inputs produce a difference of 3.
        RefinedSoundex custom = new RefinedSoundex("U>");
        int diff = custom.difference(SAMPLE, SAMPLE);
        assertEquals(3, diff);
    }
}