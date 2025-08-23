package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the {@link Soundex} encoder against a set of examples from the U.S. National Archives.
 */
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Tests encoding based on examples from the
     * <a href="http://www.archives.gov/research_room/genealogy/census/soundex.html">U.S. National Archives</a>.
     * <p>
     * Note: For "VanDeusen", the archives list D-250 as a possible encoding.
     * This implementation produces V-532, which is also a valid interpretation of the Soundex algorithm.
     * </p>
     *
     * @param name the input name to encode
     * @param expectedCode the expected Soundex code
     */
    @ParameterizedTest
    @CsvSource({
        "Washington, W252",
        "Lee, L000",
        "Gutierrez, G362",
        "Pfister, P236",
        "Jackson, J250",
        "Tymczak, T522",
        "VanDeusen, V532"
    })
    @DisplayName("Encoding '{0}' should result in '{1}'")
    void testNationalArchivesExamples(final String name, final String expectedCode) {
        assertEquals(expectedCode, getStringEncoder().encode(name));
    }
}