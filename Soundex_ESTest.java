package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoundexTest {

    // Basic encoding behavior (well-known examples)

    @Test
    public void encodesClassicExamplesWithDefaultMapping() {
        Soundex soundex = Soundex.US_ENGLISH;

        assertEquals("R163", soundex.soundex("Robert"));
        assertEquals("R163", soundex.soundex("Rupert")); // Same code as Robert
        assertEquals("R150", soundex.soundex("Rubin"));

        assertEquals("A261", soundex.soundex("Ashcraft"));
        assertEquals("T522", soundex.soundex("Tymczak"));
        assertEquals("P236", soundex.soundex("Pfister"));
    }

    // Difference scoring

    @Test
    public void differenceReturnsFullScoreForEqualNames() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals(4, soundex.difference("Robert", "Robert"));
    }

    @Test
    public void differenceCountsMatchingPositions() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;

        // Same Soundex code: 4
        assertEquals(4, soundex.difference("Robert", "Rupert"));

        // R163 vs R150 -> two matching positions (R and 1)
        assertEquals(2, soundex.difference("Robert", "Rubin"));
    }

    // Null and empty handling

    @Test
    public void encodeAndSoundexReturnNullForNullInput() {
        assertNull(Soundex.US_ENGLISH.encode((String) null));
        assertNull(Soundex.US_ENGLISH.soundex(null));
    }

    @Test
    public void encodeAndSoundexReturnEmptyStringForEmptyOrNonLetterInput() {
        Soundex soundex = Soundex.US_ENGLISH;

        assertEquals("", soundex.encode(""));
        assertEquals("", soundex.soundex(""));
        assertEquals("", soundex.soundex(")")); // no letters
    }

    // Object-based encoding

    @Test
    public void encodeObjectRejectsNonString() {
        EncoderException ex = assertThrows(EncoderException.class,
                () -> Soundex.US_ENGLISH.encode(new Object()));
        assertTrue(ex.getMessage().contains("not of type java.lang.String"));
    }

    // Constructor validation

    @Test
    public void constructorRejectsNullMappings() {
        assertThrows(NullPointerException.class, () -> new Soundex((char[]) null));
        assertThrows(NullPointerException.class, () -> new Soundex((String) null));
        assertThrows(NullPointerException.class, () -> new Soundex((String) null, true));
    }

    // Custom mapping validation

    @Test
    public void shortCustomCharArrayMappingThrowsOnUnmappedCharacter() {
        // Only map A, B, C to some codes; others are unmapped (array too short for A..Z)
        char[] shortMap = new char[] {'0', '1', '2'};
        Soundex soundex = new Soundex(shortMap);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> soundex.soundex("F"));
        assertTrue(ex.getMessage().toLowerCase().contains("not mapped"));
    }

    @Test
    public void shortCustomStringMappingThrowsOnUnmappedCharacter() {
        // String mapping shorter than 26 letters -> 'Z' will be unmapped
        Soundex soundex = new Soundex("0123", true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> soundex.soundex("Z"));
        assertTrue(ex.getMessage().toLowerCase().contains("not mapped"));
    }

    // Deprecated maxLength behavior (still commonly referenced)

    @Test
    @SuppressWarnings("deprecation")
    public void deprecatedMaxLengthIsMutable() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength()); // default

        soundex.setMaxLength(8);
        assertEquals(8, soundex.getMaxLength());
    }
}