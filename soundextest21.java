package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for the {@link Soundex} encoder.
 */
@DisplayName("Tests for Soundex")
class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        // This provides the standard Soundex instance for tests in AbstractStringEncoderTest
        return new Soundex();
    }

    @Nested
    @DisplayName("US_ENGLISH_SIMPLIFIED Algorithm")
    class SimplifiedSoundexTest {

        // The simplified algorithm is documented at http://west-penwith.org.uk/misc/soundex.htm (link is now defunct).
        // Its key feature is treating vowels (A, E, I, O, U, Y) and the letters 'H' and 'W' as separators.
        // This differs from the standard Soundex, where 'H' and 'W' are typically ignored.

        @DisplayName("should correctly encode reference examples")
        @ParameterizedTest(name = "encode(\"{0}\") -> \"{1}\"")
        @CsvSource({
            "WILLIAMS,    W452",
            "BARAGWANATH, B625",
            "DONNELL,     D540",
            "LLOYD,       L300",
            "WOOLCOCK,    W422"
        })
        void shouldEncodeReferenceExamples(String input, String expected) {
            final Soundex soundexEncoder = Soundex.US_ENGLISH_SIMPLIFIED;
            assertEquals(expected, soundexEncoder.encode(input));
        }

        @Test
        @DisplayName("should treat 'H' and 'W' as code separators, like vowels")
        void shouldTreatHAndWAsSeparators() {
            final Soundex soundexEncoder = Soundex.US_ENGLISH_SIMPLIFIED;

            // In "Dodds", the vowel 'o' separates the first 'D' from the subsequent 'd'.
            // This allows the code for the second 'd' to be included.
            // D-o-d-d-s -> D-3-2-0
            final String doddsCode = soundexEncoder.encode("Dodds");
            assertEquals("D320", doddsCode, "Vowel 'o' should separate the two 'd' sounds");

            // In the simplified algorithm, 'W' and 'H' act as separators, just like 'o'.
            // Therefore, "Dwdds" and "Dhdds" should produce the same Soundex code as "Dodds".
            assertEquals(doddsCode, soundexEncoder.encode("Dwdds"), "'W' should act as a separator, like a vowel");
            assertEquals(doddsCode, soundexEncoder.encode("Dhdds"), "'H' should act as a separator, like a vowel");
        }
    }
}