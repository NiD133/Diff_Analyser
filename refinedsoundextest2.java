package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the {@link RefinedSoundex} class.
 */
@DisplayName("RefinedSoundex Encoder")
class RefinedSoundexTest extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @ParameterizedTest
    @CsvSource({
        // The classic pangram
        "The,    T60",
        "quick,  Q503",
        "brown,  B1908",
        "fox,    F205",
        "jumped, J408106",
        "over,   O0209",
        "the,    T60",
        "lazy,   L7050",
        "dogs,   D6043",
        // Additional test cases
        "testing, T6036084",
        "TESTING, T6036084"
    })
    @DisplayName("should encode various words correctly")
    void testEncoding(final String input, final String expected) {
        assertEquals(expected, getStringEncoder().encode(input));
    }

    @Test
    @DisplayName("should be case-insensitive")
    void testIsCaseInsensitive() {
        // The Refined Soundex algorithm is case-insensitive.
        // This test explicitly verifies that behavior by comparing the encoding
        // of a word in lowercase, uppercase, and mixed case.
        final String expected = "T6036084";
        assertEquals(expected, getStringEncoder().encode("testing"));
        assertEquals(expected, getStringEncoder().encode("TESTING"));
        assertEquals(expected, getStringEncoder().encode("tEsTiNg"));
    }

    @Test
    @DisplayName("should work correctly with the static US_ENGLISH instance (verifies CODEC-56)")
    void testUsEnglishStaticInstance() {
        // This test addresses CODEC-56, ensuring the public static final
        // instance RefinedSoundex.US_ENGLISH encodes correctly.
        assertEquals("D6043", RefinedSoundex.US_ENGLISH.encode("dogs"));
    }
}