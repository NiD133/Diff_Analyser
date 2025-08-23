package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the Soundex algorithm's special rule for the letters 'H' and 'W'.
 * This rule applies when using the default {@link Soundex#US_ENGLISH} mapping.
 */
public class SoundexHWRuleTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        // Creates a Soundex instance with the default US English mapping,
        // where 'H' and 'W' are handled specially.
        return new Soundex();
    }

    /**
     * The standard Soundex algorithm specifies that consonants with the same Soundex code
     * separated by 'H' or 'W' should be treated as a single consonant.
     * <p>
     * This test is based on the official Soundex guidelines from the U.S. National Archives:
     * "Ashcraft is coded A-261 (A, 2 for the S, C ignored, 6 for the R, 1 for the F). It is not coded A-226."
     * (Source: http://www.archives.gov/research_room/genealogy/census/soundex.html)
     * </p>
     */
    @DisplayName("Consonants from the same code group separated by 'H' or 'W' should be treated as one")
    @ParameterizedTest
    @CsvSource({
        // The Ashcraft example from the US National Archives and Records Administration
        "Ashcraft, A261",
        "Ashcroft, A261",
        // Examples demonstrating the H/W rule with different letters
        "yehudit,  Y330",
        "yhwdyt,   Y330"
    })
    void shouldTreatConsonantsWithSameCodeSeparatedByHOrWAsOne(final String name, final String expectedSoundex) {
        assertEquals(expectedSoundex, getStringEncoder().encode(name));
    }
}