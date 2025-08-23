package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Contains Soundex encoding tests based on external examples.
 * This test class focuses on the default Soundex implementation.
 */
public class SoundexTestTest7 extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Tests the Soundex encoding algorithm against a set of examples from an external source.
     * The original source (http://www.myatt.demon.co.uk/sxalg.htm) is no longer available,
     * but the examples are preserved here for regression testing.
     *
     * @param name the input name to encode
     * @param expectedCode the expected Soundex code
     */
    @ParameterizedTest(name = "Encoding of ''{0}'' should be ''{1}''")
    @CsvSource({
        "HOLMES,      H452",
        "ADOMOMI,     A355",
        "VONDERLEHR,  V536",
        "BALL,        B400",
        "SHAW,        S000",
        "JACKSON,     J250",
        "SCANLON,     S545",
        "SAINTJOHN,   S532"
    })
    void testEncodeWithVariousExamples(final String name, final String expectedCode) {
        assertEquals(expectedCode, getStringEncoder().encode(name));
    }
}