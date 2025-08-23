package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests the {@link Soundex} encoder.
 *
 * <p>This class includes test cases based on examples from the MS SQL Server T-SQL reference.</p>
 */
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Tests that different phonetic spellings of "Erickson" are all encoded to the same Soundex code.
     *
     * @param nameVariant a spelling variation of the name "Erickson"
     * @throws EncoderException if encoding fails
     */
    @DisplayName("Phonetic variations of 'Erickson' should all encode to E625")
    @ParameterizedTest(name = "Encoding \"{0}\"")
    @ValueSource(strings = {
        "Erickson",
        "Erikson",
        "Ericson",
        "Ericksen",
        "Ericsen"
    })
    void testEricksonVariationsShouldEncodeToE625(final String nameVariant) throws EncoderException {
        final String expectedSoundex = "E625";
        assertEquals(expectedSoundex, getStringEncoder().encode(nameVariant));
    }
}