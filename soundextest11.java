package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SoundexTestTest11 extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    @DisplayName("Soundex.US_ENGLISH_GENEALOGY should correctly encode names")
    @ParameterizedTest(name = "''{0}'' should be encoded as ''{1}''")
    @CsvSource({
        // Examples based on the US English Genealogy Soundex algorithm rules.
        // In this variant, vowels (AEIOUY) as well as H and W are treated as silent
        // characters (ignored) after the first letter and do not act as separators.
        "Heggenburger, H251",
        "Blackman,     B425",
        "Schmidt,      S530",
        "Lippmann,     L150",

        // Test cases demonstrating silent character handling
        "Dodds,        D200", // 'o' is silent, does not separate the 'd' sounds
        "Dhdds,        D200", // 'h' is silent
        "Dwdds,        D200"  // 'w' is silent
    })
    void testUsEnglishGenealogyVariant(final String name, final String expectedCode) {
        final Soundex soundex = Soundex.US_ENGLISH_GENEALOGY;
        assertEquals(expectedCode, soundex.encode(name));
    }
}