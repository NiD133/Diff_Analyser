package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the RefinedSoundex class.
 */
class RefinedSoundexTest extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    /**
     * Tests the difference method of RefinedSoundex.
     * This method calculates the number of matching characters in the encoded versions of two strings.
     */
    @Test
    void testDifference() throws EncoderException {
        // Edge cases: both strings are null or empty
        assertEquals(0, getStringEncoder().difference(null, null), "Difference of null strings should be 0");
        assertEquals(0, getStringEncoder().difference("", ""), "Difference of empty strings should be 0");
        assertEquals(0, getStringEncoder().difference(" ", " "), "Difference of whitespace strings should be 0");

        // Normal cases: comparing different strings
        assertEquals(6, getStringEncoder().difference("Smith", "Smythe"), "Difference between 'Smith' and 'Smythe' should be 6");
        assertEquals(3, getStringEncoder().difference("Ann", "Andrew"), "Difference between 'Ann' and 'Andrew' should be 3");
        assertEquals(1, getStringEncoder().difference("Margaret", "Andrew"), "Difference between 'Margaret' and 'Andrew' should be 1");
        assertEquals(1, getStringEncoder().difference("Janet", "Margaret"), "Difference between 'Janet' and 'Margaret' should be 1");

        // Examples from Microsoft T-SQL documentation
        assertEquals(5, getStringEncoder().difference("Green", "Greene"), "Difference between 'Green' and 'Greene' should be 5");
        assertEquals(1, getStringEncoder().difference("Blotchet-Halls", "Greene"), "Difference between 'Blotchet-Halls' and 'Greene' should be 1");

        // Additional examples
        assertEquals(8, getStringEncoder().difference("Smithers", "Smythers"), "Difference between 'Smithers' and 'Smythers' should be 8");
        assertEquals(5, getStringEncoder().difference("Anothers", "Brothers"), "Difference between 'Anothers' and 'Brothers' should be 5");
    }

    /**
     * Tests the encode method of RefinedSoundex.
     * This method encodes a string into its Refined Soundex representation.
     */
    @Test
    void testEncode() {
        // Test encoding of various words
        assertEquals("T6036084", getStringEncoder().encode("testing"), "Encoding of 'testing' should be 'T6036084'");
        assertEquals("T6036084", getStringEncoder().encode("TESTING"), "Encoding of 'TESTING' should be 'T6036084'");
        assertEquals("T60", getStringEncoder().encode("The"), "Encoding of 'The' should be 'T60'");
        assertEquals("Q503", getStringEncoder().encode("quick"), "Encoding of 'quick' should be 'Q503'");
        assertEquals("B1908", getStringEncoder().encode("brown"), "Encoding of 'brown' should be 'B1908'");
        assertEquals("F205", getStringEncoder().encode("fox"), "Encoding of 'fox' should be 'F205'");
        assertEquals("J408106", getStringEncoder().encode("jumped"), "Encoding of 'jumped' should be 'J408106'");
        assertEquals("O0209", getStringEncoder().encode("over"), "Encoding of 'over' should be 'O0209'");
        assertEquals("T60", getStringEncoder().encode("the"), "Encoding of 'the' should be 'T60'");
        assertEquals("L7050", getStringEncoder().encode("lazy"), "Encoding of 'lazy' should be 'L7050'");
        assertEquals("D6043", getStringEncoder().encode("dogs"), "Encoding of 'dogs' should be 'D6043'");

        // Test using the US_ENGLISH static instance
        assertEquals("D6043", RefinedSoundex.US_ENGLISH.encode("dogs"), "US_ENGLISH encoding of 'dogs' should be 'D6043'");
    }

    /**
     * Tests the getMappingCode method with a non-letter character.
     * This method should return 0 for non-letter characters.
     */
    @Test
    void testGetMappingCodeNonLetter() {
        final char nonLetterChar = '#';
        final char expectedCode = 0;
        assertEquals(expectedCode, getStringEncoder().getMappingCode(nonLetterChar), "Mapping code for non-letter should be 0");
    }

    /**
     * Tests encoding of a string containing all possible ASCII characters.
     * This ensures that the encoder can handle invalid characters gracefully.
     */
    @Test
    void testInvalidSoundexCharacter() {
        final char[] allAsciiChars = new char[256];
        for (int i = 0; i < allAsciiChars.length; i++) {
            allAsciiChars[i] = (char) i;
        }
        final String expectedEncoding = "A0136024043780159360205050136024043780159360205053";
        assertEquals(expectedEncoding, new RefinedSoundex().encode(new String(allAsciiChars)), "Encoding of all ASCII characters should match expected value");
    }

    /**
     * Tests creating a new instance of RefinedSoundex and encoding a string.
     */
    @Test
    void testNewInstance() {
        assertEquals("D6043", new RefinedSoundex().soundex("dogs"), "Soundex of 'dogs' using new instance should be 'D6043'");
    }

    /**
     * Tests creating a new instance of RefinedSoundex with a custom mapping and encoding a string.
     */
    @Test
    void testNewInstanceWithCustomMapping() {
        assertEquals("D6043", new RefinedSoundex(RefinedSoundex.US_ENGLISH_MAPPING_STRING.toCharArray()).soundex("dogs"), "Soundex of 'dogs' using custom mapping should be 'D6043'");
    }

    /**
     * Tests creating a new instance of RefinedSoundex with a custom mapping string and encoding a string.
     */
    @Test
    void testNewInstanceWithCustomMappingString() {
        assertEquals("D6043", new RefinedSoundex(RefinedSoundex.US_ENGLISH_MAPPING_STRING).soundex("dogs"), "Soundex of 'dogs' using custom mapping string should be 'D6043'");
    }
}