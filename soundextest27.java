package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the {@link Soundex} algorithm using the default US English mapping.
 */
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        // The default Soundex constructor uses the US English mapping.
        return new Soundex();
    }

    /**
     * Tests examples from the Wikipedia article on American Soundex. This ensures
     * the encoder conforms to well-known, standard examples.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Soundex#American_Soundex">Wikipedia - American Soundex</a>
     * @param name the input name to encode
     * @param expectedSoundexCode the expected Soundex code
     */
    @ParameterizedTest(name = "Encoding ''{0}'' should result in ''{1}''")
    @CsvSource({
        // Names that should produce the same code
        "Robert,   R163",
        "Rupert,   R163",
        // Another pair of names with the same code
        "Ashcraft, A261",
        "Ashcroft, A261",
        // Other examples
        "Tymczak,  T522",
        "Pfister,  P236"
    })
    void shouldEncodeWikipediaAmericanSoundexExamples(final String name, final String expectedSoundexCode) {
        assertEquals(expectedSoundexCode, getStringEncoder().encode(name));
    }
}