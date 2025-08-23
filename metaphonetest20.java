package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the {@link Metaphone} encoder.
 */
public class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @DisplayName("Metaphone encoding should return the expected code for various words")
    @ParameterizedTest(name = "Metaphone for \"{0}\" should be \"{1}\"")
    @CsvSource({
        // Test cases from "The quick brown fox jumped over the lazy dogs"
        "howl,    HL",
        "testing, TSTN",
        "The,     0",
        "quick,   KK",
        "brown,   BRN",
        "fox,     FKS",
        "jumped,  JMPT",
        "over,    OFR",
        "the,     0",
        "lazy,    LS",
        "dogs,    TKS"
    })
    void testMetaphoneEncoding(final String input, final String expectedCode) {
        assertEquals(expectedCode, getStringEncoder().metaphone(input));
    }

    @Test
    void isMetaphoneEqualShouldReturnTrueForPhoneticallySimilarWords() {
        final String[][] equivalentPairs = {
            {"case", "kase"},
            {"cat", "kat"},
            {"phone", "fone"},
            {"Smith", "Smythe"},
            {"phonetics", "fonetix"}
        };

        for (final String[] pair : equivalentPairs) {
            assertTrue(getStringEncoder().isMetaphoneEqual(pair[0], pair[1]),
                () -> "Expected '" + pair[0] + "' and '" + pair[1] + "' to be metaphone-equal");
        }
    }

    @Test
    void isMetaphoneEqualShouldReturnFalseForPhoneticallyDifferentWords() {
        assertFalse(getStringEncoder().isMetaphoneEqual("cat", "dog"), "'cat' and 'dog' should not be metaphone-equal");
        assertFalse(getStringEncoder().isMetaphoneEqual("apple", "orange"), "'apple' and 'orange' should not be metaphone-equal");
    }
}