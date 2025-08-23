package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Contains specific test cases for the {@link Soundex} encoder, focusing on its unique rules.
 */
class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Tests the Soundex rule where consonants from the same code group separated by 'H' or 'W'
     * are treated as a single unit. For example, in "BOOTHDAVIS", 'T' and 'D' share code '3'
     * and are separated by 'H', so they are encoded as a single '3'.
     *
     * <p>Test data from http://www.myatt.demon.co.uk/sxalg.htm</p>
     *
     * @param name The input string to be encoded.
     */
    @DisplayName("Consonants with the same code separated by 'H' should be encoded as a single code")
    @ParameterizedTest
    @ValueSource(strings = {"BOOTHDAVIS", "BOOTH-DAVIS"})
    void shouldTreatConsonantsSeparatedByHAsSingleUnit(final String name) throws EncoderException {
        // The expected Soundex code for both "BOOTHDAVIS" and "BOOTH-DAVIS" is B312.
        // B -> B (first letter)
        // O, O -> Vowels, ignored
        // T -> 3
        // H -> Separator, ignored
        // D -> 3 (same code as previous consonant 'T', so 'D' is dropped)
        // A -> Vowel, ignored
        // V -> 1
        // I -> Vowel, ignored
        // S -> 2
        // Final code: B312
        final String expectedCode = "B312";

        assertEquals(expectedCode, getStringEncoder().encode(name));
    }
}