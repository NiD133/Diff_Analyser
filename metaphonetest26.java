package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the Metaphone algorithm, focusing on specific phonetic rules for "CH" and "SCH".
 *
 * <p>This class verifies the encoding of words where "CH" and "SCH" combinations
 * are translated into different phonetic codes (e.g., 'SK', 'K', 'X') based on their
 * context within the word.</p>
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    /**
     * The Metaphone algorithm has distinct rules for handling 'CH' and 'SCH' character combinations.
     * This parameterized test verifies the correct phonetic code is generated for various inputs.
     */
    @ParameterizedTest(name = "metaphone(\"{0}\") should be \"{1}\"")
    @CsvSource({
        // 'SCH' is pronounced 'SK'
        "SCHEDULE,  SKTL",
        "SCHEMATIC, SKMT",

        // 'CH' can be pronounced 'K' (e.g., in words of Greek origin)
        "CHARACTER, KRKT",

        // 'CH' at the end of a word can be pronounced 'X'
        "TEACH,     TX"
    })
    void shouldEncodeSchAndChCombinationsCorrectly(final String input, final String expectedMetaphone) {
        assertEquals(expectedMetaphone, getStringEncoder().metaphone(input));
    }
}