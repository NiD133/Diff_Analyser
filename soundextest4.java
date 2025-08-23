package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the default Soundex algorithm implementation.
 */
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Tests the US English Soundex encoding for a variety of common words.
     * The test cases are derived from the sentence "The quick brown fox jumped over the lazy dogs".
     */
    @DisplayName("Should encode word to expected US English Soundex code")
    @ParameterizedTest
    @CsvSource({
        "testing, T235",
        "The,     T000",
        "quick,   Q200",
        "brown,   B650",
        "fox,     F200",
        "jumped,  J513",
        "over,    O160",
        "the,     T000",
        "lazy,    L200",
        "dogs,    D200"
    })
    void shouldEncodeToExpectedSoundexCode(final String input, final String expectedCode) {
        assertEquals(expectedCode, getStringEncoder().encode(input));
    }
}