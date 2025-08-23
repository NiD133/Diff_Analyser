package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Soundex} encoder.
 */
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    @Test
    @DisplayName("Consonants with the same code separated by 'H' or 'W' should be treated as a single unit")
    void shouldTreatConsonantsSeparatedByWOrHAsOne() throws EncoderException {
        // This test demonstrates the H/W rule. According to the Soundex algorithm,
        // consonants with the same code are treated as one if they are adjacent.
        // The H/W rule extends this: they are also treated as one if separated by 'H' or 'W'.
        //
        // In this example, 'S' and 'G' both map to code '2'.
        // - In "Sgler", 'g' is adjacent to 's' and is dropped.
        // - In "Swhgler", the 'w' and 'h' are ignored, making 'g' effectively adjacent to 's', so it's also dropped.
        // Both strings should therefore produce the same Soundex code.
        final Soundex soundex = getStringEncoder();

        assertEquals("S460", soundex.encode("Sgler"));
        assertEquals("S460", soundex.encode("Swhgler"));
    }

    @Test
    @DisplayName("Should produce the same Soundex code for various similar-sounding names")
    void shouldEncodeSimilarSoundingNamesToSameCode() throws EncoderException {
        // This test verifies that a list of names, which are phonetically similar to "Sailor",
        // all correctly map to the same Soundex code "S460".
        final String[] names = {
            "SAILOR", "SALYER", "SAYLOR", "SCHALLER", "SCHELLER", "SCHILLER",
            "SCHOOLER", "SCHULER", "SCHUYLER", "SEILER", "SEYLER", "SHOLAR",
            "SHULER", "SILAR", "SILER", "SILLER"
        };

        checkEncodingVariations("S460", names);
    }
}